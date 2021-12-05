package com.jobesk.thesecretpsychics.ClientActivities.ClientFragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.Adapter.AdapterAdvisorSpinnerDays;
import com.jobesk.thesecretpsychics.Adapter.AdapterAdvisorSpinnerTitles;
import com.jobesk.thesecretpsychics.Adapter.ClientOrdersAdapter;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorModels.SpinnerTitleModel;
import com.jobesk.thesecretpsychics.ClientActivities.ClientDrawerActivity;
import com.jobesk.thesecretpsychics.ClientActivities.EventBusClient.EventOrderRefresh;
import com.jobesk.thesecretpsychics.Model.ClientOrdersModel;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ClientFragMyOrders extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ImageView menu_img;
    private TextView toolbar_title;
    private RecyclerView recyclerView;
    private ClientOrdersAdapter mAdapter;
    private ArrayList<ClientOrdersModel> jobListData = new ArrayList<>();
    private String TAG = "ClientFragMyOrders";
    private Activity activity;
    private RelativeLayout no_data_contianer;
    private SwipeRefreshLayout refreshLayout;
    private boolean refresh = false;
    private int toolBarVisibility = 0;
    private RelativeLayout right_toolbar_container;
    private ImageView filter_img;
    private Spinner spinnerDays, spinner;
    private AdapterAdvisorSpinnerDays myAdapterDays;
    private ArrayList<SpinnerTitleModel> titlesList = new ArrayList<>();
    private boolean searchFilter = false;
    private TextView apply_tv;
    private String selectedIDzUsers;
    private String days = "0";
    private SpinnerTitleModel model;


    @Override
    public void onStart() {
        super.onStart();
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {


            e.printStackTrace();
        }


    }

    @Override
    public void onStop() {
        super.onStop();


        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {

            e.printStackTrace();

        }

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.client_frag_my_orders, container, false);


        activity = (ClientDrawerActivity) rootView.getContext();


        toolbar_title = rootView.findViewById(R.id.toolbar_title);
        toolbar_title.setText(getActivity().getResources().getString(R.string.my_orders));


        refreshLayout = rootView.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);


        menu_img = rootView.findViewById(R.id.menu_img);
        menu_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientDrawerActivity.openDrawer();
                toolBarVisibility = 0;
                right_toolbar_container.setVisibility(View.GONE);
            }
        });

        filter_img = rootView.findViewById(R.id.filter_img);


        filter_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (toolBarVisibility == 0) {
                    toolBarVisibility = 1;
                    right_toolbar_container.setVisibility(View.VISIBLE);
                } else {
                    toolBarVisibility = 0;
                    right_toolbar_container.setVisibility(View.GONE);
                }


            }
        });

        right_toolbar_container = rootView.findViewById(R.id.right_toolbar_container);
        right_toolbar_container.setVisibility(View.GONE);

        right_toolbar_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        spinner = (Spinner) rootView.findViewById(R.id.spinner_titles);
        spinnerDays = (Spinner) rootView.findViewById(R.id.spinner_days);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mAdapter = new ClientOrdersAdapter(getActivity(), jobListData);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        no_data_contianer = rootView.findViewById(R.id.no_data_contianer);


        apply_tv = rootView.findViewById(R.id.apply_tv);
        apply_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                StringBuilder builder = new StringBuilder();

                for (int i = 0; i < titlesList.size(); i++) {

                    String checked = titlesList.get(i).getChecked();
                    if (checked.equalsIgnoreCase("1")) {
                        builder.append(titlesList.get(i).getId() + ",");

                    }


                }
                if (builder.length() > 0) {

                    selectedIDzUsers = builder.substring(0, builder.lastIndexOf(","));

                    Log.d("selectedIDzUsers", selectedIDzUsers);
                    searchFilter = true;
                    toolBarVisibility = 0;
                    right_toolbar_container.setVisibility(View.GONE);
                    getMyORders();

                } else {

                    Toast.makeText(activity, "Perform Proper Search", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        searchFilter = false;
        toolBarVisibility = 0;
        right_toolbar_container.setVisibility(View.GONE);
        getMyORders();

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventOrderRefresh event) {

        Log.d("ClientOrderRefresh", "yes");
        onRefresh();
    }


    @Override
    public void onRefresh() {

        days = "0";
        searchFilter = false;
        refresh = true;
        getMyORders();
    }

    private void getMyORders() {

        if (GlobalClass.isOnline(getActivity()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("screenName", screen);


            String clientID = GlobalClass.getPref("clientID", getActivity());


            if (searchFilter == false) {


                WebReq.get(getActivity(), "showUserOrder?userId=" + clientID, mParams, new MyTextHttpResponseHandler());

            } else {

//                String url = "orderfilter?advisorId=" + AdvisorID + "&userId=" + selectedIDzUsers + "&days=" + days;
//
//                WebReq.get(getActivity(), url, mParams, new MyTextHttpResponseHandler());
                WebReq.get(getActivity(), "showUserOrder?advisorId=" + selectedIDzUsers + "&userId=" + clientID + "&days=" + days, mParams, new MyTextHttpResponseHandler());
            }


//

        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }

    private class MyTextHttpResponseHandler extends JsonHttpResponseHandler {
        MyTextHttpResponseHandler() {


        }

        @Override
        public void onStart() {
            super.onStart();

            if (jobListData.size() > 0) {
                jobListData.clear();
                mAdapter.notifyDataSetChanged();
            }
            if (refresh == true) {
                refreshLayout.setRefreshing(true);
                refresh = false;
            } else {

                GlobalClass.showLoading(activity);

            }
            Log.d(TAG, "onStart");

        }

        @Override
        public void onFinish() {
            super.onFinish();
            GlobalClass.dismissLoading();
            refreshLayout.setRefreshing(false);
//            Log.d(TAG, "onFinish");

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
//            Log.d(TAG, "OnFailure" + e);
            GlobalClass.dismissLoading();
            refreshLayout.setRefreshing(false);
            filter_img.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
//            Log.d(TAG, responseString);
            GlobalClass.dismissLoading();
            refreshLayout.setRefreshing(false);
            filter_img.setVisibility(View.GONE);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {
            refreshLayout.setRefreshing(false);
            GlobalClass.dismissLoading();
            Log.d(TAG, mResponse.toString() + "Respo");
            if (mResponse != null && mResponse.length() != 0) {
                try {
                    String status = mResponse.getString("statusCode");

                    if (status.equals("1")) {

                        JSONArray jsonArray = mResponse.getJSONArray("Result");

                        if (jsonArray.length() > 0) {
                            filter_img.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonArray.length(); i++) {


                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("id");
                                String advisorId = jsonObject.getString("advisorId");
                                String userId = jsonObject.getString("userId");
                                String order_heading = jsonObject.getString("order_heading");
                                String order_details = jsonObject.getString("order_details");
                                String order_video = jsonObject.getString("order_video");
                                String order_status = jsonObject.getString("order_status");
                                String created_at = jsonObject.getString("created_at");
                                String name = jsonObject.getString("screenName");
                                String profileImage = jsonObject.getString("advisorImage");


                                String reply_heading = jsonObject.getString("reply_heading");
                                String reply_details = jsonObject.getString("reply_details");
                                String reply_Video = jsonObject.getString("reply_Video");
//                                String appIcons = jsonObject.getString("appIcons");
//                                String categoryName = jsonObject.getString("categoryName");
                                String isSeen = jsonObject.getString("isSeen");
                                String isCompleted = jsonObject.getString("isCompleted");
                                String isReviewed = jsonObject.getString("isReviewed");
                                String liveChat = jsonObject.getString("liveChat");
                                String isOnline = jsonObject.getString("isOnline");
                                String TextChatRate = jsonObject.getString("TextChatRate");


                                ClientOrdersModel model = new ClientOrdersModel();
                                model.setId(id);
                                model.setAdvisorId(advisorId);
                                model.setUserId(userId);
                                model.setOrder_heading(order_heading);
                                model.setOrder_details(order_details);
                                model.setOrder_video(order_video);
                                model.setOrder_status(order_status);
                                model.setCreated_at(created_at);
                                model.setName(name);

                                model.setReply_heading(reply_heading);
                                model.setReply_details(reply_details);
                                model.setReply_Video(reply_Video);
                                model.setImage(profileImage);
//                                model.setCategoryName(categoryName);
                                model.setIsSeen(isSeen);
                                model.setIsCompleted(isCompleted);
                                model.setIsReviewed(isReviewed);
                                model.setIsLiveChat(liveChat);
                                model.setIsOnline(isOnline);
                                model.setTextChatRate(TextChatRate);


                                jobListData.add(model);

                            }


                            recyclerView.setVisibility(View.VISIBLE);
                            mAdapter.notifyDataSetChanged();


                            JSONArray jsonArray1 = mResponse.getJSONArray("advisors");

                            if (jsonArray1.length() > 0) {

                                if (titlesList.size() > 0) {

                                    titlesList.clear();
                                }

                                model = new SpinnerTitleModel();
                                model.setName(activity.getResources().getString(R.string.advisor_name));
                                model.setId("000000");
                                model.setChecked("0");
                                titlesList.add(model);

                                for (int j = 0; j < jsonArray1.length(); j++) {
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);

                                    String name = jsonObject1.getString("screenName");
                                    String id = jsonObject1.getString("id");

                                    model = new SpinnerTitleModel();
                                    model.setName(name);
                                    model.setId(id);
                                    model.setChecked("0");
                                    titlesList.add(model);

                                }


                                makeSpinnerTitles();
                                makeSpinnerDays();
                                filter_img.setVisibility(View.VISIBLE);

                            } else {

                                filter_img.setVisibility(View.GONE);
                            }


                        } else {
                            filter_img.setVisibility(View.GONE);
                            no_data_contianer.setVisibility(View.VISIBLE);

                        }


                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    private void makeSpinnerTitles() {

        AdapterAdvisorSpinnerTitles myAdapter = new AdapterAdvisorSpinnerTitles(activity, titlesList);
        spinner.setAdapter(myAdapter);

    }

    private void makeSpinnerDays() {

        final ArrayList<String> DaysList = new ArrayList<>();

        if (DaysList.size() > 0) {
            DaysList.clear();
        }


        DaysList.add(getActivity().getString(R.string.past_7_days));
        DaysList.add(getActivity().getString(R.string.past_14_days));
        DaysList.add(getActivity().getString(R.string.past_30_days));
        DaysList.add(getActivity().getString(R.string.past_60_days));


//
//        myAdapterDays = new AdapterAdvisorSpinnerDays(activity, DaysList);
//        spinnerDays.setAdapter(myAdapterDays);


        ArrayAdapter dataAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, DaysList);

        // Drop down style will be listview with radio button
        dataAdapter.setDropDownViewResource(R.layout.select_dialog_singlechoice);

        // attaching data adapter to spinner
        spinnerDays.setAdapter(dataAdapter);

        spinnerDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


//                spinnerDays.setSelection(position);


                switch (Integer.valueOf(position)) {

                    case 0:
                        days = "7";
                        break;

                    case 1:
                        days = "14";
                        break;

                    case 2:
                        days = "30";
                        break;

                    case 3:
                        days = "60";
                        break;


                }
                Log.d("itemCliekd", position + "" + "days= " + days);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }
}

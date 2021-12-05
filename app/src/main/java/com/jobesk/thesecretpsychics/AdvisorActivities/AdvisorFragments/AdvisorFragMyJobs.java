package com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorFragments;

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
import com.jobesk.thesecretpsychics.Adapter.AdvisorMyJobsAdapter;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorDrawerActivity;

import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorEventBus.AdvisorOrderRefresh;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorModels.SpinnerTitleModel;
import com.jobesk.thesecretpsychics.ClientActivities.EventBusClient.EventOrderRefresh;
import com.jobesk.thesecretpsychics.Model.AdvisorOrdersModel;
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

public class AdvisorFragMyJobs extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ImageView menu_img;
    private TextView toolbar_title;
    private RecyclerView recyclerView;
    private AdvisorMyJobsAdapter mAdapter;
    private ArrayList<AdvisorOrdersModel> jobListData = new ArrayList<>();
    private String TAG = "AdvisorFragMyJobs";
    private Activity activity;
    private RelativeLayout no_data_contianer;
    private SwipeRefreshLayout refreshLayout;
    private boolean refresh = false;
    private ImageView filter_img;
    private RelativeLayout right_toolbar_container;
    private int toolBarVisibility = 0;
    private View rootView;
    private Spinner spinnerDays, spinner;
    private ArrayList<SpinnerTitleModel> titlesList = new ArrayList<>();
    private SpinnerTitleModel model;
    private AdapterAdvisorSpinnerDays myAdapterDays;
    private TextView apply_tv;
    private boolean searchFilter = false;
    private String days = "0";
    private String selectedIDzUsers;


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
        rootView = inflater.inflate(R.layout.advisor_frag_my_jobs, container, false);

        activity = (AdvisorDrawerActivity) rootView.getContext();

        toolbar_title = rootView.findViewById(R.id.toolbar_title);
        toolbar_title.setText(getActivity().getResources().getString(R.string.my_jobs));

        refreshLayout = rootView.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);

        filter_img = rootView.findViewById(R.id.filter_img);


        right_toolbar_container = rootView.findViewById(R.id.right_toolbar_container);
        right_toolbar_container.setVisibility(View.GONE);
        no_data_contianer = rootView.findViewById(R.id.no_data_contianer);

        menu_img = rootView.findViewById(R.id.menu_img);
        menu_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvisorDrawerActivity.openDrawer();

                toolBarVisibility = 0;
                right_toolbar_container.setVisibility(View.GONE);

            }
        });


        right_toolbar_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        spinner = (Spinner) rootView.findViewById(R.id.spinner_titles);
        spinnerDays = (Spinner) rootView.findViewById(R.id.spinner_days);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mAdapter = new AdvisorMyJobsAdapter(getActivity(), jobListData);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

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
    public void onMessageEvent(AdvisorOrderRefresh event) {
        onRefresh();
        Log.d("AdvisorOrderRefresh", "yes");

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

            String AdvisorID = GlobalClass.getPref("advisorID", getActivity());


            if (searchFilter == false) {


                WebReq.get(getActivity(), "showAdvisorOrder?advisorId=" + AdvisorID, mParams, new MyTextHttpResponseHandler());

            } else {

//                String url = "orderfilter?advisorId=" + AdvisorID + "&userId=" + selectedIDzUsers + "&days=" + days;
//
//                WebReq.get(getActivity(), url, mParams, new MyTextHttpResponseHandler());
                WebReq.get(getActivity(), "showAdvisorOrder?advisorId=" + AdvisorID + "&userId=" + selectedIDzUsers + "&days=" + days, mParams, new MyTextHttpResponseHandler());
            }


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
            Log.d(TAG, "onFinish");

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d(TAG, "OnFailure" + e);
            GlobalClass.dismissLoading();
            refreshLayout.setRefreshing(false);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d(TAG, responseString);
            GlobalClass.dismissLoading();
            refreshLayout.setRefreshing(false);
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
                            no_data_contianer.setVisibility(View.GONE);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String resEmail = jsonObject.getString("userEmail");
                                String resImage = jsonObject.getString("userImage");
                                String resHeading = jsonObject.getString("order_heading");
                                String resBody = jsonObject.getString("order_details");
                                String resVideoLink = jsonObject.getString("order_video");
                                String resDate = jsonObject.getString("created_at");
                                String resName = jsonObject.getString("customerName");
                                String isSeen = jsonObject.getString("isSeen");
                                String isCompleted = jsonObject.getString("isCompleted");
                                String id = jsonObject.getString("id");
                                String advisorId = jsonObject.getString("advisorId");
                                String userId = jsonObject.getString("userId");


                                String reply_heading = jsonObject.getString("reply_heading");
                                String reply_details = jsonObject.getString("reply_details");
                                String reply_Video = jsonObject.getString("reply_Video");


                                AdvisorOrdersModel model = new AdvisorOrdersModel();
                                model.setEmail(resEmail);
                                model.setUserImage(resImage);
                                model.setHeading(resHeading);
                                model.setBody(resBody);
                                model.setVideLink(resVideoLink);
                                model.setDate(resDate);
                                model.setName(resName);
                                model.setIsSeeen(isSeen);
                                model.setIsCompleted(isCompleted);
                                model.setId(id);


                                model.setReplyHeading(reply_heading);
                                model.setReplyDetails(reply_details);
                                model.setReplyVideo(reply_Video);
                                model.setAdvisorID(advisorId);
                                model.setUserID(userId);


                                jobListData.add(model);

                            }


                            recyclerView.setVisibility(View.VISIBLE);
                            mAdapter.notifyDataSetChanged();


                            JSONArray jsonArray1 = mResponse.getJSONArray("users");

                            if (jsonArray1.length() > 0) {

                                if (titlesList.size() > 0) {

                                    titlesList.clear();
                                }

                                model = new SpinnerTitleModel();
                                model.setName(activity.getResources().getString(R.string.client_name));
                                model.setId("000000");
                                model.setChecked("0");
                                titlesList.add(model);

                                for (int j = 0; j < jsonArray1.length(); j++) {
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);

                                    String name = jsonObject1.getString("customerName");
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

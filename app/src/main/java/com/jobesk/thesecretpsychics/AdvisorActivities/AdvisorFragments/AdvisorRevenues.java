package com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorFragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.Adapter.RevenuesAdapter;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorDrawerActivity;
import com.jobesk.thesecretpsychics.AdvisorActivities.RequestWithDrawActivity;
import com.jobesk.thesecretpsychics.AdvisorActivities.RevenueListActivity;
import com.jobesk.thesecretpsychics.Model.RevenueModel;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class AdvisorRevenues extends Fragment {

    private ImageView menu_img;
    private TextView toolbar_title;
    private View rootView;
    private RecyclerView recyclerView;
    private RevenuesAdapter mAdapter;
    private Activity activity;
    private ArrayList<RevenueModel> arrayList = new ArrayList<>();
    private TextView all_tranactions;
    private TextView balacnce_tv, withDraw_tv;
    private LinearLayout transactionContainer;
    private String TAG = "AdvisorRevenues";
    private String userId, advisorId, credit, refrence, id, created_at;
    private NestedScrollView scrollView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.frag_advisor_revenue, container, false);
        activity = (AdvisorDrawerActivity) rootView.getContext();


        scrollView = rootView.findViewById(R.id.scrollView);

        scrollView.setVisibility(View.INVISIBLE);

        menu_img = rootView.findViewById(R.id.menu_img);
        menu_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvisorDrawerActivity.openDrawer();

            }
        });

        toolbar_title = rootView.findViewById(R.id.toolbar_title);
        toolbar_title.setText(getActivity().getResources().getString(R.string.revenue));


        withDraw_tv = rootView.findViewById(R.id.withDraw_tv);
        balacnce_tv = rootView.findViewById(R.id.balacnce_tv);

        all_tranactions = rootView.findViewById(R.id.all_tranactions);


        transactionContainer = rootView.findViewById(R.id.transactionContainer);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        mAdapter = new RevenuesAdapter(activity, arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        all_tranactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, RevenueListActivity.class);
                startActivity(intent);


            }
        });


        withDraw_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, RequestWithDrawActivity.class);
                startActivity(intent);


            }
        });
        getDetails();
        return rootView;

    }


    @Override
    public void onResume() {
        super.onResume();
        getDetails();

    }

    private void getDetails() {

        if (GlobalClass.isOnline(activity) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("email", email);

            String firebaseToken = GlobalClass.getToken();
            mParams.put("advisorFcmToken", firebaseToken);
            String advisorID = GlobalClass.getPref("advisorID", activity);
            WebReq.get(getActivity(), "payment?advisorId=" + advisorID, mParams, new MyTextHttpResponseHandler());

        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandler extends JsonHttpResponseHandler {
        MyTextHttpResponseHandler() {
        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(activity);
            Log.d(TAG, "onStart");

        }

        @Override
        public void onFinish() {
            super.onFinish();
            GlobalClass.dismissLoading();

            Log.d(TAG, "onFinish");

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d(TAG, "OnFailure" + e);
            GlobalClass.dismissLoading();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d(TAG, responseString);
            GlobalClass.dismissLoading();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {

            GlobalClass.dismissLoading();
            Log.d(TAG, mResponse.toString() + "Respo");
            if (mResponse != null && mResponse.length() != 0) {
                try {
                    String status = mResponse.getString("statusCode");

                    if (status.equals("1")) {



                        JSONObject jsonObject = mResponse.getJSONObject("Advisor_credit");
                        String Advisor_credit = jsonObject.getString("advisor_credit");
                        balacnce_tv.setText(activity.getResources().getString(R.string.pound) + "" + Advisor_credit);


                        if (arrayList.size() > 0) {
                            arrayList.clear();

                        }

                        JSONArray array = mResponse.getJSONArray("paymentHistory");
                        if (array.length() > 0) {

                            for (int i = 0; i < array.length(); i++) {


                                JSONObject jsonObject1 = array.getJSONObject(i);


                                userId = jsonObject1.getString("userId");
                                advisorId = jsonObject1.getString("advisorId");
                                credit = jsonObject1.getString("credit");
                                refrence = jsonObject1.getString("refrence");
                                created_at = jsonObject1.getString("created_at");
                                id = jsonObject1.getString("id");

                                RevenueModel model = new RevenueModel();
                                model.setUserId(userId);
                                model.setAdvisorId(advisorId);
                                model.setCredit(credit);
                                model.setRefrence(refrence);
                                model.setCreated_at(created_at);
                                model.setId(id);


                                if (i < 3) {

                                    arrayList.add(model);

                                }


                            }

                            mAdapter.notifyDataSetChanged();
                        } else {
                            transactionContainer.setVisibility(View.GONE);
                            withDraw_tv.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            all_tranactions.setVisibility(View.GONE);

                        }
                        scrollView.setVisibility(View.VISIBLE);

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
}

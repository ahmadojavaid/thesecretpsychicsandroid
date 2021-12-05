package com.jobesk.thesecretpsychics.AdvisorActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.Adapter.RevenuesAdapter;
import com.jobesk.thesecretpsychics.Model.RevenueModel;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class RevenueListActivity extends AppCompatActivity {
    private RevenuesAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<RevenueModel> arrayList = new ArrayList<>();
    private TextView toolbar_title;
    private ImageView back_img;
    private String TAG = "RevenueListActivity";
    private String userId, advisorId, credit, refrence, id, created_at;
    private RevenueModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue_list);


        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getApplicationContext().getResources().getString(R.string.all_transactions));

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new RevenuesAdapter(RevenueListActivity.this, arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        getDetails();
    }

    private void getDetails() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("email", email);

            String firebaseToken = GlobalClass.getToken();
            mParams.put("advisorFcmToken", firebaseToken);
            String advisorID = GlobalClass.getPref("advisorID", getApplicationContext());
            WebReq.get(getApplicationContext(), "payment?advisorId=" + advisorID, mParams, new MyTextHttpResponseHandler());

        } else {
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandler extends JsonHttpResponseHandler {
        MyTextHttpResponseHandler() {
        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(RevenueListActivity.this);
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

                                model = new RevenueModel();
                                model.setUserId(userId);
                                model.setAdvisorId(advisorId);
                                model.setCredit(credit);
                                model.setRefrence(refrence);
                                model.setCreated_at(created_at);
                                model.setId(id);
                                arrayList.add(model);

                            }

                            mAdapter.notifyDataSetChanged();
                        } else {
                            recyclerView.setVisibility(View.GONE);

                        }


                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


}

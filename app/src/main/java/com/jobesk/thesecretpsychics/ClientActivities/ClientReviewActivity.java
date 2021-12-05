package com.jobesk.thesecretpsychics.ClientActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ClientReviewActivity extends AppCompatActivity {

    private ImageView back_img;
    private TextView toolbar_title;
    private EditText review_et;
    private RatingBar rattingBar;
    private TextView submit_ratting_tv;
    private float RattingValue = 0;
    private String review = "";
    private String TAG = "ClientReviewActivity";
    private String orderID, advisorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_review);


        Bundle bundle = getIntent().getExtras();
        advisorID = bundle.getString("advisorID");
        orderID = bundle.getString("orderID");

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_title = findViewById(R.id.toolbar_title);

        toolbar_title.setText(getApplicationContext().getResources().getString(R.string.review_advisor));


        review_et = findViewById(R.id.review_et);
        rattingBar = findViewById(R.id.rattingBar);


        submit_ratting_tv = findViewById(R.id.submit_ratting_tv);

        submit_ratting_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RattingValue = rattingBar.getRating();

                review = review_et.getText().toString().trim();

                if (RattingValue == 0) {

                    Toast.makeText(ClientReviewActivity.this, getApplicationContext().getResources().getString(R.string.enter_rating), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (review.equalsIgnoreCase("")) {

                    Toast.makeText(ClientReviewActivity.this, getApplicationContext().getResources().getString(R.string.enter_feedback), Toast.LENGTH_SHORT).show();
                    return;
                }

                ReviewIt();
            }
        });


    }


    private void ReviewIt() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {


            String clientID = GlobalClass.getPref("clientID", getApplicationContext());


            RequestParams mParams = new RequestParams();
            mParams.put("advisorId", advisorID);
            mParams.put("feedback", review);
            mParams.put("rating", Integer.valueOf((int) RattingValue) + "");
            mParams.put("userId", clientID);
            mParams.put("orderId", orderID);


            Log.d("paramsReview", mParams + "");

            WebReq.post(getApplicationContext(), "addreview", mParams, new MyTextHttpResponseHandler());

        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandler extends JsonHttpResponseHandler {
        MyTextHttpResponseHandler() {
        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(ClientReviewActivity.this);
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


                        Intent intent = new Intent();
                        intent.putExtra("keyName", "1");
                        setResult(RESULT_OK, intent);
                        finish();

                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(ClientReviewActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


}

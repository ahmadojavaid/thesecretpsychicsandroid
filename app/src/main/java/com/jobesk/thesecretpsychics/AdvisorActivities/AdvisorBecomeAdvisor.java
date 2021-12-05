package com.jobesk.thesecretpsychics.AdvisorActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class AdvisorBecomeAdvisor extends AppCompatActivity {
    private TextView toolbar_title;

    private LinearLayout next_btn;
    private ImageView back_img;
    private String TAG = "AdvisorBecomeAdvisor";
    private TextView text_tv;
    private String textValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advisor_become);


        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getApplicationContext().getResources().getString(R.string.become_advisor));


        next_btn = findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdvisorBecomeAdvisor.this, AdvisorTermsofServices.class);
                startActivity(i);
            }
        });

        text_tv = findViewById(R.id.text_tv);

        getBecomeAdvisor();
    }


    private void getBecomeAdvisor() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("email", email);
//            mParams.put("password", password);


            WebReq.get(getApplicationContext(), "getbecomeAdvisor", mParams, new MyTextHttpResponseHandler());

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
            GlobalClass.showLoading(AdvisorBecomeAdvisor.this);
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

                        JSONArray jsonArray = mResponse.getJSONArray("Result");

                        for (int i = 0; i < jsonArray.length(); i++) {


                            JSONObject jsonObject = jsonArray.getJSONObject(0);

                            textValue = jsonObject.getString("text");


                        }
                        text_tv.setText(textValue);

//                        String message = mResponse.getString("statusMessage");
//                        Toast.makeText(AdvisorBecomeAdvisor.this, "" + message, Toast.LENGTH_SHORT).show();


                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(AdvisorBecomeAdvisor.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}

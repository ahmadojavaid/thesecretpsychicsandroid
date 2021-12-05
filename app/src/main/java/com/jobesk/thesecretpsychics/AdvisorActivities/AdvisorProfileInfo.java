package com.jobesk.thesecretpsychics.AdvisorActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class AdvisorProfileInfo extends AppCompatActivity {

    private TextView toolbar_title;
    private LinearLayout next_btn;
    private ImageView back_img;
    private String TAG = "AdvisorProfileInfo";
    private EditText service_description_et, about_et;
    private String service, about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advisor_profile_info);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getApplicationContext().getResources().getString(R.string.profile_info));


        service_description_et = findViewById(R.id.service_description_et);
        about_et = findViewById(R.id.about_et);


        next_btn = findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                service = service_description_et.getText().toString().trim();
                about = about_et.getText().toString().trim();


                if (service.equals("")) {
                    Toast.makeText(AdvisorProfileInfo.this, getApplicationContext().getResources().getString(R.string.enter_Service), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (about.equals("")) {
                    Toast.makeText(AdvisorProfileInfo.this, getApplicationContext().getResources().getString(R.string.enter_about_me), Toast.LENGTH_SHORT).show();
                    return;
                }
                advisorPrivateInfo();

            }
        });


    }

    private void advisorPrivateInfo() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
            mParams.put("aboutYourServices", service);
            mParams.put("aboutMe", about);

            String AdvisorID = GlobalClass.getPref("advisorID", getApplicationContext());
            WebReq.post(getApplicationContext(), "updateAdvisorInfo/" + AdvisorID, mParams, new MyTextHttpResponseHandler());

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
            GlobalClass.showLoading(AdvisorProfileInfo.this);
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

                        Intent i = new Intent(AdvisorProfileInfo.this, AdvisorOrderingInstruction.class);
                        startActivity(i);

                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(AdvisorProfileInfo.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


}

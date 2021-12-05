package com.jobesk.thesecretpsychics.AdvisorActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.SplashScreen.WelcomeScreen_5;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SignUPAdvisorSwitch extends AppCompatActivity {
  private ImageView back_img;
  private EditText name_et;
  private TextView submit_tv;
  private String name;
  private String ID;
  private String TAG = "SignUPAdvisorSwitch";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_upadvisor_switch);

    Bundle bundle = getIntent().getExtras();
    ID = bundle.getString("userID");

    name_et = findViewById(R.id.name_et);
    submit_tv = findViewById(R.id.submit_tv);

    back_img = findViewById(R.id.back_img);
    back_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent i = new Intent(getApplicationContext(), WelcomeScreen_5.class);
        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

      }
    });


    submit_tv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {


        name = name_et.getText().toString().trim();

        if (name.equals("")) {

          Toast.makeText(SignUPAdvisorSwitch.this, getApplicationContext().getResources().getString(R.string.enter_user_name), Toast.LENGTH_SHORT).show();

        } else {
          updateuserName();

        }


      }
    });
  }

  private void updateuserName() {

    if (GlobalClass.isOnline(getApplicationContext()) == true) {

      RequestParams mParams = new RequestParams();
      mParams.put("username", name);
      mParams.put("new_status", "1");

      WebReq.post(getApplicationContext(), "updateAdvisorInfo/" + ID, mParams, new MyTextHttpResponseHandler());

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
      GlobalClass.showLoading(SignUPAdvisorSwitch.this);
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


            JSONObject jsonObject = mResponse.getJSONObject("Result");
            String id = jsonObject.getString("id");
            String email = jsonObject.getString("email");
            String token = jsonObject.getString("token");
            String profileStatus = jsonObject.getString("profileStatus");
            String new_status = jsonObject.getString("new_status");

            GlobalClass.putPref("advisorID", id, getApplicationContext());
            GlobalClass.putPref("advisorEmail", email, getApplicationContext());
            GlobalClass.putPref("advisorToken", token, getApplicationContext());
            GlobalClass.putPref("advisorProfileStatus", profileStatus, getApplicationContext());
            GlobalClass.putPref("userType", "advisor", getApplicationContext());

            if (profileStatus.equals("0")) {

              Intent i = new Intent(getApplicationContext(), AdvisorLanguage.class);
              i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(i);

            } else {

              Intent i = new Intent(getApplicationContext(), AdvisorDrawerActivity.class);
              i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(i);

            }


          } else {
            String message = mResponse.getString("statusMessage");
            Toast.makeText(SignUPAdvisorSwitch.this, "" + message, Toast.LENGTH_SHORT).show();
          }


        } catch (JSONException e) {
          e.printStackTrace();
        }


      }
    }
  }

}

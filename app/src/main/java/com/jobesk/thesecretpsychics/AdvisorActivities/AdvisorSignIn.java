package com.jobesk.thesecretpsychics.AdvisorActivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.Adapter.SignUpHorizontalRecyclerAdapter;
import com.jobesk.thesecretpsychics.Model.HorizontalModel;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class AdvisorSignIn extends AppCompatActivity {

    private TextView forgetPass_tv, signInTv;
    private EditText email_et, password_et;
    private String email, password;
    private String TAG = "AdvisorSignIn";
    private AlertDialog passChangeAlert;
    private String emailUserforget = "";
    private TextView text_signup;
    private LinearLayout signUpContainer;
    private ArrayList<HorizontalModel> ArrayList1;
    private SignUpHorizontalRecyclerAdapter adapter;
    private int scrollCount = 0;

    private int[] myImageList = new int[]{
            R.drawable.image_user1,
            R.drawable.image_user2,
            R.drawable.image_user3,
            R.drawable.image_user4,
            R.drawable.image_user5,
            R.drawable.image_user6,
            R.drawable.image_user7,
            R.drawable.image_user8,
            R.drawable.image_user9,
            R.drawable.image_user10,
    };

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_advisor);


        signInTv = findViewById(R.id.signUp_tv);
        forgetPass_tv = findViewById(R.id.forgetPass_tv);
        signInTv.setText(getApplicationContext().getResources().getString(R.string.sign_in));


        text_signup = findViewById(R.id.text_signup);
        text_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);


        forgetPass_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertpassChange();
            }
        });


        signInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                email = email_et.getText().toString().trim();
                password = password_et.getText().toString().trim();


                if (email.equals("")) {
                    Toast.makeText(AdvisorSignIn.this, getApplicationContext().getResources().getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!GlobalClass.emailValidator(email)) {
                    Toast.makeText(AdvisorSignIn.this, getApplicationContext().getResources().getString(R.string.enter_valid_email), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.equals("")) {
                    Toast.makeText(AdvisorSignIn.this, getApplicationContext().getResources().getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(AdvisorSignIn.this, getApplicationContext().getResources().getString(R.string.password_length), Toast.LENGTH_SHORT).show();

                    return;
                }

                signInAdvisor();
            }
        });


        ArrayList1 = new ArrayList<HorizontalModel>();

        for (int i = 0; i < 10; i++) {
            HorizontalModel horizontalModel = new HorizontalModel();
            horizontalModel.setImage(myImageList[i]);

            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            horizontalModel.setColor(String.valueOf(color));
            ArrayList1.add(horizontalModel);

        }

        initRecyclerView();


    }

    private void signInAdvisor() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
            mParams.put("email", email);
            mParams.put("password", password);
            mParams.put("devicePlatform", "2");

            String firebaseToken = GlobalClass.getToken();
            mParams.put("advisorFcmToken", firebaseToken);


            WebReq.post(getApplicationContext(), "advisordoLogin", mParams, new MyTextHttpResponseHandler());

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
            GlobalClass.showLoading(AdvisorSignIn.this);
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
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(AdvisorSignIn.this, "" + message, Toast.LENGTH_SHORT).show();

                        JSONObject jsonObject = mResponse.getJSONObject("result");
                        String id = jsonObject.getString("id");
                        String email = jsonObject.getString("email");
                        String token = jsonObject.getString("token");
                        String profileStatus = jsonObject.getString("profileStatus");

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
                        Toast.makeText(AdvisorSignIn.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


    public void alertpassChange() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AdvisorSignIn.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogView = inflater.inflate(R.layout.custom_dialog_pass_reset, null);
        dialogBuilder.setView(dialogView);

        final EditText emailet = (EditText) dialogView.findViewById(R.id.enter_email_tv);
        TextView submitTv = (TextView) dialogView.findViewById(R.id.submit_tv);
        submitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                emailUserforget = emailet.getText().toString().trim();
                if (emailUserforget.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.enter_email), Toast.LENGTH_SHORT).show();

                } else if (GlobalClass.emailValidator(emailUserforget) == false) {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.enter_valid_email), Toast.LENGTH_SHORT).show();
                } else {
                    changePass();
                }


            }
        });


        passChangeAlert = dialogBuilder.create();
        passChangeAlert.show();
    }


    private void changePass() {


        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("screenName", screen);
//            mParams.put("profileImage", encodedImage);
//            mParams.put("serviceName", service);


            String AdvisorID = GlobalClass.getPref("advisorID", getApplicationContext());

            WebReq.post(getApplicationContext(), "advisorforgotPassword?email=" + emailUserforget, mParams, new MyTextHttpResponseHandlerChangepass());

        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerChangepass extends JsonHttpResponseHandler {


        MyTextHttpResponseHandlerChangepass() {


        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(AdvisorSignIn.this);
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
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(AdvisorSignIn.this, "" + message, Toast.LENGTH_LONG).show();
                        passChangeAlert.dismiss();
                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(AdvisorSignIn.this, "" + message, Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {

                    e.printStackTrace();

                }


            }
        }
    }


    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");

//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = findViewById(R.id.recyclerView1);


        adapter = new SignUpHorizontalRecyclerAdapter(this, (ArrayList<HorizontalModel>) ArrayList1);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getApplicationContext()) {
                    final float SPEED = 4000f;// Change this value (default=25f)

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }

        };
        autoScrollAnother();

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
    }


    public void autoScrollAnother() {
        scrollCount = 0;
        final int speedScroll = 1200;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (scrollCount == adapter.getItemCount()) {
//                    adapter.load();
                    adapter.notifyDataSetChanged();
                }
                recyclerView.smoothScrollToPosition((scrollCount++));
                handler.postDelayed(this, speedScroll);
            }
        };
        handler.postDelayed(runnable, speedScroll);
    }

}

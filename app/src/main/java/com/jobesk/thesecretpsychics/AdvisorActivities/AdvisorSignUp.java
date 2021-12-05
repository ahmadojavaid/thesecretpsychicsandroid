package com.jobesk.thesecretpsychics.AdvisorActivities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.Adapter.SignUpHorizontalRecyclerAdapter;
import com.jobesk.thesecretpsychics.ClientActivities.ClientEditProfile;
import com.jobesk.thesecretpsychics.Model.HorizontalModel;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.UsernameValidator;
import com.jobesk.thesecretpsychics.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import cz.msebera.android.httpclient.Header;


public class AdvisorSignUp extends AppCompatActivity {

    //    private static int SPLASH_TIME_OUT = 3000;
    private TextView sign_in_tv;
    private RelativeLayout sign_up_tv;
    private EditText email_et, pass_et, userName_et, confirm_pass_et;
    private String email, password, passwordConfirm;
    private String TAG = "AdvisorSignUp";
    private CheckBox checkBox_age;
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
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advisor_sign_up);


        sign_in_tv = findViewById(R.id.sign_in_tv);
        sign_in_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(AdvisorSignUp.this, AdvisorSignIn.class);
                startActivity(i);

            }
        });

        email_et = findViewById(R.id.email_et);
        pass_et = findViewById(R.id.pass_et);
        confirm_pass_et = findViewById(R.id.confirm_pass_et);
        userName_et = findViewById(R.id.userName_et);


        checkBox_age = findViewById(R.id.checkBox_age);

        sign_up_tv = findViewById(R.id.sign_up_tv);
        sign_up_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                userName = userName_et.getText().toString().trim();
                email = email_et.getText().toString().trim();
                password = pass_et.getText().toString().trim();
                passwordConfirm = confirm_pass_et.getText().toString().trim();


                if (email.equalsIgnoreCase("")) {

                    Toast.makeText(AdvisorSignUp.this, getApplicationContext().getResources().getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
                    return;
                }


                if (GlobalClass.emailValidator(email) == false) {

                    Toast.makeText(AdvisorSignUp.this, getApplicationContext().getResources().getString(R.string.enter_valid_email), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (userName.equalsIgnoreCase("")) {

                    Toast.makeText(AdvisorSignUp.this, getApplicationContext().getResources().getString(R.string.enter_user_name), Toast.LENGTH_SHORT).show();
                    return;
                }

                UsernameValidator validatorName = new UsernameValidator();


                char first = userName.charAt(0);

                if (Character.isDigit(first)) {

                    Toast.makeText(AdvisorSignUp.this, getApplicationContext().getResources().getString(R.string.name_first_letter), Toast.LENGTH_SHORT).show();

                    return;

                }


                boolean nameValidator = validatorName.validate(userName);
                if (nameValidator == false) {

                    Toast.makeText(AdvisorSignUp.this, getApplicationContext().getResources().getString(R.string.please_enter_valid_name), Toast.LENGTH_SHORT).show();

                    return;
                }


                if (password.equalsIgnoreCase("")) {

                    Toast.makeText(AdvisorSignUp.this, getApplicationContext().getResources().getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {

                    Toast.makeText(AdvisorSignUp.this, getApplicationContext().getResources().getString(R.string.password_length), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (passwordConfirm.equalsIgnoreCase("")) {

                    Toast.makeText(AdvisorSignUp.this, getApplicationContext().getResources().getString(R.string.enter_confirm_pass), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (passwordConfirm.length() < 6) {

                    Toast.makeText(AdvisorSignUp.this, getApplicationContext().getResources().getString(R.string.confirm_password_length), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!passwordConfirm.equals(password)) {

                    Toast.makeText(AdvisorSignUp.this, getApplicationContext().getResources().getString(R.string.password_not_match), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (checkBox_age.isChecked() == false) {

                    Toast.makeText(AdvisorSignUp.this, getApplicationContext().getResources().getString(R.string.over_18_make_sure), Toast.LENGTH_SHORT).show();
                    return;
                }

                signUpAdvisor();


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

    private void signUpAdvisor() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
            mParams.put("username", userName);
            mParams.put("email", email);
            mParams.put("password", password);
            mParams.put("new_status", "1");

            WebReq.post(getApplicationContext(), "advisorsignup", mParams, new MyTextHttpResponseHandler());

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
            GlobalClass.showLoading(AdvisorSignUp.this);
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
                        Toast.makeText(AdvisorSignUp.this, "" + message, Toast.LENGTH_SHORT).show();

                        JSONObject jsonObject = mResponse.getJSONObject("Result");
                        String id = jsonObject.getString("id");
                        String email = jsonObject.getString("email");
                        String token = jsonObject.getString("token");
//
//                        GlobalClass.putPref("advisorID", id, getApplicationContext());
//                        GlobalClass.putPref("advisorEmail", email, getApplicationContext());
//                        GlobalClass.putPref("advisorToken", token, getApplicationContext());


                        finish();

                        Intent i = new Intent(AdvisorSignUp.this, AdvisorSignIn.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);


                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(AdvisorSignUp.this, "" + message, Toast.LENGTH_SHORT).show();
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

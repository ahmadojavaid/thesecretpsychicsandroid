package com.jobesk.thesecretpsychics.AdvisorActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class AdvisorPaymentDetails extends AppCompatActivity {

    private TextView toolbar_title;
    private LinearLayout next_btn;
    private ImageView back_img;
    private String TAG = "AdvisorPaymentDetails";
    private EditText time_rate_et, text_chat_rate_et, name_et, address_et, zipcode_et, day_et, month_et, year_et, country_et, city_et, bank_details_et, threshold_et;

    private String timeRate = "10.00", chatRate, name, address, zipcode, day, month, year, country, city, threshold, bankDetails;
    private String dateOfBirth;
    private TextView chat_rate_disCount_tv;
    private float percentageVal = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advisor_payment_details);

        GlobalClass.hideKeyboard(this);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getApplicationContext().getResources().getString(R.string.payment_details));


        time_rate_et = findViewById(R.id.time_rate_et);
        time_rate_et.setClickable(false);
        time_rate_et.setEnabled(false);

        text_chat_rate_et = findViewById(R.id.text_chat_rate_et);
        name_et = findViewById(R.id.name_et);
        address_et = findViewById(R.id.address_et);
        zipcode_et = findViewById(R.id.zipcode_et);
        day_et = findViewById(R.id.day_et);
        month_et = findViewById(R.id.month_et);
        year_et = findViewById(R.id.year_et);
        country_et = findViewById(R.id.country_et);
        city_et = findViewById(R.id.city_et);
        bank_details_et = findViewById(R.id.bank_details_et);
        threshold_et = findViewById(R.id.threshold_et);
        threshold_et.setEnabled(false);
        chat_rate_disCount_tv = findViewById(R.id.chat_rate_disCount);

        chat_rate_disCount_tv.setText(getApplicationContext().getResources().getString(R.string.you_will_earn_with_colon) + " " + getApplicationContext().getResources().getString(R.string.pound) + "0");
        text_chat_rate_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                Log.d("characterLength", s.length() + "");


                String value = s.toString().trim();

                if (value.length() == 1 && value.equals(".")) {

                    text_chat_rate_et.setText("");
                    return;

                }


                if (String.valueOf(s).equalsIgnoreCase("") || s.length() <= 0) {
                    chat_rate_disCount_tv.setText(getApplicationContext().getResources().getString(R.string.you_will_earn_with_colon) + " " + getApplicationContext().getResources().getString(R.string.pound) + "0");
                } else {
                    float userValue = Float.valueOf(String.valueOf(s));

                    float percentage = userValue * percentageVal / 100;

                    chat_rate_disCount_tv.setText(getApplicationContext().getResources().getString(R.string.you_will_earn_with_colon) + " " + getApplicationContext().getResources().getString(R.string.pound) + percentage);


                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        next_btn = findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                chatRate = text_chat_rate_et.getText().toString().trim();
                name = name_et.getText().toString().trim();
                address = address_et.getText().toString().trim();
                zipcode = zipcode_et.getText().toString().trim();
                day = day_et.getText().toString().trim();
                month = month_et.getText().toString().trim();
                year = year_et.getText().toString().trim();
                country = country_et.getText().toString().trim();
                city = city_et.getText().toString().trim();
                bankDetails = bank_details_et.getText().toString().trim();
                threshold = "50";


                if (chatRate.equals("")) {
                    Toast.makeText(AdvisorPaymentDetails.this, getApplicationContext().getResources().getString(R.string.enter_chat_rate), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (chatRate.equals("0")) {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.enter_chat_rate), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (name.equals("")) {
                    Toast.makeText(AdvisorPaymentDetails.this, getApplicationContext().getResources().getString(R.string.enter_individual_name), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (day.equals("")) {
                    Toast.makeText(AdvisorPaymentDetails.this, getApplicationContext().getResources().getString(R.string.enter_individual_name), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Integer.valueOf(day) > 31 || Integer.valueOf(day) == 0) {
                    Toast.makeText(AdvisorPaymentDetails.this, getApplicationContext().getResources().getString(R.string.enter_valid_day), Toast.LENGTH_SHORT).show();
                    return;
                }


                if (month.equals("")) {
                    Toast.makeText(AdvisorPaymentDetails.this, getApplicationContext().getResources().getString(R.string.enter_month), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Integer.valueOf(month) > 12 || Integer.valueOf(month) == 0) {
                    Toast.makeText(AdvisorPaymentDetails.this, getApplicationContext().getResources().getString(R.string.enter_valid_month), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (year.equals("")) {
                    Toast.makeText(AdvisorPaymentDetails.this, getApplicationContext().getResources().getString(R.string.enter_year), Toast.LENGTH_SHORT).show();
                    return;
                }

                int yearCurrent = Calendar.getInstance().get(Calendar.YEAR);

                if (Integer.valueOf(year) < 1910 || Integer.valueOf(year) > yearCurrent) {
                    Toast.makeText(AdvisorPaymentDetails.this, getApplicationContext().getResources().getString(R.string.enter_valid_year), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (country.equals("")) {
                    Toast.makeText(AdvisorPaymentDetails.this, getApplicationContext().getResources().getString(R.string.enter_country), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (city.equals("")) {
                    Toast.makeText(AdvisorPaymentDetails.this, getApplicationContext().getResources().getString(R.string.enter_city), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (bankDetails.equals("")) {
                    Toast.makeText(AdvisorPaymentDetails.this, getApplicationContext().getResources().getString(R.string.enter_paypal_address), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (GlobalClass.emailValidator(bankDetails) == false) {
                    Toast.makeText(AdvisorPaymentDetails.this, getApplicationContext().getResources().getString(R.string.enter_valid_paypal_address), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (threshold.equals("")) {
                    Toast.makeText(AdvisorPaymentDetails.this, getApplicationContext().getResources().getString(R.string.enter_bank_threshold), Toast.LENGTH_SHORT).show();
                    return;
                }

                dateOfBirth = year + "/" + month + "/" + day;
                savePayment();

            }
        });

    }


    private void savePayment() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
            mParams.put("paymentThreshold", threshold);
            mParams.put("bankDetails", bankDetails);
            mParams.put("city", city);
            mParams.put("zipCode", zipcode);
            mParams.put("permanentAddress", address);
            mParams.put("country", country);
            mParams.put("dateOfBirth", dateOfBirth);
            mParams.put("legalNameOfIndividual", name);
            mParams.put("TextChatRate", chatRate);
            mParams.put("timeRate", timeRate);
            mParams.put("profileStatus", "1");


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
            GlobalClass.showLoading(AdvisorPaymentDetails.this);
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

//                        JSONArray jsonArray = mResponse.getJSONArray("Result");
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//
//
//                            JSONObject jsonObject = jsonArray.getJSONObject(0);
//
////                            textValue = jsonObject.getString("text");
//
//
//                        }
//                        text_tv.setText(textValue);

//                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(AdvisorPaymentDetails.this, getApplicationContext().getResources().getString(R.string.profile_gone_for_approval), Toast.LENGTH_LONG).show();

                        GlobalClass.clearPref(getApplicationContext());
                        GlobalClass.putPref("advisorProfileStatus", "1", getApplicationContext());

                        Intent i = new Intent(AdvisorPaymentDetails.this, WelcomeScreen_5.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(AdvisorPaymentDetails.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}

package com.jobesk.thesecretpsychics.AdvisorActivities.EditProfile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorDrawerActivity;
import com.jobesk.thesecretpsychics.EventBuses.EditProfileBus;
import com.jobesk.thesecretpsychics.Model.CategoryModel;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class AdvisorEditProfile extends AppCompatActivity {

    public static TextView toolbar_title;
    private ImageView back_img;
    private String TAG = "AdvisorEditProfile";
    public static String profileVideo, orderInstruction, serviceDescription, profileImage, serviceName, aboutME, legalNameOfIndividual, profileStatus, token, email, idResponse, screenName;
    public static File VideoFile;
    public static String threshold, timeRate, TextChatRate, legalName, chatRate, name, dateOfBirth, country, permanentAddress, zipCode, address, zipcode, city, bankDetails, experience, paymentThreshold;
    public static int height, width;
    public static ArrayList<CategoryModel> catListData = new ArrayList<>();
    public static String selectedIdz = "";

    public static boolean uploadedViedo = false;
    public static boolean uploadedImage = false;


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_edit_profile);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;


        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertExit();


            }
        });


        toolbar_title = findViewById(R.id.toolbar_title);

        toolbar_title.setText(getApplicationContext().getResources().getString(R.string.advisor_general_info));


        getCategories();

        getProfile();

    }


    private void openFrag() {
        AdvisorEditFragGeneralInfo frag = new AdvisorEditFragGeneralInfo();
        FragmentManager fmd = getSupportFragmentManager();
        FragmentTransaction fragmentTransactionde = fmd.beginTransaction();
        fragmentTransactionde.replace(R.id.editFrame, frag);
        fragmentTransactionde.addToBackStack(null);
        fragmentTransactionde.commit();

    }

    private void getProfile() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("aboutYourServices", service);


            String AdvisorID = GlobalClass.getPref("advisorID", getApplicationContext());
            WebReq.get(getApplicationContext(), "showAdvisorInfo/" + AdvisorID, mParams, new MyTextHttpResponseHandler());

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
            GlobalClass.showLoading(AdvisorEditProfile.this);
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


                            serviceName = jsonObject.getString("serviceName");
                            profileImage = jsonObject.getString("profileImage");
                            aboutME = jsonObject.getString("aboutMe");
                            legalNameOfIndividual = jsonObject.getString("legalNameOfIndividual");
                            profileStatus = jsonObject.getString("profileStatus");
                            token = jsonObject.getString("token");
                            email = jsonObject.getString("email");
                            idResponse = jsonObject.getString("id");
                            screenName = jsonObject.getString("screenName");
                            serviceDescription = jsonObject.getString("aboutYourServices");

                            orderInstruction = jsonObject.getString("instructionForOrder");
                            profileVideo = jsonObject.getString("profileVideo");
                            timeRate = jsonObject.getString("timeRate");
                            TextChatRate = jsonObject.getString("TextChatRate");
                            legalName = jsonObject.getString("legalNameOfIndividual");
                            dateOfBirth = jsonObject.getString("dateOfBirth");
                            country = jsonObject.getString("country");
                            permanentAddress = jsonObject.getString("permanentAddress");
                            zipCode = jsonObject.getString("zipCode");
                            city = jsonObject.getString("city");
                            bankDetails = jsonObject.getString("bankDetails");
                            paymentThreshold = jsonObject.getString("paymentThreshold");
                            experience = jsonObject.getString("expirience");


                            StringBuilder categoryString = new StringBuilder();
                            JSONArray jsonArrayCateGories = jsonObject.getJSONArray("advisorscategories");
                            for (int j = 0; j < jsonArrayCateGories.length(); j++) {

                                JSONObject jsonCatObj = jsonArrayCateGories.getJSONObject(j);
                                String categoryName = jsonCatObj.getString("categoryName");
                                String appIcons = jsonCatObj.getString("appIcons");
                                String idCat = jsonCatObj.getString("id");
                                categoryString.append(categoryName + ", ");


                                for (int k = 0; k < catListData.size(); k++) {
                                    String myCatId = catListData.get(k).getId();
                                    if (myCatId.equalsIgnoreCase(idCat)) {

                                        catListData.get(k).setChecked("1");
                                    }
                                }


                            }

                            try {
                                categoryString.deleteCharAt(categoryString.length() - 2);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }

                        openFrag();
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


    private void getCategories() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {


            RequestParams mParams = new RequestParams();
            WebReq.get(getApplicationContext(), "category", mParams, new MyTextHttpResponseHandlergetCat());


        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlergetCat extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlergetCat() {


        }

        @Override
        public void onStart() {
            super.onStart();

            GlobalClass.showLoading(AdvisorEditProfile.this);

        }

        @Override
        public void onFinish() {
            super.onFinish();
            GlobalClass.dismissLoading();

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d(TAG, "OnFailure");
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


                        if (catListData.size() > 0) {
                            catListData.clear();
                        }

                        JSONArray jsonArrayResult = mResponse.getJSONArray("Result");
                        for (int i = 0; i < jsonArrayResult.length(); i++) {

                            JSONObject object = jsonArrayResult.getJSONObject(i);
                            String id = object.getString("id");
                            String name = object.getString("categoryName");
                            String image = object.getString("appIcons");

                            CategoryModel model = new CategoryModel();
                            model.setId(id);
                            model.setName(name);
                            model.setImage(image);
                            model.setChecked("0");

                            catListData.add(model);

                        }


                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(AdvisorEditProfile.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


    private void uploadVideo() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();


            String AdvisorID = GlobalClass.getPref("advisorID", getApplicationContext());
            try {
                mParams.put("profileVideo", VideoFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            mParams.put("advisorId", AdvisorID);

            WebReq.post(getApplicationContext(), "uploadVid", mParams, new MyTextHttpResponseHandlerUploadVideo());

        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerUploadVideo extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerUploadVideo() {
        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(AdvisorEditProfile.this);
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

                        Intent i = new Intent(AdvisorEditProfile.this, AdvisorDrawerActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);

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

    private void uploadCat() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {


            String advisorID = GlobalClass.getPref("advisorID", getApplicationContext());
            RequestParams mParams = new RequestParams();
            mParams.put("categories", selectedIdz);
            mParams.put("advisorId", advisorID);
            WebReq.post(getApplicationContext(), "assignCat", mParams, new MyTextHttpResponseHandlerUploadCat());


        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerUploadCat extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerUploadCat() {


        }

        @Override
        public void onStart() {
            super.onStart();

            GlobalClass.showLoading(AdvisorEditProfile.this);

        }

        @Override
        public void onFinish() {
            super.onFinish();
            GlobalClass.dismissLoading();

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d(TAG, "OnFailure");
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

                        savePayment();
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


            mParams.put("screenName", screenName);
            mParams.put("serviceName", serviceName);
            mParams.put("aboutYourServices", serviceDescription);
            mParams.put("aboutMe", aboutME);

            mParams.put("instructionForOrder", orderInstruction);
            mParams.put("expirience", experience);


            if (uploadedImage == true) {
                mParams.put("profileImage", profileImage);
            }


            String AdvisorID = GlobalClass.getPref("advisorID", getApplicationContext());
            WebReq.post(getApplicationContext(), "updateAdvisorInfo/" + AdvisorID, mParams, new MyTextHttpResponseHandlerSavepayment());


        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerSavepayment extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerSavepayment() {


        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(AdvisorEditProfile.this);
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

                        if (uploadedViedo == true) {

                            uploadVideo();
                        } else {


                            Intent i = new Intent(AdvisorEditProfile.this, AdvisorDrawerActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);

                        }
                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(AdvisorEditProfile.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EditProfileBus event) {


        uploadCat();


    }

    @Override
    public void onBackPressed() {


        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 1) {

//            finish();
            alertExit();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }


    private void alertExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

//        builder.setTitle("Confirm");
        builder.setMessage(getApplicationContext().getResources().getString(R.string.skip_edit_profile));

        builder.setPositiveButton(getApplicationContext().getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                finish();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getApplicationContext().getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


}

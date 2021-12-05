package com.jobesk.thesecretpsychics.ClientActivities.ClientFragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.Activities.AboutUS;
import com.jobesk.thesecretpsychics.ClientActivities.ClientDrawerActivity;
import com.jobesk.thesecretpsychics.ClientActivities.ClientEditProfile;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ClientFragSetting extends Fragment implements View.OnClickListener {


    private TextView toolbar_title;
    private ImageView menu_img;
    private TextView edit_profile_tv;
    private TextView about_us_tv, terms_of_service_tv, privacy_tv, website_tv;
    private Intent intent;
    private Bundle bundle;
    private TextView
            changePassTv;

    private String TAG = "ClientFragSetting";
    private Activity activity;
    private AlertDialog passChangeAlert;

    private String emailUserforget = "";
    private  SwitchCompat switchNoti;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.client_setting_frag, container, false);


        activity = (ClientDrawerActivity) rootView.getContext();

        toolbar_title = rootView.findViewById(R.id.toolbar_title);
        toolbar_title.setText(getActivity().getResources().getString(R.string.settings));


        menu_img = rootView.findViewById(R.id.menu_img);
        menu_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientDrawerActivity.openDrawer();

            }
        });


        edit_profile_tv = rootView.findViewById(R.id.edit_profile_tv);
        edit_profile_tv.setOnClickListener(this);


        about_us_tv = rootView.findViewById(R.id.about_us_tv);
        terms_of_service_tv = rootView.findViewById(R.id.terms_of_service_tv);
        privacy_tv = rootView.findViewById(R.id.privacy_tv);
        website_tv = rootView.findViewById(R.id.website_tv);


        about_us_tv.setOnClickListener(this);
        terms_of_service_tv.setOnClickListener(this);
        privacy_tv.setOnClickListener(this);
        website_tv.setOnClickListener(this);


        changePassTv = rootView.findViewById(R.id.changePassTv);
        changePassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertpassChange();
            }
        });



        String notiAllow = GlobalClass.getPref("clientNotiAllow", getActivity());
        switchNoti = (SwitchCompat) rootView.findViewById(R.id.switchNoti);
        if (notiAllow.equalsIgnoreCase("")) {

            switchNoti.setChecked(true);
        } else {
            switchNoti.setChecked(false);
        }

        switchNoti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked == true) {
                    GlobalClass.putPref("clientNotiAllow", "", getActivity());
                } else {
                    GlobalClass.putPref("clientNotiAllow", "0", getActivity());
                }


            }
        });


        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.edit_profile_tv:
                Intent editIntent = new Intent(getActivity(), ClientEditProfile.class);
                startActivity(editIntent);

                break;
            case R.id.about_us_tv:
                intent = new Intent(getActivity(), AboutUS.class);
                bundle = new Bundle();
                bundle.putString("value", "about");
                intent.putExtras(bundle);
                startActivity(intent);

                break;
            case R.id.terms_of_service_tv:

                intent = new Intent(getActivity(), AboutUS.class);
                bundle = new Bundle();
                bundle.putString("value", "terms");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.privacy_tv:

                intent = new Intent(getActivity(), AboutUS.class);
                bundle = new Bundle();
                bundle.putString("value", "privacy");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.website_tv:
                goToOurWebsite();

                break;

        }
    }

    private void goToOurWebsite() {
        String url = "http://thesecretpsychics.com/";
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setPackage("com.android.chrome");
        try {
            startActivity(i);
        } catch (ActivityNotFoundException e) {
            // Chrome is probably not installed
            // Try with the default browser
            i.setPackage(null);
            startActivity(i);
        }
    }

    public void alertpassChange() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogView = inflater.inflate(R.layout.custom_dialog_pass_reset, null);
        dialogBuilder.setView(dialogView);

        final EditText emailet = (EditText) dialogView.findViewById(R.id.enter_email_tv);
        TextView submitTv = (TextView) dialogView.findViewById(R.id.submit_tv);
        submitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                emailUserforget = emailet.getText().toString().trim();
                if (emailUserforget.equalsIgnoreCase("")) {
                    Toast.makeText(activity, activity.getResources().getString(R.string.enter_email), Toast.LENGTH_SHORT).show();

                } else if (GlobalClass.emailValidator(emailUserforget) == false) {
                    Toast.makeText(activity, activity.getResources().getString(R.string.enter_valid_email), Toast.LENGTH_SHORT).show();
                } else {
                    changePass();
                }


            }
        });


        passChangeAlert = dialogBuilder.create();
        passChangeAlert.show();
    }


    private void changePass() {


        if (GlobalClass.isOnline(getActivity()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("screenName", screen);
//            mParams.put("profileImage", encodedImage);
//            mParams.put("serviceName", service);


            String AdvisorID = GlobalClass.getPref("advisorID", getActivity());

            WebReq.post(getActivity(), "userforgotPassword?email=" + emailUserforget, mParams, new MyTextHttpResponseHandlerChangepass());

        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerChangepass extends JsonHttpResponseHandler {


        MyTextHttpResponseHandlerChangepass() {


        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(activity);
//            Log.d(TAG, "onStart");

        }

        @Override
        public void onFinish() {
            super.onFinish();
            GlobalClass.dismissLoading();

//            Log.d(TAG, "onFinish");

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
//            Log.d(TAG, "OnFailure" + e);
            GlobalClass.dismissLoading();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
//            Log.d(TAG, responseString);
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
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                        passChangeAlert.dismiss();
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

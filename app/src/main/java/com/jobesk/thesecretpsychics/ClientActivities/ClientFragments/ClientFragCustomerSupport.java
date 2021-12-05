package com.jobesk.thesecretpsychics.ClientActivities.ClientFragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.ClientActivities.ClientDrawerActivity;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ClientFragCustomerSupport extends Fragment {
    private ImageView menu_img;
    private TextView toolbar_title;

    private Activity activity;
    private String TAG = "ClientFragCustomerSupport";
    private TextView suggestion_et, description_et;

    private LinearLayout send_btn;
    private String suggestion, description;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.advisor_frag_customer_support, container, false);

        activity = (ClientDrawerActivity) rootView.getContext();

        toolbar_title = rootView.findViewById(R.id.toolbar_title);
        toolbar_title.setText(getActivity().getResources().getString(R.string.customer_support));

        menu_img = rootView.findViewById(R.id.menu_img);
        menu_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientDrawerActivity.openDrawer();

            }
        });

        suggestion_et = rootView.findViewById(R.id.suggestion_et);
        description_et = rootView.findViewById(R.id.description_et);

        send_btn = rootView.findViewById(R.id.send_btn);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                suggestion = suggestion_et.getText().toString().trim();
                description = description_et.getText().toString().trim();

                if (suggestion.equals("")) {

                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_question), Toast.LENGTH_SHORT).show();

                    return;
                }
                if (description.equals("")) {

                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_question), Toast.LENGTH_SHORT).show();

                    return;
                }
                sendSupportMsg();
            }
        });

        return rootView;
    }


    private void sendSupportMsg() {

        if (GlobalClass.isOnline(getActivity()) == true) {


            String clientID = GlobalClass.getPref("clientID", getActivity());
            RequestParams mParams = new RequestParams();
            mParams.put("userId", clientID);
            mParams.put("heading", suggestion);
            mParams.put("suggestion", description);

            WebReq.post(getActivity(), "usersupport", mParams, new MyTextHttpResponseHandler());

        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandler extends JsonHttpResponseHandler {
        MyTextHttpResponseHandler() {

        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(activity);
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
                        Toast.makeText(activity, getActivity().getResources().getString(R.string.query_has_been_send), Toast.LENGTH_SHORT).show();


                        getActivity().getSupportFragmentManager().popBackStack();


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

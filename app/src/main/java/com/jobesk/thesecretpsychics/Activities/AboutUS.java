package com.jobesk.thesecretpsychics.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

public class AboutUS extends AppCompatActivity {
    private TextView text_tv, toolbar_title;
    private ImageView back_img;
    private String TAG = "AboutUS";
    private String url = "";
    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);


        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar_title = findViewById(R.id.toolbar_title);
        text_tv = findViewById(R.id.text_tv);

        Bundle extras = getIntent().getExtras();
        value = extras.getString("value");

        switch (value) {

            case "about":
                toolbar_title.setText(getApplicationContext().getResources().getString(R.string.about_us));
                url = "aboutUs";

                break;
            case "terms":
                toolbar_title.setText(getApplicationContext().getResources().getString(R.string.terms_of_service));
                url = "getterms_of_use";

                break;
            case "privacy":
                toolbar_title.setText(getApplicationContext().getResources().getString(R.string.privacy_policy));

                url = "getprivacypolicies";
                break;


        }


        getContent();
    }


    private void getContent() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("screenName", screen);


            String AdvisorID = GlobalClass.getPref("advisorID", getApplicationContext());

            WebReq.get(getApplicationContext(), url, mParams, new MyTextHttpResponseHandler());

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
            GlobalClass.showLoading(AboutUS.this);
            Log.d(TAG, "onStart");

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

                        JSONObject jsonObject = mResponse.getJSONObject("Result");
                        switch (value) {

                            case "about":
                                String aboutUs = jsonObject.getString("aboutUs");
                                text_tv.setText(stripHtml(aboutUs));
                                break;
                            case "terms":
                                String termsOfUse = jsonObject.getString("termsOfUse");
                                text_tv.setText(stripHtml(termsOfUse));

                                break;
                            case "privacy":
                                String privacy = jsonObject.getString("privacy");
                                text_tv.setText(stripHtml(privacy));
                                break;


                        }


                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(AboutUS.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


    public String stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(html).toString();
        }
    }
}

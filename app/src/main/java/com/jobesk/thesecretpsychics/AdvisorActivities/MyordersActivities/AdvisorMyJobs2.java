package com.jobesk.thesecretpsychics.AdvisorActivities.MyordersActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorShowVideo;
import com.jobesk.thesecretpsychics.Model.AdvisorOrdersModel;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.CircleTransform;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.Urls;
import com.jobesk.thesecretpsychics.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdvisorMyJobs2 extends AppCompatActivity {
    private ArrayList<AdvisorOrdersModel> jobsArrayList;
    private String datapos, position;
    private TextView counter_tv;
    private ImageView back_img;
    private TextView toolbar_title, userName, heading_tv, body_tv, repondTorequest_tv, date_tv;
    private ImageView userImage;
    private String orderID, isCompleted, videoLink, videoLinkReply, replyHeading, userID, advisorID, replyDetails;
    private String TAG = "AdvisorMyJobs2";
    private LinearLayout frameContainer;
    private RelativeLayout videoContainer;
    private TextView attachVideo_tv, no_video_attached_tv;
    private CircleImageView imageViewVideo;
    private LinearLayout videoReplyContainer;
    private CircleImageView imageViewVideoReply;
    private TextView commentsText_tv;
    private LinearLayout replyContainer;
    private RelativeLayout replyContainerVideoClick;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    private TextView declineTv;
    private TextView advisor_reply_Tv;
    private RelativeLayout reasonContainer;
    private TextView reason_tv;
    private AlertDialog alertDecline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jobs2);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_title = findViewById(R.id.toolbar_title);


        Bundle args = getIntent().getExtras();
        position = args.getString("position");
        datapos = args.getString("datapos");
        jobsArrayList = (ArrayList<AdvisorOrdersModel>) args.getSerializable("jobsArrayList");

        Log.d("poition", position + "");
        String name = jobsArrayList.get(Integer.valueOf(datapos)).getName();
        String body = jobsArrayList.get(Integer.valueOf(datapos)).getBody();
        String heading = jobsArrayList.get(Integer.valueOf(datapos)).getHeading();
        String image = jobsArrayList.get(Integer.valueOf(datapos)).getUserImage();
        String date = jobsArrayList.get(Integer.valueOf(datapos)).getDate();
        orderID = jobsArrayList.get(Integer.valueOf(datapos)).getId();
        isCompleted = jobsArrayList.get(Integer.valueOf(datapos)).getIsCompleted();
        videoLink = jobsArrayList.get(Integer.valueOf(datapos)).getVideLink();
        videoLinkReply = jobsArrayList.get(Integer.valueOf(datapos)).getReplyVideo();
        replyHeading = jobsArrayList.get(Integer.valueOf(datapos)).getHeading();
        replyDetails = jobsArrayList.get(Integer.valueOf(datapos)).getReplyDetails();
        replyDetails = jobsArrayList.get(Integer.valueOf(datapos)).getReplyDetails();
        advisorID = jobsArrayList.get(Integer.valueOf(datapos)).getAdvisorID();
        userID = jobsArrayList.get(Integer.valueOf(datapos)).getUserID();

        reason_tv = findViewById(R.id.reason_tv);

        reasonContainer = findViewById(R.id.reasonContainer);
        counter_tv = findViewById(R.id.counter_tv);
        userImage = findViewById(R.id.userImage);
        userName = findViewById(R.id.userName);
        heading_tv = findViewById(R.id.heading_tv);
        body_tv = findViewById(R.id.body_tv);
        date_tv = findViewById(R.id.date_tv);
        repondTorequest_tv = findViewById(R.id.repondTorequest_tv);

        advisor_reply_Tv = findViewById(R.id.advisor_reply_Tv);
        toolbar_title.setText(jobsArrayList.get(Integer.valueOf(datapos)).getName());

        Picasso.with(getApplicationContext()).load(Urls.IMAGE_BASEURL + "" + image).fit().centerCrop().transform(new CircleTransform()).into(userImage);

        userName.setText(name);
        heading_tv.setText(heading);
        body_tv.setText(body);

        String modifiedDate = GlobalClass.parseDate(date, getApplicationContext());
        date_tv.setText(modifiedDate);

        videoContainer = findViewById(R.id.videoContainer);
        counter_tv.setText(String.valueOf(Integer.valueOf(position)));
        frameContainer = findViewById(R.id.frameContainer);
        declineTv = findViewById(R.id.declineTv);
        replyContainerVideoClick = findViewById(R.id.replyContainerVideoClick);


        repondTorequest_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent itemIntent = new Intent(getApplicationContext(), AdvisorMyJobs3.class);
                Bundle bundle = new Bundle();
                bundle.putString("position", datapos + "");
                bundle.putSerializable("jobsArrayList", jobsArrayList);
                itemIntent.putExtras(bundle);


                startActivityForResult(itemIntent, SECOND_ACTIVITY_REQUEST_CODE);

            }
        });

        replyContainer = findViewById(R.id.replyContainer);
        imageViewVideoReply = findViewById(R.id.imageViewVideoReply);
        videoReplyContainer = findViewById(R.id.videoReplyContainer);
        imageViewVideo = findViewById(R.id.imageViewVideo);
        no_video_attached_tv = findViewById(R.id.attachVideo_tv);
        attachVideo_tv = findViewById(R.id.attachVideo_tv);


        commentsText_tv = findViewById(R.id.commentsText_tv);

        if (videoLink.equalsIgnoreCase("0")) {

            attachVideo_tv.setVisibility(View.GONE);

            attachVideo_tv.setText(getApplicationContext().getResources().getString(R.string.attach_video));
            no_video_attached_tv.setText(getApplicationContext().getResources().getString(R.string.no_video_attached));
            no_video_attached_tv.setVisibility(View.VISIBLE);
            videoContainer.setVisibility(View.GONE);
        } else {

            attachVideo_tv.setVisibility(View.VISIBLE);
            videoContainer.setVisibility(View.VISIBLE);

            no_video_attached_tv.setText(getApplicationContext().getResources().getString(R.string.attached_video));
            no_video_attached_tv.setVisibility(View.GONE);


            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.user_image_general_info);
            requestOptions.error(R.drawable.user_image_general_info);
            requestOptions.encodeQuality(50);


            String videoThumb = Urls.BASEURL + videoLink;

            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(videoThumb)
                    .apply(requestOptions)

                    .into(imageViewVideo);


        }


        if (isCompleted.equalsIgnoreCase("1")) {
            counter_tv.setBackgroundResource(R.drawable.circle_green);
            repondTorequest_tv.setVisibility(View.GONE);
            declineTv.setVisibility(View.GONE);
            replyContainer.setVisibility(View.VISIBLE);
            frameContainer.setBackgroundResource(R.drawable.rounded_green);


            if (videoLinkReply.equalsIgnoreCase("0")) {
                videoReplyContainer.setVisibility(View.GONE);

            } else {
                videoReplyContainer.setVisibility(View.VISIBLE);

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.user_image_general_info);
                requestOptions.error(R.drawable.user_image_general_info);
                requestOptions.encodeQuality(50);
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(Urls.BASEURL + videoLinkReply)
                        .apply(requestOptions)
                        .into(imageViewVideoReply);


            }
            commentsText_tv.setVisibility(View.VISIBLE);
            commentsText_tv.setText(replyDetails);
        } else if (isCompleted.equalsIgnoreCase("3")) {
            counter_tv.setBackgroundResource(R.drawable.circle_red);
            replyContainer.setVisibility(View.GONE);
            commentsText_tv.setVisibility(View.GONE);
            frameContainer.setBackgroundResource(R.drawable.rounded_red);
            repondTorequest_tv.setVisibility(View.GONE);
            declineTv.setVisibility(View.GONE);
            advisor_reply_Tv.setText(getApplicationContext().getResources().getString(R.string.order_delclined));
            reason_tv.setText(replyDetails);
            reasonContainer.setVisibility(View.VISIBLE);
        } else {


            counter_tv.setBackgroundResource(R.drawable.circle_blue);
            replyContainer.setVisibility(View.GONE);
            commentsText_tv.setVisibility(View.GONE);
            setSeen();
        }


        videoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent videoIntent = new Intent(getApplicationContext(), AdvisorShowVideo.class);
                Bundle bundle = new Bundle();
                bundle.putString("videoLink", Urls.BASEURL + "" + videoLink);
                videoIntent.putExtras(bundle);
                startActivity(videoIntent);
            }
        });


        replyContainerVideoClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoIntent = new Intent(getApplicationContext(), AdvisorShowVideo.class);
                Bundle bundle = new Bundle();
                bundle.putString("videoLink", Urls.BASEURL + "" + videoLinkReply);
                videoIntent.putExtras(bundle);
                startActivity(videoIntent);
            }
        });


        declineTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showDeclineAlert();
            }
        });

    }


    private void setSeen() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("isSeen", "1");


            String AdvisorID = GlobalClass.getPref("advisorID", getApplicationContext());
            WebReq.post(getApplicationContext(), "updateOrderSeen/" + orderID + "?isSeen=1", mParams, new MyTextHttpResponseHandler());

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

            Log.d(TAG, "onStart");

        }

        @Override
        public void onFinish() {
            super.onFinish();


            Log.d(TAG, "onFinish");

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d(TAG, "OnFailure" + e);

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d(TAG, responseString);

        }


        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {


            Log.d(TAG, mResponse.toString() + "Respo");
            if (mResponse != null && mResponse.length() != 0) {


            }
        }
    }

    // This method is called when the second activity finishes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK

                // get String data from Intent
                String returnString = data.getStringExtra("keyName");


                if (returnString.equalsIgnoreCase("1")) {


                    finish();
                }

            }
        }
    }


    private void setDecline(String declineValue) {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
            mParams.put("isCompleted", "3");
            mParams.put("reply_details", declineValue);
            mParams.put("advisorId", advisorID);
            mParams.put("userId", userID);

            WebReq.post(getApplicationContext(), "updateOrderStatus/" + orderID, mParams, new MyTextHttpResponseHandlerDecline());

        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerDecline extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerDecline() {


        }

        @Override
        public void onStart() {
            super.onStart();


            GlobalClass.showLoading(AdvisorMyJobs2.this);

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

                finish();

            }
        }
    }


    public void showDeclineAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AdvisorMyJobs2.this);
        dialogBuilder.setCancelable(false);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogView = inflater.inflate(R.layout.decline_alert, null);
        dialogBuilder.setView(dialogView);


        final EditText declineTv = (EditText) dialogView.findViewById(R.id.decline_et);


        TextView submitReason_tv = (TextView) dialogView.findViewById(R.id.submitReason_tv);
        TextView cancel_tv = (TextView) dialogView.findViewById(R.id.cancel_tv);


        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                alertDecline.dismiss();
            }
        });

        submitReason_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String declinevalue = declineTv.getText().toString().trim();

                if (declinevalue.equalsIgnoreCase("")) {

                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.enter_decline_text), Toast.LENGTH_SHORT).show();
                    return;

                }


                alertDecline.dismiss();
                setDecline(declinevalue);
            }
        });
//
//        dialogBuilder.setTitle("Custom dialog");
//        dialogBuilder.setMessage("Enter text below");
//        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                //do something with edt.getText().toString();
//            }
//        });
//        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                //pass
//            }
//        });
        alertDecline = dialogBuilder.create();
        alertDecline.show();
    }

}

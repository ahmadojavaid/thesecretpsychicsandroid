package com.jobesk.thesecretpsychics.ClientActivities.ClientsOrders;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorShowVideo;
import com.jobesk.thesecretpsychics.ClientActivities.Chat.ClientGetChat;
import com.jobesk.thesecretpsychics.ClientActivities.ClientHostProfileActivity;
import com.jobesk.thesecretpsychics.ClientActivities.ClientReviewActivity;
import com.jobesk.thesecretpsychics.Model.ClientOrdersModel;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.CircleTransform;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.Urls;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClientJobs2 extends AppCompatActivity {
    private String dataPos, position;
    private ArrayList<ClientOrdersModel> jobsArrayList;
    private String orderID, isCompleted, videoLink, videoLinkReply, replyHeading, replyDetails;
    private ImageView back_img;
    private TextView toolbar_title;
    private RelativeLayout videoContainer;
    private LinearLayout videoReplyContainer;
    private RelativeLayout replyContainerVideoClick;
    private ImageView imageViewVideoReply;
    private TextView userName, heading_tv, body_tv, attachVideo_tv, no_video_attached_tv, commentsText_tv;
    ImageView userImage;
    private TextView date_tv, counter_tv;
    private String date, TextChatRate, isOnline, AdvisorID, isLiveChat, isRewiewed, advisorID;
    private LinearLayout frameContainer;
    private LinearLayout replyContainer;
    private TextView no_response_tv, review_tv;
    private RelativeLayout reasonContainer;
    private TextView reason_tv;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    private RelativeLayout reviewContainer;
    private String isReviewed;
    private LinearLayout live_chat_container;
    private String name;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinet_jobs2);

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
        dataPos = args.getString("dataPos");
        jobsArrayList = (ArrayList<ClientOrdersModel>) args.getSerializable("jobsArrayList");

        Log.d("poition", position + "");
        name = jobsArrayList.get(Integer.valueOf(dataPos)).getName() + "";
        String body = jobsArrayList.get(Integer.valueOf(dataPos)).getOrder_details() + "";
        String heading = jobsArrayList.get(Integer.valueOf(dataPos)).getOrder_heading() + "";
        image = jobsArrayList.get(Integer.valueOf(dataPos)).getImage() + "";
        date = jobsArrayList.get(Integer.valueOf(dataPos)).getCreated_at() + "";
        orderID = jobsArrayList.get(Integer.valueOf(dataPos)).getId() + "";
        isCompleted = jobsArrayList.get(Integer.valueOf(dataPos)).getIsCompleted() + "";
        videoLink = jobsArrayList.get(Integer.valueOf(dataPos)).getOrder_video() + "";
        videoLinkReply = jobsArrayList.get(Integer.valueOf(dataPos)).getReply_Video() + "";
        replyHeading = jobsArrayList.get(Integer.valueOf(dataPos)).getReply_heading() + "";
        replyDetails = jobsArrayList.get(Integer.valueOf(dataPos)).getReply_details() + "";
        advisorID = jobsArrayList.get(Integer.valueOf(dataPos)).getAdvisorId() + "";
        isRewiewed = jobsArrayList.get(Integer.valueOf(dataPos)).getIsReviewed() + "";
        isLiveChat = jobsArrayList.get(Integer.valueOf(dataPos)).getIsLiveChat() + "";
        AdvisorID = jobsArrayList.get(Integer.valueOf(dataPos)).getAdvisorId() + "";
        isOnline = jobsArrayList.get(Integer.valueOf(dataPos)).getIsOnline() + "";
        TextChatRate = jobsArrayList.get(Integer.valueOf(dataPos)).getTextChatRate() + "";


        reasonContainer = findViewById(R.id.reasonContainer);
        reason_tv = findViewById(R.id.reason_tv);

        toolbar_title.setText(name);

        replyContainer = findViewById(R.id.replyContainer);
        userImage = findViewById(R.id.userImage);

        Picasso.with(getApplicationContext()).load(Urls.IMAGE_BASEURL + image).transform(new CircleTransform()).fit().centerCrop().into(userImage);

        userName = findViewById(R.id.userName);

        userName.setText(name);
        heading_tv = findViewById(R.id.heading_tv);
        heading_tv.setText(heading);

        body_tv = findViewById(R.id.body_tv);
        body_tv.setText(body);


        no_response_tv = findViewById(R.id.no_response_tv);
        date_tv = findViewById(R.id.date_tv);


        date_tv.setText(GlobalClass.parseDate(date, getApplicationContext()));

        attachVideo_tv = findViewById(R.id.attachVideo_tv);
        no_video_attached_tv = findViewById(R.id.no_video_attached_tv);
        frameContainer = findViewById(R.id.frameContainer);
        reviewContainer = findViewById(R.id.reviewContainer);


        live_chat_container = findViewById(R.id.live_chat_container);

        live_chat_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String clientID = GlobalClass.getPref("clientID", getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), ClientGetChat.class);
                Bundle bundle = new Bundle();
                bundle.putString("sendTo", AdvisorID);
                bundle.putString("sendBy", clientID);
                bundle.putString("preName", name);
                bundle.putString("preImage", image);
                bundle.putString("preOnline", isOnline);
                bundle.putString("preChatRate", TextChatRate);

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        review_tv = findViewById(R.id.review_tv);

        review_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ClientJobs2.this, ClientReviewActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("advisorID", advisorID);
                bundle.putString("orderID", orderID);
                i.putExtras(bundle);
                startActivityForResult(i, SECOND_ACTIVITY_REQUEST_CODE);


            }
        });


        commentsText_tv = findViewById(R.id.commentsText_tv);
        counter_tv = findViewById(R.id.counter_tv);

        counter_tv.setText(String.valueOf(position));
        imageViewVideoReply = findViewById(R.id.imageViewVideoReply);

        videoContainer = findViewById(R.id.videoContainer);

        CircleImageView imageViewVideo;

        imageViewVideo = findViewById(R.id.imageViewVideo);


        replyContainerVideoClick = findViewById(R.id.replyContainerVideoClick);


        videoReplyContainer = findViewById(R.id.videoReplyContainer);


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


            if (isLiveChat.equals("1")) {
                live_chat_container.setVisibility(View.VISIBLE);
            } else {

                live_chat_container.setVisibility(View.GONE);
            }


            reviewContainer.setVisibility(View.VISIBLE);
            no_response_tv.setVisibility(View.GONE);
            counter_tv.setBackgroundResource(R.drawable.circle_green);

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
        } else if (isCompleted.equalsIgnoreCase("0")) {
            no_response_tv.setVisibility(View.VISIBLE);
            reviewContainer.setVisibility(View.GONE);
            counter_tv.setBackgroundResource(R.drawable.circle_blue);
            replyContainer.setVisibility(View.GONE);
            commentsText_tv.setVisibility(View.GONE);

        } else if (isCompleted.equalsIgnoreCase("3")) {

            counter_tv.setBackgroundResource(R.drawable.circle_red);

            frameContainer.setBackgroundResource(R.drawable.rounded_red);
            no_response_tv.setText(getApplicationContext().getResources().getString(R.string.order_delclined));
            reason_tv.setText(replyDetails);
            reasonContainer.setVisibility(View.VISIBLE);
            replyContainer.setVisibility(View.GONE);
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


        replyContainerVideoClick = findViewById(R.id.replyContainerVideoClick);
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


        if (isRewiewed.equalsIgnoreCase("1")) {
            reviewContainer.setVisibility(View.GONE);
        } else {

            if (!isCompleted.equalsIgnoreCase("1")) {
                reviewContainer.setVisibility(View.GONE);
            } else {
                reviewContainer.setVisibility(View.VISIBLE);
            }


        }


        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(getApplicationContext(), ClientHostProfileActivity.class);
                Bundle b = new Bundle();
                b.putString("id", advisorID);
                profileIntent.putExtras(b);
                startActivity(profileIntent);
            }
        });

        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(getApplicationContext(), ClientHostProfileActivity.class);
                Bundle b = new Bundle();
                b.putString("id", advisorID);
                profileIntent.putExtras(b);
                startActivity(profileIntent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK

                // get String data from Intent
                String returnString = data.getStringExtra("keyName");

                if (returnString.equalsIgnoreCase("1")) {
                    reviewContainer.setVisibility(View.GONE);

                }


            }
        }
    }

    @Override
    protected void onDestroy() {

        GlobalClass.hideKeyboard(ClientJobs2.this);
        super.onDestroy();
    }
}

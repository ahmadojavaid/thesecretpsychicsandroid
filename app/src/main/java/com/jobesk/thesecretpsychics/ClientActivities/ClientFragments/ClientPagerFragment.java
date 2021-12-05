package com.jobesk.thesecretpsychics.ClientActivities.ClientFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jobesk.thesecretpsychics.ClientActivities.ClientHostProfileActivity;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.Urls;
import com.squareup.picasso.Picasso;

public class ClientPagerFragment extends Fragment {
    private ImageView userImage;
    private TextView nameTv, serViceNameTv;
    private RatingBar rattingBar;
    private CardView card_view;
    private String id;
    private ImageView online_img, chat_img;
    private String clientID, image, title, isOnline, videoLink;
    ImageView ic_video;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.client_row_pager, container, false);

        clientID = GlobalClass.getPref("clientID", getActivity());
        id = getArguments().getString("id");
        isOnline = getArguments().getString("isOnline");
        String serViceNmae = getArguments().getString("serviceName");
        title = getArguments().getString("title");
        image = getArguments().getString("image");
        videoLink = getArguments().getString("videoLink");
        String description = getArguments().getString("description");
        String ratting = getArguments().getString("ratting");
        String isLiveChat = getArguments().getString("isLiveChat");
        String isVideoMsg = getArguments().getString("isVideoMsg");
        String screenName = getArguments().getString("screenName");


        chat_img = rootView.findViewById(R.id.chat_img);
        userImage = rootView.findViewById(R.id.userImage);
        ic_video = rootView.findViewById(R.id.ic_video);
        online_img = rootView.findViewById(R.id.online_img);


        if (isOnline.equalsIgnoreCase("1")) {
            online_img.setImageResource(R.drawable.ic_online);
        } else {
            online_img.setImageResource(R.drawable.ic_offline);

        }




        if (isLiveChat.equalsIgnoreCase("1")) {
            chat_img.setVisibility(View.VISIBLE);
        } else {
            chat_img.setVisibility(View.GONE);
        }
        if (isVideoMsg.equalsIgnoreCase("1")) {
            ic_video.setVisibility(View.VISIBLE);
        } else {
            ic_video.setVisibility(View.GONE);
        }


//        chat_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                Intent intent = new Intent(getActivity(), ClientGetChat.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("sendTo", id);
//                bundle.putString("sendBy", clientID);
//                bundle.putString("preName", title);
//                bundle.putString("preImage", image);
//                bundle.putString("preOnline", isOnline);
//                intent.putExtras(bundle);
//                startActivity(intent);
//
//
//            }
//        });


        Log.d("valueFrag", title);

        Picasso.with(getActivity()).load(Urls.IMAGE_BASEURL + image).fit().centerCrop().into(userImage);


        nameTv = rootView.findViewById(R.id.nameTv);
        serViceNameTv = rootView.findViewById(R.id.serViceNameTv);

        nameTv.setText(screenName);
        serViceNameTv.setText(serViceNmae);

        rattingBar = rootView.findViewById(R.id.rattingBar);

        rattingBar.setRating(Float.valueOf(ratting));


        card_view = rootView.findViewById(R.id.card_view);
        card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent profileIntent = new Intent(getActivity(), ClientHostProfileActivity.class);
                Bundle b = new Bundle();
                b.putString("id", id);
                profileIntent.putExtras(b);
                startActivity(profileIntent);
            }
        });


//        ic_video.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                Intent videoIntent = new Intent(getActivity(), AdvisorShowVideo.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("videoLink", Urls.BASEURL + "" + videoLink);
//                videoIntent.putExtras(bundle);
//                startActivity(videoIntent);
//
//
//            }
//        });


        RelativeLayout layout = (RelativeLayout)rootView.findViewById(R.id.layoutCard);
        layout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v(null, "TOUCH EVENT"); // handle your fragment number here
                return false;
            }
        });


        return rootView;
    }
}

package com.jobesk.thesecretpsychics.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jobesk.thesecretpsychics.ClientActivities.ClientDrawerActivity;
import com.jobesk.thesecretpsychics.ClientActivities.ClientHostProfileActivity;
import com.jobesk.thesecretpsychics.Model.ClientFavouriteAdvisors;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.Urls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ClientFavouriteAdvisorsAdapter extends RecyclerView.Adapter<ClientFavouriteAdvisorsAdapter.MyViewHolder> {

    private ArrayList<ClientFavouriteAdvisors> arrayListPsychic;


    private Activity activity;
    Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, service_name_tv;
        private ImageView imageViewUser;
        private LinearLayout rootLayout;
        private RatingBar AVL_rating;
        private ImageView chat_img;
        ImageView ic_video;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            service_name_tv = (TextView) view.findViewById(R.id.designation);
            imageViewUser = (ImageView) view.findViewById(R.id.imageViewUser);
            rootLayout = view.findViewById(R.id.rootLayout);
            chat_img = view.findViewById(R.id.chat_img);
            AVL_rating = view.findViewById(R.id.AVL_rating);
            ic_video = view.findViewById(R.id.ic_video);

        }
    }


    public ClientFavouriteAdvisorsAdapter(Activity activity, Context context, ArrayList<ClientFavouriteAdvisors> arrayListPsychic) {
        this.arrayListPsychic = arrayListPsychic;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_row_psychic_favourtite, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        int width = ClientDrawerActivity.width / 2 - 15;

        ViewGroup.LayoutParams layoutParams = holder.rootLayout.getLayoutParams();
        layoutParams.width = width;
        holder.rootLayout.setLayoutParams(layoutParams);


        final ClientFavouriteAdvisors model = arrayListPsychic.get(position);
        holder.title.setText(model.getName());
        holder.service_name_tv.setText(model.getServiceName());


        String value = model.getRatting();
        String[] parts = value.split("\\.");
        String ratting = parts[0];


        holder.AVL_rating.setRating(Integer.valueOf(ratting));


        Picasso.with(context).load(Urls.IMAGE_BASEURL + "" + model.getImage()).fit().centerCrop().into(holder.imageViewUser);


        String liveChat = model.getLiveChat();
        String videoMsg = model.getVideoMsg();

        if (liveChat.equalsIgnoreCase("1")) {
            holder.chat_img.setVisibility(View.VISIBLE);
        } else {
            holder.chat_img.setVisibility(View.GONE);
        }
        if (videoMsg.equalsIgnoreCase("1")) {
            holder.ic_video.setVisibility(View.VISIBLE);
        } else {
            holder.ic_video.setVisibility(View.VISIBLE);
        }



//        holder.chat_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String clientID = GlobalClass.getPref("clientID", activity);
//                Intent intent = new Intent(activity, ClientGetChat.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("sendTo",model.getAdvisorID() );
//                bundle.putString("sendBy", clientID );
//                bundle.putString("preName", model.getName());
//                bundle.putString("preImage", model.getImage());
//                bundle.putString("preOnline", model.getIsOnline());
//                intent.putExtras(bundle);
//                activity.startActivity(intent);
//
//            }
//        });
//
//
//        holder.ic_video.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                Intent videoIntent = new Intent(activity, AdvisorShowVideo.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("videoLink", Urls.BASEURL + "" + model.getVideoUrl());
//                videoIntent.putExtras(bundle);
//                activity.startActivity(videoIntent);
//
//
//            }
//        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent profileIntent = new Intent(context, ClientHostProfileActivity.class);
                Bundle b = new Bundle();
                b.putString("id", model.getAdvisorID());
                profileIntent.putExtras(b);
                context.startActivity(profileIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayListPsychic.size();
    }
}
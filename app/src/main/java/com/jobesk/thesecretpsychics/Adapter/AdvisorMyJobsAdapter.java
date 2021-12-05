package com.jobesk.thesecretpsychics.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorShowVideo;
import com.jobesk.thesecretpsychics.AdvisorActivities.MyordersActivities.AdvisorMyJobs2;
import com.jobesk.thesecretpsychics.Model.AdvisorOrdersModel;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.CircleTransform;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.Urls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdvisorMyJobsAdapter extends RecyclerView.Adapter<AdvisorMyJobsAdapter.MyViewHolder> {

    private ArrayList<AdvisorOrdersModel> jobsListData;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, counter_tv, heading_tv, body_tv, date_tv;
        ImageView userImage, video_icon;
        LinearLayout rounded_root;

        public MyViewHolder(View view) {
            super(view);

            userName = (TextView) view.findViewById(R.id.userName);
            date_tv = (TextView) view.findViewById(R.id.date_tv);
            body_tv = (TextView) view.findViewById(R.id.body_tv);
            heading_tv = (TextView) view.findViewById(R.id.heading_tv);
            counter_tv = (TextView) view.findViewById(R.id.counter_tv);
            userImage = (ImageView) view.findViewById(R.id.userImage);
            video_icon = (ImageView) view.findViewById(R.id.video_icon);
            rounded_root = view.findViewById(R.id.rounded_root);
        }
    }

    public AdvisorMyJobsAdapter(Context context, ArrayList<AdvisorOrdersModel> jobsListData) {
        this.jobsListData = jobsListData;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.advisor_row_jobs, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final AdvisorOrdersModel jobmodel = jobsListData.get(position);

        //        holder.title.setText(movie.getTitle());


        final int value = jobsListData.size() - position;
        holder.counter_tv.setText(String.valueOf(value));


        holder.userName.setText(jobmodel.getName());
        holder.heading_tv.setText(jobmodel.getHeading());
        holder.body_tv.setText(jobmodel.getBody());


        String modifiedDate = GlobalClass.parseDate(jobmodel.getDate(),context);
        holder.date_tv.setText(modifiedDate);
        String userImageLink = Urls.IMAGE_BASEURL + "" + jobmodel.getUserImage();
        Picasso.with(context).load(userImageLink).fit().centerCrop().transform(new CircleTransform()).into(holder.userImage);


        String videoLink = jobmodel.getVideLink();


        if (videoLink.equalsIgnoreCase("0")) {
            holder.video_icon.setVisibility(View.GONE);
        } else {
            holder.video_icon.setVisibility(View.VISIBLE);
        }

        String getCompleted = jobmodel.getIsCompleted();

        if (getCompleted.equalsIgnoreCase("0")) {
            holder.counter_tv.setBackgroundResource(R.drawable.circle_blue);
            holder.rounded_root.setBackgroundResource(R.drawable.rounded_blue);
        } else if (getCompleted.equalsIgnoreCase("3")) {
            holder.counter_tv.setBackgroundResource(R.drawable.circle_red);
            holder.rounded_root.setBackgroundResource(R.drawable.rounded_red);
        } else {
            holder.rounded_root.setBackgroundResource(R.drawable.rounded_green);
            holder.counter_tv.setBackgroundResource(R.drawable.circle_green);
        }


        holder.video_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent videoIntent = new Intent(context, AdvisorShowVideo.class);
                Bundle bundle = new Bundle();
                bundle.putString("videoLink", Urls.BASEURL + "" + jobmodel.getVideLink());
                videoIntent.putExtras(bundle);
                context.startActivity(videoIntent);

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent itemIntent = new Intent(context, AdvisorMyJobs2.class);
                Bundle bundle = new Bundle();
                bundle.putString("position", value + "");
                bundle.putString("datapos", position + "");
                bundle.putSerializable("jobsArrayList", jobsListData);
                itemIntent.putExtras(bundle);
                context.startActivity(itemIntent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return jobsListData.size();
    }
}
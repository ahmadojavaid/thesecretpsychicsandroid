package com.jobesk.thesecretpsychics.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jobesk.thesecretpsychics.ClientActivities.ClientsOrders.ClientJobs2;
import com.jobesk.thesecretpsychics.Model.ClientOrdersModel;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.CircleTransform;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.TimeSince;
import com.jobesk.thesecretpsychics.Utils.Urls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ClientOrdersAdapter extends RecyclerView.Adapter<ClientOrdersAdapter.MyViewHolder> {

    private ArrayList<ClientOrdersModel> jobsListData;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView counter_tv, username_tv, heading_tv, date_tv, order_detail_tv;
        ImageView categoryImage, status_img;
        LinearLayout rounded_root;
        private ImageView video_icon;

        public MyViewHolder(View view) {
            super(view);
            username_tv = (TextView) view.findViewById(R.id.username_tv);
            counter_tv = (TextView) view.findViewById(R.id.counter_tv);

            heading_tv = (TextView) view.findViewById(R.id.heading_tv);
            date_tv = (TextView) view.findViewById(R.id.date_tv);
            order_detail_tv = (TextView) view.findViewById(R.id.order_detail_tv);


            categoryImage = view.findViewById(R.id.categoryImage);
            status_img = view.findViewById(R.id.status_img);
            rounded_root = view.findViewById(R.id.rounded_root);
            video_icon = view.findViewById(R.id.video_icon);


        }
    }

    public ClientOrdersAdapter(Context context, ArrayList<ClientOrdersModel> jobsListData) {
        this.jobsListData = jobsListData;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_row_orders, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ClientOrdersModel model = jobsListData.get(position);


        final int value = jobsListData.size() - position;
        holder.counter_tv.setText(String.valueOf(value));


        String isCompleted = model.getIsCompleted();


        holder.username_tv.setText(model.getName());


        holder.heading_tv.setText(model.getOrder_heading());


        String modifiedDate = GlobalClass.parseDate(model.getCreated_at(), context);
        holder.date_tv.setText(modifiedDate);

        holder.order_detail_tv.setText(model.getOrder_details());
        holder.order_detail_tv.setText(model.getOrder_details());

        String userimage = Urls.IMAGE_BASEURL + model.getImage();


        Log.d("caticon", userimage);
        Picasso.with(context).load(userimage).transform(new CircleTransform()).fit().placeholder(R.drawable.user_image_placeholder).centerCrop().into(holder.categoryImage);


        String isSeen = model.getIsSeen();


        if (isSeen.equalsIgnoreCase("1")) {
            holder.status_img.setImageResource(R.drawable.ic_tick_double);

        } else {
            holder.status_img.setImageResource(R.drawable.ic_tick_white);
        }


        String isCpmpleted = model.getIsCompleted();

        if (isCpmpleted.equalsIgnoreCase("0")) {
            holder.rounded_root.setBackgroundResource(R.drawable.rounded_blue);
            holder.counter_tv.setBackgroundResource(R.drawable.circle_blue);
        } else if (isCpmpleted.equalsIgnoreCase("3")) {
            holder.status_img.setImageResource(R.drawable.ic_tick_double);
            holder.rounded_root.setBackgroundResource(R.drawable.rounded_red);
            holder.counter_tv.setBackgroundResource(R.drawable.circle_red);
        } else {
            holder.status_img.setImageResource(R.drawable.ic_tick_double);
            holder.rounded_root.setBackgroundResource(R.drawable.rounded_green);
            holder.counter_tv.setBackgroundResource(R.drawable.circle_green);
        }


        String isVideo = model.getReply_Video();
        if (isVideo.equalsIgnoreCase("0")) {
            holder.video_icon.setVisibility(View.GONE);

        } else {
            holder.video_icon.setVisibility(View.VISIBLE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent itemIntent = new Intent(context, ClientJobs2.class);
                Bundle bundle = new Bundle();

                bundle.putString("position", value + "");
                bundle.putString("dataPos", position + "");
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
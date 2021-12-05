package com.jobesk.thesecretpsychics.ClientActivities.Chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.CircleTransform;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.Urls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ClientConversationAdapter extends RecyclerView.Adapter<ClientConversationAdapter.MyViewHolder> {

    private Activity activity;
    private ArrayList<GetconversationModel> arrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date_tv, body_tv, userName_tv, chat_counter_tv;
        ImageView userImage;

        public MyViewHolder(View view) {
            super(view);
            date_tv = (TextView) view.findViewById(R.id.date_tv);
            body_tv = (TextView) view.findViewById(R.id.body_tv);
            userName_tv = (TextView) view.findViewById(R.id.userName_tv);
            chat_counter_tv = (TextView) view.findViewById(R.id.chat_counter_tv);
            userImage = (ImageView) view.findViewById(R.id.userImage);

        }
    }


    public ClientConversationAdapter(Activity activity, ArrayList<GetconversationModel> arrayList) {
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_row_conversation, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final GetconversationModel model = arrayList.get(position);

        holder.body_tv.setText(model.getMessage());
        holder.userName_tv.setText(model.getName());

        Picasso.with(activity).load(Urls.IMAGE_BASEURL + model.getProfileImage()).transform(new CircleTransform()).fit().centerCrop().into((holder.userImage));

        String date = model.getDate();
        holder.date_tv.setText(GlobalClass.parseDate(date, activity));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, ClientGetChat.class);
                Bundle bundle = new Bundle();
                bundle.putString("sendTo", model.getSentTo());
                bundle.putString("sendBy", model.getSentBy());
                bundle.putString("preName", model.getName());
                bundle.putString("preImage", model.getProfileImage());
                bundle.putString("preOnline", model.getIsOnline());
                bundle.putString("preChatRate", model.getChatRateText());
                intent.putExtras(bundle);
                activity.startActivity(intent);


            }
        });
        String chatCount = model.getCounter();

        if (chatCount.equals("0")) {
            holder.chat_counter_tv.setVisibility(View.GONE);
        } else if (Integer.valueOf(chatCount) > 9) {
//            holder.chat_counter_tv.setText(activity.getApplicationContext().getResources().getString(R.string.nine_plus));
            holder.chat_counter_tv.setVisibility(View.VISIBLE);
        } else {
            holder.chat_counter_tv.setVisibility(View.VISIBLE);
//            holder.chat_counter_tv.setText(chatCount);
//            holder.chat_counter_tv.setText(chatCount);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
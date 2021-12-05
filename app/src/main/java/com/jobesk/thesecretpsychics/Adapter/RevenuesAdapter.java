package com.jobesk.thesecretpsychics.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jobesk.thesecretpsychics.AdvisorActivities.RevenueDetailActivity;
import com.jobesk.thesecretpsychics.Model.RevenueModel;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;

import java.util.ArrayList;

public class RevenuesAdapter extends RecyclerView.Adapter<RevenuesAdapter.MyViewHolder> {

    private ArrayList<RevenueModel> arrayList;
    private Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title_tv, amount_tv, date_tv;

        public MyViewHolder(View view) {
            super(view);
            title_tv = (TextView) view.findViewById(R.id.title_tv);
            amount_tv = (TextView) view.findViewById(R.id.amount_tv);
            date_tv = (TextView) view.findViewById(R.id.date_tv);

        }
    }


    public RevenuesAdapter(Activity activity, ArrayList<RevenueModel> arrayList) {
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_revenues, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final RevenueModel model = arrayList.get(position);

        holder.title_tv.setText(model.getRefrence());
        holder.amount_tv.setText(activity.getResources().getString(R.string.pound) + "" + model.getCredit());


        String inputDate = model.getCreated_at();
        String outputdate = GlobalClass.changeDateFormat("yyyy-MM-dd HH:mm:ss", "hh:mm aa  dd-MM-yyyy", inputDate);
        holder.date_tv.setText(outputdate);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(activity, RevenueDetailActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("cash", model.getCredit());
                bundle.putString("dateTime", model.getCreated_at());
                bundle.putString("refernce", model.getRefrence());
                bundle.putString("earning", model.getCredit());
                bundle.putString("Trid", model.getId());

                i.putExtras(bundle);

                activity.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
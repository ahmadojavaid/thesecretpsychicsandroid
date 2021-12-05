package com.jobesk.thesecretpsychics.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jobesk.thesecretpsychics.Model.AdvisorReviewModel;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;

import java.util.ArrayList;

public class AdvisorProfileReviewsAdapter extends RecyclerView.Adapter<AdvisorProfileReviewsAdapter.MyViewHolder> {
    Activity activity;
    private ArrayList<AdvisorReviewModel> reviewsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, content_tv;
        RatingBar reviewRattingbar;
        View line;
        TextView date_Tv;

        public MyViewHolder(View view) {
            super(view);

            userName = (TextView) view.findViewById(R.id.userName);
            content_tv = (TextView) view.findViewById(R.id.content_tv);
            reviewRattingbar = (RatingBar) view.findViewById(R.id.reviewRattingbar);
            line = (View) view.findViewById(R.id.line);
            date_Tv = (TextView) view.findViewById(R.id.date_Tv);


        }
    }

    public AdvisorProfileReviewsAdapter(Activity activity, ArrayList<AdvisorReviewModel> reviewsList) {
        this.reviewsList = reviewsList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.advisor_reviews_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AdvisorReviewModel model = reviewsList.get(position);
        holder.userName.setText(model.getUserName());
        holder.content_tv.setText(model.getBody());
        holder.reviewRattingbar.setRating(Integer.valueOf(model.getRatting()));


        String dateValue = GlobalClass.parseDate(model.getDate(), activity);
        holder.date_Tv.setText(dateValue);

        if (position == reviewsList.size()) {
            holder.line.setVisibility(View.GONE);

        }


    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }
}
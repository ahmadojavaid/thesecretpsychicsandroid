package com.jobesk.thesecretpsychics.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jobesk.thesecretpsychics.Model.HorizontalModel;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SignUpHorizontalRecyclerAdapter extends RecyclerView.Adapter<SignUpHorizontalRecyclerAdapter.HorizontalViewholder> {

    private ArrayList<HorizontalModel> arrayList;
    private Context context;


    public SignUpHorizontalRecyclerAdapter(Context context,
                                           ArrayList<HorizontalModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount() {

        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(HorizontalViewholder holder, final int position) {
        HorizontalModel model = arrayList.get(position);


        Picasso.with(context).load(model.getImage()).transform(new CircleTransform()).into(holder.imageview);


        holder.cardView.setCardBackgroundColor(Integer.valueOf(model.getColor()));
    }

    @Override
    public HorizontalViewholder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.horizontal_listitem, viewGroup, false);
        HorizontalViewholder listHolder = new HorizontalViewholder(mainGroup);

        return listHolder;
    }

    public class HorizontalViewholder extends RecyclerView.ViewHolder {

        public ImageView imageview;
        public CardView cardView;


        public HorizontalViewholder(View view) {
            super(view);
            // Find all views ids


            this.imageview = (ImageView) view.findViewById(R.id.image_view);


            cardView = (CardView) view.findViewById(R.id.horizontal_cardview);


        }
    }
}
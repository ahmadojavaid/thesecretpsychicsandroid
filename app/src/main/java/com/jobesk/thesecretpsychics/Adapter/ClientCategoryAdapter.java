package com.jobesk.thesecretpsychics.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jobesk.thesecretpsychics.ClientActivities.AdvisorSearchByCategory;
import com.jobesk.thesecretpsychics.ClientActivities.ClientCategories;
import com.jobesk.thesecretpsychics.Model.CategoryModel;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.Urls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ClientCategoryAdapter extends RecyclerView.Adapter<ClientCategoryAdapter.MyViewHolder> {

    private ArrayList<CategoryModel> moviesList;
    private Activity activity;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        ImageView imageview;
        private RelativeLayout rootlayoutRow;
        CheckBox checkbox;

        public MyViewHolder(View view) {
            super(view);
            imageview = (ImageView) view.findViewById(R.id.imageview);
            title = (TextView) view.findViewById(R.id.title);
            rootlayoutRow = (RelativeLayout) view.findViewById(R.id.rootlayoutRow);

            checkbox = (CheckBox) view.findViewById(R.id.checkbox);
        }
    }


    public ClientCategoryAdapter(Activity activity, ArrayList<CategoryModel> moviesList) {
        this.moviesList = moviesList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_category_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        int width = ClientCategories.width / 2 - 30;
//        int height = AdvisorCategories.height / 4;

        Log.d("widthHeight", width + "");
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        holder.rootlayoutRow.setLayoutParams(layoutParams);


        final CategoryModel model = moviesList.get(position);
        holder.title.setText(model.getName());
        String imageURl = Urls.IMAGE_BASEURL + "" + model.getImage();

        Log.d("ImageUrl", imageURl);
        Picasso.with(activity).load(imageURl).fit().centerInside().into(holder.imageview);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String catID = model.getId();

                Log.d("catID", catID + "");


                Intent intent = new Intent(activity, AdvisorSearchByCategory.class);

                Bundle bundle = new Bundle();
                bundle.putString("catID", catID);
                intent.putExtras(bundle);

                activity.startActivity(intent);


            }
        });


//        String checkedValue = model.getChecked();
//        if (checkedValue.equals("1")) {
//            holder.checkbox.setChecked(true);
//        } else {
//            holder.checkbox.setChecked(false);
//
//        }
//
//        holder.checkbox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String checkedValue = model.getChecked();
//                if (checkedValue.equals("1")) {
//                    moviesList.get(position).setChecked("0");
//
//                } else {
//                    moviesList.get(position).setChecked("1");
//                }
//                notifyDataSetChanged();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


}
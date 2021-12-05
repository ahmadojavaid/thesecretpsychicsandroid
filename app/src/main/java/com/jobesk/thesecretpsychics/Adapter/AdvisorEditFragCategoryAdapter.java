package com.jobesk.thesecretpsychics.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jobesk.thesecretpsychics.AdvisorActivities.EditProfile.AdvisorEditProfile;
import com.jobesk.thesecretpsychics.Model.CategoryModel;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.Urls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdvisorEditFragCategoryAdapter extends RecyclerView.Adapter<AdvisorEditFragCategoryAdapter.MyViewHolder> {

    private ArrayList<CategoryModel> moviesList;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        ImageView imageview;
        private RelativeLayout rootlayoutRow;
        ImageView checkbox;

        public MyViewHolder(View view) {
            super(view);
            imageview = (ImageView) view.findViewById(R.id.imageview);
            title = (TextView) view.findViewById(R.id.title);
            rootlayoutRow = (RelativeLayout) view.findViewById(R.id.rootlayoutRow);

            checkbox = (ImageView) view.findViewById(R.id.checkbox);
        }
    }


    public AdvisorEditFragCategoryAdapter(Context context, ArrayList<CategoryModel> moviesList) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.advisor_category_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        int width = AdvisorEditProfile.width / 2 - 30;
//        int height = AdvisorCategories.height / 4;

        Log.d("widthHeight", width + "");

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        holder.rootlayoutRow.setLayoutParams(layoutParams);

        final CategoryModel model = moviesList.get(position);
        holder.title.setText(model.getName());
        String imageURl = Urls.IMAGE_BASEURL + "" + model.getImage();

        Log.d("ImageUrl", imageURl);
        Picasso.with(context).load(imageURl).fit().centerInside().into(holder.imageview);


        String checkedValue = model.getChecked();
        if (checkedValue.equals("1")) {
            holder.checkbox.setImageResource(R.drawable.tick_icon);
        } else {
            holder.checkbox.setImageResource(0);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkedValue = model.getChecked();
                if (checkedValue.equals("1")) {
                    moviesList.get(position).setChecked("0");

                } else {
                    moviesList.get(position).setChecked("1");
                }
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


}
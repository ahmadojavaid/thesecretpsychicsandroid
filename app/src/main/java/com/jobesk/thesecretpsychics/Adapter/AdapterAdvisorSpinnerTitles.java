package com.jobesk.thesecretpsychics.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorModels.SpinnerTitleModel;
import com.jobesk.thesecretpsychics.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterAdvisorSpinnerTitles extends BaseAdapter implements SpinnerAdapter {

    ArrayList<SpinnerTitleModel> list;

    Activity activity;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public AdapterAdvisorSpinnerTitles(Activity activity, ArrayList<SpinnerTitleModel> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(activity, R.layout.spinner_layout_titles, null);


        TextView textView = (TextView) view.findViewById(R.id.userName_tv);
        CheckBox checkbox = (CheckBox) view.findViewById(R.id.checkbox);


        if (position == 0) {
            checkbox.setVisibility(View.GONE);
            textView.setText(list.get(position).getName());
        } else {

            if (list.get(position).getChecked().equalsIgnoreCase("1")) {
                checkbox.setChecked(true);
            } else {
                checkbox.setChecked(false);
            }

            textView.setText(list.get(position).getName());

        }



        checkbox.setTag(position);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                int pos = (int) buttonView.getTag();

                if (isChecked == true) {
                    list.get(pos).setChecked("1");
                } else {
                    list.get(pos).setChecked("0");
                }

            }
        });


        return view;
    }

//    @Override
//    public View getDropDownView(int position, View convertView, ViewGroup parent) {
//
//        View view;
//        view = View.inflate(activity, R.layout.row_spinner_add_trip, null);
//
//
//        ImageView imageView = (ImageView) view.findViewById(R.id.img);
//        TextView textView = (TextView) view.findViewById(R.id.txt);
//        TextView no_plate_tv = (TextView) view.findViewById(R.id.no_plate_tv);
//
//
//        if (position == 0) {
//            no_plate_tv.setVisibility(View.GONE);
//            imageView.setVisibility(View.GONE);
//            textView.setTextSize(16);
//            textView.setText(list.get(position).getType());
//        } else {
//
//            no_plate_tv.setText(list.get(position).getLicencePlate());
//            textView.setText(list.get(position).getType());
//            String imageValue = list.get(position).getImage();
//            if (imageValue.equalsIgnoreCase("")) {
//
//            } else {
//                Picasso.with(activity).load(imageValue).fit().centerCrop().into(imageView);
//            }
//        }
//
//
//        return view;
//    }


}
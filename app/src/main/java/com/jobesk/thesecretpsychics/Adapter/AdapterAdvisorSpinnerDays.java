package com.jobesk.thesecretpsychics.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorEventBus.AdvisorSpinnerEvent;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorModels.SpinnerTitleModel;
import com.jobesk.thesecretpsychics.R;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class AdapterAdvisorSpinnerDays extends BaseAdapter implements SpinnerAdapter {

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

    public AdapterAdvisorSpinnerDays(Activity activity, ArrayList<SpinnerTitleModel> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(activity, R.layout.spinner_layout_days, null);


        TextView textView = (TextView) view.findViewById(R.id.userName_tv);
//        CheckBox checkbox = (CheckBox) view.findViewById(R.id.checkbox);


        if (position == 0) {
//           checkbox.setVisibility(View.GONE);
            textView.setText(list.get(position).getName());
        } else {

//            if (list.get(position).getChecked().equalsIgnoreCase("1")) {
//                checkbox.setChecked(true);
//            } else {
//                checkbox.setChecked(false);
//            }

            textView.setText(list.get(position).getName());

        }

//
//        checkbox.setTag(position);
//        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                int pos = (int) buttonView.getTag();
//
//                if (isChecked == true) {
//
//                    for (int i = 0; i < list.size(); i++) {
//                        list.get(i).setChecked("0");
//                    }
//                    list.get(pos).setChecked("1");
//                } else {
//                    list.get(pos).setChecked("0");
//                }
//                notifyDataSetChanged();
//
//
//
//
//
//            }
//        });



        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        View view;
        view = View.inflate(activity, R.layout.spinner_layout_days, null);

        TextView textView = (TextView) view.findViewById(R.id.userName_tv);
//        CheckBox checkbox = (CheckBox) view.findViewById(R.id.checkbox);


        if (position == 0) {
//            checkbox.setVisibility(View.GONE);
            textView.setText(list.get(position).getName());
        } else {

//            if (list.get(position).getChecked().equalsIgnoreCase("1")) {
//                checkbox.setChecked(true);
//            } else {
//                checkbox.setChecked(false);
//            }

            textView.setText(list.get(position).getName());

        }

//
//        checkbox.setTag(position);
//        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                int pos = (int) buttonView.getTag();
//
//                if (isChecked == true) {
//
//                    for (int i = 0; i < list.size(); i++) {
//                        list.get(i).setChecked("0");
//                    }
//                    list.get(pos).setChecked("1");
//                } else {
//                    list.get(pos).setChecked("0");
//                }
//                notifyDataSetChanged();
//
//
//
//
//
//            }
//        });


        return view;
    }


}
package com.jobesk.thesecretpsychics.ClientActivities.ClientFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.ClientActivities.ClientDrawerActivity;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;

public class ClientFragLanguage extends Fragment {

    private ImageView menu_img;
    private TextView toolbar_title;
    private TextView espanol_tv, english_tv;
    private TextView save_tv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.client_language, container, false);


        toolbar_title = rootView.findViewById(R.id.toolbar_title);
        toolbar_title.setText(getActivity().getResources().getString(R.string.select_language));


        english_tv = rootView.findViewById(R.id.english_tv);
        espanol_tv = rootView.findViewById(R.id.espanol_tv);

        save_tv = rootView.findViewById(R.id.save_tv);

        SharedPreferences preferences = getActivity().getSharedPreferences(GlobalClass.PREF_NAME, Context.MODE_PRIVATE);
        String langvalue = preferences.getString("clientLang", "en");


        if (langvalue.equalsIgnoreCase("en")) {
            english_tv.setBackgroundResource(R.drawable.english_btn_blue);
            espanol_tv.setBackgroundResource(R.drawable.esponal_btn_gray);


            espanol_tv.setTextColor(getResources().getColor(R.color.black));
            english_tv.setTextColor(getResources().getColor(R.color.white));

        } else {

            english_tv.setBackgroundResource(R.drawable.english_btn_gray);
            espanol_tv.setBackgroundResource(R.drawable.esponal_btn_blue);

            espanol_tv.setTextColor(getResources().getColor(R.color.white));
            english_tv.setTextColor(getResources().getColor(R.color.black));

        }


        menu_img = rootView.findViewById(R.id.menu_img);
        menu_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientDrawerActivity.openDrawer();
            }
        });


        english_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                english_tv.setBackgroundResource(R.drawable.english_btn_blue);
                espanol_tv.setBackgroundResource(R.drawable.esponal_btn_gray);

                espanol_tv.setTextColor(getResources().getColor(R.color.black));
                english_tv.setTextColor(getResources().getColor(R.color.white));
                GlobalClass.putPref("clientLang", "en", getActivity());
            }
        });
        espanol_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                english_tv.setBackgroundResource(R.drawable.english_btn_gray);
                espanol_tv.setBackgroundResource(R.drawable.esponal_btn_blue);

                espanol_tv.setTextColor(getResources().getColor(R.color.white));
                english_tv.setTextColor(getResources().getColor(R.color.black));
                GlobalClass.putPref("clientLang", "es", getActivity());
            }
        });

//        GlobalClass.putPref("clientLang","es",getActivity());
//        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.language_saved), Toast.LENGTH_SHORT).show();
//        getActivity().getSupportFragmentManager().popBackStack();

        save_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.language_saved), Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });

        return rootView;
    }
}

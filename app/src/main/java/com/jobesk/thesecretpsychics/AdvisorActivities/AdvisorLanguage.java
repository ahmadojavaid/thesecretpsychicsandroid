package com.jobesk.thesecretpsychics.AdvisorActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.SplashScreen.WelcomeScreen_5;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;


public class AdvisorLanguage extends AppCompatActivity {

    private TextView toolbar_title;
    private ImageView back_img;
    private TextView english_tv, espanol_tv;
    private LinearLayout next_tv;
    private ImageView logout_user_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advisor_language);

//        Intent i = new Intent(AdvisorLanguage.this, AdvisorBecomeAdvisor.class);
////        startActivity(i);


        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getApplicationContext().getResources().getString(R.string.select_language));


        english_tv = findViewById(R.id.english_tv);
        espanol_tv = findViewById(R.id.espanol_tv);

        logout_user_img = findViewById(R.id.logout_user_img);
        logout_user_img.setVisibility(View.VISIBLE);
        logout_user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                GlobalClass.clearPref(getApplicationContext());
                Intent i = new Intent(getApplicationContext(), WelcomeScreen_5.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);


            }
        });


        GlobalClass.putPref("advisorLang", "en", getApplicationContext());
        english_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                english_tv.setBackgroundResource(R.drawable.english_btn_blue);
                espanol_tv.setBackgroundResource(R.drawable.esponal_btn_gray);


                espanol_tv.setTextColor(getResources().getColor(R.color.black));
                english_tv.setTextColor(getResources().getColor(R.color.white));
                GlobalClass.putPref("advisorLang", "en", getApplicationContext());
            }
        });
        espanol_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                english_tv.setBackgroundResource(R.drawable.english_btn_gray);
                espanol_tv.setBackgroundResource(R.drawable.esponal_btn_blue);

                espanol_tv.setTextColor(getResources().getColor(R.color.white));
                english_tv.setTextColor(getResources().getColor(R.color.black));
                GlobalClass.putPref("advisorLang", "es", getApplicationContext());
            }
        });


        next_tv = findViewById(R.id.next_tv);
        next_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent termsIntent = new Intent(AdvisorLanguage.this, AdvisorBecomeAdvisor.class);
                startActivity(termsIntent);

            }
        });

    }
}

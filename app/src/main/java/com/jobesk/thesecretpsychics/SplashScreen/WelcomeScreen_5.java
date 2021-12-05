package com.jobesk.thesecretpsychics.SplashScreen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jobesk.thesecretpsychics.Adapter.SignUpHorizontalRecyclerAdapter;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorSelectionScreen;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorSignIn;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorSignUp;
import com.jobesk.thesecretpsychics.ClientActivities.ClientSelectionScreen;
import com.jobesk.thesecretpsychics.ClientActivities.ClientSignIn;
import com.jobesk.thesecretpsychics.ClientActivities.ClientSignup;
import com.jobesk.thesecretpsychics.Model.HorizontalModel;
import com.jobesk.thesecretpsychics.R;

import java.util.ArrayList;
import java.util.Random;

public class WelcomeScreen_5 extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "WelcomeScreen_5";
    private TextView client_tv, advisor_tv;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen_5);

        client_tv = findViewById(R.id.client_tv);
        advisor_tv = findViewById(R.id.advisor_tv);

        client_tv.setOnClickListener(this);
        advisor_tv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.client_tv:

                i = new Intent(WelcomeScreen_5.this, ClientSignIn.class);
                startActivity(i);

                break;
            case R.id.advisor_tv:

                i = new Intent(WelcomeScreen_5.this, AdvisorSignUp.class);
                startActivity(i);
                break;


        }

    }


}

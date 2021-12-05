package com.jobesk.thesecretpsychics.AdvisorActivities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jobesk.thesecretpsychics.Adapter.SignUpHorizontalRecyclerAdapter;
import com.jobesk.thesecretpsychics.ClientActivities.ClientSelectionScreen;
import com.jobesk.thesecretpsychics.ClientActivities.ClientSignIn;
import com.jobesk.thesecretpsychics.ClientActivities.ClientSignup;
import com.jobesk.thesecretpsychics.Model.HorizontalModel;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.SplashScreen.WelcomeScreen_5;

import java.util.ArrayList;
import java.util.Random;

public class AdvisorSelectionScreen extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private String TAG = "AdvisorSelectionScreen";
    private ArrayList<HorizontalModel> ArrayList1;
    private SignUpHorizontalRecyclerAdapter adapter;
    private int scrollCount = 0;
    private int[] myImageList = new int[]{

            R.drawable.image_user1,
            R.drawable.image_user2,
            R.drawable.image_user3,
            R.drawable.image_user4,
            R.drawable.image_user5,
            R.drawable.image_user6,
            R.drawable.image_user7,
            R.drawable.image_user8,
            R.drawable.image_user9,
            R.drawable.image_user10,

    };
    private TextView sign_up_advoisor_tv, sign_up_as_client_tv;

    private Intent i;
    private LinearLayout botom_sign_in_advoiser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_selection_screen);


        ArrayList1 = new ArrayList<HorizontalModel>();

        for (int i = 0; i < 10; i++) {
            HorizontalModel horizontalModel = new HorizontalModel();
            horizontalModel.setImage(myImageList[i]);

            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            horizontalModel.setColor(String.valueOf(color));
            ArrayList1.add(horizontalModel);

        }

        initRecyclerView();


        sign_up_advoisor_tv = findViewById(R.id.sign_up_advoisor_tv);
        sign_up_advoisor_tv.setOnClickListener(this);

        botom_sign_in_advoiser = findViewById(R.id.botom_sign_in_advoiser);
        botom_sign_in_advoiser.setOnClickListener(this);
    }


    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");

//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = findViewById(R.id.recyclerView1);


        adapter = new SignUpHorizontalRecyclerAdapter(this, (ArrayList<HorizontalModel>) ArrayList1);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getApplicationContext()) {
                    final float SPEED = 4000f;// Change this value (default=25f)

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }

        };
        autoScrollAnother();

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
    }


    //new auto scroll
    public void autoScrollAnother() {
        scrollCount = 0;
        final int speedScroll = 1200;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (scrollCount == adapter.getItemCount()) {
//                    adapter.load();
                    adapter.notifyDataSetChanged();
                }
                recyclerView.smoothScrollToPosition((scrollCount++));
                handler.postDelayed(this, speedScroll);
            }
        };
        handler.postDelayed(runnable, speedScroll);
    }

    public void autoScroll() {
        final int speedScroll = 1000;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;

            @Override
            public void run() {
                if (count == adapter.getItemCount())
                    count = 0;
                if (count < adapter.getItemCount()) {
                    recyclerView.smoothScrollToPosition(++count);
                    handler.postDelayed(this, speedScroll);
                }
            }
        };
        handler.postDelayed(runnable, speedScroll);
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.botom_sign_in_advoiser:
                i = new Intent(AdvisorSelectionScreen.this, AdvisorSignIn.class);
                startActivity(i);
                break;
            case R.id.sign_up_advoisor_tv:
                i = new Intent(AdvisorSelectionScreen.this, AdvisorSignUp.class);
                startActivity(i);
                break;


        }

    }

}

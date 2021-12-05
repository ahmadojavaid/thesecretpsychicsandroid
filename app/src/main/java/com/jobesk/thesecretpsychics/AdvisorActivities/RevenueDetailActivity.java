package com.jobesk.thesecretpsychics.AdvisorActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jobesk.thesecretpsychics.Adapter.RevenuesAdapter;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;

import java.util.ArrayList;

public class RevenueDetailActivity extends AppCompatActivity {
    private RevenuesAdapter mAdapter;
    private RecyclerView recyclerView;
    private ArrayList<String> arrayList = new ArrayList<>();
    private TextView toolbar_title;
    private ImageView back_img;
    private TextView date_tv, time_tv, type_tv, earning_tv, trid_tv;
    private TextView title_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue_detail);

        Bundle bundle = getIntent().getExtras();

        String cash = bundle.getString("cash");
        String dateTime = bundle.getString("dateTime");
        String refernce = bundle.getString("refernce");
        String earning = bundle.getString("earning");
        String Trid = bundle.getString("Trid");


        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getApplicationContext().getResources().getString(R.string.payment_details));


        title_tv = findViewById(R.id.title_tv);
//        amount_tv = findViewById(R.id.amount_tv);
        time_tv = findViewById(R.id.time_tv);
        date_tv = findViewById(R.id.date_tv);
        type_tv = findViewById(R.id.type_tv);
        earning_tv = findViewById(R.id.earning_tv);
        trid_tv = findViewById(R.id.trid_tv);


        title_tv.setText(refernce);


//        amount_tv.setText(getApplicationContext().getResources().getString(R.string.pound) + "" + cash);

        String time= GlobalClass.changeDateFormat("yyyy-MM-dd HH:mm:ss","hh:mm aa",dateTime);
        time_tv.setText(time);


        String date= GlobalClass.changeDateFormat("yyyy-MM-dd HH:mm:ss","dd-MM-yyyy",dateTime);
        date_tv.setText(date);
        type_tv.setText(refernce);
        trid_tv.setText(Trid);
        earning_tv.setText(getApplicationContext().getResources().getString(R.string.pound) + "" + cash);


    }
}

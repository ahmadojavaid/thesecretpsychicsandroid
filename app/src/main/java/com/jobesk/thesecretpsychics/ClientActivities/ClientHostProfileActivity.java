package com.jobesk.thesecretpsychics.ClientActivities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.jobesk.thesecretpsychics.ClientActivities.ClientFragments.ClientFragOtherProfile;
import com.jobesk.thesecretpsychics.R;

public class ClientHostProfileActivity extends AppCompatActivity {
    private ImageView back_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_host_profile);


        Bundle bundle = getIntent().getExtras();
        String psychicID = bundle.getString("id");


        Fragment defaultfrag = new ClientFragOtherProfile();
        Bundle fragBundle = new Bundle();
        fragBundle.putString("id", psychicID);
        defaultfrag.setArguments(fragBundle);
        FragmentManager fmd = getSupportFragmentManager();
        FragmentTransaction fragmentTransactionde = fmd.beginTransaction();
        fragmentTransactionde.replace(R.id.clientProfileFrame, defaultfrag);
        fragmentTransactionde.addToBackStack(null);
        fragmentTransactionde.commit();

//
//        back_img = findViewById(R.id.back_img);
//        back_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });



    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 1) {
//                super.onBackPressed();
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}

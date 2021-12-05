package com.jobesk.thesecretpsychics.ClientActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorSelectionScreen;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorSignIn;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorSignUp;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.SplashScreen.WelcomeScreen_5;

public class ClientSelectionScreen extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout botom_sign_in_client;
    private Intent signInActivityIntent;
    private TextView sign_up_as_client_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_selection_screen);

        sign_up_as_client_tv = findViewById(R.id.sign_up_as_client_tv);
        sign_up_as_client_tv.setOnClickListener(this);


        botom_sign_in_client = findViewById(R.id.botom_sign_in_client);
        botom_sign_in_client.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.sign_up_as_client_tv:
                signInActivityIntent = new Intent(ClientSelectionScreen.this, ClientSignup.class);
                startActivity(signInActivityIntent);
                break;


            case R.id.botom_sign_in_client:
                signInActivityIntent = new Intent(ClientSelectionScreen.this, ClientSignIn.class);
                startActivity(signInActivityIntent);
                break;


        }

    }

}

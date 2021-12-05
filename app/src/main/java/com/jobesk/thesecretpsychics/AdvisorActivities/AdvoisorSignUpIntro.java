package com.jobesk.thesecretpsychics.AdvisorActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jobesk.thesecretpsychics.ClientActivities.ClientSignup;
import com.jobesk.thesecretpsychics.R;

public class AdvoisorSignUpIntro extends AppCompatActivity implements View.OnClickListener {
    private TextView signUp_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advoisor_sign_up_intro);


        signUp_tv = findViewById(R.id.signUp_tv);
        signUp_tv.setText(getApplicationContext().getResources().getString(R.string.signup));
        signUp_tv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUp_tv:


                Intent singUPIntent = new Intent(AdvoisorSignUpIntro.this, ClientSignup.class);
                startActivity(singUPIntent);
                break;


        }
    }
}

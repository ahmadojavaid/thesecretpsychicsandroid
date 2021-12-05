package com.jobesk.thesecretpsychics.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;


public class WelcomeScreen_4 extends Fragment {


    private LinearLayout getMEStart;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_welcome_screen_4, container, false);

        getMEStart = rootView.findViewById(R.id.getMEStart);
        getMEStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                GlobalClass.setIntro("intro", "1", getActivity());

                launchNextScreen();


            }
        });


        return rootView;
    }

    private void launchNextScreen() {

        Intent i = new Intent(getActivity(), WelcomeScreen_5.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }


}

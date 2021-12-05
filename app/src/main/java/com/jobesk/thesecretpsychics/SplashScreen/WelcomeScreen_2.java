package com.jobesk.thesecretpsychics.SplashScreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jobesk.thesecretpsychics.R;

public class WelcomeScreen_2 extends Fragment {


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_welcome_screen_2);
//
//
//    }




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootview = inflater.inflate(R.layout.activity_welcome_screen_2, container, false);




        return rootview;


    }
}

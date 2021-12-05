package com.jobesk.thesecretpsychics.AdvisorActivities.EditProfile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.R;

public class AdvisorEditFragProfileInfo extends Fragment {

    private String TAG = "AdvisorEditFragProfileInfo";
    private EditText service_description_et, about_et;
    private String service, about;
    private LinearLayout next_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_advisor_profile_info, container, false);


        service_description_et = rootView.findViewById(R.id.service_description_et);
        about_et = rootView.findViewById(R.id.about_et);

        AdvisorEditProfile.toolbar_title.setText(getActivity().getResources().getString(R.string.profile_info));
        service_description_et.setText(AdvisorEditProfile.serviceDescription);
        about_et.setText(AdvisorEditProfile.aboutME);

        next_btn = rootView.findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                service = service_description_et.getText().toString().trim();
                about = about_et.getText().toString().trim();


                if (service.equals("")) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_Service), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (about.equals("")) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_about_me), Toast.LENGTH_SHORT).show();
                    return;
                }

                AdvisorEditProfile.serviceDescription = service;
                AdvisorEditProfile.aboutME = about;

                openFrag();

            }
        });


        return rootView;
    }

    private void openFrag() {
        AdvisorEditFragOrderingInstruction frag = new AdvisorEditFragOrderingInstruction();
        FragmentManager fmd = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransactionde = fmd.beginTransaction();
        fragmentTransactionde.replace(R.id.editFrame, frag);
        fragmentTransactionde.addToBackStack(null);
        fragmentTransactionde.commit();

    }

}

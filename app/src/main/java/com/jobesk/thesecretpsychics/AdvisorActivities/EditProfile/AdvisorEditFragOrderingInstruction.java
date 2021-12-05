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

public class AdvisorEditFragOrderingInstruction extends Fragment {

    private LinearLayout next_btn;
    private EditText instruction_et;
    private String TAG = "AdvisorEditFragOrderingInstruction";
    private String instruction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_advisor_ordering_instruction, container, false);


        AdvisorEditProfile.toolbar_title.setText(getActivity().getResources().getString(R.string.ordering_instruction));

        instruction_et = rootView.findViewById(R.id.instruction_et);

        instruction_et.setText(AdvisorEditProfile.orderInstruction);
        next_btn = rootView.findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                instruction = instruction_et.getText().toString().trim();


                if (instruction.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_order_instructions), Toast.LENGTH_SHORT).show();
                    return;
                }
                AdvisorEditProfile.orderInstruction = instruction;

                openFrag();
            }
        });


        return rootView;
    }

    private void openFrag() {

        AdvisorEditFragExperience frag = new AdvisorEditFragExperience();
        FragmentManager fmd = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransactionde = fmd.beginTransaction();
        fragmentTransactionde.replace(R.id.editFrame, frag);
        fragmentTransactionde.addToBackStack(null);
        fragmentTransactionde.commit();

    }

}

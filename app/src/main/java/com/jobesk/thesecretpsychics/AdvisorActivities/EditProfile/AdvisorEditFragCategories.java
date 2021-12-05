package com.jobesk.thesecretpsychics.AdvisorActivities.EditProfile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.Adapter.AdvisorEditFragCategoryAdapter;
import com.jobesk.thesecretpsychics.R;

import java.util.ArrayList;

public class AdvisorEditFragCategories extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<String> CheckedListIdz = new ArrayList();
    private AdvisorEditFragCategoryAdapter mAdapter;
    LinearLayout next_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_advisor_categories, container, false);
        AdvisorEditProfile.toolbar_title.setText(getActivity().getResources().getString(R.string.categories));


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerviewcat);
        mAdapter = new AdvisorEditFragCategoryAdapter(getActivity(), AdvisorEditProfile.catListData);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(mAdapter);


        next_btn = rootView.findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder selectedIds = new StringBuilder(100);
                if (AdvisorEditProfile.catListData.size() > 0) {


                    if (CheckedListIdz.size() > 0) {
                        CheckedListIdz.clear();
                    }


                    for (int i = 0; i < AdvisorEditProfile.catListData.size(); i++) {
                        String checkedValue = AdvisorEditProfile.catListData.get(i).getChecked();
                        if (checkedValue.equals("1")) {


                            String ids = AdvisorEditProfile.catListData.get(i).getId();
                            selectedIds.append(ids + ",");
                            CheckedListIdz.add(ids);

                        }


                    }


                }
                if (CheckedListIdz.size() < 0) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.select_categories), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (CheckedListIdz.size() > 3) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.select_upto_3), Toast.LENGTH_SHORT).show();
                    return;
                }
                AdvisorEditProfile.selectedIdz = String.valueOf(selectedIds);

                if (String.valueOf(AdvisorEditProfile.selectedIdz).endsWith(",")) {
                    AdvisorEditProfile.selectedIdz = AdvisorEditProfile.selectedIdz.substring(0, AdvisorEditProfile.selectedIdz.length() - 1);
                }
                Log.d("selectedIds", AdvisorEditProfile.selectedIdz + "");

                openFrag();
            }


        });
        return rootView;
    }

    private void openFrag() {
        AdvisorEditFragProfileInfo frag = new AdvisorEditFragProfileInfo();
        FragmentManager fmd = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransactionde = fmd.beginTransaction();
        fragmentTransactionde.replace(R.id.editFrame, frag);
        fragmentTransactionde.addToBackStack(null);
        fragmentTransactionde.commit();

    }


}
package com.jobesk.thesecretpsychics.ClientActivities.ClientFragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.Adapter.ClientFavouriteAdvisorsAdapter;
import com.jobesk.thesecretpsychics.ClientActivities.ClientDrawerActivity;
import com.jobesk.thesecretpsychics.Model.ClientFavouriteAdvisors;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ClientFragFavouriteAdvisors extends Fragment {
    private Activity activity;

    private ArrayList<ClientFavouriteAdvisors> favAdvisors = new ArrayList<>();
    ClientFavouriteAdvisorsAdapter adapter;
    private RecyclerView recyclerView_psychic;
    private ImageView menu_img;
    private TextView toolbar_title;
    private RelativeLayout no_data_contianer;
    private String TAG = "ClientFragFavouriteAdvisors";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.client_frag_favourite_advisors, container, false);


        activity = (ClientDrawerActivity) rootView.getContext();


        no_data_contianer = rootView.findViewById(R.id.no_data_contianer);

        recyclerView_psychic = rootView.findViewById(R.id.recycler_view);
        adapter = new ClientFavouriteAdvisorsAdapter(activity, getActivity(), favAdvisors);


        recyclerView_psychic.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        recyclerView_psychic.setAdapter(adapter);


        toolbar_title = rootView.findViewById(R.id.toolbar_title);
        toolbar_title.setText(getActivity().getResources().getString(R.string.favourite_advisors));
        menu_img = rootView.findViewById(R.id.menu_img);
        menu_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClientDrawerActivity.openDrawer();


            }
        });
        FavouriteAdvisors();


        return rootView;
    }

    private void FavouriteAdvisors() {

        if (GlobalClass.isOnline(getActivity()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("email", email);
//            mParams.put("password", password);

            String clientID = GlobalClass.getPref("clientID", getActivity());
            WebReq.get(getActivity(), "favourite?userId=" + clientID, mParams, new MyTextHttpResponseHandler());

        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandler extends JsonHttpResponseHandler {
        MyTextHttpResponseHandler() {
        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(activity);
            Log.d(TAG, "onStart");

        }

        @Override
        public void onFinish() {
            super.onFinish();
            GlobalClass.dismissLoading();

//            Log.d(TAG, "onFinish");

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
//            Log.d(TAG, "OnFailure" + e);
            GlobalClass.dismissLoading();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
//            Log.d(TAG, responseString);
            GlobalClass.dismissLoading();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {

            GlobalClass.dismissLoading();
            Log.d(TAG, mResponse.toString() + "Respo");
            if (mResponse != null && mResponse.length() != 0) {
                try {
                    String status = mResponse.getString("statusCode");

                    if (status.equals("1")) {

                        JSONArray jsonArray = mResponse.getJSONArray("Result");
                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String id = jsonObject.getString("id");
                                String serviceName = jsonObject.getString("serviceName");
                                String name = jsonObject.getString("legalNameOfIndividual");
                                String image = jsonObject.getString("profileImage");
                                String ratting = jsonObject.getString("rating");
                                String profileVideo = jsonObject.getString("profileVideo");
                                String isOnline = jsonObject.getString("isOnline");
                                String liveChat = jsonObject.getString("liveChat");
                                String threeMinuteVideo = jsonObject.getString("threeMinuteVideo");
                                String screenName = jsonObject.getString("screenName");

                                ClientFavouriteAdvisors model = new ClientFavouriteAdvisors();
                                model.setAdvisorID(id);
                                model.setServiceName(serviceName);
                                model.setName(screenName);
                                model.setImage(image);
                                model.setRatting(ratting);
                                model.setVideoUrl(profileVideo);
                                model.setIsOnline(isOnline);
                                model.setLiveChat(liveChat);
                                model.setVideoMsg(threeMinuteVideo);
                                favAdvisors.add(model);
                                adapter.notifyDataSetChanged();

                            }


                            no_data_contianer.setVisibility(View.GONE);
                            recyclerView_psychic.setVisibility(View.VISIBLE);

                        } else {

                            no_data_contianer.setVisibility(View.VISIBLE);
                            recyclerView_psychic.setVisibility(View.GONE);
                        }


                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

}

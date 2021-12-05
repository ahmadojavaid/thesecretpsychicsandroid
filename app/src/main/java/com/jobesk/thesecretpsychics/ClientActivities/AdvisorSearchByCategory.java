package com.jobesk.thesecretpsychics.ClientActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.Adapter.ClientCategoryAdapter;
import com.jobesk.thesecretpsychics.Adapter.ClientSearchAdvisorsAdapter;
import com.jobesk.thesecretpsychics.Model.CategoryModel;
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

public class AdvisorSearchByCategory extends AppCompatActivity {
    private TextView toolbar_title;
    private LinearLayout next_btn;
    private ImageView back_img;

    private RecyclerView recyclerView_psychic;
    private ClientCategoryAdapter mAdapter;
    private ArrayList<CategoryModel> catListData = new ArrayList<>();
    private String TAG = "AdvisorSearchByCategory";
    private ClientSearchAdvisorsAdapter adapter;
    private ArrayList<ClientFavouriteAdvisors> favAdvisors = new ArrayList<>();
    private String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor_search_by_category);


        Bundle extras = getIntent().getExtras();
        categoryId = extras.getString("catID");


        toolbar_title = findViewById(R.id.toolbar_title);

        toolbar_title.setText(getApplicationContext().getResources().getString(R.string.advisor));


        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView_psychic = findViewById(R.id.recycler_view);
        adapter = new ClientSearchAdvisorsAdapter(AdvisorSearchByCategory.this, getApplicationContext(), favAdvisors);


        recyclerView_psychic.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        recyclerView_psychic.setAdapter(adapter);


        getSearchedCat();
    }


    private void getSearchedCat() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("email", email);
//            mParams.put("password", password);


            WebReq.get(getApplicationContext(), "showPsychicsByCat?categoryId=" + categoryId, mParams, new MyTextHttpResponseHandler());

        } else {
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandler extends JsonHttpResponseHandler {
        MyTextHttpResponseHandler() {
        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(AdvisorSearchByCategory.this);
            Log.d(TAG, "onStart");

        }

        @Override
        public void onFinish() {
            super.onFinish();
            GlobalClass.dismissLoading();

            Log.d(TAG, "onFinish");

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d(TAG, "OnFailure" + e);
            GlobalClass.dismissLoading();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d(TAG, responseString);
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

                        JSONArray jsonArray = mResponse.getJSONArray("All psychics");
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

                                ClientFavouriteAdvisors model = new ClientFavouriteAdvisors();
                                model.setAdvisorID(id);
                                model.setServiceName(serviceName);
                                model.setName(name);
                                model.setImage(image);
                                model.setRatting(ratting);
                                model.setVideoUrl(profileVideo);
                                model.setIsOnline(isOnline);
                                model.setLiveChat(liveChat);
                                model.setVideoMsg(threeMinuteVideo);

                                favAdvisors.add(model);
                                adapter.notifyDataSetChanged();

                            }


                            recyclerView_psychic.setVisibility(View.VISIBLE);

                        } else {


                            recyclerView_psychic.setVisibility(View.GONE);
                        }


                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}

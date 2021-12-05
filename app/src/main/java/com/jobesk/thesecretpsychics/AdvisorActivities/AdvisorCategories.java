package com.jobesk.thesecretpsychics.AdvisorActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.Adapter.AdvisorCategoryAdapter;
import com.jobesk.thesecretpsychics.Model.CategoryModel;
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

public class AdvisorCategories extends AppCompatActivity {
    private TextView toolbar_title;
    private LinearLayout next_btn;
    private ImageView back_img;

    private RecyclerView recyclerView;
    private AdvisorCategoryAdapter mAdapter;
    private ArrayList<CategoryModel> catListData = new ArrayList<>();

    private String TAG = "AdvisorCategories";
    public static int height, width;
    private String selectedIdz = "";
    ArrayList<String> CheckedListIdz = new ArrayList();
    private int apiStatus = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advisor_categories);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;


        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getApplicationContext().getResources().getString(R.string.categories));


        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewcat);
        mAdapter = new AdvisorCategoryAdapter(getApplicationContext(), catListData);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(mAdapter);


        getCategories();


        next_btn = findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder selectedIds = new StringBuilder(100);
                if (catListData.size() > 0) {


                    if (CheckedListIdz.size() > 0) {
                        CheckedListIdz.clear();
                    }

                    for (int i = 0; i < catListData.size(); i++) {
                        String checkedValue = catListData.get(i).getChecked();
                        if (checkedValue.equals("1")) {


                            String ids = catListData.get(i).getId();
                            selectedIds.append(ids + ",");
                            CheckedListIdz.add(ids);

                        }


                    }


                }


                selectedIdz = String.valueOf(selectedIds);

                if (String.valueOf(selectedIdz).endsWith(",")) {
                    selectedIdz = selectedIdz.substring(0, selectedIdz.length() - 1);
                }
                Log.d("selectedIds", selectedIdz + "");

                apiStatus = 2;
                getCategories();


            }
        });

    }

    private void getCategories() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {


            if (apiStatus == 1) {
                RequestParams mParams = new RequestParams();
                WebReq.get(getApplicationContext(), "category", mParams, new MyTextHttpResponseHandler());
            }

            if (apiStatus == 2) {


                if (selectedIdz.equals("")) {
                    Toast.makeText(this, getApplicationContext().getResources().getString(R.string.select_categories), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (CheckedListIdz.size()>0 && CheckedListIdz.size()<=3){
                    String advisorID = GlobalClass.getPref("advisorID", getApplicationContext());
                    RequestParams mParams = new RequestParams();
                    mParams.put("categories", selectedIdz);
                    mParams.put("advisorId", advisorID);
                    WebReq.post(getApplicationContext(), "assignCat", mParams, new MyTextHttpResponseHandler());
                }else {
                    Toast.makeText(this, getApplicationContext().getResources().getString(R.string.select_service_upto_3), Toast.LENGTH_SHORT).show();


                }


            }


        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandler extends JsonHttpResponseHandler {
        MyTextHttpResponseHandler() {
        }

        @Override
        public void onStart() {
            super.onStart();

            GlobalClass.showLoading(AdvisorCategories.this);

        }

        @Override
        public void onFinish() {
            super.onFinish();
            GlobalClass.dismissLoading();

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d(TAG, "OnFailure");
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

                        if (apiStatus == 1) {
                            JSONArray jsonArrayResult = mResponse.getJSONArray("Result");
                            for (int i = 0; i < jsonArrayResult.length(); i++) {

                                JSONObject object = jsonArrayResult.getJSONObject(i);

                                String id = object.getString("id");
                                String name = object.getString("categoryName");
                                String image = object.getString("appIcons");

                                CategoryModel model = new CategoryModel();
                                model.setId(id);
                                model.setName(name);
                                model.setImage(image);
                                model.setChecked("0");

                                catListData.add(model);

                            }

                            mAdapter.notifyDataSetChanged();

                        }


                        if (apiStatus == 2) {

                            Intent profileInfoIntnet = new Intent(AdvisorCategories.this, AdvisorProfileInfo.class);
                            startActivity(profileInfoIntnet);

                        }

                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(AdvisorCategories.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

}

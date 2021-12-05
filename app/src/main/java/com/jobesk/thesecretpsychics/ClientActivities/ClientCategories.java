package com.jobesk.thesecretpsychics.ClientActivities;

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

import com.jobesk.thesecretpsychics.Adapter.ClientCategoryAdapter;
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

public class ClientCategories extends AppCompatActivity {
    private TextView toolbar_title;
    private LinearLayout next_btn;
    private ImageView back_img;

    private RecyclerView recyclerView;
    private ClientCategoryAdapter mAdapter;
    private ArrayList<CategoryModel> catListData = new ArrayList<>();
    private String TAG = "ClientCategories";
    public static int height, width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_categories);


        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getApplicationContext().getResources().getString(R.string.categories));



        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;



        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewcat);
        mAdapter = new ClientCategoryAdapter(ClientCategories.this, catListData);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(mAdapter);


        getCategories();
//
//
//        next_btn=findViewById(R.id.next_btn);
//        next_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent profileInfoIntnet=new Intent(ClientCategories.this,AdvisorProfileInfo.class);
//                startActivity(profileInfoIntnet);
//            }
//        });

    }

    private void getCategories() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("parent_fname", parentFirstName);


            WebReq.get(getApplicationContext(), "category", mParams, new MyTextHttpResponseHandler());


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

            GlobalClass.showLoading(ClientCategories.this);

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
                    } else {

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

}

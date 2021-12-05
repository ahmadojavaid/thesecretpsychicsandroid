package com.jobesk.thesecretpsychics.ClientActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.Adapter.ClientSearchAdvisorsAdapter;
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

public class ClientSearchAdvisor extends AppCompatActivity {
  private EditText et_search;


  private String TAG = "ClientSearchAdvisor";
  private String keyword = "";
  private ArrayList<ClientFavouriteAdvisors> favAdvisors = new ArrayList<>();
  private ImageView back_img;
  private RecyclerView recyclerView_psychic;
  private ClientSearchAdvisorsAdapter adapter;
  public static int height, width;
  private TextView toolbar_title;
  private GridLayoutManager gridLayoutManager;
  private boolean loading = true;
  private int currentPage = 1;
  private int totalPage;
  private int previousTotal = 0;
  private int visibleThreshold = 12;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_client_search_advisor);


    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    height = displayMetrics.heightPixels;
    width = displayMetrics.widthPixels;


    et_search = findViewById(R.id.et_search);
    toolbar_title = findViewById(R.id.toolbar_title);

    toolbar_title.setText(getApplicationContext().getResources().getString(R.string.search_advisor));


    back_img = findViewById(R.id.back_img);
    back_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    recyclerView_psychic = findViewById(R.id.recycler_view);
    adapter = new ClientSearchAdvisorsAdapter(ClientSearchAdvisor.this, getApplicationContext(), favAdvisors);

    gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
    recyclerView_psychic.setLayoutManager(gridLayoutManager);

    recyclerView_psychic.setAdapter(adapter);


    et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_DONE) {


          keyword = et_search.getText().toString();


          if (keyword.equalsIgnoreCase("")) {
            Toast.makeText(ClientSearchAdvisor.this, getApplicationContext().getResources().getString(R.string.enter_name_to_search), Toast.LENGTH_SHORT).show();

          } else {


            if (favAdvisors.size() > 0) {

              favAdvisors.clear();
            }
            FavouriteAdvisors();
          }


          handled = true;
        }
        return handled;
      }
    });


    FavouriteAdvisors();
  }


  private void FavouriteAdvisors() {

    if (GlobalClass.isOnline(getApplicationContext()) == true) {

      RequestParams mParams = new RequestParams();
//            mParams.put("email", email);
//            mParams.put("password", password);


      WebReq.get(getApplicationContext(), "searchAdvisor?legalName=" + keyword + "&page=" + currentPage, mParams, new MyTextHttpResponseHandler());

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
      GlobalClass.showLoading(ClientSearchAdvisor.this);
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


            JSONObject jsonObjectSearch = mResponse.getJSONObject("All psychics");
            JSONArray jsonArray = jsonObjectSearch.getJSONArray("data");
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

          ScrollViewRecyclerAndroid();
        } catch (JSONException e) {
          e.printStackTrace();
        }


      }
    }
  }


  private void ScrollViewRecyclerAndroid() {


    recyclerView_psychic.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = gridLayoutManager.getChildCount();
        int totalItemCount = gridLayoutManager.getItemCount();
        int firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition();
        int pastVisibleItems = gridLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
          if (totalItemCount > previousTotal) {
            loading = false;
            previousTotal = totalItemCount;
          }
        }

        int value = totalItemCount - visibleItemCount;
        int value2 = firstVisibleItemPosition + visibleThreshold;
//
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItemPosition + visibleThreshold)) {
          if (currentPage > totalPage) {


//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(activity, R.string.no_more_advisor, Toast.LENGTH_SHORT).show();
//                                }
//                            }, 2000);
          } else {
            try {
              currentPage++;

              if (GlobalClass.isOnline(getApplicationContext())) {
//                                    progressBar.setVisibility(View.VISIBLE);
              } else {
//                                    progressBar.setVisibility(View.GONE);
              }
              if (currentPage <= totalPage) {

                FavouriteAdvisors();
              } else {
//                                    progressBar.setVisibility(View.GONE);
              }


            } catch (Exception e) {
              e.printStackTrace();
            }
          }
          loading = true;
        }
      }
    });


  }

}

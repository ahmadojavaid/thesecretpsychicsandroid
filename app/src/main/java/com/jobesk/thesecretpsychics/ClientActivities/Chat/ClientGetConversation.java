package com.jobesk.thesecretpsychics.ClientActivities.Chat;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ClientGetConversation extends AppCompatActivity {
    private ImageView back_img;
    private TextView toolbar_title, availableconnections;
    private String TAG = "ClientGetConversation";
    private ListView listView;
    private int conversactionSize = 0;
    private GetconversationModel userchatmodel;
    private ArrayList<GetconversationModel> mUsersData = new ArrayList<>();
    private ClientConversationAdapter chatUsersAdapter;
    private RecyclerView recyclerView;
    private ClientConversationAdapter mAdapter;
    private boolean firstTime = true;
    private int delay = 3000;
    private Runnable runnable;
    private Handler h;
    private String clientID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_get_conversation);


        clientID = GlobalClass.getPref("clientID", getApplicationContext());

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getApplicationContext().getResources().getString(R.string.your_conversation));


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new ClientConversationAdapter(ClientGetConversation.this, mUsersData);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        availableconnections = (TextView) findViewById(R.id.availableconnections);


        OnlineUsersRequest();

    }

    @Override
    protected void onStart() {

        h = new Handler();
        recusiveCallForMessages();
        super.onStart();
    }

    private void recusiveCallForMessages() {
        //start handler as activity become visible
        h.postDelayed(new Runnable() {
            public void run() {

                OnlineUsersRequest();

                runnable = this;
                h.postDelayed(runnable, delay);
            }
        }, delay);
    }

    protected void OnlineUsersRequest() {


        if (GlobalClass.isOnline(getApplicationContext()) == true) {


            RequestParams mParams = new RequestParams();
//            mParams.put("user_id", UserID);

            WebReq.get(getApplicationContext(), "getConversationuser?sentBy=" + clientID, mParams, new MyTextHttpResponseHandlerOnlineUsers());
        } else {
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    class MyTextHttpResponseHandlerOnlineUsers extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerOnlineUsers() {


        }

        @Override
        public void onStart() {
            super.onStart();
//            Log.d(TAG, " OnStart ");

            if (firstTime == true) {
                firstTime = false;

                GlobalClass.showLoading(ClientGetConversation.this);

            }


        }

        @Override
        public void onFinish() {
            super.onFinish();
//            Log.d(TAG, " OnFinish ");
            GlobalClass.dismissLoading();
        }


        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            GlobalClass.dismissLoading();
//            Log.d(TAG, "Fail: " + e.toString() + " ");

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject mResponse) {
            GlobalClass.dismissLoading();
            Log.d(TAG, "Success: " + mResponse.toString() + " ");
//

            try {
                JSONArray jsonArray = mResponse.getJSONArray("Result");


                if (jsonArray.length() > 0) {

                    conversactionSize = jsonArray.length();


                    if (mUsersData.size() > 0) {
                        mUsersData.clear();

                    }


                    for (int i = 0; i < jsonArray.length(); i++) {


                        JSONObject object = jsonArray.getJSONObject(i);

                        String sentBy = object.getString("sentBy");
                        String sentTo = object.getString("sentTo");
                        String message = object.getString("message");
                        String created_at = object.getString("created_at");
                        String name = object.getString("screenName");
                        String profileImage = object.getString("profileImage") + "";
//                        String isOnline = object.getString("isOnline");
                        String TextChatRate = object.getString("TextChatRate");
                        String chat_counter = object.getString("chat_counter") + "";
                        String userId = object.getString("userId") + "";


                        userchatmodel = new GetconversationModel();

                        userchatmodel.setMessage(message);
                        userchatmodel.setDate(created_at);
                        userchatmodel.setName(name);
                        userchatmodel.setProfileImage(profileImage);
//                        userchatmodel.setIsOnline(isOnline);
                        userchatmodel.setChatRateText(TextChatRate);
                        userchatmodel.setCounter(chat_counter);




                        if (clientID.equals(sentBy)) {
                            userchatmodel.setSentBy(sentBy);
                            userchatmodel.setSentTo(sentTo);
                        } else {
                            userchatmodel.setSentBy(sentTo);
                            userchatmodel.setSentTo(sentBy);
                        }


                        mUsersData.add(userchatmodel);

//                        availableconnections.setText(getApplicationContext().getResources().getString(R.string.online) + "(" + conversactionSize + ")");

                    }
                    mAdapter.notifyDataSetChanged();
                } else {
//                    availableconnections.setText(getApplicationContext().getResources().getString(R.string.online) + "(0)");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


            mAdapter.notifyDataSetChanged();

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d(TAG, "Fail: " + responseString.toString() + " " + throwable);
            GlobalClass.dismissLoading();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Log.d(TAG, "Fail: " + errorResponse.toString() + " " + throwable);
            GlobalClass.dismissLoading();
        }
    }

    @Override
    protected void onPause() {

//        h.removeCallbacks(runnable); //stop handler when activity not visible
//        h.removeCallbacksAndMessages(null);
        super.onPause();

    }

    @Override
    protected void onStop() {
//        h.removeCallbacks(runnable); //stop handler when activity not visible
        super.onStop();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recusiveCallForMessages();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        h.removeCallbacks(runnable); //stop handler when activity not visible
        h.removeCallbacksAndMessages(null);

    }
}

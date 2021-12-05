package com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorChat;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.ClientActivities.Chat.ClientChatAdapter;
import com.jobesk.thesecretpsychics.ClientActivities.Chat.ClientGetChatMessagesModel;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.CircleTransform;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.Urls;
import com.jobesk.thesecretpsychics.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class AdvisorGetChat extends AppCompatActivity {


    private ImageView back_img;
    private TextView toolbar_title;
    private String TAG = "AdvisorGetChat";
    private String sendBy, sendToPre, sendByPre, sentTo, message, chatStatus, created_at, newMessage = "";
    private ArrayList<ClientGetChatMessagesModel> chatArrayList = new ArrayList<>();
    private ClientGetChatMessagesModel model;
    private RecyclerView recyclerView;
    private ClientChatAdapter mAdapter;
    private TextView send_tv;
    private int previousLength = 0;
    private EditText send_et;
    private String sendTo;
    private int delay = 3000;
    private Runnable runnable;
    private Handler h;
    private boolean firtTime = true;
    private String preName;
    private ImageView userImageView;
    private String advisorID, preImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_get_chat);

        advisorID = GlobalClass.getPref("advisorID", getApplicationContext());

        Bundle bundle = getIntent().getExtras();







        sendToPre = bundle.getString("sendTo");
        sendByPre = bundle.getString("sendBy");
        preName = bundle.getString("preName");
//        preOnline = bundle.getString("preOnline");
        preImage = bundle.getString("preImage");


        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(preName);

        userImageView = findViewById(R.id.userImageProfile);
//        online_img = findViewById(R.id.online_img);

        try {
            Picasso.with(getApplicationContext()).load(Urls.IMAGE_BASEURL + preImage).fit().centerCrop().transform(new CircleTransform()).into(userImageView);

//            if (preOnline.equalsIgnoreCase("1")) {
//                online_img.setImageResource(R.drawable.circle_online_chat_green);
//            } else {
//                online_img.setImageResource(R.drawable.circle_online_chat_green);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new ClientChatAdapter(AdvisorGetChat.this, chatArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        send_tv = findViewById(R.id.send_tv);
        send_et = findViewById(R.id.send_et);


        send_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newMessage = send_et.getText().toString().trim();

                if (newMessage.equalsIgnoreCase("")) {
                    Toast.makeText(AdvisorGetChat.this, getApplicationContext().getResources().getString(R.string.enter_message), Toast.LENGTH_SHORT).show();
                    return;
                }
                send_et.setText("");

                SendMessage();

            }
        });

        Apicalls();


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


                Apicalls();

                runnable = this;
                h.postDelayed(runnable, delay);
            }
        }, delay);
    }


    private void Apicalls() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {


//            if (apiStaus == 1) {
            // getPrevious chat
            RequestParams mParams = new RequestParams();
//                mParams.put("screenName", screen);
            WebReq.get(getApplicationContext(), "detailedChat?sentTo=" + sendByPre + "&sentBy=" + advisorID + "&type=1", mParams, new MyTextHttpResponseHandler());


//            }


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
            if (firtTime == true) {
                GlobalClass.showLoading(AdvisorGetChat.this);

            }


            Log.d(TAG, "getChat:onStart");

        }

        @Override
        public void onFinish() {
            super.onFinish();
            GlobalClass.dismissLoading();

            Log.d(TAG, "getChat:onFinish");

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d(TAG, "getChat:OnFailure" + e);
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
            Log.d(TAG, "getChat:" + mResponse.toString() + "Respo");
            if (mResponse != null && mResponse.length() != 0) {
                try {
                    String status = mResponse.getString("statusCode");

                    if (status.equals("1")) {


                        JSONArray jsonArray = mResponse.getJSONArray("Result");

                        if (jsonArray.length() > 0) {


                            if (chatArrayList.size() > 0) {

                                chatArrayList.clear();
                            }

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                sendBy = jsonObject.getString("sentBy");
                                sentTo = jsonObject.getString("sentTo");
                                message = jsonObject.getString("message");
                                chatStatus = jsonObject.getString("chatStatus");
                                created_at = jsonObject.getString("created_at");

                                model = new ClientGetChatMessagesModel();
                                model.setSendBy(sendBy);
                                model.setSentTo(sentTo);
                                model.setMessage(message);
                                model.setChatStatus(chatStatus);
                                model.setCreated_at(created_at);

                                if (sendBy.equalsIgnoreCase(advisorID)) {

                                    model.setType(ClientGetChatMessagesModel.TYPE_right);
                                } else {
                                    model.setType(ClientGetChatMessagesModel.TYPE_left);

                                }

                                chatArrayList.add(model);


                            }
                            mAdapter.notifyDataSetChanged();


                            if (firtTime == true) {

                                recyclerView.scrollToPosition(chatArrayList.size() - 1);


                            }
                            firtTime = false;
                            if (previousLength > 0) {
                                if (chatArrayList.size() > previousLength) {
                                    //MediaPlayer.create(mContext,R.raw.message).start();
                                    recyclerView.scrollToPosition(chatArrayList.size() - 1);
                                }
                            }
                            previousLength = chatArrayList.size();


                        } else {
                            if (firtTime == true) {
                                Toast.makeText(AdvisorGetChat.this, getApplicationContext().getResources().getString(R.string.no_chat_messages_found), Toast.LENGTH_SHORT).show();
                                firtTime = false;
                            }


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


    private void SendMessage() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {


            RequestParams mParams = new RequestParams();
            mParams.put("sentBy", advisorID);
            mParams.put("sentTo", sendByPre);
            mParams.put("message", newMessage);
            mParams.put("type", "1");
            mParams.put("deductionCount", "0");


            Log.d("hereMsg", mParams + "");
            WebReq.post(getApplicationContext(), "chat", mParams, new MyTextHttpResponseHandlerSendMsg());


        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerSendMsg extends JsonHttpResponseHandler {

        MyTextHttpResponseHandlerSendMsg() {

        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(AdvisorGetChat.this);
            Log.d(TAG, "sendMsg:onStart");

        }

        @Override
        public void onFinish() {
            super.onFinish();
            GlobalClass.dismissLoading();

            Log.d(TAG, "sendMsg:onFinish");

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
            Log.d(TAG, "sendMsg:OnFailure" + e);
            GlobalClass.dismissLoading();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Log.d(TAG, "sendMsg:" + responseString);
            GlobalClass.dismissLoading();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {

            GlobalClass.dismissLoading();
            Log.d(TAG, "sendMsg:" + mResponse.toString() + "Respo");
            if (mResponse != null && mResponse.length() != 0) {
                try {
                    String status = mResponse.getString("statusCode");

                    if (status.equals("1")) {

                        Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.message_send_successfully) , Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onPause() {


        super.onPause();

    }

    @Override
    protected void onStop() {
        h.removeCallbacks(runnable); //stop handler when activity not visible
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

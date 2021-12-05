package com.jobesk.thesecretpsychics.ClientActivities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorDrawerActivity;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorLanguage;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorSignUp;
import com.jobesk.thesecretpsychics.AdvisorActivities.SignUPAdvisorSwitch;
import com.jobesk.thesecretpsychics.ClientActivities.Chat.ClientGetChat;
import com.jobesk.thesecretpsychics.ClientActivities.Chat.ClientGetConversation;
import com.jobesk.thesecretpsychics.ClientActivities.ClientFragments.ClientFragCredits;

import com.jobesk.thesecretpsychics.ClientActivities.ClientFragments.ClientFragCustomerSupport;
import com.jobesk.thesecretpsychics.ClientActivities.ClientFragments.ClientFragFavouriteAdvisors;
import com.jobesk.thesecretpsychics.ClientActivities.ClientFragments.ClientFragHome;
import com.jobesk.thesecretpsychics.ClientActivities.ClientFragments.ClientFragLanguage;
import com.jobesk.thesecretpsychics.ClientActivities.ClientFragments.ClientFragMyOrders;
import com.jobesk.thesecretpsychics.ClientActivities.ClientFragments.ClientFragSetting;
import com.jobesk.thesecretpsychics.EventBuses.ClientEditProfileEvent;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.SplashScreen.WelcomeScreen_5;
import com.jobesk.thesecretpsychics.Utils.CircleTransform;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.Urls;
import com.jobesk.thesecretpsychics.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ClientDrawerActivity extends AppCompatActivity implements View.OnClickListener {

    // implements NavigationView.OnNavigationItemSelectedListener
//    private Toolbar toolbar;
    public static DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    // This ViewPager for Banner
    public static int screenWidth;
    public static int screenHeight;
    private TextView home_tv, categories_tv, my_orders_tv, credit_tv, favourite_advisor_tv, cashback_tv,
            invite_friends_tv, rate_tv, promo_code_tv, language_tv, customer_support_tv, apply_here_tv, our_website_tv, setting_tv;

    private RelativeLayout logout_container;
    private AlertDialog alertForPromo, alertForCashBack;
    private View headerLayout;
    private ImageView imageViewUser;
    private TextView userName_tv, chat_counter_tv, balance_user_tv;
    public static int height, width;

    private ImageView chat_img;
    private String TAG = "ClientDrawerActivity";
    private int count;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
    private String clientCredit, clientEmail;
    private int delay = 2000;
    private Runnable runnable;
    private Handler h;
    private String clientID;
    private String cashBackPercentage = "", cashBackAmount = "", cashBackActualAmount = "";
    private ImageView jobs_dot_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_activity_drawer);


        clientID = GlobalClass.getPref("clientID", getApplicationContext());
        Log.d("userID", clientID);
        Log.d("token", GlobalClass.getPref("clientToken", getApplicationContext()));
        clientEmail = GlobalClass.getPref("clientEmail", getApplicationContext());
        clientCredit = GlobalClass.getPref("clientCredit", getApplicationContext());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;


        screenWidth = width / 2 - 14;
        screenHeight = height / 3;


//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        new Handler().postDelayed(new Runnable() {
//
//
//            @Override
//            public void run() {54
//                // This method will be executed once the timer is over
//                // Start your app main activity
//                Intent i = new Intent(HomeActivity.this, Sortby.class);
//                startActivity(i);
//
//                // close this activity
//                finish();
//            }
//        }, SPLASH_TIME_OUT);

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.action_bar);


//        recyclerView_psychic.setHasFixedSize(true);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
        headerLayout = navigationView.getHeaderView(0);


        imageViewUser = headerLayout.findViewById(R.id.imageViewUser);
        userName_tv = headerLayout.findViewById(R.id.userName_tv);
        balance_user_tv = headerLayout.findViewById(R.id.balance_user_tv);
        chat_counter_tv = headerLayout.findViewById(R.id.chat_counter_tv);

        balance_user_tv.setText(getApplicationContext().getResources().getString(R.string.pound) + "" + clientCredit);

        chat_img = headerLayout.findViewById(R.id.chat_img);
        ImageView search_img = headerLayout.findViewById(R.id.search_img);
        search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ClientDrawerActivity.this, ClientSearchAdvisor.class);
                startActivity(i);


            }
        });

        String userID = GlobalClass.getPref("clientID", getApplicationContext());
        String userEmail = GlobalClass.getPref("clientEmail", getApplicationContext());
        String userToken = GlobalClass.getPref("clientToken", getApplicationContext());
        String userName = GlobalClass.getPref("clientName", getApplicationContext());
        String userImage = GlobalClass.getPref("clientProfileImage", getApplicationContext());


        Log.d("UserID", userName + "---" + userID);

        Picasso.with(getApplicationContext()).load(Urls.IMAGE_BASEURL + userImage).fit().centerCrop().transform(new CircleTransform()).into(imageViewUser);
        userName_tv.setText(userName);

        jobs_dot_img = findViewById(R.id.jobs_dot_img);
        home_tv = findViewById(R.id.home_tv);
        categories_tv = findViewById(R.id.categories_tv);
        my_orders_tv = findViewById(R.id.my_orders_tv);
        credit_tv = findViewById(R.id.credit_tv);
        favourite_advisor_tv = findViewById(R.id.favourite_advisor_tv);
        cashback_tv = findViewById(R.id.cashback_tv);
        cashback_tv.setVisibility(View.GONE);
        invite_friends_tv = findViewById(R.id.invite_friends_tv);
        rate_tv = findViewById(R.id.rate_tv);
        promo_code_tv = findViewById(R.id.promo_code_tv);
        language_tv = findViewById(R.id.language_tv);
        customer_support_tv = findViewById(R.id.customer_support_tv);
        apply_here_tv = findViewById(R.id.apply_here_tv);
        our_website_tv = findViewById(R.id.our_website_tv);
        setting_tv = findViewById(R.id.setting_tv);
        logout_container = findViewById(R.id.logout_container);


        logout_container.setOnClickListener(this);
        home_tv.setOnClickListener(this);
        categories_tv.setOnClickListener(this);
        my_orders_tv.setOnClickListener(this);
        credit_tv.setOnClickListener(this);
        favourite_advisor_tv.setOnClickListener(this);
        cashback_tv.setOnClickListener(this);
        invite_friends_tv.setOnClickListener(this);
        rate_tv.setOnClickListener(this);
        promo_code_tv.setOnClickListener(this);
        language_tv.setOnClickListener(this);
        customer_support_tv.setOnClickListener(this);
        apply_here_tv.setOnClickListener(this);
        our_website_tv.setOnClickListener(this);
        setting_tv.setOnClickListener(this);


        chat_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(ClientDrawerActivity.this, ClientGetConversation.class);
                startActivity(i);


            }
        });


        showOnlineOffline();


        String newStatus = GlobalClass.getPref("clientNewStatus", getApplicationContext());
        if (newStatus.equals("1")) {
            apply_here_tv.setText(getApplicationContext().getResources().getString(R.string.switch_to_advisor));
        }


        h = new Handler();
        recusiveCallForMessages();


        Intent intent = getIntent();

        if (intent.hasExtra("type")) {

            String type = intent.getExtras().getString("type");

            if (type.equalsIgnoreCase("chat")) {


                Fragment defaultfrag = new ClientFragHome();
                FragmentManager fmd = getSupportFragmentManager();
                FragmentTransaction fragmentTransactionde = fmd.beginTransaction();
                fragmentTransactionde.replace(R.id.client_fragment_container, defaultfrag);
                fragmentTransactionde.commit();

                String sendTo = intent.getExtras().getString("sendTo");
                String sendBy = intent.getExtras().getString("sendBy");
                String preName = intent.getExtras().getString("preName");
                String preChatRate = intent.getExtras().getString("preChatRate");
                String preImage = intent.getExtras().getString("preImage");


                String advisorID = GlobalClass.getPref("advisorID", getApplicationContext());


                Intent intent1 = new Intent(this, ClientGetChat.class);
                Bundle bundle = new Bundle();
                bundle.putString("sendTo", sendTo);
                bundle.putString("sendBy", sendBy);
                bundle.putString("preName", preName);
                bundle.putString("preChatRate", preChatRate);
                bundle.putString("type", "chat");
                bundle.putString("preImage", preImage);
                intent1.putExtras(bundle);
                startActivity(intent1);

            }


            if (type.equalsIgnoreCase("order")) {

                Fragment defaultfrag = new ClientFragMyOrders();
                FragmentManager fmd = getSupportFragmentManager();
                FragmentTransaction fragmentTransactionde = fmd.beginTransaction();
                fragmentTransactionde.replace(R.id.client_fragment_container, defaultfrag);
//                fragmentTransactionde.addToBackStack(null);
                fragmentTransactionde.commit();


            }

            return;
        } else {
            Fragment defaultfrag = new ClientFragHome();
            FragmentManager fmd = getSupportFragmentManager();
            FragmentTransaction fragmentTransactionde = fmd.beginTransaction();
            fragmentTransactionde.replace(R.id.client_fragment_container, defaultfrag);
//        fragmentTransactionde.addToBackStack(null);
            fragmentTransactionde.commit();
        }


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ClientEditProfileEvent event) {


        String userName = GlobalClass.getPref("clientName", getApplicationContext());
        String userImage = GlobalClass.getPref("clientProfileImage", getApplicationContext());

        Picasso.with(getApplicationContext()).load(Urls.IMAGE_BASEURL + userImage).fit().centerCrop().transform(new CircleTransform()).into(imageViewUser);
        userName_tv.setText(userName);

    }

    @Override
    public void onStart() {

        try {
            super.onStart();
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStop() {
//        h.removeCallbacks(runnable);
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        h.removeCallbacks(runnable);
        EventBus.getDefault().unregister(this);

    }

    @Override
    public void onClick(View v) {


        ClientDrawerActivity.openDrawer();

        switch (v.getId()) {
            case R.id.home_tv:
                count = getSupportFragmentManager().getBackStackEntryCount();

                ClientFragHome fragHome = new ClientFragHome();
                openFragment(fragHome);


                break;
            case R.id.categories_tv:

                Intent catIntent = new Intent(ClientDrawerActivity.this, ClientCategories.class);
                startActivity(catIntent);
                break;
            case R.id.my_orders_tv:


                ClientFragMyOrders fragOrder = new ClientFragMyOrders();
                openFragment(fragOrder);
                break;
            case R.id.credit_tv:


                ClientFragCredits fragCredit = new ClientFragCredits();
                openFragment(fragCredit);
                break;
            case R.id.favourite_advisor_tv:


                ClientFragFavouriteAdvisors fragFavouriteP = new ClientFragFavouriteAdvisors();
                openFragment(fragFavouriteP);
                break;
            case R.id.cashback_tv:
                getUSerCashBack();

                break;
            case R.id.invite_friends_tv:


                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object


                String ShareTxt = getApplicationContext().getResources().getString(R.string.share_with_friends) + "\n" +
                        "https://play.google.com/store/apps/details?id=" + appPackageName;


                GlobalClass.shareApp(ClientDrawerActivity.this, ShareTxt);
                break;
            case R.id.rate_tv:

                GlobalClass.rateApp(ClientDrawerActivity.this);

                break;
            case R.id.promo_code_tv:

                applyPromo_code();
                break;
            case R.id.language_tv:

                ClientFragLanguage fragLanguage = new ClientFragLanguage();
                openFragment(fragLanguage);
                break;
            case R.id.customer_support_tv:

                ClientFragCustomerSupport fragSupport = new ClientFragCustomerSupport();
                openFragment(fragSupport);
                break;
            case R.id.apply_here_tv:

                alertForSwitch();


                break;
            case R.id.our_website_tv:

                goToOurWebsite();
                break;
            case R.id.setting_tv:


                ClientFragSetting fragSetting = new ClientFragSetting();
                openFragment(fragSetting);
                break;
            case R.id.logout_container:
                exitAlert();
                break;


        }

    }


    private void exitAlert() {
        // Alert dialouge for exit
        new AlertDialog.Builder(ClientDrawerActivity.this).

                setIcon(R.drawable.alert_icon)
                .setTitle(getApplicationContext().getResources().getString(R.string.logout_))
                .setMessage(getApplicationContext().getResources().getString(R.string.logout_txt))
                .setPositiveButton(getApplicationContext().getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        GlobalClass.clearPref(ClientDrawerActivity.this);
                        Intent logoutIntent = new Intent(ClientDrawerActivity.this, WelcomeScreen_5.class);
                        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(logoutIntent);

                    }

                })
                .setNegativeButton(getApplicationContext().getResources().getString(R.string.no), null)
                .show();
    }

    private void alertForSwitch() {

        new AlertDialog.Builder(ClientDrawerActivity.this).

                setIcon(R.drawable.alert_icon)
//                .setTitle(getApplicationContext().getResources().getString(R.string.logout_))
                .setMessage(getApplicationContext().getResources().getString(R.string.switch_text_client_to_advisor))
                .setPositiveButton(getApplicationContext().getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switchuser();

                    }

                })
                .setNegativeButton(getApplicationContext().getResources().getString(R.string.no), null)
                .show();
    }

    private void openFragment(Fragment frag) {

        FragmentManager fmd = getSupportFragmentManager();
        FragmentTransaction fragmentTransactionde = fmd.beginTransaction();
        fragmentTransactionde.replace(R.id.client_fragment_container, frag);
//        fragmentTransactionde.addToBackStack(null);
        fragmentTransactionde.commit();


    }

    public static void openDrawer() {


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }

    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed();
                return;
            } else {
                Toast.makeText(getBaseContext(), getApplicationContext().getResources().getString(R.string.click_back_to_exit), Toast.LENGTH_LONG).show();
            }

            mBackPressed = System.currentTimeMillis();

//            int count = getSupportFragmentManager().getBackStackEntryCount();
//
//            if (count == 1) {
////                super.onBackPressed();
//
//            } else {
//                getSupportFragmentManager().popBackStack();
//            }


        }
    }


    public void getCashBackDefault() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ClientDrawerActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogView = inflater.inflate(R.layout.client_cash_back, null);
        dialogBuilder.setView(dialogView);
        dialogView.setClickable(false);
        ImageView ic_close = (ImageView) dialogView.findViewById(R.id.ic_close);
        ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertForCashBack.dismiss();
            }
        });
        RelativeLayout okBtn = (RelativeLayout) dialogView.findViewById(R.id.okbtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertForCashBack.dismiss();
            }
        });


        alertForCashBack = dialogBuilder.create();
        alertForCashBack.show();
    }

    public void getCashBackResult() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ClientDrawerActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogView = inflater.inflate(R.layout.client_cash_back_result, null);
        dialogBuilder.setView(dialogView);
        dialogView.setClickable(false);
        ImageView ic_close = (ImageView) dialogView.findViewById(R.id.ic_close);
        ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertForCashBack.dismiss();
            }
        });

        RelativeLayout okBtn = (RelativeLayout) dialogView.findViewById(R.id.okbtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertForCashBack.dismiss();
            }
        });

        TextView first_text_tv, second_text_tv, third_text_tv;


        first_text_tv = dialogView.findViewById(R.id.first_text_tv);
        second_text_tv = dialogView.findViewById(R.id.second_text_tv);
        third_text_tv = dialogView.findViewById(R.id.third_text_tv);

        String firstString = getApplicationContext().getResources().getString(R.string.you_got) + " " + cashBackPercentage + "% " + getApplicationContext().getResources().getString(R.string.cashback_label);
        first_text_tv.setText(firstString);


        String secondString = getApplicationContext().getResources().getString(R.string.your_last_purchase_was) + " " + getApplicationContext().getResources().getString(R.string.pound) + "" + cashBackActualAmount;
        second_text_tv.setText(secondString);


        String third_String = getApplicationContext().getResources().getString(R.string.you_have_received) + " " + getApplicationContext().getResources().getString(R.string.pound) + "" + cashBackAmount + " " + getApplicationContext().getResources().getString(R.string.worth_of_credit);
        third_text_tv.setText(third_String);


        alertForCashBack = dialogBuilder.create();
        alertForCashBack.show();


    }

    public void applyPromo_code() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ClientDrawerActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogView = inflater.inflate(R.layout.client_apply_promo, null);
        dialogBuilder.setView(dialogView);
        dialogView.setClickable(false);
        ImageView ic_close = (ImageView) dialogView.findViewById(R.id.ic_close);
        ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertForPromo.dismiss();
            }
        });
        RelativeLayout submitBtn = (RelativeLayout) dialogView.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertForPromo.dismiss();
            }
        });

        alertForPromo = dialogBuilder.create();
        alertForPromo.show();
    }

    private void showOnlineOffline() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {


            String advisorID = GlobalClass.getPref("advisorID", getApplicationContext());

            RequestParams mParams = new RequestParams();
//            mParams.put("updateOnlineStatus/" + advisorID + "?status=" +, screen);


            WebReq.get(getApplicationContext(), "updateOnlineStatus/" + advisorID + "?status=" + "1", mParams, new MyTextHttpResponseHandlerOnline());

        } else {
//            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerOnline extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerOnline() {


        }

        @Override
        public void onStart() {
            super.onStart();

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


            }
        }
    }

    private void goToOurWebsite() {
        String url = "http://thesecretpsychics.com/";
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setPackage("com.android.chrome");
        try {
            startActivity(i);
        } catch (ActivityNotFoundException e) {
            // Chrome is probably not installed
            // Try with the default browser
            i.setPackage(null);
            startActivity(i);
        }
    }


    private void switchuser() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
            mParams.put("email", clientEmail);
            mParams.put("switch", "advisor");

            WebReq.post(getApplicationContext(), "switch", mParams, new MyTextHttpResponseHandler());

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
            GlobalClass.showLoading(ClientDrawerActivity.this);
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

                        GlobalClass.clearPref(getApplicationContext());

                        JSONObject jsonObject = mResponse.getJSONObject("Result");
                        String newSignUPstatus = jsonObject.getString("new_status");

                        String id = jsonObject.getString("id");

                        String email = jsonObject.getString("email");
                        String token = jsonObject.getString("token");
                        String profileStatus = jsonObject.getString("profileStatus");

                        GlobalClass.putPref("advisorID", id, getApplicationContext());
                        GlobalClass.putPref("advisorEmail", email, getApplicationContext());
                        GlobalClass.putPref("advisorToken", token, getApplicationContext());
                        GlobalClass.putPref("advisorProfileStatus", profileStatus, getApplicationContext());
                        GlobalClass.putPref("userType", "advisor", getApplicationContext());

                        if (newSignUPstatus.equals("0")) {

                            Intent i = new Intent(getApplicationContext(), SignUPAdvisorSwitch.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            Bundle bundle = new Bundle();
                            bundle.putString("userID", id);
                            i.putExtras(bundle);
                            startActivity(i);

                        } else {

                            if (profileStatus.equals("0")) {
                                Intent i = new Intent(getApplicationContext(), AdvisorLanguage.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            } else {

                                Intent i = new Intent(getApplicationContext(), AdvisorDrawerActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }

                        }

                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(ClientDrawerActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    private void recusiveCallForMessages() {
        //start handler as activity become visible
        h.postDelayed(new Runnable() {
            public void run() {

                getUserCredit();

                runnable = this;
                h.postDelayed(runnable, delay);
            }
        }, delay);
    }


    private void getUserCredit() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("email", clientEmail);
            String urlCredit = "getCredit?userId=" + clientID + "&type=2";

            WebReq.get(getApplicationContext(), urlCredit, mParams, new MyTextHttpResponseHandlerGetCredit());

        } else {
//            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerGetCredit extends JsonHttpResponseHandler {

        MyTextHttpResponseHandlerGetCredit() {


        }

        @Override
        public void onStart() {
            super.onStart();
//            GlobalClass.showLoading(ClientDrawerActivity.this);
            Log.d(TAG, "onStart");

        }

        @Override
        public void onFinish() {
            super.onFinish();
//            GlobalClass.dismissLoading();

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
//            GlobalClass.dismissLoading();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {

//            GlobalClass.dismissLoading();
            Log.d(TAG, "CreditApi" + mResponse.toString() + "Respo");
            if (mResponse != null && mResponse.length() != 0) {
                try {

                    String status = mResponse.getString("statusCode");

                    if (status.equals("1")) {


                        String credit = mResponse.getString("credit");
                        String chat_counter = mResponse.getString("chat_counter");
                        String pending_orders = mResponse.getString("new_reply");

                        if (Integer.valueOf(chat_counter) == 0) {
                            chat_counter_tv.setVisibility(View.GONE);
                        } else {
                            chat_counter_tv.setVisibility(View.VISIBLE);

                        }


                        if (Integer.valueOf(pending_orders) == 0) {
                            jobs_dot_img.setVisibility(View.GONE);
                        } else {
                            jobs_dot_img.setVisibility(View.VISIBLE);

                        }


                        balance_user_tv.setText(getApplicationContext().getResources().getString(R.string.pound) + "" + credit);


                    } else {
                        String message = mResponse.getString("statusMessage");
//                        Toast.makeText(ClientDrawerActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    protected void onPause() {

//        h.removeCallbacks(runnable);
//        h.removeCallbacksAndMessages(null);
        super.onPause();

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        recusiveCallForMessages();
    }


    private void getUSerCashBack() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("email", clientEmail);


            WebReq.get(getApplicationContext(), "getCashback?userId=" + clientID, mParams, new MyTextHttpResponseHandlerGetCashback());

        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerGetCashback extends JsonHttpResponseHandler {

        MyTextHttpResponseHandlerGetCashback() {


        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(ClientDrawerActivity.this);
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
            GlobalClass.dismissLoading();
            Log.d(TAG, "OnFailure" + e);

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            GlobalClass.dismissLoading();

            Log.d(TAG, responseString);

        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {

            GlobalClass.dismissLoading();
            Log.d(TAG, mResponse.toString() + "Respo");
            if (mResponse != null && mResponse.length() != 0) {
                try {
                    String status = mResponse.getString("statusCode");

                    if (status.equals("1")) {

                        JSONObject jsonObject = mResponse.getJSONObject("Result");


                        cashBackPercentage = jsonObject.getString("percentage");
                        cashBackAmount = jsonObject.getString("amount");
                        cashBackActualAmount = jsonObject.getString("actualAmount");


                        getCashBackResult();


                    } else {

                        getCashBackDefault();

//                        String message = mResponse.getString("statusMessage");
//                        Toast.makeText(ClientDrawerActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}

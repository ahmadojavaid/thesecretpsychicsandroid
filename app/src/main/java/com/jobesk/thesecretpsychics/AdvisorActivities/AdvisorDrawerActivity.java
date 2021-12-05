package com.jobesk.thesecretpsychics.AdvisorActivities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorChat.AdvisorGetChat;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorChat.AdvisorGetConversation;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorFragments.AdvisorRevenues;
import com.jobesk.thesecretpsychics.AdvisorActivities.EditProfile.AdvisorEditProfile;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorFragments.AdvisorFragCustomerSupport;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorFragments.AdvisorFragLanguage;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorFragments.AdvisorFragMyJobs;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorFragments.AdvisorFragMyProfile;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorFragments.AdvisorFragSetting;
import com.jobesk.thesecretpsychics.ClientActivities.ClientDrawerActivity;
import com.jobesk.thesecretpsychics.ClientActivities.ClientFragments.ClientFragMyOrders;
import com.jobesk.thesecretpsychics.ClientActivities.ClientSignup;
import com.jobesk.thesecretpsychics.ClientActivities.SignUpClientSwitch;
import com.jobesk.thesecretpsychics.EventBuses.AdvisorDrawerImage;
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

public class AdvisorDrawerActivity extends AppCompatActivity implements View.OnClickListener {
    //    private Toolbar toolbar_with_back;
    public static DrawerLayout drawer;
    //    private ActionBarDrawerToggle toggle;
//    private NavigationView navigationView;
    private NavigationView navigationView;
    private TextView userNameHeader, my_jobs_tv, revenues_tv, edit_profile_tv, rate_us_tv, invite_friends_tv, language_tv, customer_support_tv, our_website_tv, settings_tv, switch_to_client_tv;
    private ImageView imageViewHeader;
    private String userImage = "", advisorName = "";
    private View headerLayout;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
    public static int height, width;

    private ImageView chat_img;
    private int count;
    private String userEmail;
    private String TAG = "AdvisorDrawerActivity";
    private final int REQUEST_WRITE_PERMISSION = 32;
    private int delay = 2000;
    private Runnable runnable;
    private Handler h;
    private String AdvisorID;

    private TextView chat_counter_tv;
    private ImageView jobs_dot_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advisor_activity_drawer);

        AdvisorID = GlobalClass.getPref("advisorID", getApplicationContext());
        Log.d("userID", AdvisorID);
        Log.d("token", GlobalClass.getPref("advisorToken", getApplicationContext()));

        userEmail = GlobalClass.getPref("advisorEmail", getApplicationContext());


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;


//        screenWidth = width / 2 - 14;
//
//        screenHeight = height / 3;


//        toolbar_with_back = (Toolbar) findViewById(R.id.toolbar_with_back);
//        setSupportActionBar(toolbar_with_back);

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.advisor_custom_action_bar);

//
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar_with_back, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.AdvisorRevenuesaddDrawerListener(toggle);
//        toggle.syncState();
//


        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);


        jobs_dot_img = findViewById(R.id.jobs_dot_img);
        my_jobs_tv = findViewById(R.id.my_jobs_tv);
        my_jobs_tv.setOnClickListener(this);
        revenues_tv = findViewById(R.id.revenues_tv);
        revenues_tv.setOnClickListener(this);
        edit_profile_tv = findViewById(R.id.edit_profile_tv);
        edit_profile_tv.setOnClickListener(this);
        rate_us_tv = findViewById(R.id.rate_us_tv);
        rate_us_tv.setOnClickListener(this);
        invite_friends_tv = findViewById(R.id.invite_friends_tv);
        invite_friends_tv.setOnClickListener(this);
        language_tv = findViewById(R.id.language_tv);
        language_tv.setOnClickListener(this);
        customer_support_tv = findViewById(R.id.customer_support_tv);
        customer_support_tv.setOnClickListener(this);
        our_website_tv = findViewById(R.id.our_website_tv);
        our_website_tv.setOnClickListener(this);
        settings_tv = findViewById(R.id.settings_tv);
        settings_tv.setOnClickListener(this);
        switch_to_client_tv = findViewById(R.id.switch_to_client_tv);
        switch_to_client_tv.setOnClickListener(this);

        RelativeLayout logout_container;

        logout_container = findViewById(R.id.logout_container);
        logout_container.setOnClickListener(this);


        headerLayout = navigationView.getHeaderView(0);

        imageViewHeader = headerLayout.findViewById(R.id.imageViewHeader);
        userNameHeader = headerLayout.findViewById(R.id.userNameHeader);
        chat_img = headerLayout.findViewById(R.id.chat_img);
        chat_counter_tv = headerLayout.findViewById(R.id.chat_counter_tv);


        chat_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AdvisorDrawerActivity.this, AdvisorGetConversation.class);
                startActivity(i);

            }
        });

        userImage = GlobalClass.getPref("advisorImage", getApplicationContext());
        advisorName = GlobalClass.getPref("advisorName", getApplicationContext());
        if (!advisorName.equals("")) {

            Picasso.with(getApplicationContext()).load(Urls.IMAGE_BASEURL + "" + userImage).fit().centerCrop().transform(new CircleTransform()).into(imageViewHeader);

            userNameHeader.setText(advisorName);
        }


        Fragment defaultfrag = new AdvisorFragMyProfile();
        FragmentManager fmd = getSupportFragmentManager();
        FragmentTransaction fragmentTransactionde = fmd.beginTransaction();
        fragmentTransactionde.replace(R.id.advisor_frame_container, defaultfrag);
        fragmentTransactionde.addToBackStack(null);
        fragmentTransactionde.commit();


        h = new Handler();
        recusiveCallForMessages();


        Intent intent = getIntent();

        if (intent.hasExtra("type")) {

            String type = intent.getExtras().getString("type");

            if (type.equalsIgnoreCase("chat")) {


                String sendTo = intent.getExtras().getString("sendTo");
                String sendBy = intent.getExtras().getString("sendBy");
                String preName = intent.getExtras().getString("preName");
                String preChatRate = intent.getExtras().getString("preChatRate");
                String preImage = intent.getExtras().getString("preImage");


                String advisorID = GlobalClass.getPref("advisorID", getApplicationContext());


                Intent intent1 = new Intent(this, AdvisorGetChat.class);
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

                Fragment frag = new AdvisorFragMyJobs();
                openFragment(frag);


            }


        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void openFragment(Fragment frag) {
//        Fragment defaultfrag = new AdvisorFragMyProfile();
        FragmentManager fmd = getSupportFragmentManager();
        FragmentTransaction fragmentTransactionde = fmd.beginTransaction();
        fragmentTransactionde.replace(R.id.advisor_frame_container, frag);
        fragmentTransactionde.addToBackStack(null);
        fragmentTransactionde.commit();

        fmd.executePendingTransactions();
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.my_jobs_tv:

                count = getSupportFragmentManager().getBackStackEntryCount();
                if (count > 1) {

                    getSupportFragmentManager().popBackStack();
                }

                Fragment defaultfrag = new AdvisorFragMyJobs();
                openFragment(defaultfrag);

                break;
            case R.id.revenues_tv:


                if (GlobalClass.isOnline(getApplicationContext()) == true) {

                    count = getSupportFragmentManager().getBackStackEntryCount();

                    if (count > 1) {

                        getSupportFragmentManager().popBackStack();

                    }

                    Fragment revenueFrag = new AdvisorRevenues();
                    openFragment(revenueFrag);

                } else {
                    Toast.makeText(this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.edit_profile_tv:
                count = getSupportFragmentManager().getBackStackEntryCount();
                if (count > 1) {

                    getSupportFragmentManager().popBackStack();
                }
                requestPermission();


                break;
            case R.id.rate_us_tv:

                count = getSupportFragmentManager().getBackStackEntryCount();
                if (count > 1) {

                    getSupportFragmentManager().popBackStack();
                }

                GlobalClass.rateApp(AdvisorDrawerActivity.this);

                break;
            case R.id.invite_friends_tv:

                count = getSupportFragmentManager().getBackStackEntryCount();
                if (count > 1) {

                    getSupportFragmentManager().popBackStack();
                }

                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object


                String ShareTxt = getApplicationContext().getResources().getString(R.string.share_with_friends) + "\n" +
                        "https://play.google.com/store/apps/details?id=" + appPackageName;


                GlobalClass.shareApp(AdvisorDrawerActivity.this, ShareTxt);

                break;
            case R.id.language_tv:

                count = getSupportFragmentManager().getBackStackEntryCount();
                if (count > 1) {

                    getSupportFragmentManager().popBackStack();
                }

                Fragment languageFrag = new AdvisorFragLanguage();
                openFragment(languageFrag);

                break;
            case R.id.customer_support_tv:
                count = getSupportFragmentManager().getBackStackEntryCount();
                if (count > 1) {

                    getSupportFragmentManager().popBackStack();
                }
                Fragment customerSupportFrag = new AdvisorFragCustomerSupport();
                openFragment(customerSupportFrag);
                break;
            case R.id.our_website_tv:
                count = getSupportFragmentManager().getBackStackEntryCount();
                if (count > 1) {

                    getSupportFragmentManager().popBackStack();
                }

                goToOurWebsite();
                break;
            case R.id.settings_tv:
                count = getSupportFragmentManager().getBackStackEntryCount();
                if (count > 1) {

                    getSupportFragmentManager().popBackStack();
                }
                Fragment advisorSetting = new AdvisorFragSetting();
                openFragment(advisorSetting);

                break;
            case R.id.switch_to_client_tv:
                count = getSupportFragmentManager().getBackStackEntryCount();
                if (count > 1) {

                    getSupportFragmentManager().popBackStack();
                }
                alertForSwitch();


                break;
            case R.id.logout_container:

                exitAlert();

                break;

        }
        openDrawer();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            Intent editIntent = new Intent(AdvisorDrawerActivity.this, AdvisorEditProfile.class);
            startActivity(editIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent editIntent = new Intent(AdvisorDrawerActivity.this, AdvisorEditProfile.class);
                startActivity(editIntent);
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(AdvisorDrawerActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show permission explanation dialog...
                } else {
                    //Never ask again selected, or device policy prohibits the app from having that permission.
                    //So, disable that feature, or fall back to another situation...
                }
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

    private void exitAlert() {
        // Alert dialouge for exit
        new AlertDialog.Builder(AdvisorDrawerActivity.this).

                setIcon(R.drawable.alert_icon)
                .setTitle(getApplicationContext().getResources().getString(R.string.logout_))
                .setMessage(getApplicationContext().getResources().getString(R.string.logout_txt))
                .setPositiveButton(getApplicationContext().getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        GlobalClass.clearPref(AdvisorDrawerActivity.this);
                        Intent logoutIntent = new Intent(AdvisorDrawerActivity.this, WelcomeScreen_5.class);
                        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(logoutIntent);

                    }

                })
                .setNegativeButton(getApplicationContext().getResources().getString(R.string.no), null)
                .show();
    }

    private void alertForSwitch() {

        new AlertDialog.Builder(AdvisorDrawerActivity.this).

                setIcon(R.drawable.alert_icon)
//                .setTitle(getApplicationContext().getResources().getString(R.string.logout_))
                .setMessage(getApplicationContext().getResources().getString(R.string.switch_text_advisor_to_client))
                .setPositiveButton(getApplicationContext().getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switchuser();

                    }

                })
                .setNegativeButton(getApplicationContext().getResources().getString(R.string.no), null)
                .show();
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


            int count = getSupportFragmentManager().getBackStackEntryCount();

            if (count == 1) {
//                super.onBackPressed();
                finish();
//                doubleTapExit();
            } else {
                getSupportFragmentManager().popBackStack();
            }


        }
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AdvisorDrawerImage event) {

        userImage = GlobalClass.getPref("advisorImage", getApplicationContext());
        advisorName = GlobalClass.getPref("advisorName", getApplicationContext());
        if (!advisorName.equals("")) {

            Picasso.with(getApplicationContext()).load(Urls.IMAGE_BASEURL + "" + userImage).fit().centerCrop().transform(new CircleTransform()).into(imageViewHeader);


            userNameHeader.setText(advisorName);
        }


    }


    private void switchuser() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
            mParams.put("email", userEmail);
            mParams.put("switch", "user");


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
            GlobalClass.showLoading(AdvisorDrawerActivity.this);
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
                        String id = jsonObject.getString("id");
                        String email = jsonObject.getString("email");
                        String token = jsonObject.getString("token");
                        String name = jsonObject.getString("name");
                        String profileImage = jsonObject.getString("profileImage");
                        String new_status = jsonObject.getString("new_status");


                        if (new_status.equals("1")) {


                            GlobalClass.putPref("clientID", id, getApplicationContext());
                            GlobalClass.putPref("clientEmail", email, getApplicationContext());
                            GlobalClass.putPref("clientToken", token, getApplicationContext());
                            GlobalClass.putPref("clientName", name, getApplicationContext());
                            GlobalClass.putPref("clientProfileImage", profileImage, getApplicationContext());
                            GlobalClass.putPref("userType", "client", getApplicationContext());
                            GlobalClass.putPref("clientNewStatus", new_status, getApplicationContext());

                            Intent i = new Intent(getApplicationContext(), ClientDrawerActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else {

                            Intent i = new Intent(AdvisorDrawerActivity.this, SignUpClientSwitch.class);

                            i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | i.FLAG_ACTIVITY_CLEAR_TASK);

                            Bundle bundle = new Bundle();
                            bundle.putString("userID", id);
                            i.putExtras(bundle);
                            startActivity(i);

                        }


                    } else {

                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(AdvisorDrawerActivity.this, "" + message, Toast.LENGTH_SHORT).show();

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
            String urlCredit = "getCredit?userId=" + AdvisorID + "&type=1";

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


                        String chat_counter = mResponse.getString("chat_counter");
                        String pending_orders = mResponse.getString("pending_orders");


                        if (Integer.valueOf(pending_orders) == 0) {
                            jobs_dot_img.setVisibility(View.GONE);
                        } else {
                            jobs_dot_img.setVisibility(View.VISIBLE);

                        }


                        if (Integer.valueOf(chat_counter) == 0) {
                            chat_counter_tv.setVisibility(View.GONE);
                        } else {
                            chat_counter_tv.setVisibility(View.VISIBLE);

                        }


                    } else {
                        String message = mResponse.getString("statusMessage");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


}

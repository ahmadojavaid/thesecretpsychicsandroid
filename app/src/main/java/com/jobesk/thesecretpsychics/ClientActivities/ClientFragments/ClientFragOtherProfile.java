package com.jobesk.thesecretpsychics.ClientActivities.ClientFragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jobesk.thesecretpsychics.Adapter.AdvisorProfileReviewsAdapter;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorShowVideo;
import com.jobesk.thesecretpsychics.ClientActivities.Chat.ClientGetChat;
import com.jobesk.thesecretpsychics.ClientActivities.ClientHostProfileActivity;
import com.jobesk.thesecretpsychics.ClientActivities.ClientRequestVideoActivity;
import com.jobesk.thesecretpsychics.EventBuses.AdvisorDrawerImage;
import com.jobesk.thesecretpsychics.Model.AdvisorReviewModel;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.ShareUtils;
import com.jobesk.thesecretpsychics.Utils.Urls;
import com.jobesk.thesecretpsychics.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ClientFragOtherProfile extends Fragment {

    private ScrollView scrollView;
    private String TAG = "ClientFragOtherProfile";
    private Activity activity;
    private ImageView userImage;
    private TextView userName_tv, categories_tv, about_tv, service_tv;
    private View rootView;
    private RecyclerView recyclerView;
    private AdvisorProfileReviewsAdapter mAdapter;
    private ArrayList<AdvisorReviewModel> reviewList = new ArrayList<>();
    private int positiveReviews = 0, negativeReviews = 0;
    private TextView sadReview_tv, hayyyReview_tv;
    private RatingBar rattingBar2, myRatingBar;
    int totalRattingValue = 0;
    private ImageView menu_icon;
    private int menuState = 0;
    private String AdvisorID;
    private ImageView back_img;
    private TextView psychic_txt2;
    private ImageView upload_video_img;
    private String serviceName, profileImage, aboutME, legalNameOfIndividual, profileStatus, token, email, advisorId, screenName;
    private JSONObject jsonObject;
    boolean favouriteCheck = false;
    private ArrayList<String> favouriteUsers = new ArrayList<>();
    private String ClientID;
    private AlertDialog alertShare;
    private ImageView chat_img;
    private String favuorited;
    private String isLiveChat, profileVideo = "", Is3MinuteVideo;
    private TextView three_min_video_title, three_min_video_desc, live_chat_title, live_chat_desc;
    private ImageView video_thumb_img;
    private FrameLayout video_container;
    private String TextChatRate, username, isOnline, aboutYourServices;
    private View ratting_line;
    private int shareScreenAppear = 0;
    private Bitmap map = null;
    private Drawable draw;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.client_frag_psychic_profile, container, false);
        AdvisorID = getArguments().getString("id");

        activity = (ClientHostProfileActivity) rootView.getContext();

        ClientID = GlobalClass.getPref("clientID", activity);
        scrollView = (ScrollView) rootView.findViewById(R.id.scrollView);
        scrollView.setVisibility(View.GONE);

        userImage = rootView.findViewById(R.id.userImage);
        chat_img = rootView.findViewById(R.id.chat_img);

        service_tv = rootView.findViewById(R.id.service_tv);
        about_tv = rootView.findViewById(R.id.about_tv);
        categories_tv = rootView.findViewById(R.id.categories_tv);


        userName_tv = rootView.findViewById(R.id.userName_tv);


        sadReview_tv = rootView.findViewById(R.id.sadReview_tv);
        hayyyReview_tv = rootView.findViewById(R.id.hayyyReview_tv);

        myRatingBar = rootView.findViewById(R.id.myRatingBar);
        rattingBar2 = rootView.findViewById(R.id.rattingBar2);

        menu_icon = rootView.findViewById(R.id.menu_icon);


        psychic_txt2 = rootView.findViewById(R.id.psychic_txt2);
        ratting_line = rootView.findViewById(R.id.ratting_line);

        three_min_video_title = rootView.findViewById(R.id.three_min_video_txt);
        three_min_video_desc = rootView.findViewById(R.id.three_min_video);
        live_chat_title = rootView.findViewById(R.id.live_chat_txt);
        live_chat_desc = rootView.findViewById(R.id.live_chat_text);
        video_thumb_img = rootView.findViewById(R.id.video_thumb_img);
        video_container = rootView.findViewById(R.id.video_container);

        getProfile();


        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY(); // For ScrollView
                int scrollX = scrollView.getScrollX(); // For HorizontalScrollView

                Log.d("scrollValue", scrollY + "");

                if (scrollY > 200) {

                    menu_icon.setImageResource(R.drawable.three_dots_blue);
                    menuState = 1;
                } else {
                    menuState = 0;
                    menu_icon.setImageResource(R.drawable.three_dots_gray);
                }


            }
        });

        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (menuState == 1) {

                    if (shareScreenAppear == 0) {
                        showChangeLangDialog();
                    }
                }


            }
        });

        back_img = rootView.findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().finish();
            }
        });


        upload_video_img = rootView.findViewById(R.id.upload_video_img);
        upload_video_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent videoIntent = new Intent(getActivity(), ClientRequestVideoActivity.class);
                Bundle bundle = new Bundle();
                videoIntent.putExtra("jsonObject", jsonObject.toString());
                videoIntent.putExtras(bundle);
                startActivity(videoIntent);


            }
        });


        chat_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String clientID = GlobalClass.getPref("clientID", activity);
                Intent intent = new Intent(activity, ClientGetChat.class);
                Bundle bundle = new Bundle();
                bundle.putString("sendTo", AdvisorID);
                bundle.putString("sendBy", clientID);
                bundle.putString("preName", screenName);
                bundle.putString("preImage", profileImage);
                bundle.putString("preOnline", isOnline);
                bundle.putString("preChatRate", TextChatRate);
                intent.putExtras(bundle);
                activity.startActivity(intent);


            }
        });


        video_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!profileVideo.equalsIgnoreCase("")) {

                    Intent videoIntent = new Intent(getActivity(), AdvisorShowVideo.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("videoLink", Urls.BASEURL + "" + profileVideo);
                    videoIntent.putExtras(bundle);
                    startActivity(videoIntent);
                }


            }
        });

        return rootView;
    }


    private void getProfile() {

        if (GlobalClass.isOnline(getActivity()) == true) {

            RequestParams mParams = new RequestParams();


            String clientID = GlobalClass.getPref("clientID", getActivity());
            mParams.put("userId", clientID);
//            mParams.put("aboutMe", about);


            WebReq.get(getActivity(), "showAdvisorInfo/" + AdvisorID + "?userId=" + clientID, mParams, new MyTextHttpResponseHandler());

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
                    positiveReviews = 0;
                    negativeReviews = 0;
                    totalRattingValue = 0;
                    if (status.equals("1")) {
                        JSONArray jsonArray = mResponse.getJSONArray("Result");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            jsonObject = jsonArray.getJSONObject(0);


                            serviceName = jsonObject.getString("serviceName");
                            profileImage = jsonObject.getString("profileImage");


                            aboutME = jsonObject.getString("aboutMe");
                            legalNameOfIndividual = jsonObject.getString("legalNameOfIndividual");
                            profileStatus = jsonObject.getString("profileStatus");
                            token = jsonObject.getString("token");
                            email = jsonObject.getString("email");
                            advisorId = jsonObject.getString("id");
                            screenName = jsonObject.getString("screenName");
                            favuorited = jsonObject.getString("favuorited");
                            isLiveChat = jsonObject.getString("liveChat");
                            Is3MinuteVideo = jsonObject.getString("threeMinuteVideo");
                            profileVideo = jsonObject.getString("profileVideo");
                            aboutYourServices = jsonObject.getString("aboutYourServices");
                            isOnline = jsonObject.getString("isOnline");
                            username = jsonObject.getString("username");
                            TextChatRate = jsonObject.getString("TextChatRate");


                            String videoThumb = Urls.BASEURL + profileVideo;


                            RequestOptions requestOptions = new RequestOptions();

                            requestOptions.encodeQuality(50);
                            requestOptions.centerCrop();

                            Glide.with(getActivity())
                                    .asBitmap()
                                    .load(videoThumb)

                                    .apply(requestOptions)
                                    .into(video_thumb_img);


                            live_chat_desc.setText(getActivity().getResources().getText(R.string.start_live_chat_text) + "" + TextChatRate + " " + getActivity().getResources().getString(R.string.per_160_characters));


                            if (isLiveChat.equalsIgnoreCase("1")) {
                            } else {
                                live_chat_title.setTextColor(getActivity().getResources().getColor(R.color.gray_text));
                                live_chat_desc.setTextColor(getActivity().getResources().getColor(R.color.gray_text));
                                chat_img.setImageResource(R.drawable.ic_chat_off);

                                chat_img.setEnabled(false);
                            }

                            if (Is3MinuteVideo.equalsIgnoreCase("1")) {
                            } else {
                                three_min_video_title.setTextColor(getActivity().getResources().getColor(R.color.gray_text));
                                three_min_video_desc.setTextColor(getActivity().getResources().getColor(R.color.gray_text));
                                upload_video_img.setImageResource(R.drawable.upload_video_icon_off);

                                upload_video_img.setEnabled(false);
                            }


                            if (favuorited.equalsIgnoreCase("1")) {
                                favouriteCheck = true;
                            } else {
                                favouriteCheck = false;
                            }


                            service_tv.setText(aboutYourServices + "");
                            about_tv.setText(aboutME + "");
                            psychic_txt2.setText(serviceName);


                            EventBus.getDefault().post(new AdvisorDrawerImage());
                            StringBuilder categoryString = new StringBuilder();
                            JSONArray jsonArrayCateGories = jsonObject.getJSONArray("advisorscategories");
                            for (int j = 0; j < jsonArrayCateGories.length(); j++) {

                                JSONObject jsonCatObj = jsonArrayCateGories.getJSONObject(j);
                                String categoryName = jsonCatObj.getString("categoryName");
                                categoryString.append(categoryName + ", ");


                            }

                            try {
                                categoryString.deleteCharAt(categoryString.length() - 2);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            categories_tv.setText(categoryString + "");


                            userName_tv.setText(screenName + "");

                            Picasso.with(getActivity()).load(Urls.IMAGE_BASEURL + "" + profileImage).fit().centerCrop().into(userImage);


                            JSONArray jsonArrayReviews = jsonObject.getJSONArray("advisors_reviews");
                            if (jsonArrayReviews.length() > 0) {
                                for (int k = 0; k < jsonArrayReviews.length(); k++) {
                                    JSONObject jsonObjectReviews = jsonArrayReviews.getJSONObject(k);


                                    String feedback = jsonObjectReviews.getString("feedback");
                                    String name = jsonObjectReviews.getString("name");
                                    String rating = jsonObjectReviews.getString("rating");
                                    String created_at = jsonObjectReviews.getString("created_at");


                                    AdvisorReviewModel model = new AdvisorReviewModel();
                                    model.setBody(feedback);
                                    model.setRatting(rating);
                                    model.setUserName(name);
                                    model.setDate(created_at);
                                    reviewList.add(model);


                                    if (Integer.valueOf(rating) > 3) {

                                        positiveReviews = positiveReviews + 1;

                                    }
                                    if (Integer.valueOf(rating) <= 3) {

                                        negativeReviews = negativeReviews + 1;

                                    }
                                    totalRattingValue = totalRattingValue + Integer.valueOf(rating);
                                }

                            } else {
                                ratting_line.setVisibility(View.GONE);
                            }


                            sadReview_tv.setText(String.valueOf(negativeReviews) + "");
                            hayyyReview_tv.setText(String.valueOf(positiveReviews) + "");


                            try {
                                int totalRatting = totalRattingValue / jsonArrayReviews.length();
                                myRatingBar.setRating(totalRatting);
                                rattingBar2.setRating(totalRatting);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            generateReviews();


                        }

                        scrollView.setVisibility(View.VISIBLE);
                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }
    }


    private void generateReviews() {

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        mAdapter = new AdvisorProfileReviewsAdapter(activity, reviewList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


    }

    public void showChangeLangDialog() {
        shareScreenAppear = 1;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity, R.style.Theme_D1NoTitleDim);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogView = inflater.inflate(R.layout.tooltip_custom, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        LinearLayout bg_alert = (LinearLayout) dialogView.findViewById(R.id.bg_alert);
        bg_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareScreenAppear = 0;
                alertShare.dismiss();

            }
        });


        ImageView fb = (ImageView) dialogView.findViewById(R.id.facebook_tv);
        ImageView twitter = (ImageView) dialogView.findViewById(R.id.twitter_tv);
        final ImageView favourite = (ImageView) dialogView.findViewById(R.id.favoutite_tv);
        ImageView menu_icon_alert = (ImageView) dialogView.findViewById(R.id.menu_icon_alert);
        menu_icon_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareScreenAppear = 0;
                alertShare.dismiss();
            }
        });

        if (favouriteCheck == true) {
            favourite.setBackgroundResource(R.drawable.ic_favourite);
        } else {
            favourite.setBackgroundResource(R.drawable.ic_unfavourite);
        }


        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String urlString = Urls.BASEURL + "profileLink?userName=" + username;
                ShareUtils.shareFacebook(activity, "", urlString);


            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlString = Urls.BASEURL + "profileLink?userName=" + username;
                ShareUtils.shareTwitter(activity, "", urlString, "", "");
            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (favouriteCheck == true) {
                    favourite.setBackgroundResource(R.drawable.ic_unfavourite);
                    UnFavourite();
                    favouriteCheck = false;
                } else {
                    favourite.setBackgroundResource(R.drawable.ic_favourite);
                    favouriteCheck = true;
                    makeItFavourite();
                }


            }
        });


        alertShare = dialogBuilder.create();


        if (map == null) {
            map = takeScreenShot(activity);
            Bitmap fast = fastblur(map, 24);
            draw = new BitmapDrawable(getResources(), fast);
        }


        alertShare.getWindow().setBackgroundDrawable(draw);
//        alertShare.getWindow().setLayout(AdvisorDrawerActivity.width, AdvisorDrawerActivity.height);


        alertShare.show();


    }


    public static boolean isPackageInstalled(Context c, String targetPackage) {
        PackageManager pm = c.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    private Bitmap takeScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);

        b = darkenBitMap(b);
        b = darkenBitMap(b);
        view.destroyDrawingCache();
        return b;
    }

    private Bitmap darkenBitMap(Bitmap bm) {

        Canvas canvas = new Canvas(bm);
        Paint p = new Paint(Color.BLACK);
        //ColorFilter filter = new LightingColorFilter(0xFFFFFFFF , 0x00222222); // lighten
        ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000);    // darken
        p.setColorFilter(filter);
        canvas.drawBitmap(bm, new Matrix(), p);

        return bm;
    }

    public Bitmap fastblur(Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }


    private void makeItFavourite() {

        if (GlobalClass.isOnline(getActivity()) == true) {

            RequestParams mParams = new RequestParams();
            mParams.put("userId", ClientID);
            mParams.put("advisorId", advisorId);


            Log.d("paramsFav", mParams + "");

            WebReq.post(getActivity(), "favourite", mParams, new MyTextHttpResponseHandlerFavourite());

        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerFavourite extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerFavourite() {


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


    private void UnFavourite() {

        if (GlobalClass.isOnline(getActivity()) == true) {

            RequestParams mParams = new RequestParams();
            mParams.put("userId", ClientID);
            mParams.put("advisorId", advisorId);


            WebReq.post(getActivity(), "delfavourite", mParams, new MyTextHttpResponseHandlerUnFavourite());

        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerUnFavourite extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerUnFavourite() {


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

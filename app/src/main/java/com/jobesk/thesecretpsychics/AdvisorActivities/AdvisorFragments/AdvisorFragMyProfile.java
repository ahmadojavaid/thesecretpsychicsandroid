package com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jobesk.thesecretpsychics.Adapter.AdvisorProfileReviewsAdapter;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorDrawerActivity;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorShowVideo;
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

public class AdvisorFragMyProfile extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ImageView menu_img;
    private TextView toolbar_title;
    private ScrollView scrollView;
    private String TAG = "AdvisorFragMyProfile";
    private Activity activity;
    private ImageView userImage;
    private TextView userName_tv, categories_tv, about_tv, service_tv;

    private RecyclerView recyclerView;
    private AdvisorProfileReviewsAdapter mAdapter;
    private ArrayList<AdvisorReviewModel> reviewList = new ArrayList<>();
    private int positiveReviews = 0, negativeReviews = 0;
    private TextView sadReview_tv, hayyyReview_tv;
    private RatingBar rattingBar2, myRatingBar;
    int totalRattingValue = 0;
    private ImageView menu_icon;
    private int menuState = 0;
    private int shareScreenAppear = 0;
    private TextView psychic_txt2;
    private View lineAboveReviesRows;
    private AlertDialog alertShare;
    private LinearLayout no_internetContainer;
    private TextView refresh_tv;
    private View rootView = null;
    private String username, profileVideo;
    private FrameLayout video_container;
    private Bitmap map = null;
    private ImageView video_thumb_img;
    private Drawable draw;
    private SwipeRefreshLayout refreshLayout;
    private boolean refresh = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (rootView == null) {

            rootView = inflater.inflate(R.layout.advisor_frag_my_profile, container, false);


            activity = (AdvisorDrawerActivity) rootView.getContext();


            menu_img = rootView.findViewById(R.id.menu_img);
            menu_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AdvisorDrawerActivity.openDrawer();

                }
            });

            toolbar_title = rootView.findViewById(R.id.toolbar_title);
            toolbar_title.setText(getActivity().getResources().getString(R.string.my_profile));


            refreshLayout = rootView.findViewById(R.id.refresh);
            refreshLayout.setOnRefreshListener(this);


            no_internetContainer = rootView.findViewById(R.id.no_internetContainer);

            scrollView = (ScrollView) rootView.findViewById(R.id.scrollView);
            refresh_tv = rootView.findViewById(R.id.refresh_tv);


            userImage = rootView.findViewById(R.id.userImage);


            lineAboveReviesRows = rootView.findViewById(R.id.lineAboveReviesRows);
            service_tv = rootView.findViewById(R.id.service_tv);
            about_tv = rootView.findViewById(R.id.about_tv);
            categories_tv = rootView.findViewById(R.id.categories_tv);

            video_thumb_img = rootView.findViewById(R.id.video_thumb_img);
            video_container = rootView.findViewById(R.id.video_container);


            userName_tv = rootView.findViewById(R.id.userName_tv);


            sadReview_tv = rootView.findViewById(R.id.sadReview_tv);
            hayyyReview_tv = rootView.findViewById(R.id.hayyyReview_tv);

            myRatingBar = rootView.findViewById(R.id.myRatingBar);
            rattingBar2 = rootView.findViewById(R.id.rattingBar2);

            menu_icon = rootView.findViewById(R.id.menu_icon);


            psychic_txt2 = rootView.findViewById(R.id.psychic_txt2);


            getProfile();


            scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    int scrollY = scrollView.getScrollY(); // For ScrollView
                    int scrollX = scrollView.getScrollX(); // For HorizontalScrollView

                    Log.d("scrollValue", scrollY + "");

                    if (scrollY > 200) {

                        menu_icon.setImageResource(R.drawable.three_dots_blue_ad);
                        menuState = 1;
                    } else {
                        menuState = 0;
                        menu_icon.setImageResource(R.drawable.three_dots_gray_ad);
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


            if (GlobalClass.isOnline(getActivity()) == true) {
                scrollView.setVisibility(View.VISIBLE);
                no_internetContainer.setVisibility(View.GONE);
            } else {
                scrollView.setVisibility(View.GONE);
                no_internetContainer.setVisibility(View.VISIBLE);
            }


            refresh_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getProfile();


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

        }
        return rootView;
    }

    @Override
    public void onRefresh() {

        refresh = true;
        if (reviewList.size() > 0) {

            reviewList.clear();

        }

        getProfile();
    }


    private void getProfile() {

        if (GlobalClass.isOnline(getActivity()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("aboutYourServices", service);
//            mParams.put("aboutMe", about);

            String AdvisorID = GlobalClass.getPref("advisorID", getActivity());
            WebReq.get(getActivity(), "showAdvisorInfo/" + AdvisorID, mParams, new MyTextHttpResponseHandler());

        } else {
            no_internetContainer.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandler extends JsonHttpResponseHandler {
        MyTextHttpResponseHandler() {

        }

        @Override
        public void onStart() {

            if (refresh==false){
                GlobalClass.showLoading(activity);
            }



            refresh = false;

            refreshLayout.setRefreshing(false);
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
                scrollView.setVisibility(View.VISIBLE);
                no_internetContainer.setVisibility(View.GONE);
                try {
                    String status = mResponse.getString("statusCode");
                    positiveReviews = 0;
                    negativeReviews = 0;
                    totalRattingValue = 0;
                    if (status.equals("1")) {
                        JSONArray jsonArray = mResponse.getJSONArray("Result");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(0);

                            String serviceName = jsonObject.getString("serviceName");
                            String profileImage = jsonObject.getString("profileImage");

                            GlobalClass.putPref("advisorImage", profileImage, getActivity());


                            String aboutME = jsonObject.getString("aboutMe");
                            String legalNameOfIndividual = jsonObject.getString("legalNameOfIndividual");
                            String profileStatus = jsonObject.getString("profileStatus");
                            String token = jsonObject.getString("token");
                            String email = jsonObject.getString("email");
                            String id = jsonObject.getString("id");
                            String screenName = jsonObject.getString("screenName");
                            String threeMinuteVideo = jsonObject.getString("threeMinuteVideo");
                            String liveChat = jsonObject.getString("liveChat");
                            String aboutYourServices = jsonObject.getString("aboutYourServices");
                            profileVideo = jsonObject.getString("profileVideo");
                            username = jsonObject.getString("username");


                            Log.d("userID", id);

                            GlobalClass.putPref("advisorName", screenName, getActivity());
                            GlobalClass.putPref("advisorID", id, getActivity());
                            GlobalClass.putPref("advisorEmail", email, getActivity());
                            GlobalClass.putPref("advisorToken", token, getActivity());
                            GlobalClass.putPref("advisorProfileStatus", profileStatus, getActivity());
                            GlobalClass.putPref("advisor3MinVideo", threeMinuteVideo, getActivity());
                            GlobalClass.putPref("advisorLiveChat", liveChat, getActivity());


                            String videoThumb = Urls.BASEURL + profileVideo;


                            RequestOptions requestOptions = new RequestOptions();

                            requestOptions.encodeQuality(50);
                            requestOptions.centerCrop();

                            Glide.with(getActivity())
                                    .asBitmap()
                                    .load(videoThumb)

                                    .apply(requestOptions)
                                    .into(video_thumb_img);


                            psychic_txt2.setText(serviceName + "");

                            service_tv.setText(aboutYourServices + "");
                            about_tv.setText(aboutME + "");

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

                            if (jsonArrayReviews.length() > 0) {

                                sadReview_tv.setText(String.valueOf(negativeReviews) + "");
                                hayyyReview_tv.setText(String.valueOf(positiveReviews) + "");


                                int totalRatting = totalRattingValue / jsonArrayReviews.length();
                                myRatingBar.setRating(totalRatting);
                                rattingBar2.setRating(totalRatting);
                                generateReviews();

                            } else {

                                lineAboveReviesRows.setVisibility(View.GONE);

                            }


                        }


                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
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
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity, R.style.Theme_D1NoTitleDim);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        dialogBuilder.setCancelable(false);

        final View dialogView = inflater.inflate(R.layout.tooltip_custom_own_profile, null);
        dialogBuilder.setView(dialogView);


        ImageView fb = (ImageView) dialogView.findViewById(R.id.facebook_tv);
        ImageView twitter = (ImageView) dialogView.findViewById(R.id.twitter_tv);
        ImageView favourite = (ImageView) dialogView.findViewById(R.id.favoutite_tv);
        ImageView menu_icon_alert = (ImageView) dialogView.findViewById(R.id.menu_icon_alert);


        LinearLayout

                bg_alert = dialogView.findViewById(R.id.bg_alert);


        bg_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareScreenAppear = 0;

                alertShare.dismiss();
            }
        });


        menu_icon_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareScreenAppear = 0;

                alertShare.dismiss();


            }
        });


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
//
//        favourite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


        alertShare = dialogBuilder.create();


        if (map == null) {
            map = takeScreenShot(activity);

            Bitmap fast = fastblur(map, 24);
            draw = new BitmapDrawable(getResources(), fast);
        }

        alertShare.getWindow().setBackgroundDrawable(draw);
        alertShare.getWindow().setLayout(AdvisorDrawerActivity.width, AdvisorDrawerActivity.height);
        alertShare.show();


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
}

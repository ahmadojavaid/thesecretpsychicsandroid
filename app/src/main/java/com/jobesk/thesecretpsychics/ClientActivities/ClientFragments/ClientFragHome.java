package com.jobesk.thesecretpsychics.ClientActivities.ClientFragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.jobesk.thesecretpsychics.Adapter.ClientPsychicAdapter;
import com.jobesk.thesecretpsychics.ClientActivities.ClientDrawerActivity;
import com.jobesk.thesecretpsychics.Model.ClientPsychicModel;
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

public class ClientFragHome extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private RecyclerView recyclerView_psychic;
    private ArrayList<ClientPsychicModel> psychicArrayList = new ArrayList<ClientPsychicModel>();
    private ArrayList<ClientPsychicModel> featurePsychicArrayList = new ArrayList<ClientPsychicModel>();
    private ImageView menu_img;
    private ClientPsychicAdapter adapter;
    private ClientPsychicModel psychicModel;
    private GridLayoutManager mLayoutManager;
    private Activity activity;
    private AlertDialog altertFilter;
    private AlertDialog altertSort;
    private ImageView filter_img, sort_img;
    private String TAG = "ClientFragHome";
    private View rootView = null;
    private SwipeRefreshLayout refreshLayout;

    int filterUserStatus = 3;


    int reviewValue = 10;
    String lowerLimitValue = "0";
    String highlimitValue = "100";

    // 1 for normal psychics
    // 2 for sort psychics
    // 3 for filter psychics

    private int visibleThreshold = 12;
    private int apiStatus = 1;
    private String sortValue = "";
    private String filterValue = "";
    private boolean refresh = false;
    private int previousTotal = 0;
    //    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        EventBus.getDefault().register(this);
//    }
    private int currentPage = 1;
    private boolean loading = true;
    private int totalPage;
    boolean headerAvailable = true;
    private String lowerLimitReviewValue = "0";
    private String highlimitReviewValue = "100";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (rootView == null) {
            rootView = inflater.inflate(R.layout.client_frag_home, container, false);

            activity = (ClientDrawerActivity) rootView.getContext();


            refreshLayout = rootView.findViewById(R.id.refresh);
            refreshLayout.setOnRefreshListener(this);

//            FragmentManager fragmentManger = getActivity().getSupportFragmentManager();
            FragmentManager fragmentManger = getChildFragmentManager();

            adapter = new ClientPsychicAdapter(activity, getActivity(), (ArrayList<ClientPsychicModel>) psychicArrayList, fragmentManger, featurePsychicArrayList);


            recyclerView_psychic = rootView.findViewById(R.id.recycler_view);

//        recyclerView_psychic.setLayoutManager(new GridLayoutManager(getActivity(), 2));

            mLayoutManager = new GridLayoutManager(getActivity(), 2);
            mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (adapter.getItemViewType(position)) {
                        case ClientPsychicModel.TYPE_HEADER:
                            return 2;

                        case ClientPsychicModel.TYPE_ROW:
                            return 1;

                        default:
                            return 1;
                    }
                }
            });
            recyclerView_psychic.setLayoutManager(mLayoutManager);
            recyclerView_psychic.setAdapter(adapter);

            adapter.notifyDataSetChanged();

            menu_img = rootView.findViewById(R.id.menu_img);
            menu_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ClientDrawerActivity.openDrawer();


                }
            });


            filter_img = rootView.findViewById(R.id.filter_img);
            sort_img = rootView.findViewById(R.id.sort_img);

            filter_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFilterDialouge();
                }
            });

            sort_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSortDialouge();
                }
            });


            getPSychics();


        }


        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onRefresh() {

        refresh = true;


        if (psychicArrayList.size() > 0) {
            psychicArrayList.clear();
            featurePsychicArrayList.clear();
            adapter.notifyDataSetChanged();

        }

        loading = true;
        currentPage = 1;
        headerAvailable = true;
        previousTotal = 0;
        apiStatus = 1;
        totalPage = 0;
        getPSychics();
    }

    private void getPSychics() {

        if (GlobalClass.isOnline(activity) == true) {


            // 1 for normal psychics
            // 2 for sort psychics
            // 3 for filter psychics

            if (apiStatus == 1) {


                RequestParams mParams = new RequestParams();
//            mParams.put("aboutYourServices", service);

                WebReq.get(activity, "showPsychics?page=" + currentPage, mParams, new MyTextHttpResponseHandler());

            } else if (apiStatus == 2) {

                RequestParams mParams = new RequestParams();
//                mParams.put(sortValue, "");

                WebReq.get(activity, "search?" + sortValue + "&page=" + currentPage, mParams, new MyTextHttpResponseHandler());


            } else if (apiStatus == 3) {

                RequestParams mParams = new RequestParams();
                mParams.put("lowerLimitRate", String.valueOf(lowerLimitValue));
                mParams.put("upperLimitRate", String.valueOf(highlimitValue));
                mParams.put("lowerLimitReview", String.valueOf(lowerLimitReviewValue));
                mParams.put("upperLimitReview", String.valueOf(highlimitReviewValue));

                if (filterUserStatus == 1) {

                    mParams.put("online", "1");

                } else if (filterUserStatus == 2) {

                    mParams.put("offline", "0");

                } else if (filterUserStatus == 3) {

                    mParams.put("both", "1");

                }

                Log.d("FilterHomeParams", mParams + "");

                WebReq.get(activity, "advanceSearch?page=" + currentPage, mParams, new MyTextHttpResponseHandler());

            }

        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }

    public void logLargeString(String str) {
        if (str.length() > 3000) {
            Log.i(TAG, str.substring(0, 3000));
            logLargeString(str.substring(3000));
        } else {
            Log.i(TAG, str); // continuation
        }
    }

    private class MyTextHttpResponseHandler extends JsonHttpResponseHandler {

        MyTextHttpResponseHandler() {


        }

        @Override
        public void onStart() {
            super.onStart();
            if (refresh == true) {
                refreshLayout.setRefreshing(true);
                refresh = false;
            } else {

                GlobalClass.showLoading(activity);

            }


        }

        @Override
        public void onFinish() {
            super.onFinish();
            GlobalClass.dismissLoading();
            refreshLayout.setRefreshing(false);
//            Log.d(TAG, "onFinish");

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {
//            Log.d(TAG, "OnFailure" + e);
            GlobalClass.dismissLoading();
            refreshLayout.setRefreshing(false);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
//            Log.d(TAG, responseString);
            GlobalClass.dismissLoading();
            refreshLayout.setRefreshing(false);
        }


        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {
            refreshLayout.setRefreshing(false);
            GlobalClass.dismissLoading();

            logLargeString(mResponse.toString());

            if (mResponse != null && mResponse.length() != 0) {

                try {
                    String status = mResponse.getString("statusCode");

                    if (status.equals("1")) {

                        if (headerAvailable == true) {


                        }


                        JSONObject jsonObject = mResponse.getJSONObject("All psychics");

                        currentPage = Integer.valueOf(jsonObject.getString("current_page"));
                        totalPage = Integer.valueOf(jsonObject.getString("total"));
                        JSONArray jsonPsychics = jsonObject.getJSONArray("data");


                        if (jsonPsychics.length() > 0) {

                            if (headerAvailable == true) {


                                psychicModel = new ClientPsychicModel();
                                psychicModel.setType(ClientPsychicModel.TYPE_HEADER);
                                psychicModel.setDescription("its DesCription");
                                psychicModel.setImage("");
                                psychicArrayList.add(psychicModel);
                                headerAvailable = false;

                                JSONArray jsonFeaturePsychics = mResponse.getJSONArray("featured");

                                for (int i = 0; i < jsonFeaturePsychics.length(); i++) {

                                    JSONObject jsonObject1 = jsonFeaturePsychics.getJSONObject(i);
                                    String id = jsonObject1.getString("advisorId");
                                    String email = jsonObject1.getString("email");
                                    String profileImage = jsonObject1.getString("profileImage");
                                    String legalNameOfIndividual = jsonObject1.getString("legalNameOfIndividual");
                                    String aboutMe = jsonObject1.getString("aboutMe");
                                    String ratting = jsonObject1.getString("rating");
                                    String serviceName = jsonObject1.getString("serviceName");

                                    String isOnline = jsonObject1.getString("isOnline");
                                    String liveChat = jsonObject1.getString("liveChat");
                                    String threeMinuteVideo = jsonObject1.getString("threeMinuteVideo");
                                    String screenName = jsonObject1.getString("screenName");
                                    String TextChatRate = jsonObject1.getString("TextChatRate");
                                    String timeRate = jsonObject1.getString("timeRate");

                                    ClientPsychicModel model = new ClientPsychicModel();
                                    model.setScreenName(screenName);
                                    model.setPsychicID(id);
                                    model.setEmail(email);
                                    model.setImage(profileImage);
                                    model.setName(legalNameOfIndividual);
                                    model.setDescription(aboutMe);
                                    model.setType(ClientPsychicModel.TYPE_ROW);
                                    model.setRatting(ratting);
                                    model.setIsOnline(isOnline);
                                    model.setServiceName(serviceName);
                                    model.setIsLiveChat(liveChat);
                                    model.setIsLiveVideo(threeMinuteVideo);
                                    featurePsychicArrayList.add(model);

                                }

                            }
                            for (int i = 0; i < jsonPsychics.length(); i++) {

                                JSONObject jsonObject1 = jsonPsychics.getJSONObject(i);
                                String id = jsonObject1.getString("id");

                                String email = jsonObject1.getString("email");
                                String profileImage = jsonObject1.getString("profileImage");
                                String legalNameOfIndividual = jsonObject1.getString("legalNameOfIndividual");
                                String aboutMe = jsonObject1.getString("aboutMe");
                                String ratting = jsonObject1.getString("rating");
                                String serviceName = jsonObject1.getString("serviceName");
                                String isOnline = jsonObject1.getString("isOnline");
                                String profileVideo = jsonObject1.getString("profileVideo");
                                String liveChat = jsonObject1.getString("liveChat");
                                String threeMinuteVideo = jsonObject1.getString("threeMinuteVideo");
                                String screenName = jsonObject1.getString("screenName");


                                ClientPsychicModel model = new ClientPsychicModel();
                                model.setScreenName(screenName);
                                model.setPsychicID(id);
                                model.setEmail(email);
                                model.setImage(profileImage);
                                model.setName(legalNameOfIndividual);
                                model.setDescription(aboutMe);
                                model.setType(ClientPsychicModel.TYPE_ROW);
                                model.setRatting(ratting);
                                model.setIsOnline(isOnline);
                                model.setServiceName(serviceName);
                                model.setAdvisorID(id);
                                model.setProfileVideo(profileVideo);
                                model.setIsLiveChat(liveChat);
                                model.setIsLiveVideo(threeMinuteVideo);
                                psychicArrayList.add(model);


                            }


                            adapter.notifyDataSetChanged();

                        }


                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
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
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
                int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

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

                            if (GlobalClass.isOnline(getActivity())) {
//                                    progressBar.setVisibility(View.VISIBLE);
                            } else {
//                                    progressBar.setVisibility(View.GONE);
                            }
                            if (currentPage <= totalPage) {


                                getPSychics();
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

    public void showSortDialouge() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogView = inflater.inflate(R.layout.custom_dialog_sort, null);
        dialogBuilder.setView(dialogView);

        ImageView closeAlert_img = dialogView.findViewById(R.id.closeAlert_img);
        closeAlert_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                altertSort.dismiss();
            }
        });


        TextView userRatting_tv = dialogView.findViewById(R.id.userRatting_tv);
        TextView price_low_to_high = dialogView.findViewById(R.id.price_low_to_high);
        TextView price_high_to_low = dialogView.findViewById(R.id.price_high_to_low);
        TextView no_of_reviews_tv = dialogView.findViewById(R.id.no_of_reviews_tv);


        userRatting_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                altertSort.dismiss();
                apiStatus = 2;
                sortValue = "rating";


                if (psychicArrayList.size() > 0) {
                    psychicArrayList.clear();
                    featurePsychicArrayList.clear();
                    adapter.notifyDataSetChanged();
                }

                loading = true;
                currentPage = 1;
                headerAvailable = true;
                previousTotal = 0;
                totalPage = 0;
                getPSychics();
            }
        });
        price_low_to_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                altertSort.dismiss();
                apiStatus = 2;
                sortValue = "lowToHigh";


                if (psychicArrayList.size() > 0) {
                    psychicArrayList.clear();
                    featurePsychicArrayList.clear();
                    adapter.notifyDataSetChanged();

                }

                loading = true;
                currentPage = 1;
                headerAvailable = true;
                previousTotal = 0;
                totalPage = 0;
                getPSychics();

            }
        });

        price_high_to_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                altertSort.dismiss();
                apiStatus = 2;
                sortValue = "highToLow";


                if (psychicArrayList.size() > 0) {
                    psychicArrayList.clear();
                    featurePsychicArrayList.clear();
                    adapter.notifyDataSetChanged();

                }

                loading = true;
                currentPage = 1;
                headerAvailable = true;
                previousTotal = 0;
                totalPage = 0;
                getPSychics();
            }
        });

        no_of_reviews_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                altertSort.dismiss();

                apiStatus = 2;
                sortValue = "reviews";


                if (psychicArrayList.size() > 0) {
                    psychicArrayList.clear();
                    featurePsychicArrayList.clear();
                    adapter.notifyDataSetChanged();

                }

                loading = true;
                currentPage = 1;
                headerAvailable = true;
                previousTotal = 0;
                totalPage = 0;
                getPSychics();

            }
        });


        altertSort = dialogBuilder.create();
        altertSort.show();
    }


//    filterValue

    public void showFilterDialouge() {

        String userStatus = "";
        String numberofReview = "0";
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogView = inflater.inflate(R.layout.custom_dialog_filter, null);
        dialogBuilder.setView(dialogView);

        final TextView online_users_tv = dialogView.findViewById(R.id.online_users_tv);
        final TextView both_tv = dialogView.findViewById(R.id.both_tv);


        final TextView offline_users_tv = dialogView.findViewById(R.id.offline_users_tv);
        ImageView ic_close = dialogView.findViewById(R.id.ic_close);


        TextView search_tv = dialogView.findViewById(R.id.search_tv);
        search_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                altertFilter.dismiss();


                if (psychicArrayList.size() > 0) {
                    psychicArrayList.clear();
                    featurePsychicArrayList.clear();
                    adapter.notifyDataSetChanged();

                }


                apiStatus = 3;
                loading = true;
                currentPage = 1;
                headerAvailable = true;
                previousTotal = 0;
                totalPage = 0;
                getPSychics();


            }
        });
        ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                altertFilter.dismiss();


            }
        });

        online_users_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                online_users_tv.setBackgroundResource(R.drawable.english_btn_blue);
                online_users_tv.setTextColor(getResources().getColor(R.color.white));

                offline_users_tv.setBackgroundResource(R.drawable.live_btn_gray);
                offline_users_tv.setTextColor(getResources().getColor(R.color.border_color));

                both_tv.setBackgroundResource(R.drawable.both_btn_gray);
                both_tv.setTextColor(getResources().getColor(R.color.border_color));


                filterUserStatus = 1;
            }
        });
        both_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                online_users_tv.setBackgroundResource(R.drawable.vide_msg_btn_gray);
                online_users_tv.setTextColor(getResources().getColor(R.color.border_color));

                offline_users_tv.setBackgroundResource(R.drawable.live_btn_gray);
                offline_users_tv.setTextColor(getResources().getColor(R.color.border_color));

                both_tv.setBackgroundResource(R.drawable.both_btn_blue);
                both_tv.setTextColor(getResources().getColor(R.color.white));


                filterUserStatus = 3;

            }
        });
        offline_users_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                online_users_tv.setBackgroundResource(R.drawable.vide_msg_btn_gray);
                online_users_tv.setTextColor(getResources().getColor(R.color.border_color));

                offline_users_tv.setBackgroundResource(R.drawable.live_btn_blue);
                offline_users_tv.setTextColor(getResources().getColor(R.color.white));

                both_tv.setBackgroundResource(R.drawable.both_btn_gray);
                both_tv.setTextColor(getResources().getColor(R.color.border_color));

                filterUserStatus = 2;


            }
        });


        // get seekbar from view
        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) dialogView.findViewById(R.id.rangeSeekbar1);

// get min and max text view
        final TextView tvMin = (TextView) dialogView.findViewById(R.id.low_valueTv);
        final TextView tvMax = (TextView) dialogView.findViewById(R.id.high_value_tv);

// set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText(String.valueOf(getActivity().getResources().getString(R.string.pound) + "" + minValue));
                tvMax.setText(String.valueOf(getActivity().getResources().getString(R.string.pound) + "" + maxValue));


                lowerLimitValue = String.valueOf(minValue);
                highlimitValue = String.valueOf(maxValue);


            }
        });

// set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });


        final TextView zero_tv = dialogView.findViewById(R.id.zero_tv);
        final TextView ten_tv = dialogView.findViewById(R.id.ten_tv);
        final TextView hundred_tv = dialogView.findViewById(R.id.hundred_tv);
        final TextView five_hundred_tv = dialogView.findViewById(R.id.five_hundred_tv);

        zero_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zero_tv.setBackgroundResource(R.drawable.video_msg_btn_blue);
                ten_tv.setBackgroundResource(R.drawable.live_btn_gray);
                hundred_tv.setBackgroundResource(R.drawable.live_btn_gray);
                five_hundred_tv.setBackgroundResource(R.drawable.both_btn_gray);

                zero_tv.setTextColor(getResources().getColor(R.color.white));
                ten_tv.setTextColor(getResources().getColor(R.color.border_color));
                hundred_tv.setTextColor(getResources().getColor(R.color.border_color));
                five_hundred_tv.setTextColor(getResources().getColor(R.color.border_color));


                reviewValue = 0;


            }
        });
        ten_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                zero_tv.setBackgroundResource(R.drawable.vide_msg_btn_gray);
                ten_tv.setBackgroundResource(R.drawable.live_btn_blue);
                hundred_tv.setBackgroundResource(R.drawable.live_btn_gray);
                five_hundred_tv.setBackgroundResource(R.drawable.both_btn_gray);

                zero_tv.setTextColor(getResources().getColor(R.color.border_color));
                ten_tv.setTextColor(getResources().getColor(R.color.white));
                hundred_tv.setTextColor(getResources().getColor(R.color.border_color));
                five_hundred_tv.setTextColor(getResources().getColor(R.color.border_color));

                reviewValue = 10;

            }
        });
        hundred_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zero_tv.setBackgroundResource(R.drawable.vide_msg_btn_gray);
                ten_tv.setBackgroundResource(R.drawable.live_btn_gray);
                hundred_tv.setBackgroundResource(R.drawable.live_btn_blue);
                five_hundred_tv.setBackgroundResource(R.drawable.both_btn_gray);

                zero_tv.setTextColor(getResources().getColor(R.color.border_color));
                ten_tv.setTextColor(getResources().getColor(R.color.border_color));
                hundred_tv.setTextColor(getResources().getColor(R.color.white));
                five_hundred_tv.setTextColor(getResources().getColor(R.color.border_color));

                reviewValue = 100;

            }
        });
        five_hundred_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zero_tv.setBackgroundResource(R.drawable.vide_msg_btn_gray);
                ten_tv.setBackgroundResource(R.drawable.live_btn_gray);
                hundred_tv.setBackgroundResource(R.drawable.live_btn_gray);
                five_hundred_tv.setBackgroundResource(R.drawable.both_btn_blue);

                zero_tv.setTextColor(getResources().getColor(R.color.border_color));
                ten_tv.setTextColor(getResources().getColor(R.color.border_color));
                hundred_tv.setTextColor(getResources().getColor(R.color.border_color));
                five_hundred_tv.setTextColor(getResources().getColor(R.color.white));
                reviewValue = 500;

            }
        });


        /////////////////////////seekbar reviews here
        final TextView low_reviews_tv, high_reviews_tv;
        low_reviews_tv = dialogView.findViewById(R.id.low_reviews_tv);
        high_reviews_tv = dialogView.findViewById(R.id.high_reviews_tv);


        // get seekbar from view
        final CrystalRangeSeekbar rangeSeekbarReviews = (CrystalRangeSeekbar) dialogView.findViewById(R.id.rangeSeekbarReviews);
// set listener
        rangeSeekbarReviews.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                low_reviews_tv.setText(String.valueOf(minValue));
                high_reviews_tv.setText(String.valueOf(maxValue));


                lowerLimitReviewValue = String.valueOf(minValue);
                highlimitReviewValue = String.valueOf(maxValue);


            }
        });


        altertFilter = dialogBuilder.create();
        altertFilter.show();
    }


//    @Override
//    public void onDetach() {
//        super.onDetach();
//        EventBus.getDefault().unregister(this);
//
//    }
}

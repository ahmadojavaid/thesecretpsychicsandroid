package com.jobesk.thesecretpsychics.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.jobesk.thesecretpsychics.ClientActivities.ClientDrawerActivity;
import com.jobesk.thesecretpsychics.ClientActivities.ClientFragments.ClientFragCredits;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

public class CreditBuyActivity extends AppCompatActivity implements View.OnClickListener, PurchasesUpdatedListener, ConsumeResponseListener {
    private ImageView back_img;
    private TextView toolbar_title;


    private TextView five_pound_tv, ten_pound_tv, fifteen_pound_tv, fifty_pound_tv, hundred_pound_tv;


    private AlertDialog.Builder builder;
    private final String LICENCE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhVPtYBBvlhFwg3FsTvLjeSkqUc6xIpzR/X0+bqS+W7S42BMPnpyTtK5p2fTtIF4Dk1NlXG47nVJKK+kCwgCNZrt+8U473FIowLkt3knFj+DEdy5vMEN4e6CwuTWmoq9/hciisggJUYiqfo+er/DWF8yGF7x+gF3zlPpbPVAEOuTf7JWRiVI3r17wAX8YRJVw8t98nP14t3SJTdsjWaWYsl5uWHIIT78GgvoWUDlcZ5qHzTTWoXFEmTSs8X7Wk7+zqHYT31Vz6WmlaO9MGs66awzPWl4enlDOFhSVQIGmk/p34m+KJ3AkZl7wunKr/htVWV39hrKkdY9rAdDAOWB1zQIDAQAB";


    private static final String MERCHANT_ID = "06903003836601096044";

    private String TAG = "ClientFragCredits";

    private double buyingStatus;

    private String ProductName = "";
    private BillingClient billingClient;

    private List skuList;
    private SkuDetailsParams.Builder params;
    private Set<String> mTokensToBeConsumed;
    private EditText promo_code_et;

    private String promoCode = "";
    private TextView message_tv_promo;
    private String discount = "0";
    private boolean validPromo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_buy);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getApplicationContext().getResources().getString(R.string.buy_credit));

        back_img = findViewById(R.id.back_img);
        five_pound_tv = findViewById(R.id.five_pound_tv);
        ten_pound_tv = findViewById(R.id.ten_pound_tv);
        fifteen_pound_tv = findViewById(R.id.fifteen_pound_tv);
        fifty_pound_tv = findViewById(R.id.fifty_pound_tv);
        hundred_pound_tv = findViewById(R.id.hundred_pound_tv);


        promo_code_et = findViewById(R.id.promo_code_et);
        message_tv_promo = findViewById(R.id.message_tv_promo);

        five_pound_tv.setOnClickListener(this);
        ten_pound_tv.setOnClickListener(this);
        fifteen_pound_tv.setOnClickListener(this);
        fifty_pound_tv.setOnClickListener(this);
        hundred_pound_tv.setOnClickListener(this);


        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });


        billingClient = BillingClient.newBuilder(CreditBuyActivity.this).setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    // The billing client is ready. You can query purchases here.
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });


        promo_code_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                Log.d("TextWatcherTest", "afterTextChanged:\t" + s.toString() + "           length" + s.length());


                if (s.length() == 9) {

                    promoCode = promo_code_et.getText().toString().trim();

                    checkPromo();


                } else {
                    validPromo = false;
                    discount = "0";
                    message_tv_promo.setVisibility(View.GONE);


                    five_pound_tv.setBackgroundResource(R.drawable.rounded_blue);
                    ten_pound_tv.setBackgroundResource(R.drawable.rounded_blue);
                    fifteen_pound_tv.setBackgroundResource(R.drawable.rounded_blue);
                    fifty_pound_tv.setBackgroundResource(R.drawable.rounded_gray);
                    hundred_pound_tv.setBackgroundResource(R.drawable.rounded_gray);

                }


            }
        });


    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {

        String tokens = "";
        if (responseCode == BillingClient.BillingResponse.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {
                tokens = purchase.getPurchaseToken();
//                Toast.makeText(activity, "handlePurchae", Toast.LENGTH_SHORT).show();
                billingClient.consumeAsync(tokens, this);
                updateCredit();
            }
            consumeAsync(tokens);
        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Log.d(TAG, "User Canceled" + responseCode);
        } else if (responseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED) {
            Log.d(TAG, "ITEM_ALREADY_OWNED" + responseCode);

            for (Purchase purchase : purchases) {
                tokens = purchase.getPurchaseToken();
                Log.d("tokenshere", tokens);
                billingClient.consumeAsync(tokens, this);
            }


//            mSharedPreferences.edit().putBoolean(getResources().getString(R.string.pref_remove_ads_key), true).commit();
//            setAdFree(true);
//            mBuyButton.setText(getResources().getString(R.string.pref_ad_removal_purchased));
//            mBuyButton.setEnabled(false);
        } else {
            Log.d(TAG, "Other code" + responseCode);
            // Handle any other error codes.
        }


    }

    @Override
    public void onConsumeResponse(int responseCode, String purchaseToken) {


        Log.d("TAG", responseCode + "    " + purchaseToken);

    }

    public void consumeAsync(final String purchaseToken) {
        // If we've already scheduled to consume this token - no action is needed (this could happen
        // if you received the token when querying purchases inside onReceive() and later from
        // onActivityResult()
        if (mTokensToBeConsumed == null) {
            mTokensToBeConsumed = new HashSet<>();
        } else if (mTokensToBeConsumed.contains(purchaseToken)) {
            Log.i(TAG, "Token was already scheduled to be consumed - skipping...");
            return;
        }
        mTokensToBeConsumed.add(purchaseToken);

        // Generating Consume Response listener
        final ConsumeResponseListener onConsumeListener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(@BillingClient.BillingResponse int responseCode, String purchaseToken) {
                // If billing service was disconnected, we try to reconnect 1 time
                // (feel free to introduce your retry policy here).
//                billingClient.onConsumeFinished(purchaseToken, responseCode);
            }
        };

        // Creating a runnable from the request to use it inside our connection retry policy below
        Runnable consumeRequest = new Runnable() {
            @Override
            public void run() {
                // Consume the purchase async
                billingClient.consumeAsync(purchaseToken, onConsumeListener);
            }
        };

//        executeServiceRequest(consumeRequest);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.five_pound_tv:

                buyingStatus = 4.99;
                ProductName = "five_pound_credit";

                skuList = new ArrayList<>();
                skuList.add(ProductName);
                params = SkuDetailsParams.newBuilder();
                params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                billingClient.querySkuDetailsAsync(params.build(),
                        new SkuDetailsResponseListener() {
                            @Override
                            public void onSkuDetailsResponse(int responseCode, List skuDetailsList) {
                                // Process the result.

                                if (responseCode == BillingClient.BillingResponse.OK
                                        && skuDetailsList != null) {
                                    for (Object skuDetailsObject : skuDetailsList) {
                                        SkuDetails skuDetails = (SkuDetails) skuDetailsObject;
                                        String sku = skuDetails.getSku();
                                        String price = skuDetails.getPrice();
                                        if (ProductName.equals(sku)) {

                                            BillingFlowParams flowParams = BillingFlowParams.newBuilder()

                                                    .setSku(ProductName)
                                                    .setType(BillingClient.SkuType.INAPP)
                                                    .build();
                                            int code = billingClient.launchBillingFlow(CreditBuyActivity.this, flowParams);


                                        }
                                    }
                                }
                            }
                        });

                break;
            case R.id.ten_pound_tv:

                buyingStatus = 9.99;
                ProductName = "ten_pound_credit";

                skuList = new ArrayList<>();
                skuList.add(ProductName);
                params = SkuDetailsParams.newBuilder();
                params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                billingClient.querySkuDetailsAsync(params.build(),
                        new SkuDetailsResponseListener() {
                            @Override
                            public void onSkuDetailsResponse(int responseCode, List skuDetailsList) {
                                // Process the result.

                                if (responseCode == BillingClient.BillingResponse.OK
                                        && skuDetailsList != null) {
                                    for (Object skuDetailsObject : skuDetailsList) {
                                        SkuDetails skuDetails = (SkuDetails) skuDetailsObject;
                                        String sku = skuDetails.getSku();
                                        String price = skuDetails.getPrice();
                                        if (ProductName.equals(sku)) {


                                            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                                    .setSku(ProductName)
                                                    .setType(BillingClient.SkuType.INAPP)
                                                    .build();
                                            int code = billingClient.launchBillingFlow(CreditBuyActivity.this, flowParams);


                                        }
                                    }
                                }
                            }
                        });


                break;
            case R.id.fifteen_pound_tv:

                buyingStatus = 14.99;
                ProductName = "fifteen_pound_credit";

                skuList = new ArrayList<>();
                skuList.add(ProductName);
                params = SkuDetailsParams.newBuilder();
                params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                billingClient.querySkuDetailsAsync(params.build(),
                        new SkuDetailsResponseListener() {
                            @Override
                            public void onSkuDetailsResponse(int responseCode, List skuDetailsList) {

                                if (responseCode == BillingClient.BillingResponse.OK
                                        && skuDetailsList != null) {
                                    for (Object skuDetailsObject : skuDetailsList) {
                                        SkuDetails skuDetails = (SkuDetails) skuDetailsObject;
                                        String sku = skuDetails.getSku();
                                        String price = skuDetails.getPrice();
                                        if (ProductName.equals(sku)) {


                                            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                                    .setSku(ProductName)
                                                    .setType(BillingClient.SkuType.INAPP)
                                                    .build();
                                            int code = billingClient.launchBillingFlow(CreditBuyActivity.this, flowParams);


                                        }
                                    }
                                }
                            }
                        });
                break;
            case R.id.fifty_pound_tv:

                buyingStatus = 54.99;
                ProductName = "fifty_pound_credit";

                skuList = new ArrayList<>();
                skuList.add(ProductName);
                params = SkuDetailsParams.newBuilder();
                params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                billingClient.querySkuDetailsAsync(params.build(),
                        new SkuDetailsResponseListener() {
                            @Override
                            public void onSkuDetailsResponse(int responseCode, List skuDetailsList) {
                                // Process the result.

                                if (responseCode == BillingClient.BillingResponse.OK
                                        && skuDetailsList != null) {
                                    for (Object skuDetailsObject : skuDetailsList) {
                                        SkuDetails skuDetails = (SkuDetails) skuDetailsObject;
                                        String sku = skuDetails.getSku();
                                        String price = skuDetails.getPrice();
                                        if (ProductName.equals(sku)) {


                                            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                                    .setSku(ProductName)
                                                    .setType(BillingClient.SkuType.INAPP)
                                                    .build();
                                            int code = billingClient.launchBillingFlow(CreditBuyActivity.this, flowParams);


                                        }
                                    }
                                }
                            }
                        });

                break;
            case R.id.hundred_pound_tv:
                buyingStatus = 109.99;
                ProductName = "hundred_pound_credit";

                skuList = new ArrayList<>();
                skuList.add(ProductName);
                params = SkuDetailsParams.newBuilder();
                params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                billingClient.querySkuDetailsAsync(params.build(),
                        new SkuDetailsResponseListener() {
                            @Override
                            public void onSkuDetailsResponse(int responseCode, List skuDetailsList) {
                                // Process the result.

                                if (responseCode == BillingClient.BillingResponse.OK
                                        && skuDetailsList != null) {
                                    for (Object skuDetailsObject : skuDetailsList) {
                                        SkuDetails skuDetails = (SkuDetails) skuDetailsObject;
                                        String sku = skuDetails.getSku();
                                        String price = skuDetails.getPrice();
                                        if (ProductName.equals(sku)) {


                                            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                                    .setSku(ProductName)
                                                    .setType(BillingClient.SkuType.INAPP)
                                                    .build();
                                            int code = billingClient.launchBillingFlow(CreditBuyActivity.this, flowParams);


                                        }
                                    }
                                }
                            }
                        });

                break;

        }


    }


    private void updateCredit() {

        if (GlobalClass.isOnline(CreditBuyActivity.this) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("email", email);
//            mParams.put("password", password);

            String clientID = GlobalClass.getPref("clientID", CreditBuyActivity.this);
            String url = "";
            if (validPromo == true) {
                Double creditSum = buyingStatus + Double.valueOf(discount);

                url = "credit?userId=" + clientID + "&credit=" + creditSum;
            } else {
                url = "credit?userId=" + clientID + "&credit=" + buyingStatus;
            }

            Log.d("urlPay", url);
            WebReq.post(CreditBuyActivity.this, url, mParams, new MyTextHttpResponseHandler());

        } else {
            Toast.makeText(CreditBuyActivity.this, CreditBuyActivity.this.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandler extends JsonHttpResponseHandler {
        MyTextHttpResponseHandler() {
        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(CreditBuyActivity.this);
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

                    if (status.equals("200")) {


                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(CreditBuyActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        finish();
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


    private void checkPromo() {

        if (GlobalClass.isOnline(CreditBuyActivity.this) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("email", email);


            String url = "verifyPromo?promo_code=" + promoCode;

            WebReq.get(CreditBuyActivity.this, url, mParams, new MyTextHttpResponseHandlerCheckPromo());

        } else {
            Toast.makeText(CreditBuyActivity.this, CreditBuyActivity.this.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerCheckPromo extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerCheckPromo() {


        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(CreditBuyActivity.this);
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

                        JSONObject jsonObject = mResponse.getJSONObject("Result");

                        discount = jsonObject.getString("discount");

                        String assignedToPackage = jsonObject.getString("assignedToPackage");


                        switch (assignedToPackage) {

                            case "5":
                                five_pound_tv.setEnabled(true);
                                ten_pound_tv.setEnabled(false);
                                fifteen_pound_tv.setEnabled(false);
                                fifty_pound_tv.setEnabled(false);
                                hundred_pound_tv.setEnabled(false);

//                                five_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);
                                ten_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);
                                fifteen_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);
                                fifty_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);
                                hundred_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);

                                break;
                            case "10":
                                five_pound_tv.setEnabled(false);
                                ten_pound_tv.setEnabled(true);
                                fifteen_pound_tv.setEnabled(false);
                                fifty_pound_tv.setEnabled(false);
                                hundred_pound_tv.setEnabled(false);

                                five_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);
//                                ten_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);
                                fifteen_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);
                                fifty_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);
                                hundred_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);

                                break;
                            case "15":
                                five_pound_tv.setEnabled(false);
                                ten_pound_tv.setEnabled(false);
                                fifteen_pound_tv.setEnabled(true);
                                fifty_pound_tv.setEnabled(false);
                                hundred_pound_tv.setEnabled(false);

                                five_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);
                                ten_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);
//                                fifteen_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);
                                fifty_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);
                                hundred_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);

                                break;
                            case "50":
                                five_pound_tv.setEnabled(false);
                                ten_pound_tv.setEnabled(false);
                                fifteen_pound_tv.setEnabled(false);
                                fifty_pound_tv.setEnabled(true);
                                hundred_pound_tv.setEnabled(false);

                                five_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);
                                ten_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);
                                fifteen_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);
//                                fifty_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);
                                hundred_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);

                                break;
                            case "100":
                                five_pound_tv.setEnabled(false);
                                ten_pound_tv.setEnabled(false);
                                fifteen_pound_tv.setEnabled(false);
                                fifty_pound_tv.setEnabled(false);
                                hundred_pound_tv.setEnabled(true);

                                five_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);
                                ten_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);
                                fifteen_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);
                                fifty_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);
//                                hundred_pound_tv.setBackgroundResource(R.drawable.rounded_gray_disable);
                                break;


                        }


                        message_tv_promo.setTextColor(getApplicationContext().getResources().getColor(R.color.green_light));
                        message_tv_promo.setVisibility(View.VISIBLE);

                        String value = getApplicationContext().getResources().getString(R.string.congrate_you_will_receive) + " " + getApplicationContext().getResources().getString(R.string.pound) + "" + discount + " " + getApplicationContext().getResources().getString(R.string.extra_credits) + ".";

                        message_tv_promo.setText(value);
                        validPromo = true;

//                        String message = mResponse.getString("statusMessage");
//                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        message_tv_promo.setTextColor(getApplicationContext().getResources().getColor(R.color.red_bg));
                        message_tv_promo.setVisibility(View.VISIBLE);
                        message_tv_promo.setText(getApplicationContext().getResources().getString(R.string.invalid_pomo_code));
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


}

package com.jobesk.thesecretpsychics.ClientActivities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.jobesk.thesecretpsychics.ClientActivities.ClientDrawerActivity;

import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.CircleTransform;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.UsernameValidator;
import com.jobesk.thesecretpsychics.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import cz.msebera.android.httpclient.Header;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

public class ClientSignup extends AppCompatActivity {


    private TextView sign_in_tv;
    private EditText userName_et, email_et, password_et, confirm_pass_et;
    private String userName, email, password, confirmPass;
    private FrameLayout imageContainer;
    private final int REQUEST_WRITE_PERMISSION = 92;
    private final int RESULT_LOAD_IMAGE = 41;
    private File compressedImageFile;
    private Uri selectedImage, uri;
    private boolean imageUploaded = false;
    private String encodedImage;
    private ImageView userImageView;
    private String TAG = "ClientSignup";
    public final int RC_SIGN_IN = 94;
    private int socialSignInStatus;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    private TextView faceBookBtn, googleBtn;
    private CallbackManager callbackManager;
    private AccessToken mAccessToken;
    private String email_user_FBB, email_user_FB, userIdFromFB, birthdayFB, genderFB, locationFB, firstNameFB;
    private URL profilePicture;
    private String firstName, lastName;
    private String Name_From_Facbook;

    private String passForFB;
    private String passwordGoogle, name_from_google, email_from_google, passwordss_from_google, social_id_from_google, socialtype_from_google, accountPhotoGoogle;

    private String encodedFB = "", encodedGoogle = "";
    private CheckBox checkBox_age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        facebookCallbacks();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_sign_up);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        sign_in_tv = findViewById(R.id.sign_in_tv);
        sign_in_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();

            }
        });


        userImageView = findViewById(R.id.userImageView);
        userName_et = findViewById(R.id.userName_et);
        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);
        confirm_pass_et = findViewById(R.id.confirm_pass_et);


        imageContainer = findViewById(R.id.imageContainer);
        imageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                requestPermission();
            }
        });


        checkBox_age = findViewById(R.id.checkBox_age);

        RelativeLayout bSearch2 = findViewById(R.id.bSearch2);
        bSearch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                userName = userName_et.getText().toString().trim();
                email = email_et.getText().toString().trim();
                password = password_et.getText().toString().trim();
                confirmPass = confirm_pass_et.getText().toString().trim();


                if (imageUploaded == false) {
                    Toast.makeText(ClientSignup.this, getApplicationContext().getResources().getString(R.string.please_upload_user_image), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (userName.equals("")) {
                    Toast.makeText(ClientSignup.this, getApplicationContext().getResources().getString(R.string.enter_user_name), Toast.LENGTH_SHORT).show();
                    return;
                }

                UsernameValidator validatorName = new UsernameValidator();


                char first = userName.charAt(0);

                if (Character.isDigit(first)) {

                    Toast.makeText(ClientSignup.this, getApplicationContext().getResources().getString(R.string.name_first_letter), Toast.LENGTH_SHORT).show();

                    return;

                }


                boolean nameValidator = validatorName.validate(userName);
                if (nameValidator == false) {

                    Toast.makeText(ClientSignup.this, getApplicationContext().getResources().getString(R.string.please_enter_valid_name), Toast.LENGTH_SHORT).show();

                    return;
                }


                if (email.equals("")) {
                    Toast.makeText(ClientSignup.this, getApplicationContext().getResources().getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (GlobalClass.emailValidator(email) == false) {
                    Toast.makeText(ClientSignup.this, getApplicationContext().getResources().getString(R.string.enter_valid_email), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.equals("")) {
                    Toast.makeText(ClientSignup.this, getApplicationContext().getResources().getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(ClientSignup.this, getApplicationContext().getResources().getString(R.string.password_length), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (confirmPass.equals("")) {
                    Toast.makeText(ClientSignup.this, getApplicationContext().getResources().getString(R.string.enter_confirm_pass), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (confirmPass.length() < 6) {
                    Toast.makeText(ClientSignup.this, getApplicationContext().getResources().getString(R.string.confirm_password_length), Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!password.equals(confirmPass)) {
                    Toast.makeText(ClientSignup.this, getApplicationContext().getResources().getString(R.string.password_not_match), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (checkBox_age.isChecked() == false) {
                    Toast.makeText(ClientSignup.this, getApplicationContext().getResources().getString(R.string.over_18_make_sure), Toast.LENGTH_SHORT).show();
                    return;
                }


                signupClient();

            }
        });


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestEmail()
                .build();


        //  AccountPermission();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                // .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addScope(new Scope(Scopes.PROFILE))
                .build();
        mGoogleApiClient.connect();


        faceBookBtn = findViewById(R.id.facebook_tv);
        googleBtn = findViewById(R.id.gmail_tv);

        faceBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacebookLogin();
            }
        });
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleLogin();
            }
        });

    }


    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Do the stuff that requires permission...
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);

            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(ClientSignup.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show permission explanation dialog...
                } else {
                    //Never ask again selected, or device policy prohibits the app from having that permission.
                    //So, disable that feature, or fall back to another situation...
                }
            }
        }


    }


    //callback method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {

            case RESULT_LOAD_IMAGE:
                if (resultCode != RESULT_CANCELED) {

                    try {

                        selectedImage = data.getData();


                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        // Bitmap resized = getResizedBitmap(bitmap, 400, 400);
                        uri = getImageUri(getApplicationContext(), bitmap);

                        String path = getRealPathFromURI(uri);

                        File filePath = new File(path);
                        imageUploaded = true;

                        compressedImageFile = new Compressor(this).compressToFile(filePath);

                        Picasso.with(getApplicationContext())
                                .load(compressedImageFile).fit().centerCrop()
                                .placeholder(R.drawable.user_image_general_info)
                                .transform(new CircleTransform()).into(userImageView);


                        encodedImage = GlobalClass.getStringFile(compressedImageFile);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


                break;

        }


        if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            callbackManager.onActivityResult(requestCode, resultCode, data);

        }


        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            try {

                handleSignInResult(result);


            } catch (Exception e) {

            }

        }

    }


    private void signupClient() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
            mParams.put("email", email);
            mParams.put("password", password);
            mParams.put("name", userName);
            mParams.put("profileImage", encodedImage);
            String token = GlobalClass.getToken();
            mParams.put("new_status", "1");
            mParams.put("userFcmToken", token);
            mParams.put("devicePlatform", "2");


            WebReq.post(getApplicationContext(), "signup", mParams, new MyTextHttpResponseHandler());

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
            GlobalClass.showLoading(ClientSignup.this);
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

                    if (status.equals("200")) {


                        JSONObject jsonObject = mResponse.getJSONObject("Result");
                        String id = jsonObject.getString("id");
                        String email = jsonObject.getString("email");
                        String token = jsonObject.getString("token");
                        String name = jsonObject.getString("name");
                        String profileImage = jsonObject.getString("profileImage");


                        GlobalClass.putPref("clientID", id, getApplicationContext());
                        GlobalClass.putPref("clientEmail", email, getApplicationContext());
                        GlobalClass.putPref("clientToken", token, getApplicationContext());
                        GlobalClass.putPref("clientName", name, getApplicationContext());
                        GlobalClass.putPref("clientProfileImage", profileImage, getApplicationContext());
                        GlobalClass.putPref("userType", "client", getApplicationContext());

                        Intent i = new Intent(ClientSignup.this, ClientDrawerActivity.class);
                        startActivity(i);

                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(ClientSignup.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


    private void FacebookLogin() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {
            socialSignInStatus = 1;
            LoginManager.getInstance().logOut();
            LoginManager.getInstance().logInWithReadPermissions(ClientSignup.this, Arrays.asList("email", "public_profile", "user_friends"));


        } else {
            Toast.makeText(this, "Connect to inernet!", Toast.LENGTH_SHORT).show();
        }


    }

    private void GoogleLogin() {


        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            socialSignInStatus = 2;
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);

        } else {
            Toast.makeText(this, "Connect to inernet!", Toast.LENGTH_SHORT).show();
        }


    }


    private void facebookCallbacks() {

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("status_fb_login", loginResult.toString());

                        mAccessToken = loginResult.getAccessToken();
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.e("respooooFB", object.toString());
                                Log.e("respooooFB", response.toString());


                                try {
                                    userIdFromFB = object.getString("id");
                                    try {
                                        profilePicture = new URL("https://graph.facebook.com/" + userIdFromFB + "/picture?width=500&height=500");
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }
                                    if (object.has("first_name"))
                                        firstName = object.getString("first_name");
                                    if (object.has("last_name"))
                                        lastName = object.getString("last_name");
                                    if (object.has("email"))
                                        email_user_FB = object.getString("email");
//                                    if (object.has("birthday"))
//                                        birthday = object.getString("birthday");
//                                    if (object.has("gender"))
//                                        gender = object.getString("gender");

                                    Name_From_Facbook = firstName + " " + lastName;
                                    if (email_user_FB == null) {
                                        Toast.makeText(ClientSignup.this, getApplicationContext().getString(R.string.fb_verify), Toast.LENGTH_SHORT).show();

                                    } else {

                                        Random r = new Random();
                                        int NUmber = r.nextInt(100000 - 0) + 0;
                                        passForFB = "987654" + NUmber + "@randomPass123_android";
                                        Log.d("passsslength", passForFB);


                                        String UrlImageFb = "https://graph.facebook.com/" + userIdFromFB + "/picture?type=large";
                                        Bitmap bitmap = getBitmapFromURL(UrlImageFb);


                                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                        byte[] byteArray = byteArrayOutputStream.toByteArray();

                                        encodedFB = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        Log.d("encodedImage", encodedFB);

                                        signInSocial(email_user_FB, encodedFB, Name_From_Facbook);
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        //Here we put the requested fields to be returned from the JSONObject
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name, email");
                        // parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                        request.setParameters(parameters);
                        request.executeAsync();


                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(ClientSignup.this, getResources().getString(R.string.login_cancel), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(ClientSignup.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("handleSignInResult", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();


            name_from_google = acct.getDisplayName();
            email_from_google = acct.getEmail();

            social_id_from_google = acct.getId();
            socialtype_from_google = "google";
            accountPhotoGoogle = null;
            try {
                accountPhotoGoogle = acct.getPhotoUrl().toString();
            } catch (Exception E) {

            }


            Log.i("GoogleRespose", acct.getPhotoUrl() + "");
            Random r = new Random();
            int number = r.nextInt(999999 - 0) + 0;

            passwordGoogle = "asdkjGth" + number;


            if (accountPhotoGoogle != null) {
                Bitmap bitsss = getBitmapFromURL(accountPhotoGoogle);
                Bitmap resized = getResizedBitmap(bitsss, 500, 500);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                resized.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                encodedGoogle = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.d("encodedImage", encodedFB);

            } else {


                encodedGoogle = "";

            }


            signInSocial(email_from_google, encodedGoogle, name_from_google);

        } else {
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.gmail_login_unsuccessfull), Toast.LENGTH_SHORT).show();
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void signInSocial(String email, String Image, String name) {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
            mParams.put("email", email);
            mParams.put("profileImage", Image);
            mParams.put("name", name);
            String firebaseToken = GlobalClass.getToken();
            mParams.put("userFcmToken", firebaseToken);

            WebReq.post(getApplicationContext(), "External_Login", mParams, new MyTextHttpResponseHandlerSocial());

        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerSocial extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerSocial() {

        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(ClientSignup.this);
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

                        JSONObject jsonObject = mResponse.getJSONObject("Result");
                        String id = jsonObject.getString("id");
                        String email = jsonObject.getString("email");
                        String token = jsonObject.getString("token");
                        String name = jsonObject.getString("name");
                        String profileImage = jsonObject.getString("profileImage");

                        GlobalClass.putPref("clientID", id, getApplicationContext());
                        GlobalClass.putPref("clientEmail", email, getApplicationContext());
                        GlobalClass.putPref("clientToken", token, getApplicationContext());
                        GlobalClass.putPref("clientName", name, getApplicationContext());
                        GlobalClass.putPref("clientProfileImage", profileImage, getApplicationContext());
                        GlobalClass.putPref("userType", "client", getApplicationContext());

                        Intent i = new Intent(getApplicationContext(), ClientDrawerActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(ClientSignup.this, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(ClientSignup.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}
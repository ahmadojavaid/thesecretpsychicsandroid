package com.jobesk.thesecretpsychics.ClientActivities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

public class ClientSignIn extends AppCompatActivity {


    private TextView signInTv;
    private EditText email_et, password_et;
    private String email, password;
    private String TAG = "ClientSignIn";
    public final int RC_SIGN_IN = 94;
    private int socialSignInStatus;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    private LinearLayout faceBookBtn, googleBtn;
    private CallbackManager callbackManager;
    private AccessToken mAccessToken;
    private String email_user_FBB, email_user_FB, userIdFromFB, birthdayFB, genderFB, locationFB, firstNameFB;
    private URL profilePicture;
    private String firstName, lastName;
    private String Name_From_Facbook;
    private String passForFB;
    private String passwordGoogle, name_from_google, email_from_google, passwordss_from_google, social_id_from_google, socialtype_from_google, accountPhotoGoogle;
    private String encodedFB = "", encodedGoogle = "";
    private TextView forgetPass_tv;
    private AlertDialog passChangeAlert;

    private String emailUserforget = "";
    private LinearLayout signUpContainer;

    private TextView text_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        facebookCallbacks();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_client);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        forgetPass_tv = findViewById(R.id.forgetPass_tv);
        signInTv = findViewById(R.id.signUp_tv);
        signInTv.setText(getApplicationContext().getResources().getString(R.string.sign_in));

        forgetPass_tv = findViewById(R.id.forgetPass_tv);


        forgetPass_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertpassChange();

            }
        });


        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);


        text_signup = findViewById(R.id.text_signup);
        text_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ClientSignIn.this, ClientSignup.class);
                startActivity(i);

            }
        });


        signInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                email = email_et.getText().toString().trim();
                password = password_et.getText().toString().trim();


                if (email.equals("")) {
                    Toast.makeText(ClientSignIn.this, getApplicationContext().getResources().getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!GlobalClass.emailValidator(email)) {
                    Toast.makeText(ClientSignIn.this, getApplicationContext().getResources().getString(R.string.enter_valid_email), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.equals("")) {
                    Toast.makeText(ClientSignIn.this, getApplicationContext().getResources().getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(ClientSignIn.this, getApplicationContext().getResources().getString(R.string.password_length), Toast.LENGTH_SHORT).show();

                    return;
                }

                signInAdvisor();
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


        faceBookBtn = findViewById(R.id.btnFacebook);
        faceBookBtn.setVisibility(View.VISIBLE);

        googleBtn = findViewById(R.id.btnGoogle);
        googleBtn.setVisibility(View.VISIBLE);

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

        printHashKey(getApplicationContext());

    }

    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    private void signInAdvisor() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
            mParams.put("email", email);
            mParams.put("devicePlatform", "2");
            mParams.put("password", password);
            String firebaseToken = GlobalClass.getToken();
            mParams.put("userFcmToken", firebaseToken);

            WebReq.post(getApplicationContext(), "doLogin", mParams, new MyTextHttpResponseHandler());

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
            GlobalClass.showLoading(ClientSignIn.this);
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
        public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
            super.onPostProcessResponse(instance, response);
        }

        @Override
        public void onPreProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
            super.onPreProcessResponse(instance, response);
        }

        @Override
        public void onProgress(long bytesWritten, long totalSize) {
            super.onProgress(bytesWritten, totalSize);

//
//            long progress = bytesWritten / totalSize * 100;
//
//            Log.d("progressHere", bytesWritten + "");


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
                        String new_status = jsonObject.getString("new_status");
                        String credit = jsonObject.getString("credit");

                        GlobalClass.putPref("clientID", id, getApplicationContext());
                        GlobalClass.putPref("clientEmail", email, getApplicationContext());
                        GlobalClass.putPref("clientToken", token, getApplicationContext());
                        GlobalClass.putPref("clientName", name, getApplicationContext());
                        GlobalClass.putPref("clientProfileImage", profileImage, getApplicationContext());
                        GlobalClass.putPref("userType", "client", getApplicationContext());
                        GlobalClass.putPref("clientNewStatus", new_status, getApplicationContext());
                        GlobalClass.putPref("clientCredit", credit, getApplicationContext());


                        Intent i = new Intent(getApplicationContext(), ClientDrawerActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(ClientSignIn.this, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(ClientSignIn.this, "" + message, Toast.LENGTH_SHORT).show();
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
            LoginManager.getInstance().logInWithReadPermissions(ClientSignIn.this, Arrays.asList("email", "public_profile", "user_friends"));


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
                                        Toast.makeText(ClientSignIn.this, getApplicationContext().getString(R.string.fb_verify), Toast.LENGTH_SHORT).show();

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


                                    }

                                    signInSocial(email_user_FB, encodedFB, Name_From_Facbook);
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
                        Toast.makeText(ClientSignIn.this, getResources().getString(R.string.login_cancel), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(ClientSignIn.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (FacebookSdk.isFacebookRequestCode(requestCode)) {
            callbackManager.onActivityResult(requestCode, resultCode, data);

        }


        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            try {
//
//                int statusCode = result.getStatus().getStatusCode();
//
//                Log.d("statusCode", statusCode + "");
                handleSignInResult(result);


            } catch (Exception e) {

            }

        }


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


    public void alertpassChange() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ClientSignIn.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogView = inflater.inflate(R.layout.custom_dialog_pass_reset, null);
        dialogBuilder.setView(dialogView);

        final EditText emailet = (EditText) dialogView.findViewById(R.id.enter_email_tv);
        TextView submitTv = (TextView) dialogView.findViewById(R.id.submit_tv);
        submitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                emailUserforget = emailet.getText().toString().trim();
                if (emailUserforget.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.enter_email), Toast.LENGTH_SHORT).show();

                } else if (GlobalClass.emailValidator(emailUserforget) == false) {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.enter_valid_email), Toast.LENGTH_SHORT).show();
                } else {
                    changePass();
                }


            }
        });


        passChangeAlert = dialogBuilder.create();
        passChangeAlert.show();
    }


    private void changePass() {


        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
//            mParams.put("screenName", screen);
//            mParams.put("profileImage", encodedImage);
//            mParams.put("serviceName", service);


            String AdvisorID = GlobalClass.getPref("advisorID", getApplicationContext());

            WebReq.post(getApplicationContext(), "userforgotPassword?email=" + emailUserforget, mParams, new MyTextHttpResponseHandlerChangepass());

        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerChangepass extends JsonHttpResponseHandler {


        MyTextHttpResponseHandlerChangepass() {


        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(ClientSignIn.this);
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
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(ClientSignIn.this, "" + message, Toast.LENGTH_LONG).show();
                        passChangeAlert.dismiss();
                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(ClientSignIn.this, "" + message, Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
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
            GlobalClass.showLoading(ClientSignIn.this);
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
                        Toast.makeText(ClientSignIn.this, "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(ClientSignIn.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

}

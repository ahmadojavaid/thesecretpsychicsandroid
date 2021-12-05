package com.jobesk.thesecretpsychics.AdvisorActivities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.ClientActivities.ClientEditProfile;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.UsernameValidator;
import com.jobesk.thesecretpsychics.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdvisorGeneralInfo extends AppCompatActivity {

    private TextView toolbar_title;
    private LinearLayout next_btn;
    private ImageView back_img;
    private String TAG = "AdvisorGeneralInfo";
    private FrameLayout frameContainer;
    private CircleImageView userImg;
    private final int REQUEST_WRITE_PERMISSION = 92;
    private final int RESULT_LOAD_IMAGE = 91;
    private File compressedImageFile;
    private Uri selectedImage, uri;
    private boolean imageUploaded = false;
    private String encodedImage;
    private EditText screen_name_et, email_et, service_name_et;
    private String screen, service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advisor_general_info);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getApplicationContext().getResources().getString(R.string.advisor_general_info));


        screen_name_et = findViewById(R.id.screen_name_et);
        service_name_et = findViewById(R.id.service_name_et);


        email_et = findViewById(R.id.email_et);
        String userEmail = GlobalClass.getPref("advisorEmail", getApplicationContext());
        email_et.setClickable(false);
        email_et.setEnabled(false);
        email_et.setText(userEmail);


        next_btn = findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                screen = screen_name_et.getText().toString().trim();
                service = service_name_et.getText().toString().trim();


                if (imageUploaded == false) {
                    Toast.makeText(AdvisorGeneralInfo.this, getApplicationContext().getResources().getString(R.string.please_upload_user_image), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (screen.equals("")) {
                    Toast.makeText(AdvisorGeneralInfo.this, getApplicationContext().getResources().getString(R.string.enter_screen_name), Toast.LENGTH_SHORT).show();
                    return;
                }


                UsernameValidator validatorName = new UsernameValidator();
                char first = screen.charAt(0);
                if (Character.isDigit(first)) {
                    Toast.makeText(AdvisorGeneralInfo.this, getApplicationContext().getResources().getString(R.string.name_first_letter), Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean nameValidator = validatorName.validate(screen);
                if (nameValidator == false) {
                    Toast.makeText(AdvisorGeneralInfo.this, getApplicationContext().getResources().getString(R.string.please_enter_valid_name), Toast.LENGTH_SHORT).show();
                    return;
                }


                if (service.equals("")) {
                    Toast.makeText(AdvisorGeneralInfo.this, getApplicationContext().getResources().getString(R.string.enter_service_name), Toast.LENGTH_SHORT).show();
                    return;
                }

                UsernameValidator validatorName1 = new UsernameValidator();
                char firstService = service.charAt(0);
                if (Character.isDigit(firstService)) {
                    Toast.makeText(AdvisorGeneralInfo.this, getApplicationContext().getResources().getString(R.string.name_first_letter), Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean nameValidator1 = validatorName1.validate(service);
                if (nameValidator1 == false) {
                    Toast.makeText(AdvisorGeneralInfo.this, getApplicationContext().getResources().getString(R.string.please_enter_valid_name), Toast.LENGTH_SHORT).show();
                    return;
                }


                advisorGeneralInfo();

            }
        });

        userImg = findViewById(R.id.userImg);

        frameContainer = (FrameLayout) findViewById(R.id.frameContainer);
        frameContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                requestPermission();


            }
        });

    }


    private void advisorGeneralInfo() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
            mParams.put("screenName", screen);
            mParams.put("profileImage", encodedImage);
            mParams.put("serviceName", service);


            String AdvisorID = GlobalClass.getPref("advisorID", getApplicationContext());

            WebReq.post(getApplicationContext(), "updateAdvisorInfo/" + AdvisorID, mParams, new MyTextHttpResponseHandler());

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
            GlobalClass.showLoading(AdvisorGeneralInfo.this);
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

//                        JSONArray jsonArray = mResponse.getJSONArray("Result");
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//
//
//                            JSONObject jsonObject = jsonArray.getJSONObject(0);
//
////                            textValue = jsonObject.getString("text");
//
//
//                        }
//                        text_tv.setText(textValue);

//                        String message = mResponse.getString("statusMessage");
//                        Toast.makeText(AdvisorBecomeAdvisor.this, "" + message, Toast.LENGTH_SHORT).show();


                        Intent i = new Intent(AdvisorGeneralInfo.this, AdvisorCategories.class);
                        startActivity(i);

                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(AdvisorGeneralInfo.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
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
                if (ActivityCompat.shouldShowRequestPermissionRationale(AdvisorGeneralInfo.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show permission explanation dialog...
                } else {
                    //Never ask again selected, or device policy prohibits the app from having that permission.
                    //So, disable that feature, or fall back to another situation...
                }
            }
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

    //callback method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Bitmap bitmap = null;

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);


                userImg.setImageURI(resultUri);

                encodedImage = encoded;
                imageUploaded = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        switch (requestCode) {

            case RESULT_LOAD_IMAGE:
                if (resultCode != RESULT_CANCELED) {

                    try {

                        selectedImage = data.getData();


                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        // Bitmap resized = getResizedBitmap(bitmap, 400, 400);
                        uri = getImageUri(getApplicationContext(), bitmap);

                        CropImage.activity(uri)
                                .start(this);
//                        String path = getRealPathFromURI(uri);
//
//                        File filePath = new File(path);
//                        imageUploaded = true;
//
//                        compressedImageFile = new Compressor(this).compressToFile(filePath);
//
//                        Picasso.with(getApplicationContext()).load(compressedImageFile)
// .fit().centerCrop().placeholder(R.drawable.user_image_general_info).transform(new CircleTransform()).into(userImg);
//
//
//                        encodedImage = GlobalClass.getStringFile(compressedImageFile);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


                break;

        }
    }

}

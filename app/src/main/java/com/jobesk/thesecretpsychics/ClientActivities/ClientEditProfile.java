package com.jobesk.thesecretpsychics.ClientActivities;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.EventBuses.ClientEditProfileEvent;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.CircleTransform;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.Urls;
import com.jobesk.thesecretpsychics.Utils.UsernameValidator;
import com.jobesk.thesecretpsychics.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class ClientEditProfile extends AppCompatActivity {
    private TextView toolbar_title;
    private ImageView back_img;
    private EditText username_et, email_et;
    private RelativeLayout save_ln;
    private CircleImageView userImageView;
    private FrameLayout imageContainer;
    private final int RESULT_LOAD_IMAGE = 41;
    private File compressedImageFile;
    private Uri selectedImage, uri, destinationUri;
    private boolean imageUploaded = false;
    private String encodedImage;
    private final int REQUEST_WRITE_PERMISSION = 92;
    private String TAG = "ClientEditProfile";
    private String userNewName;
    private final int CROP_PIC = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_edit_profile);


        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getApplicationContext().getResources().getString(R.string.edit_profile));


        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        imageContainer = findViewById(R.id.imageContainer);

        imageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                requestPermission();
            }
        });


        userImageView = findViewById(R.id.userImageView);
        String clientImage = GlobalClass.getPref("clientProfileImage", getApplicationContext());
        String clientName = GlobalClass.getPref("clientName", getApplicationContext());
        String clientEmail = GlobalClass.getPref("clientEmail", getApplicationContext());


        Picasso.with(getApplicationContext())
                .load(Urls.IMAGE_BASEURL + clientImage)
                .fit().centerCrop()
                .transform(new CircleTransform())
                .into(userImageView);


        username_et = findViewById(R.id.username_et);
        username_et.setText(clientName + "");
        email_et = findViewById(R.id.email_et);
        email_et.setText(clientEmail);
        email_et.setEnabled(false);

        save_ln = findViewById(R.id.save_ln);
        save_ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userNewName = username_et.getText().toString().trim();

                if (userNewName.equalsIgnoreCase("")) {
                    Toast.makeText(ClientEditProfile.this, getApplicationContext().getResources().getString(R.string.enter_user_name), Toast.LENGTH_SHORT).show();
                    return;

                }
                UsernameValidator validatorName = new UsernameValidator();


                char first = userNewName.charAt(0);

                if (Character.isDigit(first)) {

                    Toast.makeText(ClientEditProfile.this, getApplicationContext().getResources().getString(R.string.name_first_letter), Toast.LENGTH_SHORT).show();

                    return;

                }


                boolean nameValidator = validatorName.validate(userNewName);
                if (nameValidator == false) {

                    Toast.makeText(ClientEditProfile.this, getApplicationContext().getResources().getString(R.string.please_enter_valid_name), Toast.LENGTH_SHORT).show();

                    return;
                }


                updateClient();
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
                if (ActivityCompat.shouldShowRequestPermissionRationale(ClientEditProfile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show permission explanation dialog...
                } else {
                    //Never ask again selected, or device policy prohibits the app from having that permission.
                    //So, disable that feature, or fall back to another situation...
                }
            }
        }


    }

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
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);


                userImageView.setImageURI(resultUri);

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


                        String path = getRealPathFromURI(uri);
                        Log.d("userImage", path + "");
//                        File filePath = new File(path);
//                        imageUploaded = true;
//                        compressedImageFile = new Compressor(this).compressToFile(filePath);
//                        Picasso.with(getApplicationContext())
//                                .load(compressedImageFile).fit().centerCrop()
//                                .placeholder(R.drawable.user_image_general_info)
//                                .transform(new CircleTransform()).into(userImageView);
//                        encodedImage = GlobalClass.getStringFile(compressedImageFile);
//                        imageUploaded = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


                break;

        }

    }


    private void updateClient() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();
            mParams.put("name", userNewName);

            if (imageUploaded == true) {
                mParams.put("profileImage", encodedImage);
            }


            String clientID = GlobalClass.getPref("clientID", getApplicationContext());

            WebReq.post(getApplicationContext(), "user/" + clientID, mParams, new MyTextHttpResponseHandler());

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
            GlobalClass.showLoading(ClientEditProfile.this);
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

                        String name = jsonObject.getString("name");
                        String profileImage = jsonObject.getString("profileImage");

                        GlobalClass.putPref("clientName", name, getApplicationContext());
                        GlobalClass.putPref("clientProfileImage", profileImage, getApplicationContext());

                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(ClientEditProfile.this, "" + message, Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new ClientEditProfileEvent());
                        finish();
                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(ClientEditProfile.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
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


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}

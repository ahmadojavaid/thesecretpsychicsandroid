package com.jobesk.thesecretpsychics.ClientActivities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorDrawerActivity;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorLanguage;
import com.jobesk.thesecretpsychics.AdvisorActivities.SignUPAdvisorSwitch;
import com.jobesk.thesecretpsychics.EventBuses.ClientEditProfileEvent;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.SplashScreen.WelcomeScreen_5;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.theartofdev.edmodo.cropper.CropImage;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpClientSwitch extends AppCompatActivity {

  private String TAG = "SignUpClientSwitch";
  private String ID = "";
  private EditText name_et;
  private TextView submit_tv;
  private ImageView back_img;
  private String name, email;
  private FrameLayout imageContainer;
  private final int RESULT_LOAD_IMAGE = 41;
  private final int REQUEST_WRITE_PERMISSION = 92;
  private CircleImageView userImageView;
  private String encodedImage;
  private boolean imageUploaded;
  private Uri selectedImage, uri;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up_client_switch);

    Bundle bundle = getIntent().getExtras();
    ID = bundle.getString("userID");

    name_et = findViewById(R.id.name_et);


    submit_tv = findViewById(R.id.submit_tv);
    userImageView = findViewById(R.id.userImageView);

    imageContainer = findViewById(R.id.imageContainer);


    back_img = findViewById(R.id.back_img);
    back_img.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        Intent i = new Intent(getApplicationContext(), WelcomeScreen_5.class);
        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

      }
    });


    submit_tv.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {


        name = name_et.getText().toString().trim();


        if (name.equals("")) {
          Toast.makeText(SignUpClientSwitch.this, getApplicationContext().getResources().getString(R.string.enter_user_name), Toast.LENGTH_SHORT).show();
        } else if (imageUploaded == false) {
          Toast.makeText(SignUpClientSwitch.this, getApplicationContext().getResources().getString(R.string.please_upload_user_image), Toast.LENGTH_SHORT).show();
        } else {

          updateClient();

        }


      }
    });


    imageContainer.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        requestPermission();

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
        if (ActivityCompat.shouldShowRequestPermissionRationale(SignUpClientSwitch.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
      mParams.put("name", name);
      mParams.put("new_status", "1");


      if (imageUploaded == true) {
        mParams.put("profileImage", encodedImage);
      }

      WebReq.post(getApplicationContext(), "user/" + ID, mParams, new MyTextHttpResponseHandler());

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
      GlobalClass.showLoading(SignUpClientSwitch.this);
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
            String new_status = jsonObject.getString("new_status");

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
            String message = mResponse.getString("statusMessage");
            Toast.makeText(SignUpClientSwitch.this, "" + message, Toast.LENGTH_SHORT).show();
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

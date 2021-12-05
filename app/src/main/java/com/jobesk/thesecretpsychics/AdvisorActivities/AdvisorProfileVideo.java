package com.jobesk.thesecretpsychics.AdvisorActivities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.WebReq;
import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdvisorProfileVideo extends AppCompatActivity {

    private TextView toolbar_title;
    private LinearLayout next_btn;
    private ImageView back_img;
    private FrameLayout frameContainer;
    private String TAG = "AdvisorProfileVideo";
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 14;
    private CircleImageView videoImageView;
    private File VideoFile;
    private boolean videoCheck = false;
    private String videoPath;
    private final int REQUEST_WRITE_PERMISSION = 89;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advisor_profile_video);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getApplicationContext().getResources().getString(R.string.profile_video));


        videoImageView = findViewById(R.id.logo);

        next_btn = findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (videoCheck == false) {
                    Toast.makeText(AdvisorProfileVideo.this, getApplicationContext().getResources().getString(R.string.select_video), Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    if (videoPath != null) {

                        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), Uri.parse(videoPath));
                        int duration = mp.getDuration();
                        mp.release();

                        if ((duration / 1000) > 60) {
                            // Show Your Messages
                            Toast.makeText(AdvisorProfileVideo.this, getApplicationContext().getResources().getString(R.string.video_size), Toast.LENGTH_SHORT).show();
                        } else {
                            orderInstruction();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


        frameContainer = findViewById(R.id.frameContainer);
        frameContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                requestPermission();


            }
        });
    }

    private void getVideo() {

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            getVideo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        if (grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Do the stuff that requires permission...
                getVideo();

            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(AdvisorProfileVideo.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show permission explanation dialog...
                } else {
                    //Never ask again selected, or device policy prohibits the app from having that permission.
                    //So, disable that feature, or fall back to another situation...
                }
            }
        }


    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                Uri selectedImageUri = data.getData();

                // OI FILE Manager
//                String filemanagerstring = selectedImageUri.getPath();

                // MEDIA GALLERY
//                String selectedImagePath = getRealPathFromURI(selectedImageUri);


//                String filePath = filemanagerstring;
                videoPath = getPath(selectedImageUri);
                VideoFile = new File(videoPath);

                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(selectedImageUri)
                        .into(videoImageView);

                videoCheck = true;

            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }

    private void orderInstruction() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();


            String AdvisorID = GlobalClass.getPref("advisorID", getApplicationContext());
            try {
                mParams.put("profileVideo", VideoFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            mParams.put("advisorId", AdvisorID);

            WebReq.post(getApplicationContext(), "uploadVid", mParams, new MyTextHttpResponseHandlerUploadVideo());

        } else {
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }


    private class MyTextHttpResponseHandlerUploadVideo extends JsonHttpResponseHandler {
        MyTextHttpResponseHandlerUploadVideo() {
        }

        @Override
        public void onStart() {
            super.onStart();
            GlobalClass.showLoading(AdvisorProfileVideo.this);
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
                        Toast.makeText(AdvisorProfileVideo.this, "" + message, Toast.LENGTH_SHORT).show();
                        Intent paymentIntent = new Intent(AdvisorProfileVideo.this, AdvisorPaymentDetails.class);
                        startActivity(paymentIntent);

                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(AdvisorProfileVideo.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

}

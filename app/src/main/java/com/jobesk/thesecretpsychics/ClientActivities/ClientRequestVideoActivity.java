package com.jobesk.thesecretpsychics.ClientActivities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.deep.videotrimmer.utils.FileUtils;
import com.jobesk.thesecretpsychics.Activities.CreditBuyActivity;
import com.jobesk.thesecretpsychics.AdvisorActivities.MyordersActivities.AdvisorMyJobs3;
import com.jobesk.thesecretpsychics.ClientActivities.Chat.ClientGetChat;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.TrimmerClasses.VideoTrimmerActivity;
import com.jobesk.thesecretpsychics.Utils.CircleTransform;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.Urls;
import com.jobesk.thesecretpsychics.Utils.WebReq;
import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.jobesk.thesecretpsychics.TrimmerClasses.Constants.EXTRA_VIDEO_PATH;

public class ClientRequestVideoActivity extends AppCompatActivity {


    private ImageView back_img;
    private TextView toolbar_title;
    private LinearLayout cancel_btn;
    private JSONObject json_object;
    private String TAG = "ClientRequestVideoActivity";
    private FrameLayout
            frameContainer;
    private File VideoFile;
    private boolean videoCheck = false;
    private String videoPath;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 14;
    private CircleImageView imageVideo;

    private LinearLayout order_btn;
    private TextView order_tv, username_tv;
    private String advisorName, advisorID, advisorImage;
    private ImageView categoryImage;
    private EditText question_et, description_et;
    private String heading, details;
    private final int REQUEST_WRITE_PERMISSION = 32;
    private AlertDialog alertDialogChooser;

    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int BITMAP_SAMPLE_SIZE = 8;
    private static String imageStoragePath;
    public static final String VIDEO_EXTENSION = "mp4";
    private final int REQUEST_VIDEO_TRIMMER_RESULT = 342;
    private File thumbFile;
    private boolean cameraVideoCheck = false;
    private String selectedVideoName = null, selectedVideoFile = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_request_video);


        try {
            json_object = new JSONObject(getIntent().getStringExtra("jsonObject"));
            advisorName = json_object.getString("screenName");
            advisorID = json_object.getString("id");
            advisorImage = json_object.getString("profileImage");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        username_tv = findViewById(R.id.username_tv);
        username_tv.setText(advisorName + "");


        order_tv = findViewById(R.id.order_tv);
        order_tv.setText(getApplicationContext().getResources().getString(R.string.order) + " " + getApplicationContext().getResources().getString(R.string.pound) + "9.99");


        categoryImage = findViewById(R.id.categoryImage);

        Picasso.with(getApplicationContext()).load(Urls.IMAGE_BASEURL + advisorImage).fit().centerCrop().transform(new CircleTransform()).into(categoryImage);

        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getApplicationContext().getResources().getString(R.string.request_video_reading));


        cancel_btn = findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        frameContainer = findViewById(R.id.frameContainer);


        frameContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestCameraPermission();

            }
        });


        imageVideo = findViewById(R.id.imageVideo);


        question_et = findViewById(R.id.question_et);
        description_et = findViewById(R.id.description_et);


        order_btn = findViewById(R.id.order_btn);
        order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                heading = question_et.getText().toString().trim();
                details = description_et.getText().toString().trim();


                if (heading.equalsIgnoreCase("")) {

                    Toast.makeText(ClientRequestVideoActivity.this, getApplicationContext().getResources().getString(R.string.enter_question), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (details.equalsIgnoreCase("")) {

                    Toast.makeText(ClientRequestVideoActivity.this, getApplicationContext().getResources().getString(R.string.enter_description), Toast.LENGTH_SHORT).show();
                    return;
                }


                if (videoCheck == false) {
                    Toast.makeText(ClientRequestVideoActivity.this, getApplicationContext().getResources().getString(R.string.select_video), Toast.LENGTH_SHORT).show();
                    return;
                }


                if (videoCheck == true) {

                    try {
//                        if (videoPath != null) {

                        if (cameraVideoCheck == true) {
                            orderInstruction();
                        } else {
                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), Uri.parse(videoPath));
                            int duration = mp.getDuration();
                            mp.release();

                            if ((duration / 1000) > 180) {
                                // Show Your Messages
                                Toast.makeText(ClientRequestVideoActivity.this, getApplicationContext().getResources().getString(R.string.video_size), Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                orderInstruction();
                                return;
                            }
                        }


//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    orderInstruction();


                }


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
                if (ActivityCompat.shouldShowRequestPermissionRationale(ClientRequestVideoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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

            if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
                alertDialogChooser.cancel();

//                Uri videoFileUri = data.getData();

                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startTrimActivity(selectedUri);
                } else {
                    Toast.makeText(this, "" + getApplicationContext().getString(R.string.toast_cannot_retrieve_selected_video), Toast.LENGTH_SHORT).show();
                }
                videoCheck = false;
                cameraVideoCheck = false;
            } else if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                alertDialogChooser.cancel();
                Uri selectedImageUri = data.getData();

                videoPath = getPath(selectedImageUri);
                VideoFile = new File(videoPath);

                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(selectedImageUri)
                        .into(imageVideo);

                videoCheck = true;
                cameraVideoCheck = false;
            } else if (requestCode == REQUEST_VIDEO_TRIMMER_RESULT) {
                alertDialogChooser.cancel();
                Uri selectedVideoUri = data.getData();
                VideoFile = new File(selectedVideoUri.getPath());


                selectedVideoFile = data.getData().getPath();
                selectedVideoName = data.getData().getLastPathSegment();

                Log.d("videopath", VideoFile + "");

                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(selectedVideoUri.getPath(),
                        MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);

                Glide.with(this)
                        .asBitmap()
                        .load(getFileFromBitmap(thumb))
                        .into(imageVideo);

                videoCheck = true;
                cameraVideoCheck = true;

            }
        }
    }

    private File getFileFromBitmap(Bitmap bmp) {
        /*//create a file to write bitmap data*/
        thumbFile = new File(this.getCacheDir(), "thumb_" + selectedVideoName + ".png");
        try {
            thumbFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*//Convert bitmap to byte array*/
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        /*//write the bytes in file*/
        try {
            FileOutputStream fos = new FileOutputStream(thumbFile);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thumbFile;
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


            if (videoCheck == true) {


                try {
                    mParams.put("order_video", VideoFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }


            String clientID = GlobalClass.getPref("clientID", getApplicationContext());


            mParams.put("advisorId", advisorID);
            mParams.put("userId", clientID);
            mParams.put("order_heading", heading);
            mParams.put("order_details", details);

            WebReq.post(getApplicationContext(), "order", mParams, new MyTextHttpResponseHandler());

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
            GlobalClass.showLoading(ClientRequestVideoActivity.this);
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
            Log.d(TAG, "onFinish");

            GlobalClass.dismissLoading();

            try {
                String statusCode = e.getString("statusCode");

                if (statusCode.equals("403")) {

                    Intent i = new Intent(getApplicationContext(), CreditBuyActivity.class);
                    startActivity(i);

                    String statusMessage = e.getString("statusMessage");
                    Toast.makeText(ClientRequestVideoActivity.this, "" + statusMessage, Toast.LENGTH_LONG).show();
                }


            } catch (Exception e1) {


                e1.printStackTrace();
            }

            Log.d(TAG, "OnFailure" + e);

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
//            Log.d(TAG, responseString);
            GlobalClass.dismissLoading();
        }


        @Override
        public void onProgress(long bytesWritten, long totalSize) {
            super.onProgress(bytesWritten, totalSize);
//
//            long progress = bytesWritten / totalSize * 100;
//            Log.d("progressHere", "Bites_written" + bytesWritten + "" + "Progress=" + progress);

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
                        Toast.makeText(ClientRequestVideoActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(ClientRequestVideoActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


    private void requestCameraPermission() {
        Dexter.withActivity(ClientRequestVideoActivity.this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            showChangeLangDialog();
                        } else if (report.isAnyPermissionPermanentlyDenied()) {

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ClientRequestVideoActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View dialogView = inflater.inflate(R.layout.custom_dialog_picker, null);
        dialogBuilder.setView(dialogView);

        LinearLayout gallery_ln = dialogView.findViewById(R.id.gallery_ln);
        LinearLayout camera_ln = dialogView.findViewById(R.id.camera_ln);
        TextView cancel_tv = dialogView.findViewById(R.id.cancel_tv);

//        dialogBuilder.setTitle("Custom dialog");
//        dialogBuilder.setMessage("Enter text below");
        gallery_ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getVideo();
            }
        });
        camera_ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                captureVideo();
            }
        });
        cancel_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialogChooser.cancel();
            }
        });
        alertDialogChooser = dialogBuilder.create();
        alertDialogChooser.show();
    }


    private void captureVideo() {
        Intent videoCapture = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(videoCapture, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);


    }

    private void startTrimActivity(@NonNull Uri uri) {
        Intent intent = new Intent(this, VideoTrimmerActivity.class);
        intent.putExtra(EXTRA_VIDEO_PATH, FileUtils.getPath(this, uri));
        startActivityForResult(intent, REQUEST_VIDEO_TRIMMER_RESULT);
    }


}

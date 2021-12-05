package com.jobesk.thesecretpsychics.AdvisorActivities.MyordersActivities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.deep.videotrimmer.utils.FileUtils;
import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorShowVideo;
import com.jobesk.thesecretpsychics.Model.AdvisorOrdersModel;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.TrimmerClasses.VideoTrimmerActivity;
import com.jobesk.thesecretpsychics.Utils.CircleTransform;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;


import com.jobesk.thesecretpsychics.Utils.Urls;

import com.jobesk.thesecretpsychics.Utils.WebReq;
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
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.jobesk.thesecretpsychics.TrimmerClasses.Constants.EXTRA_VIDEO_PATH;


public class AdvisorMyJobs3 extends AppCompatActivity {
    private ArrayList<AdvisorOrdersModel> jobDataList;
    private String position;
    private ArrayList<AdvisorOrdersModel> object;
    private TextView counter_tv;
    private ImageView back_img;
    private TextView toolbar_title, userName, heading_tv, body_tv, repondTorequest_tv, date_tv;
    private ImageView userImage;
    private FrameLayout framContainerImage;
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 18;
    private final int REQUEST_WRITE_PERMISSION = 82;
    private String videoPath;
    private File VideoFile;
    private boolean videoCheck = false;
    private boolean cameraVideoCheck = false;
    private CircleImageView imageViewVideo;
    private String comments;
    private String TAG = "AdvisorMyJobs3";
    private String selectedVideoName = null, selectedVideoFile = null;
    private EditText commentsAdvisor_et;
    private String isCompleted, orderID;
    private CircleImageView imageVideoSrc;
    private RelativeLayout videoContainer;
    private String userID, videoLink = "";
    private AlertDialog alertDialogChooser;
    public static final String GALLERY_DIRECTORY_NAME = "Hello Camera";
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int BITMAP_SAMPLE_SIZE = 8;
    private static String imageStoragePath;
    public static final String VIDEO_EXTENSION = "mp4";
    private final int REQUEST_VIDEO_TRIMMER_RESULT = 342;
    private File thumbFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jobs3);


        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_title = findViewById(R.id.toolbar_title);
        imageViewVideo = findViewById(R.id.imageViewVideo);
        commentsAdvisor_et = findViewById(R.id.commentsAdvisor_et);

        Bundle args = getIntent().getExtras();
        position = args.getString("position");
        object = (ArrayList<AdvisorOrdersModel>) args.getSerializable("jobsArrayList");

        Log.d("poition", position + "");
        String name = object.get(Integer.valueOf(position)).getName();
        String body = object.get(Integer.valueOf(position)).getBody();
        String heading = object.get(Integer.valueOf(position)).getHeading();
        String image = object.get(Integer.valueOf(position)).getUserImage();
        String date = object.get(Integer.valueOf(position)).getDate();
        orderID = object.get(Integer.valueOf(position)).getId();
        isCompleted = object.get(Integer.valueOf(position)).getIsCompleted();
        videoLink = object.get(Integer.valueOf(position)).getVideLink();
        userID = object.get(Integer.valueOf(position)).getUserID();


        imageVideoSrc = findViewById(R.id.imageVideoSrc);

        videoContainer = findViewById(R.id.videoContainer);
        if (videoLink.equalsIgnoreCase("0")) {


            videoContainer.setVisibility(View.GONE);
        } else {

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.user_image_general_info);
            requestOptions.error(R.drawable.user_image_general_info);
            requestOptions.encodeQuality(50);

            String videoThumb = Urls.BASEURL + videoLink;

            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(videoThumb)
                    .apply(requestOptions)

                    .into(imageVideoSrc);

            videoContainer.setVisibility(View.VISIBLE);

        }

        videoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent videoIntent = new Intent(getApplicationContext(), AdvisorShowVideo.class);
                Bundle bundle = new Bundle();
                bundle.putString("videoLink", Urls.BASEURL + "" + videoLink);
                videoIntent.putExtras(bundle);
                startActivity(videoIntent);

            }
        });


        counter_tv = findViewById(R.id.counter_tv);

        if (isCompleted.equalsIgnoreCase("0")) {
            counter_tv.setBackgroundResource(R.drawable.circle_blue);
        } else {
            counter_tv.setBackgroundResource(R.drawable.circle_green);
        }


        userImage = findViewById(R.id.userImage);
        userName = findViewById(R.id.userName);
        heading_tv = findViewById(R.id.heading_tv);
        body_tv = findViewById(R.id.body_tv);
        date_tv = findViewById(R.id.date_tv);
        repondTorequest_tv = findViewById(R.id.repondTorequest_tv);

        toolbar_title.setText(object.get(Integer.valueOf(position)).getName());

        Picasso.with(getApplicationContext()).load(Urls.IMAGE_BASEURL + "" + image).fit().centerCrop().transform(new CircleTransform()).into(userImage);

        userName.setText(name);
        heading_tv.setText(heading);
        body_tv.setText(body);

        String modifiedDate = GlobalClass.parseDate(date, getApplicationContext());
        date_tv.setText(modifiedDate);


        counter_tv.setText(String.valueOf(Integer.valueOf(position + 1)));


        repondTorequest_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comments = commentsAdvisor_et.getText().toString().trim();


                if (comments.equalsIgnoreCase("")) {

                    Toast.makeText(AdvisorMyJobs3.this, getApplicationContext().getResources().getString(R.string.enter_comments), Toast.LENGTH_SHORT).show();
                    return;
                }


                if (videoCheck == true) {

                    if (cameraVideoCheck == true) {
                        uploadVideo();
                    } else {
                        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), Uri.parse(videoPath));
                        int duration = mp.getDuration();
                        mp.release();

                        if ((duration / 1000) > 180) {
                            // Show Your Messages
                            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.video_size), Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            uploadVideo();
                            return;
                        }
                    }


                } else {
                    Toast.makeText(AdvisorMyJobs3.this, getApplicationContext().getResources().getString(R.string.add_video), Toast.LENGTH_SHORT).show();
                    return;
//                    uploadVideo();

                }


            }
        });


        framContainerImage = findViewById(R.id.frameContainer);
        framContainerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                requestCameraPermission();


            }
        });

    }


    private void requestCameraPermission() {
        Dexter.withActivity(AdvisorMyJobs3.this)
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AdvisorMyJobs3.this);
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
                if (ActivityCompat.shouldShowRequestPermissionRationale(AdvisorMyJobs3.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
                        .into(imageViewVideo);

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
                        .into(imageViewVideo);

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

    private void startTrimActivity(@NonNull Uri uri) {
        Intent intent = new Intent(this, VideoTrimmerActivity.class);
        intent.putExtra(EXTRA_VIDEO_PATH, FileUtils.getPath(this, uri));
        startActivityForResult(intent, REQUEST_VIDEO_TRIMMER_RESULT);
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


    private void uploadVideo() {

        if (GlobalClass.isOnline(getApplicationContext()) == true) {

            RequestParams mParams = new RequestParams();


            if (videoCheck == true) {

                try {
                    mParams.put("reply_Video", VideoFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            mParams.put("isCompleted", "1");
            mParams.put("reply_details", comments);
            String advisorID = GlobalClass.getPref("advisorID", getApplicationContext());
            mParams.put("advisorId", advisorID);
            mParams.put("userId", userID);

            WebReq.post(getApplicationContext(), "order/" + orderID, mParams, new MyTextHttpResponseHandler());

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
            GlobalClass.showLoading(AdvisorMyJobs3.this);
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
                        Toast.makeText(AdvisorMyJobs3.this, message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("keyName", "1");
                        setResult(RESULT_OK, intent);
                        finish();

                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(AdvisorMyJobs3.this, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }

}

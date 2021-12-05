package com.jobesk.thesecretpsychics.AdvisorActivities.EditProfile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.Urls;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class AdvisorEditFragProfileVideo extends Fragment {

    private LinearLayout next_btn;
    private ImageView back_img;
    private FrameLayout frameContainer;
    private String TAG = "AdvisorEditFragProfileVideo";
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 14;
    private CircleImageView videoImageView;
    private File VideoFile;
    private boolean videoCheck = false;
    private String videoPath;
    private final int REQUEST_WRITE_PERMISSION = 89;
    private Activity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_advisor_profile_video, container, false);


        activity = (AdvisorEditProfile) rootView.getContext();

        videoImageView = rootView.findViewById(R.id.logo);
        AdvisorEditProfile.toolbar_title.setText(getActivity().getResources().getString(R.string.profile_video));

        AdvisorEditProfile.toolbar_title.setText(getActivity().getResources().getString(R.string.profile_video));

        String thumbnailPrevide = Urls.BASEURL + "" + AdvisorEditProfile.profileVideo;

        Glide.with(activity)
                .asBitmap()
                .load(thumbnailPrevide)
                .into(videoImageView);


        next_btn = rootView.findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (videoCheck == true) {
                    try {
                        if (videoPath != null) {

                            MediaPlayer mp = MediaPlayer.create(getActivity(), Uri.parse(videoPath));
                            int duration = mp.getDuration();
                            mp.release();

                            if ((duration / 1000) > 60) {
                                // Show Your Messages
                                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.video_size), Toast.LENGTH_SHORT).show();
                            } else {


                                AdvisorEditProfile.VideoFile = VideoFile;

                                openFrag();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    openFrag();

                }


            }
        });
        frameContainer = rootView.findViewById(R.id.frameContainer);
        frameContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVideo();
            }
        });


        return rootView;
    }



    private void getVideo() {

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
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

                Glide.with(activity)
                        .asBitmap()
                        .load(selectedImageUri)
                        .into(videoImageView);

                videoCheck = true;
                AdvisorEditProfile.uploadedViedo = true;
            }
        }
    }


    public String getPath(Uri uri) {
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = activity.getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();

        return path;
    }


    private void openFrag() {

        AdvisorEditFragPaymentDetails frag = new AdvisorEditFragPaymentDetails();
        FragmentManager fmd = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransactionde = fmd.beginTransaction();
        fragmentTransactionde.replace(R.id.editFrame, frag);
        fragmentTransactionde.addToBackStack(null);
        fragmentTransactionde.commit();

    }

}

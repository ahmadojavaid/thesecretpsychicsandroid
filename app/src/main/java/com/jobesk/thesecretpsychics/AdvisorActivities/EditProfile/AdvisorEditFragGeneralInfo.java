package com.jobesk.thesecretpsychics.AdvisorActivities.EditProfile;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jobesk.thesecretpsychics.AdvisorActivities.AdvisorGeneralInfo;
import com.jobesk.thesecretpsychics.R;
import com.jobesk.thesecretpsychics.Utils.CircleTransform;
import com.jobesk.thesecretpsychics.Utils.GlobalClass;
import com.jobesk.thesecretpsychics.Utils.Urls;
import com.jobesk.thesecretpsychics.Utils.UsernameValidator;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AdvisorEditFragGeneralInfo extends Fragment {


    private LinearLayout next_btn;
    private ImageView back_img;
    private String TAG = "AdvisorGeneralInfo";
    private FrameLayout frameContainer;
    private CircleImageView userImg;
    private final int RESULT_LOAD_IMAGE = 91;
    private File compressedImageFile;
    private Uri selectedImage, uri;
    private boolean imageUploaded = false;
    private EditText screen_name_et, email_et, service_name_et;
    private String screen, service;
    private Activity activity;
    private String encodedImage;
    private final int CROP_PIC = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_advisor_general_info, container, false);


        activity = (AdvisorEditProfile) rootView.getContext();

        AdvisorEditProfile.toolbar_title.setText(getActivity().getResources().getString(R.string.advisor_general_info));

        screen_name_et = rootView.findViewById(R.id.screen_name_et);
        service_name_et = rootView.findViewById(R.id.service_name_et);

        email_et = rootView.findViewById(R.id.email_et);
        String userEmail = GlobalClass.getPref("advisorEmail", getActivity());
        email_et.setClickable(false);
        email_et.setEnabled(false);
        email_et.setText(userEmail);


        next_btn = rootView.findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                screen = screen_name_et.getText().toString().trim();
                service = service_name_et.getText().toString().trim();


                if (screen.equals("")) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_screen_name), Toast.LENGTH_SHORT).show();
                    return;
                }

                UsernameValidator validatorName = new UsernameValidator();
                char first = screen.charAt(0);
                if (Character.isDigit(first)) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.name_first_letter), Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean nameValidator = validatorName.validate(screen);
                if (nameValidator == false) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.please_enter_valid_name), Toast.LENGTH_SHORT).show();
                    return;
                }


                if (service.equals("")) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.enter_service_name), Toast.LENGTH_SHORT).show();
                    return;
                }


                UsernameValidator validatorName1 = new UsernameValidator();
                char firstService = service.charAt(0);
                if (Character.isDigit(firstService)) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.name_first_letter), Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean nameValidator1 = validatorName1.validate(service);
                if (nameValidator1 == false) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.please_enter_valid_name), Toast.LENGTH_SHORT).show();
                    return;
                }


                AdvisorEditProfile.screenName = screen;
                AdvisorEditProfile.serviceName = service;
                AdvisorEditProfile.profileImage = encodedImage;


                openFrag();

            }
        });

        userImg = rootView.findViewById(R.id.userImg);

        frameContainer = (FrameLayout) rootView.findViewById(R.id.frameContainer);
        frameContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);


            }
        });


        screen_name_et.setText(AdvisorEditProfile.screenName);
        service_name_et.setText(AdvisorEditProfile.serviceName);


        String imagepath = Urls.IMAGE_BASEURL + AdvisorEditProfile.profileImage;
        Picasso.with(activity).load(imagepath).transform(new CircleTransform()).fit().placeholder(R.drawable.user_image_placeholder).centerCrop().into(userImg);


        return rootView;
    }


    private void openFrag() {
        AdvisorEditFragCategories frag = new AdvisorEditFragCategories();
        FragmentManager fmd = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransactionde = fmd.beginTransaction();
        fragmentTransactionde.replace(R.id.editFrame, frag);
        fragmentTransactionde.addToBackStack(null);
        fragmentTransactionde.commit();

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Bitmap bitmap = null;

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), resultUri);
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
                AdvisorEditProfile.uploadedImage = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        switch (requestCode) {

            case RESULT_LOAD_IMAGE:
                if (resultCode != RESULT_CANCELED) {

                    try {

                        selectedImage = data.getData();


                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), selectedImage);
                        // Bitmap resized = getResizedBitmap(bitmap, 400, 400);
                        uri = getImageUri(activity, bitmap);


                        performCrop(uri);

//                        Intent intent = CropImage.activity(uri)
//                                .setGuidelines(CropImageView.Guidelines.ON)
//                                .setFixAspectRatio(true)
//                                .setAspectRatio(1,1)
//                                .getIntent(getActivity());
//                        getActivity().startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
//                        String path = getRealPathFromURI(uri);
//
//                        File filePath = new File(path);
//                        imageUploaded = true;
//
//                        compressedImageFile = new Compressor(activity).compressToFile(filePath);
//
//                        Picasso.with(activity).load(compressedImageFile).fit().centerCrop().placeholder(R.drawable.user_image_general_info).transform(new CircleTransform()).into(userImg);
//
//
//                        encodedImage = GlobalClass.getStringFile(compressedImageFile);
//
//
//


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


                break;

            case CROP_PIC:

                if (data != null) {

                    Uri imageUri = data.getData();


                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String path = getRealPathFromURI(uri);
//
                    File filePath = new File(path);
                    imageUploaded = true;

                    try {
                        compressedImageFile = new Compressor(activity).compressToFile(filePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Picasso.with(activity).load(compressedImageFile).fit().centerCrop().placeholder(R.drawable.user_image_general_info).transform(new CircleTransform()).into(userImg);


                    encodedImage = GlobalClass.getStringFile(compressedImageFile);

                    imageUploaded = true;
                    AdvisorEditProfile.uploadedImage = true;

                }

        }
    }

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties here
            cropIntent.putExtra("crop", true);
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 700);
            cropIntent.putExtra("outputY", 700);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
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

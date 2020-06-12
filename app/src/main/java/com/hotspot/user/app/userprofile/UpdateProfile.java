package com.hotspot.user.app.userprofile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.hotspot.user.app.DashboardActivity;
import com.hotspot.user.app.R;
import com.hotspot.user.app.utils.AppUrls;
import com.hotspot.user.app.utils.CustomPerference;
import com.hotspot.user.app.utils.Utils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.hotspot.user.app.userprofile.ViewProfile.openDialogNoInternet;


public class UpdateProfile extends AppCompatActivity {

        EditText first_name,
                email;

        TextInputLayout input_layout_frstnm,
                input_layout_email,
                input_layout_lstnm;

        final Context context = UpdateProfile.this;

        ImageView image_user,
                iv_edit_image;

        private Uri mCropImageUri;

        private String encodedImage;

        private String userId,
                tokenId,
                userName,
                userMail,
                userNumber;
    private String userProfileImage;


    void getPreferenceValue() {
        userId = CustomPerference.getString(context, CustomPerference.USER_ID);
        tokenId = CustomPerference.getString(context, CustomPerference.USER_TOKEN);
        userName = CustomPerference.getString(context, CustomPerference.USER_NAME);
        userMail = CustomPerference.getString(context, CustomPerference.USER_EMAIL);
        userNumber = CustomPerference.getString(context, CustomPerference.USER_MOBILE);
        userProfileImage = CustomPerference.getString(context, CustomPerference.USER_PROFILE_IMAGE);
    }

        void getExecuteMethods()
        {
            if(Utils.isNetworkAvailable(context))
                {}
            else
                openDialogNoInternet(context);
        }

        void initView() {

            input_layout_frstnm = findViewById(R.id.input_layout_frstnm);
            input_layout_lstnm = findViewById(R.id.input_layout_lstnm);
            input_layout_email = findViewById(R.id.input_layout_email);

            image_user = findViewById(R.id.image_user);
            first_name = findViewById(R.id.first_name);

            EditText edt_mobNumber = findViewById(R.id.edt_mobNumber);
            email = findViewById(R.id.email);

            Utils.Picasso(userProfileImage,image_user);

            first_name.setText(userName);
            edt_mobNumber.setText(userNumber);
            email.setText(userMail);

            findViewById(R.id.submit).setOnClickListener(view ->  validateForm());

                edt_mobNumber.setOnClickListener(view -> Toast.makeText(context,
                    "You can't update your mobile number",Toast.LENGTH_LONG).show());

                iv_edit_image = findViewById(R.id.iv_edit_image);
                iv_edit_image.setOnClickListener(v -> {

                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        if (CropImage.isExplicitCameraPermissionRequired(getApplicationContext())) {
                            requestPermissions(
                                new String[]{Manifest.permission.CAMERA},
                                CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
                    } else {
                        CropImage.startPickImageActivity(UpdateProfile.this);
                    }
            });

    }

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            setTheme(R.style.AppTheme);
            super.onCreate(savedInstanceState);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setContentView(R.layout.activity_updateprofile);

            Toolbar toolbar =  findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black);
            getSupportActionBar().setTitle("Edit Profile");
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN |
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


            getPreferenceValue();
            initView();


        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == android.R.id.home) {
                onBackPressed();
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        private void validateForm() {

            if (!validateFirstNm()) {
                return;
            }

            if (!validateEmail()) {
                return;
            }

            if (validateFirstNm() && validateEmail()) {
//                execute();
            }
        }

        private boolean validateEmail() {
            userMail = email.getText().toString().trim();

            if (userMail.isEmpty()) {
                input_layout_email.setError(getText(R.string.error_field_required));
                requestFocus(email);
                return false;

            } else if (!isValidEmail(userMail)) {

                input_layout_email.setError(getText(R.string.error_invalid_email));
                requestFocus(email);
                return false;

            } else {
                input_layout_email.setErrorEnabled(false);
            }

            return true;
        }

        private static boolean isValidEmail(String email) {
            return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }

        private boolean validateFirstNm() {
            userName = first_name.getText().toString().trim();

            if (userName.isEmpty()) {
                input_layout_frstnm.setError(getText(R.string.error_field_required));
                requestFocus(first_name);
                return false;
            } else {
                input_layout_frstnm.setErrorEnabled(false);
            }

            return true;
        }

        private void requestFocus(View view) {
            if (view.requestFocus()) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        }

//        public void execute() {
//
//            Map<String, String> params = new HashMap<>();
//
//            params.put("UserId", userId);
//            params.put("TokenId", tokenId);
//            params.put("Email", email.getText().toString().trim());
//            params.put("Fname", first_name.getText().toString().trim());
//            params.put("Lname", "");
//            params.put("Dob", "");
//            params.put("Gender", "");
//
//            JSONObject jsonObj = new JSONObject(params);
//
//            System.out.println("jsonobject_update===" + jsonObj);
//            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, AppUrls.UpdateProfile, jsonObj,
//                    response -> {
//
//                        System.out.println("result of updateprofile===" + response);
//
//                        try {
//
//                            String status = response.getString("Status").trim();
//                            String msg = response.getString("Message").trim();
//                            String Login_sts = response.getString("LoginStatus").trim();
//
//
//                            if (status.equalsIgnoreCase("true")
//                                    && Login_sts.equalsIgnoreCase("true")) {
//
//                                String userName = response.getString("Name").trim();
//                                String userEmailId = response.getString("EmailId").trim();
//
//                                CustomPerference.putString(context, CustomPerference.USER_NAME,userName);
//                                CustomPerference.putString(context, CustomPerference.USER_EMAIL,userEmailId);
//
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(context, UpdateProfile.class)
//                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
//
//                            } else if (Login_sts.equalsIgnoreCase("false")) {
//
//                                startActivity(new Intent(context, DashboardActivity.class)
//                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
//                            } else {
//
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//                            }
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }, error -> {
//
//                    });
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            requestQueue.add(jsonObjReq);
//        }

        private void startCropImageActivity(Uri imageUri) {
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setMultiTouchEnabled(true)
                    .start(UpdateProfile.this);
        }

        @Override
        @SuppressLint("NewApi")
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                Uri imageUri = CropImage.getPickImageResultUri(this, data);

                // For API >= 23 we need to check specifically that we have permissions to read external storage.
                if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                    // request permissions and handle the result in onRequestPermissionsResult()
                    mCropImageUri = imageUri;
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                } else {
                    // no permissions required or already grunted, can start crop image activity
                    startCropImageActivity(imageUri);
                }
            }

            // handle result of CropImageActivity
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    image_user.setImageURI(result.getUri());


                    try {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byte[] byteArrayImage = byteArrayOutputStream.toByteArray();
                        encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

//                        UploadImages();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCropImageActivity(mCropImageUri);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }


//        public void UploadImages() {
//
//            Utils.customProgress(context,"Uploading image ...");
//            Map<String, String> params = new HashMap<>();
//            params.put("UserId", userId);
//            params.put("TokenId", tokenId);
//            params.put("profileImage", encodedImage);
//
//            JSONObject jsonObj = new JSONObject(params);
//            System.out.println("result of Profile Update===" + jsonObj);
//            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, AppUrls.SetProfileImage, jsonObj,
//                    response -> {
//
//                        Utils.customProgressStop();
//                        System.out.println("result of  Profile Update===" + response);
//
//                        try {
//
//                            boolean status = response.getBoolean("Status");
//                            String msg = response.getString("Message").trim();
//                            String Login_sts = response.getString("LoginStatus").trim();
//
//                            if (status && Login_sts.equalsIgnoreCase("true"))
//                            {
//                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
//                                getExecuteMethods();
//
//                            } else
//                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
//
//                        } catch (JSONException e) {
//                            Utils.customProgressStop();
//                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
//                        }
//
//                    }, error ->
//            {
//                Utils.customProgressStop();
//                Toast.makeText(getApplicationContext(),  error.getMessage(),Toast.LENGTH_SHORT).show();
//            });
//
//
//            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000,2,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            requestQueue.add(jsonObjReq);
//        }

    }

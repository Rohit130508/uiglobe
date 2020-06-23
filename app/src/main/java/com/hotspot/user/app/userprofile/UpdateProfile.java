package com.hotspot.user.app.userprofile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.hotspot.user.app.DashboardActivity;
import com.hotspot.user.app.R;
import com.hotspot.user.app.utils.AppUrls;
import com.hotspot.user.app.utils.CustomPerference;
import com.hotspot.user.app.utils.Utils;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.hotspot.user.app.userprofile.ViewProfile.openDialogNoInternet;


public class UpdateProfile extends AppCompatActivity {

    private static boolean USER_OK = false;
    private static boolean PAN_OK = false;
    private static boolean AADHAR_OK = false;
    private DatePickerDialog datePickerDialog;
    EditText first_name,
                email,
            edtDOB,
            edtCity, edtPanNumber, edtAadhar, edt_mobNumber;

    private Button submit;

        TextInputLayout input_layout_frstnm,
                input_layout_email,
                input_layout_lstnm;

        final Context context = UpdateProfile.this;

        ImageView image_user,
                iv_edit_image,
                imgPan, imgAadhar;

        private Uri mCropImageUri;

        private String encodedImage;
        private String encodedImagePAN;
        private String encodedImageAadhar;
//
//        private byte[] encodedImage;
//        private byte[] encodedImagePAN;
//        private byte[] encodedImageAadhar;

        private String userId,
                tokenId,
                userName,
                userMail,
                userNumber;
    private String userProfileImage;

    private RadioGroup radioGroup;

    private String sendDate;
    private String gender = null;
    private ProgressBar indeterminateBar;

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

//            input_layout_frstnm = findViewById(R.id.input_layout_frstnm);
//            input_layout_lstnm = findViewById(R.id.input_layout_lstnm);
//            input_layout_email = findViewById(R.id.input_layout_email);

            radioGroup = findViewById(R.id.radioGroup);
            indeterminateBar = findViewById(R.id.indeterminateBar);


            imgPan = findViewById(R.id.imgPan);
            imgAadhar = findViewById(R.id.imgAadhar);
            image_user = findViewById(R.id.image_user);
            first_name = findViewById(R.id.first_name);
            edtCity = findViewById(R.id.edtCity);
            edtPanNumber = findViewById(R.id.edtPanNumber);
            edtAadhar = findViewById(R.id.edtAadhar);
            edtDOB = findViewById(R.id.edtDOB);
            getDateCalendar();
            edtDOB.setOnClickListener(v -> datePickerDialog.show());

            edt_mobNumber = findViewById(R.id.edt_mobNumber);
            submit = findViewById(R.id.submit);
//            email = findViewById(R.id.email);

            Utils.Picasso(userProfileImage,image_user);

            first_name.setText(userName);
            first_name.setText(userName);
            edt_mobNumber.setText(userId);
//            email.setText(userMail);

            submit.setOnClickListener(view ->
                    {
                        indeterminateBar.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.GONE);
                        execute();
                    }
                    );

//                edt_mobNumber.setOnClickListener(view -> Toast.makeText(context,
//                    "You can't update your mobile number",Toast.LENGTH_LONG).show());

                iv_edit_image = findViewById(R.id.iv_edit_image);
                iv_edit_image.setOnClickListener(v -> {
                    USER_OK=true;
                    AADHAR_OK = false;
                    PAN_OK = false;
                    getUploadImage();

            });

                findViewById(R.id.cvPanUpload).setOnClickListener(v ->
                {
                    USER_OK = false;
                    AADHAR_OK = false;
                    PAN_OK = true;
                    getUploadImage();
                });

                findViewById(R.id.cvAadhar).setOnClickListener(v ->
                {
                    USER_OK = false;
                    AADHAR_OK = true;
                    PAN_OK = false;
                    getUploadImage();
                });

            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

                        if(checkedId == R.id.rbtnM)
                        {
                            gender = "Male";
                        }
                        else
                        {
                            gender = "Female";
                        }
            }
                   );
    }
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState)
        {
            setTheme(R.style.AppTheme);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            super.onCreate(savedInstanceState);
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

        public void execute() {

        String name = first_name.getText().toString().trim();
        String date = edtDOB.getText().toString().trim();
        String city = edtCity.getText().toString().trim();
        String panNumber = edtPanNumber.getText().toString().trim();
        String aadharNumber = edtAadhar.getText().toString().trim();
        String mobile = edt_mobNumber.getText().toString().trim();

        if(!TextUtils.isEmpty(encodedImage) && !TextUtils.isEmpty(date) && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(panNumber)
                && !TextUtils.isEmpty(aadharNumber) && !TextUtils.isEmpty(aadharNumber) && !TextUtils.isEmpty(encodedImagePAN)
                && !TextUtils.isEmpty(encodedImageAadhar)) {
            JSONObject object = new JSONObject();
            try {

                object.put("UserId", CustomPerference.getString(this, CustomPerference.USER_ID));
                object.put("Name", name);
                object.put("Dob", date);
                object.put("Gender", gender);
                object.put("ImgUrl", encodedImage);
                object.put("City", city);
                object.put("Pan", panNumber);
                object.put("Aadhar", aadharNumber);
                object.put("PanUrl", encodedImagePAN);
                object.put("AadharUrl", encodedImageAadhar);
            } catch (Exception e) {
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, AppUrls.ProfileKYC, object,

                    response -> {
                        indeterminateBar.setVisibility(View.GONE);
                        submit.setVisibility(View.VISIBLE);

                        System.out.println("result of updateprofile===" + object);
                        System.out.println("result of updateprofile===" + response);


                        try {

                            String StatusCode = response.getString("StatusCode");
                            if (StatusCode.equalsIgnoreCase("200")) {
                                JSONObject object1 = response.getJSONObject("Result");
                                String statusCode = object1.getString("ResText");
                                System.out.println("responce---" + response);
                                if (statusCode.equalsIgnoreCase("Failure")) {

                                    startActivity(new Intent(UpdateProfile.this, DashboardActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Some error occured", Toast.LENGTH_LONG).show();
                                }
                            }


                        } catch (Exception e) {
                        }


                    }, error -> {
                indeterminateBar.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjReq);
        }
        else
        {
            indeterminateBar.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),"All Feilds are required",Toast.LENGTH_LONG).show();
        }
        }

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

                    if(USER_OK)
                    image_user.setImageURI(result.getUri());

                    if(PAN_OK)
                        imgPan.setImageURI(result.getUri());

                    if(AADHAR_OK)
                        imgAadhar.setImageURI(result.getUri());


                    try {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byte[] byteArrayImage = byteArrayOutputStream.toByteArray();

                        if(USER_OK)
                            encodedImage =  resizeBase64Image(Base64.encodeToString(byteArrayImage, Base64.DEFAULT));

//                            encodedImage = byteArrayImage;

                        if(PAN_OK)
                            encodedImagePAN = resizeBase64Image(Base64.encodeToString(byteArrayImage, Base64.DEFAULT));
//                            encodedImagePAN = byteArrayImage;

                        if(AADHAR_OK)
                            encodedImageAadhar = resizeBase64Image(Base64.encodeToString(byteArrayImage, Base64.DEFAULT));
//                            encodedImageAadhar = byteArrayImage;


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



    public String resizeBase64Image(String base64image){
        byte [] encodeByte=Base64.decode(base64image.getBytes(),Base64.DEFAULT);
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inPurgeable = true;
        Bitmap image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length,options);


        if(image.getHeight() <= 400 && image.getWidth() <= 400){
            return base64image;
        }
        image = Bitmap.createScaledBitmap(image, 400, 400, false);

        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG,100, baos);

        byte [] b=baos.toByteArray();
        System.gc();
        return Base64.encodeToString(b, Base64.NO_WRAP);

    }
    void getDateCalendar()
    {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) ->
                {

                    // GET CALENDAR INSTANCE
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);

                    // SET VALUES
                    sendDate = CustomPerference.dateFormatterYear.format(newDate.getTime());
                    edtDOB.setText(CustomPerference.dateFormatter.format(newDate.getTime()));

                },
                newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        try {
            datePickerDialog.getDatePicker().setMinDate(CustomPerference.dateFormatter.parse("01/01/1950").getTime());
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.updateDate(1950,01,01);

        } catch (ParseException ignored) {}


    }

    public void getUploadImage()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (CropImage.isExplicitCameraPermissionRequired(getApplicationContext())) {
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA},
                        CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
            } else {
                CropImage.startPickImageActivity(UpdateProfile.this);
            }
    }
}

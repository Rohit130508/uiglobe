package com.hotspot.user.app.ui.dashboard.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hotspot.user.app.DashboardActivity;
import com.hotspot.user.app.R;
import com.hotspot.user.app.userprofile.UpdateProfile;
import com.hotspot.user.app.utils.AppUrls;
import com.hotspot.user.app.utils.CustomPerference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BasicDetail#newInstance} factory method to
 * create an instance of getActivity() fragment.
 */
public class BasicDetail extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatePickerDialog datePickerDialog;

    private EditText edt_mobNumber, first_name, edtDOB, edtCity;
    private String sendDate;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Animation shake;
    private TextView txtFemale, txtmale;
    private String gender;
    private String encodedImage;

    private Uri mCropImageUri;
    private ImageView image_user;
    private ProgressBar indeterminateBar;

    private Button submit;
    public BasicDetail() {
        // Required empty public constructor
    }

    /**
     * Use getActivity() factory method to create a new instance of
     * getActivity() fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BasicDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static BasicDetail newInstance(String param1, String param2) {
        BasicDetail fragment = new BasicDetail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for getActivity() fragment

        View view = inflater.inflate(R.layout.fragment_basic_detail, container, false);
        shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        initView(view);




        return view;
    }

    void initView(View view)
    {
        submit = view.findViewById(R.id.submit);
        txtmale = view.findViewById(R.id.txtmale);
        txtFemale = view.findViewById(R.id.txtFemale);
        indeterminateBar = view.findViewById(R.id.indeterminateBar);

        first_name = view.findViewById(R.id.first_name);
        first_name.startAnimation(shake);

        edtDOB = view.findViewById(R.id.edtDOB);
        edtDOB.startAnimation(shake);

        edtCity = view.findViewById(R.id.edtCity);
        edtCity.startAnimation(shake);

        image_user = view.findViewById(R.id.image_user);

        edt_mobNumber = view.findViewById(R.id.edt_mobNumber);
        edt_mobNumber.startAnimation(shake);
        edt_mobNumber.setText(CustomPerference.getString(getActivity(),CustomPerference.USER_ID));
        edt_mobNumber.setFocusable(false);

        view.findViewById(R.id.iv_edit_image).setOnClickListener(v -> getUploadImage());
        view.findViewById(R.id.submit).setOnClickListener(v -> executeBasicDetails());

        edtDOB.setOnClickListener(v -> getDateCalendar());

        view.findViewById(R.id.txtmale).setOnClickListener(v -> {
            gender = "Male";
            view.findViewById(R.id.txtmale).setBackgroundColor(Color.parseColor("#28B3DC"));
            view.findViewById(R.id.txtFemale).setBackgroundColor(Color.parseColor("#FFFFFF"));


        });
        view.findViewById(R.id.txtFemale).setOnClickListener(v ->
        {
            gender = "Female";
            view.findViewById(R.id.txtmale).setBackgroundColor(Color.parseColor("#FFFFFF"));
            view.findViewById(R.id.txtFemale).setBackgroundColor(Color.parseColor("#28B3DC"));
        });
    }

    void executeBasicDetails()
    {
        String name = first_name.getText().toString().trim();
        String dob = edtDOB.getText().toString().trim();
        String city = edtCity.getText().toString().trim();
        String date = edtDOB.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(dob)
                && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(gender))
        {
            JSONObject object = new JSONObject();
            try {

                object.put("UserId", CustomPerference.getString(getActivity(), CustomPerference.USER_ID));
                object.put("Name", name);
                object.put("Dob", date);
                object.put("Gender", gender);
                object.put("ImgUrl", encodedImage);
                object.put("City", city);

            } catch (Exception e) {
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, AppUrls.BasicInfo, object,

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

                                    startActivity(new Intent(getActivity(), DashboardActivity.class));
                                    getActivity().finish();
                                } else {
                                    Toast.makeText(getActivity(), "Some error occured", Toast.LENGTH_LONG).show();
                                }
                            }


                        } catch (Exception e) {
                        }


                    }, error -> {
                indeterminateBar.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(jsonObjReq);
            loadFragment(new IdAddress());
        }
        else
        {
            loadFragment(new IdAddress());
            Toast.makeText(getActivity(),"All feilds are required",Toast.LENGTH_LONG).show();
        }
    }

    void loadFragment(Fragment fragment)
    {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame,fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
    void getDateCalendar()
    {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getActivity(),
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

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(getActivity());
    }

    @Override
    @SuppressLint("NewApi")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getActivity(), data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(getActivity(), imageUri)) {
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

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), result.getUri());

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArrayImage = byteArrayOutputStream.toByteArray();


                        encodedImage =  resizeBase64Image(Base64.encodeToString(byteArrayImage, Base64.DEFAULT));


                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getActivity(), "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            Toast.makeText(getActivity(), "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
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

    public void getUploadImage()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (CropImage.isExplicitCameraPermissionRequired(getActivity())) {
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA},
                        CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
            } else {
                CropImage.startPickImageActivity(getActivity());
            }
    }
}
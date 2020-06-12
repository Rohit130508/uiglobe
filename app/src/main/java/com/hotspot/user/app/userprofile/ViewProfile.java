package com.hotspot.user.app.userprofile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hotspot.user.app.R;
import com.hotspot.user.app.auth.LoginActivity;
import com.hotspot.user.app.utils.AppUrls;
import com.hotspot.user.app.utils.CustomPerference;
import com.hotspot.user.app.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class ViewProfile extends AppCompatActivity {

    final Context context = ViewProfile.this;
    String userId,
            tokenId;

    AppCompatTextView name_head,
            gmail,
            number,
            walletamt;

    private ImageView imgProfilePicture;

    void getExecuteMethod()
    {
        if(Utils.isNetworkAvailable(context)){}
//            execute();

        else
            openDialogNoInternet(context);

    }

    void getPreferenceValue()
    {
        userId = CustomPerference.getString(context,CustomPerference.USER_ID);
        tokenId = CustomPerference.getString(context,CustomPerference.USER_TOKEN);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_viewprofile);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black);
        getSupportActionBar().setTitle("My Profile");

        getPreferenceValue();
        initView();
        getExecuteMethod();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void initView() {

        imgProfilePicture =  findViewById(R.id.imgProfilePicture);

        name_head =  findViewById(R.id.name);
        gmail =  findViewById(R.id.gamil);
        number =  findViewById(R.id.num);
        walletamt =  findViewById(R.id.wallet_bal);
        RelativeLayout relative_qr_code = findViewById(R.id.relative_qr_code);

//        relative_qr_code.setOnClickListener(v -> startActivity(new Intent(ViewProfile.this,
//                GenerateQRCode.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)));
    }

//    public void execute() {
//
//      Utils.customProgress(context,"Please Wait ...");
//
//        Map<String,String> params = new HashMap<>();
//
//        params.put("UserId",userId);
//        params.put("TokenId",tokenId);
//
//        JSONObject jsonObj = new JSONObject(params);
//
//        System.out.println("jsonobject_profile==="+ jsonObj);
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, AppUrls.ViewProfile, jsonObj,
//                response -> {
//
//                    Utils.customProgressStop();
//                    System.out.println("result of profile===" + response);
//                    Utils.customProgressStop();
//
//                    try {
//
//                        String status = response.getString("Status").trim();
//                        String msg = response.getString("Message").trim();
//                        String Login_sts = response.getString("LoginStatus").trim();
//
//
//                        if(status.equalsIgnoreCase("true")
//                                && Login_sts.equalsIgnoreCase("true")){
//
//                            String mobileNumber = response.getString("MobileNumber").trim();
//                            String emailId = response.getString("EmailId").trim();
//                            String firstName = response.getString("Name").trim();
//                            String walletBalance = "\u20B9" + response.getString("WalletBalance").trim();
//                            String profileImage = response.getString("ProfileImage").trim();
//                            String QRImage = response.getString("QRImage").trim();
//
//
//
//                            CustomPerference.putString(context,CustomPerference.QRCODE,QRImage);
//
//                            name_head.setText(firstName);
//                            gmail.setText(emailId);
//                            number.setText(mobileNumber);
//                            walletamt.setText(walletBalance);
//                            Utils.Picasso(profileImage,imgProfilePicture);
//
//                        }else if(Login_sts.equalsIgnoreCase("false")){
//                            startActivity(new Intent(context, LoginActivity.class)
//                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
//                        }
//                        else{
//
//                            Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
//                        }
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        Utils.customProgressStop();
//                        Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();
//
//                    }
//                }, error -> {
//            Utils.customProgressStop();
//            Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
//                });
//
//        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(jsonObjReq);
//    }

    public static void  openDialogNoInternet(Context context)
    {
        Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.no_internet);
        dialog1.show();
        Window window = dialog1.getWindow();
        assert window != null;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        AppCompatButton restart = dialog1.findViewById(R.id.retry);

        restart.setOnClickListener(v -> {


        });
    }


}

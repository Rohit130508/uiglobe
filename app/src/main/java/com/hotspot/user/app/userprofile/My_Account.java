package com.hotspot.user.app.userprofile;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.hotspot.user.app.BuildConfig;
import com.hotspot.user.app.MainActivity;
import com.hotspot.user.app.R;
import com.hotspot.user.app.auth.LoginActivity;
import com.hotspot.user.app.utils.AppUrls;
import com.hotspot.user.app.utils.CustomPerference;
import com.hotspot.user.app.utils.Utils;

import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class My_Account extends AppCompatActivity implements View.OnClickListener{

    AppCompatTextView
            txt_emailId,
            txt_mobileNumber,
            wallet,
            signout,
            version;


    CoordinatorLayout coordinatorLayout;

    String userId,
            tokenId,
            userName, mobileNumber,
            walletBal;

    String EtakId,
            MobileNumber,
            EmailId,
            FirstName,
            LastName,
            Gender,
            DOB,
            WalletBalance;

    LinearLayout login_layout;

    AppCompatButton login,
            signup;

    final Context context = My_Account.this;

    Dialog dialog1;
    private String ProfileImage;
    private ImageView imgProfilePicture;

    private void getSharedPreferencesVal()
    {
        userId = CustomPerference.getString(this, CustomPerference.USER_ID);
//        tokenId = CustomPerference.getString(My_Account.this, CustomPerference.TOKEN_ID);
        userName = CustomPerference.getString(My_Account.this, CustomPerference.USER_NAME);
        mobileNumber = CustomPerference.getString(My_Account.this, CustomPerference.USER_ID);
        walletBal = CustomPerference.getString(My_Account.this, CustomPerference.USER_WALLET);
    }

    private void getExecuteMethods()
    {
        if(Utils.isNetworkAvailable(this))
          {}
        else{
            dialog1=new Dialog(context);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.setContentView(R.layout.no_internet);
            dialog1.show();
            Window window = dialog1.getWindow();
            assert window != null;
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            AppCompatButton restart= dialog1.findViewById(R.id.retry);

            restart.setOnClickListener(v -> {

                dialog1.dismiss();
                getExecuteMethods();

            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_ser_profile);

        getSharedPreferencesVal();

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(userName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        String PACKAGE_NAME = getApplicationContext().getPackageName();

        login_layout = findViewById(R.id.login_layout);
        signout = findViewById(R.id.sign_out);
        version = findViewById(R.id.version);

        String verName =  "version : " + BuildConfig.VERSION_NAME;
        version.setText(verName);


        if (userId != null ) {

            signout.setVisibility(View.VISIBLE);
            login_layout.setVisibility(View.GONE);

        }

        initializeAll();
        getExecuteMethods();


        AppBarLayout mAppBarLayout =  findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                } else if (isShow) {
                    isShow = false;
                }
            }
        });
    }


    @SuppressLint("SetTextI18n")
    public void initializeAll() {

        coordinatorLayout = findViewById(R.id.layout) ;

        imgProfilePicture =  findViewById(R.id.imgProfilePicture);


        txt_mobileNumber = findViewById(R.id.txt_mobileNumber);
        txt_mobileNumber.setText(mobileNumber);
        txt_emailId = findViewById(R.id.txt_emailId);
        wallet = findViewById(R.id.wallet_bal);

        findViewById(R.id.txt_updateProfile).setOnClickListener(this);
        wallet.setOnClickListener(this);

        login =  findViewById(R.id.login);
        signup =  findViewById(R.id.signup);

        signout.setOnClickListener(this);


        findViewById(R.id.support).setOnClickListener(this);
        findViewById(R.id.view_profile).setOnClickListener(this);

        imgProfilePicture.setOnClickListener(v -> {
            Intent intent = new Intent(My_Account.this,FullScreenPopupWindow.class);
            intent.putExtra("image",ProfileImage);
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(My_Account.this).toBundle());
        });

        findViewById(R.id.about).setOnClickListener(view -> startActivity(new Intent(context, WebService.class)
                .putExtra("url",AppUrls.BaseUrl+"aboutus")));
        findViewById(R.id.terms).setOnClickListener(view -> startActivity(new Intent(context, WebService.class)
                .putExtra("url",AppUrls.BaseUrl+"term_condition")));
        findViewById(R.id.support).setOnClickListener(view -> startActivity(new Intent(context, WebService.class)
                .putExtra("url",AppUrls.BaseUrl+"contact")));

        findViewById(R.id.review).setOnClickListener(view -> openAppRating(context));
        findViewById(R.id.share).setOnClickListener(view -> shareApp());
        findViewById(R.id.txt_updateProfile).setOnClickListener(view -> startActivity(new Intent(context, UpdateProfile.class)));
        findViewById(R.id.change_password).setOnClickListener(view -> startActivity(new Intent(context, ChangePassword.class)));
        findViewById(R.id.view_profile).setOnClickListener(view -> startActivity(new Intent(context, ViewProfile.class)));

    }




    @Override
    public void onClick(View v) {

        if(v == login){

            Intent in = new Intent(My_Account.this,MainActivity.class);
            startActivity(in);

        }
        if(v == signup){

            Intent in = new Intent(My_Account.this,MainActivity.class);
            startActivity(in);

        }

        if(v.getId() == R.id.txt_updateProfile){
//            startActivity(new Intent(My_Account.this,Update_Profile.class));
        }


        if(v.getId() == R.id.support){

           /* Intent in = new Intent(My_Account.this,ContactUs.class);
            startActivity(in);
            overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);*/

        }


//        if(v == rl12){
//
//            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//            String RefLink = sharedpreferences.getString("ReferralCode",null);
//
//            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//            sharingIntent.setType("text/plain");
//            String shareBody = "https://play.google.com/store/apps/details?id="+PACKAGE_NAME +"&referrer=" +RefLink;
//            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "FreedomStar");
//            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//            startActivity(Intent.createChooser(sharingIntent, "Share via"));
//        }
        if(v == signout){

            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
            else
                builder = new AlertDialog.Builder(context);


            builder .setMessage("Are you sure that you want to Sign Out?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {



                        CustomPerference.clearPref(this);
                        startActivity(new Intent(My_Account.this, LoginActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

                    })
                    .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss())
                    .show();
        }

    }

    public static void openAppRating(Context context) {
        String appId = context.getPackageName();
        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + appId));
        boolean marketFound = false;

        // find all applications able to handle our rateIntent
        final List<ResolveInfo> otherApps = context.getPackageManager()
                .queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp: otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName
                    .equals("com.vexilinfotech.etaka_wallet")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                // make sure it does NOT open in the stack of your activity
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // task reparenting if needed
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                // if the Google Play was already open in a search result
                //  this make sure it still go to the app page you requested
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // this make sure only the Google Play app is allowed to
                // intercept the intent
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                marketFound = true;
                break;

            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id="+appId));
            context.startActivity(webIntent);
        }
    }

    void shareApp()
    {
//            String RefLink = sharedpreferences.getString("ReferralCode",null);
        String RefLink = CustomPerference.getString(context,CustomPerference.USER_REFERALCODE);

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName()+ "&referrer=" +RefLink;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "FreedomStar");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

}

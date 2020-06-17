package com.hotspot.user.app.ui.user;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.appbar.AppBarLayout;
import com.hotspot.user.app.BuildConfig;
import com.hotspot.user.app.DashboardActivity;
import com.hotspot.user.app.R;
import com.hotspot.user.app.ui.dashboard.DashboardFragment;
import com.hotspot.user.app.ui.dashboard.DashboardViewModel;
import com.hotspot.user.app.userprofile.ChangePassword;
import com.hotspot.user.app.userprofile.FullScreenPopupWindow;
import com.hotspot.user.app.userprofile.My_Account;
import com.hotspot.user.app.userprofile.UpdateProfile;
import com.hotspot.user.app.userprofile.ViewProfile;
import com.hotspot.user.app.userprofile.WebService;
import com.hotspot.user.app.utils.AppUrls;
import com.hotspot.user.app.utils.CustomPerference;
import com.hotspot.user.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class UserProfile extends Fragment {

    AppCompatTextView
            txt_emailId,
            txt_mobileNumber,
            wallet,
            signout,
            version;


    CoordinatorLayout coordinatorLayout;

    String userId,
            userName, mobileNumber,
            walletBal;


    LinearLayout login_layout;

    AppCompatButton login,
            signup;

    private Context context = getActivity();

    Dialog dialog1;
    private String ProfileImage;
    private ImageView imgProfilePicture;

    private void getSharedPreferencesVal()
    {
        userId = CustomPerference.getString(context, CustomPerference.USER_ID);
        userName = CustomPerference.getString(context, CustomPerference.USER_NAME);
        mobileNumber = CustomPerference.getString(context, CustomPerference.USER_ID);
        walletBal = CustomPerference.getString(context, CustomPerference.USER_WALLET);

    }

    private void getExecuteMethods(View root)
    {
        if(Utils.isNetworkAvailable(context))
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
                getExecuteMethods(root);

            });
        }
    }






    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_u_ser_profile, container, false);

        getSharedPreferencesVal();

        String verName =  "version : " + BuildConfig.VERSION_NAME;
        version.setText(verName);


        if (userId != null ) {

            signout.setVisibility(View.VISIBLE);
            login_layout.setVisibility(View.GONE);

        }

        initializeAll(root);
        getExecuteMethods(root);


        AppBarLayout mAppBarLayout =  root.findViewById(R.id.app_bar);
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


        return root;
    }


    public void initializeAll(View root) {

        login_layout =  root.findViewById(R.id.login_layout);
//        signout =  root.findViewById(R.id.sign_out);
        version =  root.findViewById(R.id.version);
        
        
        coordinatorLayout = root.findViewById(R.id.layout) ;

        imgProfilePicture =  root.findViewById(R.id.imgProfilePicture);


        txt_mobileNumber = root.findViewById(R.id.txt_mobileNumber);
        txt_mobileNumber.setText(mobileNumber);
        txt_emailId = root.findViewById(R.id.txt_emailId);
//        wallet = root.findViewById(R.id.wallet_bal);
//
//        wallet.setOnClickListener(context);

        login =  root.findViewById(R.id.login);
        signup =  root.findViewById(R.id.signup);

//        signout.setOnClickListener(context);


//        root.findViewById(R.id.support).setOnClickListener(context);
//        root.findViewById(R.id.view_profile).setOnClickListener(context);

        imgProfilePicture.setOnClickListener(v -> {
            Intent intent = new Intent(context, FullScreenPopupWindow.class);
            intent.putExtra("image",ProfileImage);
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
        });

        root.findViewById(R.id.about).setOnClickListener(view -> startActivity(new Intent(context, WebService.class)
                .putExtra("url", AppUrls.BaseUrl+"aboutus")));
        root.findViewById(R.id.terms).setOnClickListener(view -> startActivity(new Intent(context, WebService.class)
                .putExtra("url",AppUrls.BaseUrl+"term_condition")));
        root.findViewById(R.id.support).setOnClickListener(view -> startActivity(new Intent(context, WebService.class)
                .putExtra("url",AppUrls.BaseUrl+"contact")));

        root.findViewById(R.id.review).setOnClickListener(view -> openAppRating(context));
        root.findViewById(R.id.share).setOnClickListener(view -> shareApp());
        root.findViewById(R.id.txt_updateProfile).setOnClickListener(view -> startActivity(new Intent(context, UpdateProfile.class)));
        root.findViewById(R.id.change_password).setOnClickListener(view -> startActivity(new Intent(context, ChangePassword.class)));
        root.findViewById(R.id.view_profile).setOnClickListener(view -> startActivity(new Intent(context, ViewProfile.class)));
        root.findViewById(R.id.sign_out).setOnClickListener(view -> CustomPerference.clearPref(context));

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
        String shareBody = "https://play.google.com/store/apps/details?id="+context.getApplicationContext().getPackageName()+ "&referrer=" +RefLink;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "FreedomStar");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

}

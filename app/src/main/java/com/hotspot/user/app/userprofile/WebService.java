package com.hotspot.user.app.userprofile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.hotspot.user.app.R;
import com.hotspot.user.app.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

public class WebService extends AppCompatActivity
{
    boolean doubleBackToExitPressedOnce = false;
    private WebView mWebview;

    TextView tv_title;
    private static String file_type = "image/*";    // file types to be allowed for upload
    private boolean multiple_files = true;         // allowing multiple file upload

    /*-- MAIN VARIABLES --*/


    private static final String TAG = WebService.class.getSimpleName();

    String URL="";
    String Image="";
    String Title="";


    ProgressBar progressBar;
    private String cam_file_data = null;        // for storing camera file information
    private ValueCallback<Uri> file_data;       // data/header received after file selection
    private ValueCallback<Uri[]> file_path;     // received file(s) temp. location

    private final static int file_req_code = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if(Build.VERSION.SDK_INT >= 21){
            Uri[] results = null;

            /*-- if file request cancelled; exited camera. we need to send null value to make future attempts workable --*/
            if (resultCode == Activity.RESULT_CANCELED) {
                if (requestCode == file_req_code) {
                    file_path.onReceiveValue(null);
                    return;
                }
            }

            /*-- continue if response is positive --*/
            if(resultCode== Activity.RESULT_OK){
                if(requestCode == file_req_code){
                    if(null == file_path){
                        return;
                    }

                    ClipData clipData;
                    String stringData;
                    try {
                        clipData = intent.getClipData();
                        stringData = intent.getDataString();
                    }catch (Exception e){
                        clipData = null;
                        stringData = null;
                    }

                    if (clipData == null && stringData == null && cam_file_data != null) {
                        results = new Uri[]{Uri.parse(cam_file_data)};
                    }else{
                        if (clipData != null) { // checking if multiple files selected or not
                            final int numSelectedFiles = clipData.getItemCount();
                            results = new Uri[numSelectedFiles];
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                results[i] = clipData.getItemAt(i).getUri();
                            }
                        } else {
                            results = new Uri[]{Uri.parse(stringData)};
                        }
                    }
                }
            }
            file_path.onReceiveValue(results);
            file_path = null;
        }else{
            if(requestCode == file_req_code){
                if(null == file_data) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                file_data.onReceiveValue(result);
                file_data = null;
            }
        }
    } int i = 0;
     Timer t = new Timer();
     ImageView iv_back;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_web);

        URL=getIntent().getStringExtra("url");
        Image=getIntent().getStringExtra("image");
        Title=getIntent().getStringExtra("title");

        tv_title= findViewById(R.id.tv_title);
        progressBar = findViewById(R.id.progress_bar);
        iv_back=findViewById(R.id.iv_back);
        mWebview =  findViewById(R.id.web_view);
        tv_title.setText(Title);
        iv_back.setOnClickListener(view -> {
            if (mWebview.canGoBack()) {
                progressBar.setVisibility(View.VISIBLE);
                mWebview.goBack();

            } else {
                if (doubleBackToExitPressedOnce) {
                    finish();

                }

                doubleBackToExitPressedOnce = true;
                Toast.makeText(getApplicationContext(), "Please click Back again to Exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        CookieManager.getInstance().setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            // If Android 6.0+ i must add support for Third Party Cookies
            CookieManager.getInstance().setAcceptThirdPartyCookies(mWebview, true);
        }
        mWebview.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        //mWebview.getSettings().setPluginsEnabled(true);
        mWebview.getSettings().setSupportMultipleWindows(false);
        mWebview.getSettings().setSupportZoom(true);
        mWebview.setVerticalScrollBarEnabled(true);
        mWebview.setHorizontalScrollBarEnabled(true);
        mWebview.getSettings().setBuiltInZoomControls(true);
        mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.getSettings().setUseWideViewPort(true);
        mWebview.getSettings().setUserAgentString("Android WebView");
        mWebview.getSettings().setDomStorageEnabled(true);
        mWebview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setLoadsImagesAutomatically(true);
        mWebview.getSettings().setAppCacheEnabled(true);
        mWebview.getSettings().setAllowFileAccess(true);
        mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.setWebChromeClient(new WebChromeClient());

        mWebview.getSettings().setDomStorageEnabled(true);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setAppCacheEnabled(true);
        mWebview.getSettings().setAllowFileAccess(true);
        mWebview.getSettings().enableSmoothTransition();
        mWebview.getSettings().setSafeBrowsingEnabled(true);
        mWebview.getSettings().setSupportMultipleWindows(true);



        WebSettings webSettings = mWebview.getSettings();

        if(Build.VERSION.SDK_INT >= 21){
            mWebview.getSettings().setMixedContentMode(0);
            mWebview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }else if(Build.VERSION.SDK_INT >= 19){
            mWebview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }else {
            mWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mWebview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            mWebview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            mWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        WebView webview = new WebView(this);
        WebSettings ws = webview.getSettings();
        ws.setSaveFormData(true);
        ws.setSavePassword(true);
        final Activity activity = this;


        mWebview.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());

                Toast.makeText(activity, rerr.getDescription().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.v("yrl :", url);
                if (!Utils.isNetworkAvailable(WebService.this)) {
                    t.cancel();
                    progressBar.setVisibility(View.GONE);
                       Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
                } else {

                    new Handler().postDelayed(() -> {
                        progressBar.setVisibility(View.VISIBLE);
                        mWebview.setVisibility(View.GONE);

                        mWebview.loadUrl("javascript:(function() { " +
                                "var head = document.getElementsByClassName('header')[0].style.display='none'; " +
                                "})()");

                    }, 100);

                    view.loadUrl(url);
                }

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                if (!Utils.isNetworkAvailable(WebService.this)) {
                    t.cancel();
                    Toast.makeText(getApplicationContext(), "No Internet Connection! ", Toast.LENGTH_SHORT).show();
                } else {

                    t.cancel();

                    new Handler().postDelayed(() -> {
                        mWebview.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        mWebview.loadUrl("javascript:(function() { " +
                                "var head = document.getElementsByClassName('header')[0].style.display='none'; " +
                                "})()");
                    }, 500);

                    super.onPageFinished(view, url);
                }




            }
        });
        mWebview.loadUrl(URL);
        mWebview.setWebChromeClient(new WebChromeClient() {

            /*--
            openFileChooser is not a public Android API and has never been part of the SDK.
            handling input[type="file"] requests for android API 16+; I've removed support below API 21 as it was failing to work along with latest APIs.
            --*/
        /*    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                file_data = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType(file_type);
                if (multiple_files) {
                    i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                }
                startActivityForResult(Intent.createChooser(i, "File Chooser"), file_req_code);
            }
        */
            /*-- handling input[type="file"] requests for android API 21+ --*/
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {

                if(file_permission() && Build.VERSION.SDK_INT >= 21) {
                    file_path = filePathCallback;
                    Intent takePictureIntent = null;
                    Intent takeVideoIntent = null;

                    boolean includeVideo = false;
                    boolean includePhoto = false;

                    /*-- checking the accept parameter to determine which intent(s) to include --*/
                    paramCheck:
                    for (String acceptTypes : fileChooserParams.getAcceptTypes()) {
                        String[] splitTypes = acceptTypes.split(", ?+"); // although it's an array, it still seems to be the whole value; split it out into chunks so that we can detect multiple values
                        for (String acceptType : splitTypes) {
                            switch (acceptType) {
                                case "*/*":
                                    includePhoto = true;
                                    includeVideo = true;
                                    break paramCheck;
                                case "image/*":
                                    includePhoto = true;
                                    break;
                                case "video/*":
                                    includeVideo = true;
                                    break;
                            }
                        }
                    }

                    if (fileChooserParams.getAcceptTypes().length == 0) {   //no `accept` parameter was specified, allow both photo and video
                        includePhoto = true;
                        includeVideo = true;
                    }

                    if (includePhoto) {
                        takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(WebService.this.getPackageManager()) != null) {
                            File photoFile = null;
                            try {
                                photoFile = create_image();
                                takePictureIntent.putExtra("PhotoPath", cam_file_data);
                            } catch (IOException ex) {
                                Log.e(TAG, "Image file creation failed", ex);
                            }
                            if (photoFile != null) {
                                cam_file_data = "file:" + photoFile.getAbsolutePath();
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            } else {
                                cam_file_data = null;
                                takePictureIntent = null;
                            }
                        }
                    }

                    if (includeVideo) {
                        takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        if (takeVideoIntent.resolveActivity(WebService.this.getPackageManager()) != null) {
                            File videoFile = null;
                            try {
                                videoFile = create_video();
                            } catch (IOException ex) {
                                Log.e(TAG, "Video file creation failed", ex);
                            }
                            if (videoFile != null) {
                                cam_file_data = "file:" + videoFile.getAbsolutePath();
                                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
                            } else {
                                cam_file_data = null;
                                takeVideoIntent = null;
                            }
                        }
                    }

                    Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    contentSelectionIntent.setType(file_type);
                    if (multiple_files) {
                        contentSelectionIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    }

                    Intent[] intentArray;
                    if (takePictureIntent != null && takeVideoIntent != null) {
                        intentArray = new Intent[]{takePictureIntent, takeVideoIntent};
                    } else if (takePictureIntent != null) {
                        intentArray = new Intent[]{takePictureIntent};
                    } else if (takeVideoIntent != null) {
                        intentArray = new Intent[]{takeVideoIntent};
                    } else {
                        intentArray = new Intent[0];
                    }

                    Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                    chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                    chooserIntent.putExtra(Intent.EXTRA_TITLE, "File chooser");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                    startActivityForResult(chooserIntent, file_req_code);
                    return true;
                } else {
                    return false;
                }
            }
        });



    }

    /*-- callback reporting if error occurs --*/
    public class Callback extends WebViewClient{
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
            Toast.makeText(getApplicationContext(), "Failed loading app!", Toast.LENGTH_SHORT).show();
        }
    }

    /*-- checking and asking for required file permissions --*/
    public boolean file_permission(){
        if(Build.VERSION.SDK_INT >=23 && (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(WebService.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            return false;
        }else{
            return true;
        }
    }

    /*-- creating new image file here --*/
    private File create_image() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_"+timeStamp+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName,".jpg",storageDir);
    }

    /*-- creating new video file here --*/
    private File create_video() throws IOException {
        @SuppressLint("SimpleDateFormat")
        String file_name    = new SimpleDateFormat("yyyy_mm_ss").format(new Date());
        String new_name     = "file_"+file_name+"_";
        File sd_directory   = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(new_name, ".3gp", sd_directory);
    }


/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebview.canGoBack()) {
                        progressBar.setVisibility(View.VISIBLE);
                        mWebview.goBack();

                    } else {
                        if (doubleBackToExitPressedOnce) {
                            finish();

                        }

                        this.doubleBackToExitPressedOnce = true;
                        Toast.makeText(this, "Please click Back again to Exit", Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                doubleBackToExitPressedOnce = false;
                            }
                        }, 2000);
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }*/
}
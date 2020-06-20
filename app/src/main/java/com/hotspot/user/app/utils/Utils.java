package com.hotspot.user.app.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hotspot.user.app.R;
import com.hotspot.user.app.auth.SplashActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;

public class Utils {

    private static final int FLIP_VERTICAL = 1;
    private static final int FLIP_HORIZONTAL = 2;

    private static ProgressDialog mProgressDialog;
    public static int totalNights = -1;

    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp){
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
    // VALIDATE EMAIL
    public static boolean isEmailValid(Activity activity, EditText editText, String message){

        Pattern pattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");

        if (pattern.matcher(editText.getText().toString().trim()).matches())
            return true;
        else {
            return false;
        }
    }
    @SuppressLint("HardwareIds")
    public static String getDEVICEID(Context context)
    {
        return Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
    }
    // IS INTERNET AVAILABLE
    public static boolean isNetworkAvailable(final Context ctx) {

        // INITIALIZATION
        ConnectivityManager connectivityManager  = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo            = connectivityManager.getActiveNetworkInfo();

        // RETURN
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

//    public static void getDeviceID(Context context) {
//        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        if (tm != null)
//            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//        assert tm != null;
//        SplashActivity.ANDROID_ID = tm.getDeviceId();
//        if (SplashActivity.ANDROID_ID == null || SplashActivity.ANDROID_ID.length() == 0)
//            SplashActivity.ANDROID_ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//
//        SplashActivity.sharedpreferences = context.getSharedPreferences(SplashActivity.MyPREFERENCES, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = SplashActivity.sharedpreferences.edit();
//
//        System.out.println("Device_Id==" + SplashActivity.ANDROID_ID);
//        editor.putString("Device_Id", SplashActivity.ANDROID_ID);
//        editor.apply();
//
//    }

    public static String IpAddress()
    {  String      ip="";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                       ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        System.out.println("ip==" +ip);
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println("SocketException==" +ex);
        }

        return ip;
    }
//    public static void checkPermisson(final Context context)
//    {
//
//        PermissionListener permissionlistener = new PermissionListener() {
//            @Override
//            public void onPermissionGranted() {
//
//                SplashActivity.Permission=true;
//              //  getDeviceID(context);
//                if(CustomPerference.getBoolean(context,CustomPerference.LOGIN_ONETIME)){
//                   /* context.startActivity(new Intent(context, NewDashboard.class));
//                    SplashActivity.mActivity.finish();*/
//                }else
//                {
//                    context.startActivity(new Intent(context, LoginActivity.class));
//                    SplashActivity.mActivity.finish();
//                }
//
//
//                //   Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onPermissionDenied(List<String> deniedPermissions) {
//                SplashActivity.Permission=false;
//                Toast.makeText(context, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT)
//                        .show();
//            }
//
//
//        };
//
//
//        CheckPermission.with(context)
//                .setPermissionListener(permissionlistener)
//                .setRationaleTitle(R.string.rationale_title)
//                .setRationaleMessage(R.string.rationale_message)
//                .setDeniedTitle("Permission denied")
//                .setDeniedMessage(
//                        "If you Deny all  Permissions,We cannot Provide you Better and Secure Service \n\nPlease turn on All permissions at [Setting] > [Permission]")
//                .setGotoSettingButtonText("TURN ON")
//                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_FINE_LOCATION)
//                .check();
//
//    }

    public  static void  executeCities()
    {

        


    }

    public static void CustomSnackBar()
    {

    }

    public static void customProgress(Context context,String title)
    {

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setCanceledOnTouchOutside(true);
        mProgressDialog.setMessage(title);
        mProgressDialog.show();

    }


    public static void customProgressStop() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing()) {
                try {
                    mProgressDialog.dismiss();
                } catch (Exception ignored) {
                }

            }
        }

    }

    public static boolean CustomAlert(Context context , String Title ,String Message)
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.logo);
        builder.setTitle(Title);
        builder.setMessage(Message)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {

                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    dialog.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        return true;
    }


//    public static void CustomDialog(Context context, String Title,String Message, int layout, Intent intent)
//    {
//        Dialog dialog_auth = new Dialog(context);
//        dialog_auth.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog_auth.setContentView(layout);
//        dialog_auth.show();
//        dialog_auth.setCanceledOnTouchOutside(false);
//        dialog_auth.setCancelable(false);
//
//        Window window = dialog_auth.getWindow();
//        assert window != null;
//        window.setBackgroundDrawableResource(android.R.color.transparent);
//        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//
//        if(layout == R.layout.custom_dialog) {
//            AppCompatTextView txt_title = dialog_auth.findViewById(R.id.txt_title);txt_title.setText(Title);
//            AppCompatTextView txt_message = dialog_auth.findViewById(R.id.txt_message);txt_message.setText(Message);
//            AppCompatButton restart = dialog_auth.findViewById(R.id.submit);
//            restart.setOnClickListener(v -> {
//
//                context.startActivity(intent);
//
//            });
//        }
//    }

//
//    public static void popUpDialog(Context context)
//    {
//        Dialog dialog_auth = new Dialog(context);
//        dialog_auth.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog_auth.setContentView(R.layout.popup);
//        dialog_auth.show();
//        dialog_auth.setCanceledOnTouchOutside(false);
//        dialog_auth.setCancelable(false);
//
//        Window window = dialog_auth.getWindow();
//        assert window != null;
//        window.setBackgroundDrawableResource(android.R.color.transparent);
//        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT);
//
//    }

    public static void PicassoCircular(String image, ImageView imgView)
    {
        Picasso.get().load(image)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .transform(new PicassoCircleTransformation())
                .into(imgView);
    }

    public static void Picasso(String image, ImageView imgView)
    {
        Picasso.get().load(image)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imgView);
    }

    public static class PicassoCircleTransformation implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

    public static String getTomorrowDate(String dateFormat)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        return new SimpleDateFormat(dateFormat)
               .format(tomorrow);

    }

    public static String getTodayDate(String dateFormat)
    {
        return new SimpleDateFormat(dateFormat)
                .format(Calendar.getInstance().getTime());

    }

    public static String getDateString(Date date,String format)
    {

//        Date date = new Date(datetime);

        return new SimpleDateFormat(format)
                .format(date);

    }

    public static Bitmap flipImage(Bitmap src, int type) {
        // create new matrix for transformation
        Matrix matrix = new Matrix();
        // if vertical
        if(type == FLIP_VERTICAL) {
            // y = y * -1
            matrix.preScale(1.0f, -1.0f);
        }
        // if horizonal
        else if(type == FLIP_HORIZONTAL) {
            // x = x * -1
            matrix.preScale(-1.0f, 1.0f);
            // unknown type
        } else {
            return null;
        }

        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }


    public static String getCountOfDays(String createdDateString, String expireDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
        try {
            createdConvertedDate = dateFormat.parse(createdDateString);
            expireCovertedDate = dateFormat.parse(expireDateString);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int cYear = 0, cMonth = 0, cDay = 0;

        if (createdConvertedDate.after(todayWithZeroTime)) {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(createdConvertedDate);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(todayWithZeroTime);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);
        }


    /*Calendar todayCal = Calendar.getInstance();
    int todayYear = todayCal.get(Calendar.YEAR);
    int today = todayCal.get(Calendar.MONTH);
    int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);
    */

        Calendar eCal = Calendar.getInstance();
        eCal.setTime(expireCovertedDate);

        int eYear = eCal.get(Calendar.YEAR);
        int eMonth = eCal.get(Calendar.MONTH);
        int eDay = eCal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(cYear, cMonth, cDay);
        date2.clear();
        date2.set(eYear, eMonth, eDay);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        totalNights = (int) dayCount;

            return ("" + (int) dayCount);

    }




}

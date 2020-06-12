package com.hotspot.user.app.userprofile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hotspot.user.app.R;
import com.hotspot.user.app.utils.Utils;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class FullScreenPopupWindow extends AppCompatActivity {

    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_full_screen_popup_window);

        String imageURL = getIntent().getStringExtra("image");

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    EXTERNAL_STORAGE_PERMISSION_CONSTANT);
        }


        TextView txt_back = findViewById(R.id.txt_back);
        ImageView bg = findViewById(R.id.bg);
        ImageView img_profileFullScreen = findViewById(R.id.img_profileFullScreen);
        txt_back.setOnClickListener(v -> onBackPressed());

        if(getIntent()!=null) {
            Utils.Picasso(imageURL, img_profileFullScreen);
            Picasso.get()
                    .load(imageURL)
                    .transform(new BlurTransformation(FullScreenPopupWindow.this, 25, 1))
                    .into(bg);

        }
        else
            finish();
    }



}

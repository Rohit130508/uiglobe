package com.hotspot.user.app.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hotspot.user.app.R;
import com.hotspot.user.app.utils.AppUrls;
import com.hotspot.user.app.utils.Utils;

public class LoginActivity extends AppCompatActivity {

    EditText edtMobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_login);

        initView();

        findViewById(R.id.cardNavigate).setOnClickListener(v-> startActivity(new Intent(this,OTPScreen.class)));
    }

    void initView()
    {
        edtMobileNumber = findViewById(R.id.edtMobileNumber);

        if(Utils.isNetworkAvailable(this))
        executeMethods();
    }

    void executeMethods()
    {

        StringRequest request = new StringRequest(Request.Method.GET, AppUrls.checkPhone, response -> {

        }, error -> {

        });

        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(request);
    }
}

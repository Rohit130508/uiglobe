package com.hotspot.user.app.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.hotspot.user.app.R;

public class SignInActivity extends AppCompatActivity {

    EditText edtMobileNumber, edtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtMobileNumber = findViewById(R.id.edtMobileNumber);
        edtMobileNumber.setText(getIntent().getStringExtra("number"));
        edtPassword = findViewById(R.id.edtPassword);
    }
}
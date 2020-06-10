package com.hotspot.user.app.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.hotspot.user.app.DashboardActivity;
import com.hotspot.user.app.R;
import com.hotspot.user.app.utils.AppUrls;
import com.hotspot.user.app.utils.CustomPerference;
import com.hotspot.user.app.utils.Utils;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText edtMobileNumber, edtPassword, edtUserName, edtPinCode;
    private String _phone, _password, _userName, _pinCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();

        findViewById(R.id.cardNavigate).setOnClickListener(v ->
        {
            _phone = edtMobileNumber.getText().toString().trim();
            _password = edtPassword.getText().toString().trim();
            _userName = edtUserName.getText().toString().trim();
            _pinCode = edtPinCode.getText().toString().trim();

            if(Utils.isNetworkAvailable(this))
//                if(!TextUtils.isEmpty(_phone) && !TextUtils.isEmpty(_password) && !TextUtils.isEmpty(_userName)
//                        && !TextUtils.isEmpty(_pinCode))
                    executeMethods();
                else
                    Toast.makeText(getApplicationContext(),"All Feilds are required", Toast.LENGTH_LONG).show();
        });
    }
    void initView()
    {
        edtMobileNumber = findViewById(R.id.edtMobileNumber);
        edtMobileNumber.setText(getIntent().getStringExtra("number"));
        edtPassword = findViewById(R.id.edtPassword);
        edtUserName = findViewById(R.id.edtUserName);
        edtPinCode = findViewById(R.id.edtPinCode);
    }
    void executeMethods()
    {
        Utils.customProgress(this,"Please wait...");

        StringRequest request = new StringRequest(Request.Method.GET,
                AppUrls.Registration+getIntent().getStringExtra("number")+"&Password="+_password+"&UserName="+_userName+
                        "&Pincode="+_pinCode,
                response -> {
                    System.out.println("responce---" + AppUrls.Registration+getIntent().getStringExtra("number")+"Password"+_password+"UserName"+_userName+
                            "Pincode"+_pinCode);

                    Utils.customProgressStop();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("StatusCode").equalsIgnoreCase("200"))
                        {
                            System.out.println("responce---" + response);
                            JSONObject object = jsonObject.getJSONObject("Result");
                            CustomPerference.putString(this, CustomPerference.USER_ID, object.getString("UserId"));
                            CustomPerference.putString(this, CustomPerference.USER_PASSWORD, object.getString("Password"));
                            CustomPerference.putString(this, CustomPerference.USER_NAME, object.getString("UserName"));
                            CustomPerference.putString(this, CustomPerference.PinCode, object.getString("Pincode"));
                            CustomPerference.putString(this, CustomPerference.USER_WALLET, object.getString("WalletAmount"));
                            CustomPerference.putString(this, CustomPerference.USER_ROLE, object.getString("Role"));
                            startActivity(new Intent(this, PinCodeActivity.class));
                            finish();
                        }
                        else
                        {
//                            startActivity(new Intent(this,SignInActivity.class));
                        }
                    }catch (Exception e){
                        Utils.customProgressStop();

                        e.printStackTrace();
                    }
                }, error -> {

            Utils.customProgressStop();

        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
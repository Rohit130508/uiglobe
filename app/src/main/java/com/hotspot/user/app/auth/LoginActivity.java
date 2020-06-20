package com.hotspot.user.app.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hotspot.user.app.DashboardActivity;
import com.hotspot.user.app.R;
import com.hotspot.user.app.utils.AppUrls;
import com.hotspot.user.app.utils.CustomPerference;
import com.hotspot.user.app.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText edtMobileNumber;
    private String mobileNumber;
    private ProgressBar indeterminateBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        if(CustomPerference.getBoolean(this,CustomPerference.ISLOGIN))
            startActivity(new Intent(this, PinCodeActivity.class));
        setContentView(R.layout.activity_login);

        initView();

        findViewById(R.id.btnNavigate).setOnClickListener(v-> {

            mobileNumber = edtMobileNumber.getText().toString().trim();
            if(!TextUtils.isEmpty(mobileNumber)) {
                if (Utils.isNetworkAvailable(this))
                    executeMethods();
            }
            else
                Toast.makeText(getApplicationContext(),"Enter a valid input",Toast.LENGTH_LONG).show();
        });
    }

    void initView()
    {
        edtMobileNumber = findViewById(R.id.edtMobileNumber);
        indeterminateBar = findViewById(R.id.indeterminateBar);
    }

    void executeMethods()
    {

        Utils.customProgress(this,"Please Wait...");

        System.out.println("responce---" + AppUrls.checkPhone+mobileNumber);
        StringRequest request = new StringRequest(Request.Method.GET, AppUrls.checkPhone+mobileNumber,
                response -> {


                    Utils.customProgressStop();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("StatusCode").equalsIgnoreCase("200"))
                        {
                            JSONArray jsonArray = jsonObject.getJSONArray("Result");
                            JSONObject object = jsonArray.getJSONObject(0);

                            String statusCode = object.getString("ResText");
                            System.out.println("responce---" + response);
                            if(statusCode.equalsIgnoreCase("Failure")) {

                                startActivity(new Intent(this, SignInActivity.class)
                                        .putExtra("number", mobileNumber));
                            }
                            else
                                startActivity(new Intent(this,SignUpActivity.class)
                                        .putExtra("number",mobileNumber));
                        }

                    }catch (Exception e){
                        Utils.customProgressStop();

                        e.printStackTrace();
                    }
        }, error -> {
            Utils.customProgressStop();


        });

        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(request);
    }
}

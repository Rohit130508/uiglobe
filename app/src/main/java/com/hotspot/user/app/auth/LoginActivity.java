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
import com.hotspot.user.app.R;
import com.hotspot.user.app.utils.AppUrls;
import com.hotspot.user.app.utils.Utils;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText edtMobileNumber;
    private String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_login);

        initView();

        findViewById(R.id.cardNavigate).setOnClickListener(v-> {

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

    }

    void executeMethods()
    {

        Utils.customProgress(this,"Please Wait...");

        StringRequest request = new StringRequest(Request.Method.GET, AppUrls.checkPhone+mobileNumber,
                response -> {

                    Utils.customProgressStop();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("StatusCode").equalsIgnoreCase("200"))
                        {
                            System.out.println("responce---" + response);
                            startActivity(new Intent(this,SignUpActivity.class)
                            .putExtra("number",mobileNumber));
                        }
                        else
                        {
                            startActivity(new Intent(this,SignInActivity.class));
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

package com.hotspot.user.app.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hotspot.user.app.MainActivity;
import com.hotspot.user.app.R;
import com.hotspot.user.app.utils.AppUrls;
import com.hotspot.user.app.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity {

    EditText edtMobileNumber, edtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtMobileNumber = findViewById(R.id.edtMobileNumber);
        edtMobileNumber.setText(getIntent().getStringExtra("number"));
        edtPassword = findViewById(R.id.edtPassword);

        findViewById(R.id.cardNavigate).setOnClickListener(v ->
        {
            if(Utils.isNetworkAvailable(this))
                executeLoginUser();
        });

    }

    void executeLoginUser()
    {
        String mobNumber = edtMobileNumber.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        Utils.customProgress(this,"Please Wait...");

        StringRequest request = new StringRequest(Request.Method.GET, AppUrls.Login+mobNumber+"&Password="+password,
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
                            if(statusCode.equalsIgnoreCase("Failure")){}
//                                startActivity(new Intent(this,SignUpActivity.class)
//                                        .putExtra("number",mobileNumber));
                            else
                                startActivity(new Intent(this, MainActivity.class));
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

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
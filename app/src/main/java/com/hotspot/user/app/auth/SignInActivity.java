package com.hotspot.user.app.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hotspot.user.app.DashboardActivity;
import com.hotspot.user.app.MainActivity;
import com.hotspot.user.app.R;
import com.hotspot.user.app.utils.AppUrls;
import com.hotspot.user.app.utils.CustomPerference;
import com.hotspot.user.app.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity {

    EditText edtMobileNumber, edtPassword;
    private Button btnNavigate;
    private ProgressBar indeterminateBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtMobileNumber = findViewById(R.id.edtMobileNumber);
        edtMobileNumber.setText(getIntent().getStringExtra("number"));
        edtPassword = findViewById(R.id.edtPassword);
        btnNavigate = findViewById(R.id.btnNavigate);
        indeterminateBar = findViewById(R.id.indeterminateBar);

        btnNavigate.setOnClickListener(v ->
        {
            if(Utils.isNetworkAvailable(this)) {
                indeterminateBar.setVisibility(View.VISIBLE);
                btnNavigate.setVisibility(View.GONE);
                executeLoginUser();
            }
        });

    }

    void executeLoginUser()
    {
        String mobNumber = edtMobileNumber.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST, AppUrls.Login+mobNumber+"&Password="+password,
                response -> {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("StatusCode").equalsIgnoreCase("200"))
                        {

                            indeterminateBar.setVisibility(View.GONE);
                            btnNavigate.setVisibility(View.VISIBLE);
                            System.out.println("responce---" + response);

                            JSONArray jsonArray = jsonObject.getJSONArray("Result");
                            JSONObject object = (JSONObject) jsonArray.get(0);
                            String statusCode = object.getString("ResText");

                            if(statusCode.equalsIgnoreCase("Success"))
                            {
                                CustomPerference.putString(this,
                                        CustomPerference.USER_ID,object.getString("UserId"));
                                CustomPerference.putString(this,
                                        CustomPerference.USER_PASSWORD,object.getString("Password"));
                                CustomPerference.putBoolean(this,
                                        CustomPerference.ISLOGIN,true);
                                CustomPerference.putString(this,
                                        CustomPerference.USER_NAME,object.getString("UserName"));
                                CustomPerference.putString(this,
                                        CustomPerference.PinCode,object.getString("Pincode"));
                                CustomPerference.putString(this,
                                        CustomPerference.USER_WALLET,object.getString("WalletAmount"));
                                CustomPerference.putString(this,
                                        CustomPerference.USER_ROLE,object.getString("Role"));
                                startActivity(new Intent(this, PinCodeActivity.class));

                            }else{
                                 Toast.makeText(getApplicationContext(), "Invalid User", Toast.LENGTH_LONG);
                            startActivity(new Intent(this,SignUpActivity.class)
                                    .putExtra("number",mobNumber));
                            }
//                            else
//                                startActivity(new Intent(this, MainActivity.class));
                        }
                        else
                        {
                            indeterminateBar.setVisibility(View.GONE);
                            btnNavigate.setVisibility(View.VISIBLE);
                            startActivity(new Intent(this,SignInActivity.class));
                        }
                    }catch (Exception e){
                        indeterminateBar.setVisibility(View.GONE);
                        btnNavigate.setVisibility(View.VISIBLE);
                        System.out.println("responce---" + response);
                        e.printStackTrace();
                    }
                }, error -> {
            indeterminateBar.setVisibility(View.GONE);
            btnNavigate.setVisibility(View.VISIBLE);


        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
package com.hotspot.user.app.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
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

        StringRequest request = new StringRequest(Request.Method.POST, AppUrls.Login+mobNumber+"&Password="+password,
                response -> {

//                    System.out.println("res"+AppUrls.Login+mobNumber+"&Password="+password+"\n"+response);
                    Utils.customProgressStop();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("StatusCode").equalsIgnoreCase("200"))
                        {

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
                                startActivity(new Intent(this, DashboardActivity.class));

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
                            startActivity(new Intent(this,SignInActivity.class));
                        }
                    }catch (Exception e){
                        Utils.customProgressStop();
                        System.out.println("responce---" + response);
                        e.printStackTrace();
                    }
                }, error -> {
            Utils.customProgressStop();


        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
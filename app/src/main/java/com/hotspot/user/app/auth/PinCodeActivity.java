package com.hotspot.user.app.auth;

import android.content.Intent;
import android.os.Bundle;

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

public class PinCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(Utils.isNetworkAvailable(this))
            executePin();
    }

    void executePin()
    {
        Utils.customProgress(this,"Please Wait...");

        StringRequest request = new StringRequest(Request.Method.GET,
                AppUrls.PinCodeValidation+CustomPerference.getString(this,CustomPerference.PinCode),
                response -> {

                    Utils.customProgressStop();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("Result");
                        if (jsonObject.getString("StatusCode").equalsIgnoreCase("200"))
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            if(jsonObject1.getString("ResText").equalsIgnoreCase("Failure"))
                            {
                                setContentView(R.layout.activity_pin_code);
//                                startActivity(new Intent(this, DashboardActivity.class));
//                                return;
                            }
                            else{

                                startActivity(new Intent(this, DashboardActivity.class));
                                finish();
                            }
                        }
                        else
                        {

                        }
                    }catch (Exception e){
                        Utils.customProgressStop();

                        e.printStackTrace();
                    }
                }, error -> Utils.customProgressStop());

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
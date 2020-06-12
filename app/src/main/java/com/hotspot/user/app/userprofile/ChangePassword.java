package com.hotspot.user.app.userprofile;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.hotspot.user.app.R;
import com.hotspot.user.app.auth.LoginActivity;
import com.hotspot.user.app.utils.AppUrls;
import com.hotspot.user.app.utils.CustomPerference;
import com.hotspot.user.app.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.hotspot.user.app.userprofile.ViewProfile.openDialogNoInternet;


public class ChangePassword extends AppCompatActivity {

    final Context context = ChangePassword.this;

    TextInputLayout input_layout_oldpswrd,
            input_layout_newpswrd,
            input_layout_password;

    String userId,
            tokenId;

    EditText old_password,
            new_password,
            conform_password;

    String pswrd,
            new_pswrd,
            conpassword;

    private void getSharedPreferencesVal()
    {
        userId = CustomPerference.getString(context, CustomPerference.USER_ID);
        tokenId = CustomPerference.getString(context, CustomPerference.USER_TOKEN);
    }

    private void getExecuteMethods()
    {
        if(Utils.isNetworkAvailable(this)){}
//            execute();
        else
            openDialogNoInternet(context);
    }

    void initView()
    {
        input_layout_oldpswrd = findViewById(R.id.input_layout_oldpswrd);
        input_layout_newpswrd = findViewById(R.id.input_layout_newpswrd);
        input_layout_password = findViewById(R.id.input_layout_password);

        old_password= findViewById(R.id.change_password_old_password);
        new_password= findViewById(R.id.change_password_new_password);
        conform_password= findViewById(R.id.change_password_conform_password);

        old_password.addTextChangedListener(new MyTextWatcher(old_password));
        new_password.addTextChangedListener(new MyTextWatcher(new_password));
        conform_password.addTextChangedListener(new MyTextWatcher(conform_password));

        findViewById(R.id.change_password_submit_button).setOnClickListener(view -> validateForm());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_changepassword);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setTitle("Change Password");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        getSharedPreferencesVal();
        initView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void validateForm() {

        if (!validateOld()) {
            return;
        }

        if (!validateNew()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
        if (validateOld() && validateNew() && validatePassword()) {

            getExecuteMethods();
        }
    }

    private boolean validateOld() {

        pswrd = old_password.getText().toString().trim();

        if (pswrd.isEmpty()) {
            input_layout_oldpswrd.setError(getText(R.string.error_field_required));
            requestFocus(old_password);
            return false;
        } else {
            input_layout_oldpswrd.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateNew() {

        new_pswrd = new_password.getText().toString().trim();

        if (new_pswrd.isEmpty()) {
            input_layout_newpswrd.setError(getText(R.string.error_field_required));
            requestFocus(new_password);
            return false;
        } else if( new_pswrd.length()<6){
            input_layout_newpswrd.setError(getText(R.string.error_invalid_password));
            return false;
        }else {
            input_layout_newpswrd.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validatePassword() {

        conpassword = conform_password.getText().toString().trim();
        if (conpassword.isEmpty()) {
            input_layout_password.setError(getText(R.string.error_field_required));
            requestFocus(conform_password);
            return false;
        }else if( conpassword.length()<6){
            input_layout_password.setError(getText(R.string.error_invalid_password));
            return false;
        } else if(!conpassword.equals(new_pswrd)) {

            input_layout_password.setError(getText(R.string.password_mismatch));
            return false;
        }
        else {

            input_layout_password.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.change_password_old_password:
                    validateOld();
                    break;
                case R.id.change_password_new_password:
                    validateNew();
                    break;
                case R.id.change_password_conform_password:
                    validatePassword();
                    break;
            }
        }
    }
//    public void execute() {
//
//        Utils.customProgress(context,"Please Wait ...");
//        Map<String,String> params = new HashMap<>();
//
//        params.put("UserId",userId);
//        params.put("TokenId",tokenId);
//        params.put("OldPswd",pswrd);
//        params.put("NewPswd",new_pswrd);
//
//        JSONObject jsonObj = new JSONObject(params);
//
//        System.out.println("jsonobject_pass_chan==="+ jsonObj);
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, AppUrls.ChangePassword, jsonObj,
//                response -> {
//
//            Utils.customProgressStop();
//                    System.out.println("result of changepass===" + response);
//
//                    try {
//
//                        String status = response.getString("Status").trim();
//                        String msg = response.getString("Message").trim();
//                        String Login_sts = response.getString("LoginStatus").trim();
//
//                        if(status.equalsIgnoreCase("true")){
//                            Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
//
//                            if (Login_sts.equalsIgnoreCase("false")){
//
//                                Intent in = new Intent(getApplicationContext(), LoginActivity.class);
//                                startActivity(in);
//                                finish();
//                            }
//                        }
//                        else{
//
//                            Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_SHORT).show();
//                        }
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }, error -> {
//
//            Utils.customProgressStop();
//            Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
//
//        });
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(jsonObjReq);
//    }
}

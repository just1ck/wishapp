package com.rc.wishapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class Registration_activity extends AppCompatActivity {
    public String loginText = "neme";
    public String mailText = "email";
    public String passText = "password";
    public String repeatPassText = "repeatPassText";
    public EditText nameInput;
    public EditText mailInput;
    public EditText passInput;
    public EditText repeatPass;
    public Button regBtn;
    public int counter = 0;
    public EditText[] edtxtArr;
    private static String allObjects;
    public static JSONObject jsonObj = null;
    public static String loginTextR = "neme";
    public static String mailTextR = "email";
    public static String passTextR = "password";
    public static JSONObject errObject = null;
    public static String errorCode;
    public static int countErr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);



        LinearLayout back_auth = findViewById(R.id.back_auth);
        nameInput = findViewById(R.id.nameInput);
        mailInput = findViewById(R.id.mailInput);
        passInput = findViewById(R.id.passInput);
        repeatPass = findViewById(R.id.repeat_pass_Input);
        regBtn = findViewById(R.id.registrationGo_Button);
        edtxtArr = new EditText[]{nameInput, mailInput, passInput, repeatPass};


        final Animation alpha = AnimationUtils.loadAnimation(this, R.anim.button_anim);


        myApp.loadRegText(Registration_activity.this, nameInput, mailInput, passInput, repeatPass, loginText, mailText, passText, repeatPassText);

        back_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration_activity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slideoutrev, R.anim.slidinrev);
                finish();

            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(alpha);
                HiddenKeyboard.hideKeyboard(Registration_activity.this);
                myApp.saveRegText(Registration_activity.this, nameInput, mailInput, passInput, repeatPass, loginText, mailText, passText, repeatPassText);
                validateText();
                if (nameInput.getText().toString().isEmpty() || mailInput.getText().toString().isEmpty() || passInput.getText().toString().isEmpty() || repeatPass.getText().toString().isEmpty()){
                    showToast("Все поля обязательны для заполнения");
                } else if (passInput.getText().length() < 6){
                    showToast("Пароль должен быть не меньше 6 символов");
                    passInput.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                } else if(isEmailValid(mailInput.getText().toString()) == false){
                    showToast("веден неверный mail");
                    mailInput.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                } else if (passInput.getText().toString().equals(repeatPass.getText().toString()) == false){
                    showToast("Пароли не совпадают");
                    passInput.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                    repeatPass.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                }  else if (nameInput.getText().toString().isEmpty() == false|| mailInput.getText().toString().isEmpty() == false|| passInput.getText().toString().isEmpty() == false || repeatPass.getText().toString().isEmpty() == false){

                    registration(Registration_activity.this);


                }

            }
        });

        mailInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mailInput.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);

                }
            }
        });

        nameInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    nameInput.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);

                }
            }
        });

        repeatPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    repeatPass.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);

                }
            }
        });

        passInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    passInput.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);

                }
            }
        });



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Registration_activity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slideoutrev, R.anim.slidinrev);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myApp.saveRegText(Registration_activity.this, nameInput, mailInput, passInput, repeatPass, loginText, mailText, passText, repeatPassText);
    }

    @Override
    protected void onPause() {
        super.onPause();
        myApp.saveRegText(Registration_activity.this, nameInput, mailInput, passInput, repeatPass, loginText, mailText, passText, repeatPassText);
    }

    @Override
    protected void onStop() {
        super.onStop();
        myApp.saveRegText(Registration_activity.this, nameInput, mailInput, passInput, repeatPass, loginText, mailText, passText, repeatPassText);
    }

    public void showToast(String uText){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.empty_name, findViewById(R.id.toastLayout));

        TextView text = layout.findViewById(R.id.errTextView);
        text.setText(uText);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 30);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public boolean isEmailValid(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void validateText(){
        for (int i = 0; i < edtxtArr.length; i++){
            if (edtxtArr[i].getText().toString().isEmpty()){
                edtxtArr[i].getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    public void registration(Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = "https://api.wishapp.ru/auth/signup";
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("email", myApp.getEmail(context.getApplicationContext(), mailTextR));
            jsonBody.put("password", myApp.getPassword(context.getApplicationContext(), passTextR));
            jsonBody.put("name", myApp.getName(context.getApplicationContext(), loginTextR));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                allObjects = response.toString();
                Intent intent = new Intent(Registration_activity.this, MailCodeVerify.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slideout, R.anim.slidein);
                finish();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String jsonErrString = null;
                try {
                    jsonErrString = new String(error.networkResponse.data,
                            HttpHeaderParser.parseCharset(error.networkResponse.headers));
                    errorCode = jsonErrString;
                    errObject = new JSONObject(errorCode);
                    int errs = errObject.getInt("code");
                    if (errs == 1003){
                        showToast("Данный Email уже зарегистрирован");
                    }
                    System.out.println(jsonErrString);

                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }


            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-api-version", "1.0");
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        responseString = String.valueOf(jsonString);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        requestQueue.add(stringRequest);
    }




}
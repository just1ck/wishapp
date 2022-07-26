package com.rc.wishapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private Object HttpURLConnection;
    public String responsAuth;
    public static String accessToken;
    public static String refreshToken;
    String tokens;
    StringBuilder stringBuilder = new StringBuilder();
    JSONObject jsonObj = null;
    public Tokens tokensClass = new Tokens();

    public String access_token;
    public String refresh_token;
    RefreshTokens rfTokens = new RefreshTokens();
    public String errorCode;
    public String errMessage;
    JSONObject errObject = null;
    public SharedPreferences sPref;
    final String SAVED_TEXT = "saved_login";
    final String SAVED_PASS = "saved_pass";
    public static EditText Login_input;
    public static EditText Pass_input;
    final String acT = "access_token";
    final String rfT = "refresh_token";
    public String retutnTokenAccess;
    public String retutnTokenRefresh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button logInButton = findViewById(R.id.login_button);
        Login_input = findViewById(R.id.Name_login_input);
        Pass_input = findViewById(R.id.Password_login_input);
        Button refreshBtn = findViewById(R.id.refresh);
        Button showTokens = findViewById(R.id.showTokens);
        TextView logoClick = findViewById(R.id.logoClick);
        LinearLayout regBtn = findViewById(R.id.reg_btn);

        if (ErrConnection.hasConnection(MainActivity.this) == false){
            showToast("Нет соединения");
        }


        myApp.loadText(MainActivity.this, Login_input, Pass_input, SAVED_TEXT, SAVED_PASS);
        myApp.loadTokens(MainActivity.this, rfT, acT, retutnTokenRefresh, retutnTokenAccess);


        final Animation alpha = AnimationUtils.loadAnimation(this, R.anim.button_anim);


        LinearLayout layout = findViewById(R.id.layout_Inputs);


        Login_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Login_input.getBackground().setColorFilter(Color.parseColor("#66FFFFFF"), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        Pass_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Pass_input.getBackground().setColorFilter(Color.parseColor("#66FFFFFF"), PorterDuff.Mode.SRC_ATOP);

                }
            }
        });

        logoClick.setOnTouchListener(new View.OnTouchListener() {
            long startTime;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: // нажатие
                        startTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_MOVE: // движение
                        break;
                    case MotionEvent.ACTION_UP: // отпускание
                    case MotionEvent.ACTION_CANCEL:
                        long totalTime = System.currentTimeMillis() - startTime;
                        long totalSecunds = totalTime / 1000;
                        if( totalSecunds >= 3 )
                        {
                            refreshBtn.setVisibility(View.VISIBLE);
                            showTokens.setVisibility(View.VISIBLE);
                            System.out.println("Три секунды прошло с нажатия!");
                        }
                        break;
                }
                return true;
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Registration_activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slidein, R.anim.slideout);
                finish();
            }
        });


        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rfTokens.refresh_tokens(MainActivity.this);
                showToast("Токен обновлен");
            }
        });

        showTokens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh_token = rfTokens.getRefreshToken();
                access_token = rfTokens.getAcessToken();
                tokensClass.setTokens(access_token, refresh_token);
               System.out.println(tokensClass.access());

                showToast(myApp.getAccess(MainActivity.this, rfT, acT));
            }
        });



        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(alpha);
                HiddenKeyboard.hideKeyboard(MainActivity.this);
                System.out.println(isEmailValid(Login_input.getText().toString()));
                if (Login_input.getText().toString().isEmpty() && Pass_input.getText().toString().isEmpty()) {
                        Login_input.getBackground().setColorFilter(Color.parseColor("#f405fd"), PorterDuff.Mode.SRC_ATOP);
                        Pass_input.getBackground().setColorFilter(Color.parseColor("#f405fd"), PorterDuff.Mode.SRC_ATOP);
                        showToast("Заполните поля");
                }
                else if (isEmailValid(Login_input.getText().toString()) == false){
                    showToast("Email введен не верно");
                }
                else if (Pass_input.getText().toString().isEmpty()){
                    Pass_input.getBackground().setColorFilter(Color.parseColor("#f405fd"), PorterDuff.Mode.SRC_ATOP);
                    showToast("Введите пароль");
                }
                else if (Pass_input.getText().length() < 6){
                    Pass_input.getBackground().setColorFilter(Color.parseColor("#f405fd"), PorterDuff.Mode.SRC_ATOP);
                    showToast("Пароль должен быть больше 6 символов");
                }
                else if (Login_input.getText().toString().isEmpty()){
                    Login_input.getBackground().setColorFilter(Color.parseColor("#f405fd"), PorterDuff.Mode.SRC_ATOP);
                    showToast("Введите mail");
                }
                else if (Login_input.getText().toString().isEmpty() == false && Pass_input.getText().toString().isEmpty() == false){

                    postAuth(Login_input.getText().toString(), Pass_input.getText().toString());
//                    System.out.println(tokens);
//                    postAuthV2(Login_input.getText().toString(), Pass_input.getText().toString());

                } else if(errMessage.equals("1001")){
                    Login_input.getBackground().setColorFilter(Color.parseColor("#f405fd"), PorterDuff.Mode.SRC_ATOP);
                    Pass_input.getBackground().setColorFilter(Color.parseColor("#f405fd"), PorterDuff.Mode.SRC_ATOP);
                    showToast("Логин или пароль не верные");
                }//else if (errs ){
//                    showToast("Ваш Email не подтвержеден!");
//                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    public void postAuth(String Login_input, String Pass_input) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String URL = "https://api.wishapp.ru/auth/signin";
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("email", Login_input);
            jsonBody.put("password", Pass_input);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST,  URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                showToast("Вы авторизировались!");
                tokens = response.toString();


                try {
                    jsonObj = new JSONObject(tokens);
                    String ACtok = jsonObj.getString("access_token");
                    String RFtok = jsonObj.getString("refresh_token");
                    myApp.setTokens(ACtok, RFtok, getApplicationContext(), rfT, acT);
                    VerifyMail.DecodeJWT(myApp.getAccess(MainActivity.this, rfT, acT));
                    if (VerifyMail.boolMailVerify() == true){
                        showToast("Вы вторизированы!");
                        Intent intent = new Intent(MainActivity.this, wishList.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slidein, R.anim.slideout);
                        finish();
                    } else if (VerifyMail.boolMailVerify() == false){
                        showToast("Ваша электронная почта не подтверждена!");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    String jsonErrString = new String(error.networkResponse.data,
                            HttpHeaderParser.parseCharset(error.networkResponse.headers));
                    errorCode = jsonErrString;
                    errObject = new JSONObject(errorCode);
                    int errs = errObject.getInt("code");
//                    errMessage = errs;
                    System.out.println(jsonErrString);
                    if (errs == 1001){
                        showToast("Логин или пароль не верны!");
                    } else if (VerifyMail.boolMailVerify() == false){
                        showToast("Ваша электронная почта не подтверждена!");
                    }
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



    @Override
    protected void onDestroy() {
        super.onDestroy();
        myApp.saveText(MainActivity.this, SAVED_TEXT, SAVED_PASS, Login_input, Pass_input);

    }

    @Override
    protected void onStop() {
        super.onStop();
        myApp.saveText(MainActivity.this, SAVED_TEXT, SAVED_PASS, Login_input, Pass_input);
    }

    @Override
    protected void onPause() {
        super.onPause();
        myApp.saveText(MainActivity.this, SAVED_TEXT, SAVED_PASS, Login_input, Pass_input);
    }



}




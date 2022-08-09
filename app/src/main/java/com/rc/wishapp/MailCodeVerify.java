package com.rc.wishapp;

import static com.rc.wishapp.R.drawable.bg_button;
import static com.rc.wishapp.R.drawable.bg_button_disable;
import static com.rc.wishapp.R.drawable.err_inputs;
import static com.rc.wishapp.R.drawable.inputs_bg;
import static com.rc.wishapp.R.drawable.inputs_focus_bg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MailCodeVerify extends AppCompatActivity {
    public EditText btn_num1;
    public EditText btn_num2;
    public EditText btn_num3;
    public EditText btn_num4;
    public EditText btn_num5;
    public EditText btn_num6;
    public EditText[] editTextArr;
    public Drawable drawable;
    public Drawable defoultdrawable;
    public Drawable errtdrawable;
    public Button mailVerify;
    public TextView clearItems;
    public CountDownTimer cTD;
    public Drawable disableBtnDravable;
    public Drawable btnNormal;
    public String tokens;
    public JSONObject jsonObj;
    final String acT = "access_token";
    final String rfT = "refresh_token";
    public static String mailTextR = "email";
    public static String passTextR = "password";
    public static String errorCode;
    public static JSONObject errObject = null;
    public String access;
    public TextView resend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_code_verify);

        showToast("Введите код из сообщения на почте");

        access = myApp.getAccess(MailCodeVerify.this, rfT, acT);




        btn_num1 = findViewById(R.id.btn_num1);
        btn_num2 = findViewById(R.id.btn_num2);
        btn_num3 = findViewById(R.id.btn_num3);
        btn_num4 = findViewById(R.id.btn_num4);
        btn_num5 = findViewById(R.id.btn_num5);
        btn_num6 = findViewById(R.id.btn_num6);
        resend = findViewById(R.id.resend);

        mailVerify = findViewById(R.id.send_mail_button);
        clearItems = findViewById(R.id.clear);
        final Animation alpha = AnimationUtils.loadAnimation(this, R.anim.button_anim);

        editTextArr = new EditText[]{btn_num1, btn_num2, btn_num3, btn_num4, btn_num5, btn_num6};
        

        btnClick(btn_num1, btn_num2);
        btnClick(btn_num2, btn_num3);
        btnClick(btn_num3, btn_num4);
        btnClick(btn_num4, btn_num5);
        btnClick(btn_num5, btn_num6);
        btnClick(btn_num6, btn_num6);
        drawable = getResources().getDrawable(inputs_focus_bg);
        defoultdrawable = getResources().getDrawable(inputs_bg);
        errtdrawable = getResources().getDrawable(err_inputs);
        disableBtnDravable = getResources().getDrawable(bg_button_disable);
        btnNormal = getResources().getDrawable(bg_button);

        disableBtns();

        clearItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllET();
                disableBtns();
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(alpha);
                cDT();
                sendMessage();
            }
        });


        mailVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(alpha);
                HiddenKeyboard.hideKeyboard(MailCodeVerify.this);
                if (getMailCode().length() != 6){
                    validateCode();
                    showToast("Введите код");
                }else {
                    postVerify(getMailCode());


                }

            }
        });
    }




    public void btnClick(EditText btn, EditText nextBtn){

            btn.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    nextBtn.setEnabled(true);
                    nextBtn.requestFocus();
                } else if (nextBtn == btn_num6){
                    mailVerify.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                btn.setBackground(defoultdrawable);
                btn.setEnabled(false);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
        });
        btn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                btn.setBackground(drawable);
                if (btn.getText().toString().isEmpty() == false){
                    btn.setBackground(defoultdrawable);

                }

            }
        });
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

    public String getMailCode(){
        String code = btn_num1.getText().toString() + btn_num2.getText().toString() + btn_num3.getText().toString() + btn_num4.getText().toString() + btn_num5.getText().toString() + btn_num6.getText().toString();
        return code;
    }

    public void validateCode(){
        for (int i = 0; i < editTextArr.length; i++){
                editTextArr[i].setBackground(errtdrawable);
        }
    }

    public void clearAllET(){
        for (int i = 0; i < editTextArr.length; i++){
            editTextArr[i].setText(null);
            editTextArr[i].setEnabled(true);
            editTextArr[i].getBackground().clearColorFilter();
            editTextArr[i].setBackground(defoultdrawable);
        }
    }

    public void disableBtns(){
        btn_num1.requestFocus();
        btn_num2.setEnabled(false);
        btn_num3.setEnabled(false);
        btn_num4.setEnabled(false);
        btn_num5.setEnabled(false);
        btn_num6.setEnabled(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MailCodeVerify.this, Registration_activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slideoutrev, R.anim.slidinrev);
        finish();
    }

    public void cDT(){
        cTD = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long l) {
                resend.setText(l / 1000 + " c");
                resend.setEnabled(false);
            }

            @Override
            public void onFinish() {
                resend.setEnabled(false);
                resend.setText("Отправить код еще раз");
            }
        }
        .start();

    }

    public void postVerify(String verifyCode) {
        RequestQueue requestQueue = Volley.newRequestQueue(MailCodeVerify.this);
        String URL = "https://api.wishapp.ru/auth/email/verify";
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("code", verifyCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST,  URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(MailCodeVerify.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slidinrev, R.anim.slideoutrev);
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
                    if (errs == 1002){
                        showToast("Неправильный код");
                    }else if (errs == 1004){
                        showToast("Email уже подтвержден");
                    }else {
                        showToast("правильный код");
                    }
                    System.out.println(jsonErrString);

                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }

            }

            }
        ) {
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
                params.put("Authorization", "Bearer " + myApp.getAccess(MailCodeVerify.this, rfT, acT));
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

    public void sendMessage() {
        RequestQueue requestQueue = Volley.newRequestQueue(MailCodeVerify.this);
        String URL = "https://api.wishapp.ru/auth/email/resend";
        JSONObject jsonBody = new JSONObject();


        final String requestBody = jsonBody.toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST,  URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }
        ) {
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
                params.put("Authorization", "Bearer " + myApp.getAccess(MailCodeVerify.this, rfT, acT));
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
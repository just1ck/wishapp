package com.rc.wishapp.activities;

import static com.rc.wishapp.R.drawable.bg_button;
import static com.rc.wishapp.R.drawable.bg_button_disable;
import static com.rc.wishapp.R.drawable.err_inputs;
import static com.rc.wishapp.R.drawable.inputs_bg;
import static com.rc.wishapp.R.drawable.inputs_focus_bg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rc.wishapp.processing.HiddenKeyboard;
import com.rc.wishapp.R;
import com.rc.wishapp.processing.ShowToast;
import com.rc.wishapp.memory_methods.myApp;
import com.rc.wishapp.requests.Requests;

import org.json.JSONObject;

public class MailCodeVerify_activity extends AppCompatActivity {
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

        ShowToast.showToast("Введите код из сообщения на почте", MailCodeVerify_activity.this);

        access = myApp.getAccess(MailCodeVerify_activity.this, rfT, acT);




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
                Requests.sendMessage(MailCodeVerify_activity.this);
            }
        });


        mailVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(alpha);
                HiddenKeyboard.hideKeyboard(MailCodeVerify_activity.this);
                if (getMailCode().length() != 6){
                    validateCode();
                    ShowToast.showToast("Введите код", MailCodeVerify_activity.this);
                }else {
                    Requests.postVerify(getMailCode(), MailCodeVerify_activity.this, MailCodeVerify_activity.this, MainActivity.class);


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
        Intent intent = new Intent(MailCodeVerify_activity.this, Registration_activity.class);
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






}
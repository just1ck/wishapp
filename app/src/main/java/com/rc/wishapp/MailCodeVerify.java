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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_code_verify);

        showToast("Введите код из сообщения на почте");




        btn_num1 = findViewById(R.id.btn_num1);
        btn_num2 = findViewById(R.id.btn_num2);
        btn_num3 = findViewById(R.id.btn_num3);
        btn_num4 = findViewById(R.id.btn_num4);
        btn_num5 = findViewById(R.id.btn_num5);
        btn_num6 = findViewById(R.id.btn_num6);

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


        mailVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(alpha);
                HiddenKeyboard.hideKeyboard(MailCodeVerify.this);
                if (getMailCode().length() != 6){
                    validateCode();
                    showToast("Введите код");
                }else {
                    showToast(getMailCode());
                    cDT();
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
                clearItems.setEnabled(false);
                mailVerify.setText(l / 1000 + " c");
                mailVerify.setTextColor(Color.WHITE);
                mailVerify.setEnabled(false);
                mailVerify.setBackground(disableBtnDravable);
            }

            @Override
            public void onFinish() {
                clearItems.setEnabled(true);
                mailVerify.setTextColor(Color.WHITE);
                mailVerify.setText("Отправить");
                mailVerify.setEnabled(true);
                mailVerify.setBackground(btnNormal);
            }
        }
        .start();

    }
}
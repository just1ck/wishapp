package com.rc.wishapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import com.rc.wishapp.processing.HiddenKeyboard;
import com.rc.wishapp.R;
import com.rc.wishapp.processing.ShowToast;
import com.rc.wishapp.memory_methods.myApp;
import com.rc.wishapp.requests.Requests;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONObject;


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
    public String tokens;
    final String acT = "access_token";
    final String rfT = "refresh_token";




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


        back_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registration_activity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slidinrev, R.anim.slideoutrev);
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
                    ShowToast.showToast("Все поля обязательны для заполнения", Registration_activity.this);
                } else if (passInput.getText().length() < 6){
                    ShowToast.showToast("Пароль должен быть не меньше 6 символов", Registration_activity.this);
                    passInput.getBackground().setColorFilter(Color.parseColor("#f405fd"), PorterDuff.Mode.SRC_ATOP);
                } else if(isEmailValid(mailInput.getText().toString()) == false){
                    ShowToast.showToast("Веден неверный mail", Registration_activity.this);
                    mailInput.getBackground().setColorFilter(Color.parseColor("#f405fd"), PorterDuff.Mode.SRC_ATOP);
                } else if (passInput.getText().toString().equals(repeatPass.getText().toString()) == false){
                    ShowToast.showToast("Пароли не совпадают", Registration_activity.this);
                    passInput.getBackground().setColorFilter(Color.parseColor("#f405fd"), PorterDuff.Mode.SRC_ATOP);
                    repeatPass.getBackground().setColorFilter(Color.parseColor("#f405fd"), PorterDuff.Mode.SRC_ATOP);
                }  else if (nameInput.getText().toString().isEmpty() == false|| mailInput.getText().toString().isEmpty() == false|| passInput.getText().toString().isEmpty() == false || repeatPass.getText().toString().isEmpty() == false){
                    Requests.registration(Registration_activity.this, Registration_activity.this, MailCodeVerify_activity.class, mailTextR, passTextR, loginTextR);

                }

            }
        });

        mailInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mailInput.getBackground().setColorFilter(Color.parseColor("#66FFFFFF"), PorterDuff.Mode.SRC_ATOP);

                }
            }
        });

        nameInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    nameInput.getBackground().setColorFilter(Color.parseColor("#66FFFFFF"), PorterDuff.Mode.SRC_ATOP);

                }
            }
        });

        repeatPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    repeatPass.getBackground().setColorFilter(Color.parseColor("#66FFFFFF"), PorterDuff.Mode.SRC_ATOP);

                }
            }
        });

        passInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    passInput.getBackground().setColorFilter(Color.parseColor("#66FFFFFF"), PorterDuff.Mode.SRC_ATOP);

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



    public boolean isEmailValid(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void validateText(){
        for (int i = 0; i < edtxtArr.length; i++){
            if (edtxtArr[i].getText().toString().isEmpty()){
                edtxtArr[i].getBackground().setColorFilter(Color.parseColor("#f405fd"), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }


}
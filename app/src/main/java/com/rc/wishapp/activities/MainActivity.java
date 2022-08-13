package com.rc.wishapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rc.wishapp.processing.ErrConnection;
import com.rc.wishapp.processing.HiddenKeyboard;
import com.rc.wishapp.R;
import com.rc.wishapp.processing.ShowToast;
import com.rc.wishapp.requests.Requests;


import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    private Object HttpURLConnection;
    public String responsAuth;
    public static String accessToken;
    public static String refreshToken;
    String tokens;
    StringBuilder stringBuilder = new StringBuilder();
    JSONObject jsonObj = null;

    public String access_token;
    public String refresh_token;


    public String errMessage;
    final String SAVED_TEXT = "saved_login";
    final String SAVED_PASS = "saved_pass";
    public static EditText Login_input;
    public static EditText Pass_input;



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
            ShowToast.showToast("Нет соединения", MainActivity.this);
        }




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
                    case MotionEvent.ACTION_DOWN:
                        startTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
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




        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(alpha);
                HiddenKeyboard.hideKeyboard(MainActivity.this);
                System.out.println(isEmailValid(Login_input.getText().toString()));
                if (Login_input.getText().toString().isEmpty() && Pass_input.getText().toString().isEmpty()) {
                        Login_input.getBackground().setColorFilter(Color.parseColor("#f405fd"), PorterDuff.Mode.SRC_ATOP);
                        Pass_input.getBackground().setColorFilter(Color.parseColor("#f405fd"), PorterDuff.Mode.SRC_ATOP);
                    ShowToast.showToast("Заполните поля", MainActivity.this);
                }
                else if (isEmailValid(Login_input.getText().toString()) == false){
                    ShowToast.showToast("Email введен не верно", MainActivity.this);
                }
                else if (Pass_input.getText().toString().isEmpty()){
                    Pass_input.getBackground().setColorFilter(Color.parseColor("#f405fd"), PorterDuff.Mode.SRC_ATOP);
                    ShowToast.showToast("Введите пароль", MainActivity.this);
                }
                else if (Pass_input.getText().length() < 6){
                    Pass_input.getBackground().setColorFilter(Color.parseColor("#f405fd"), PorterDuff.Mode.SRC_ATOP);
                    ShowToast.showToast("Пароль должен быть больше 6 символов", MainActivity.this);
                }
                else if (Login_input.getText().toString().isEmpty()){
                    Login_input.getBackground().setColorFilter(Color.parseColor("#f405fd"), PorterDuff.Mode.SRC_ATOP);
                    ShowToast.showToast("Введите mail", MainActivity.this);
                }
                else if (Login_input.getText().toString().isEmpty() == false && Pass_input.getText().toString().isEmpty() == false){

//                    postAuth(Login_input.getText().toString(), Pass_input.getText().toString());
                    Requests.postAuth(Login_input.getText().toString(), Pass_input.getText().toString(), MainActivity.this, MainActivity.this, WishList_activity.class);
//                    System.out.println(tokens);
//                    postAuthV2(Login_input.getText().toString(), Pass_input.getText().toString());

                } else if(errMessage.equals("1001")){
                    Login_input.getBackground().setColorFilter(Color.parseColor("#f405fd"), PorterDuff.Mode.SRC_ATOP);
                    Pass_input.getBackground().setColorFilter(Color.parseColor("#f405fd"), PorterDuff.Mode.SRC_ATOP);
                    ShowToast.showToast("Логин или пароль не верные", MainActivity.this);
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


    public boolean isEmailValid(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}




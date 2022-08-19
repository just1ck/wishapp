package com.rc.wishapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rc.wishapp.R;
import com.rc.wishapp.processing.ShowToast;
import com.rc.wishapp.requests.Requests;

public class ResetPassword extends AppCompatActivity {

    private TextView passInput;
    private Button sendLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        passInput = findViewById(R.id.reset_mail_input);
        sendLink = findViewById(R.id.reset_pass_button);


        sendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isEmailValid(passInput.getText().toString()) == false) {
                    ShowToast.showToast("Email введен не верно", ResetPassword.this);
                } else if(passInput.getText().toString().isEmpty() == true) {
                    passInput.getBackground().setColorFilter(Color.parseColor("#f405fd"), PorterDuff.Mode.SRC_ATOP);
                    ShowToast.showToast("Поле не может быть пустым", ResetPassword.this);
                } else if (passInput.getText().toString().isEmpty() != true ){
                    String passInputText = passInput.getText().toString();
                    Requests.requestChangePassword(ResetPassword.this, ResetPassword.this, MainActivity.class, passInputText);
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ResetPassword.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slidinrev, R.anim.slideoutrev);
        finish();
    }

    public boolean isEmailValid(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
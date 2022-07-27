package com.rc.wishapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class proffile extends AppCompatActivity {

    public ImageView logoutButton;
    public Logout logout;
    public RefreshTokens refreshTokens;
    final String acT = "access_token";
    final String rfT = "refresh_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proffile);
        final Animation alpha = AnimationUtils.loadAnimation(this, R.anim.button_anim);

        logoutButton = findViewById(R.id.logout_Button);

        logout = new Logout();
        refreshTokens = new RefreshTokens();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(alpha);
                System.out.println(myApp.getAccess(proffile.this, rfT, acT));
                logout.logout(proffile.this);
                myApp.setTokens(null, null, proffile.this, rfT, acT);
                Intent mainIntent = new Intent(proffile.this, MainActivity.class);
                proffile.this.startActivity(mainIntent);
                proffile.this.finish();
                overridePendingTransition(R.anim.slidinrev, R.anim.slideoutrev);

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(proffile.this, wishList.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        finish();

    }
}
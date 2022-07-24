package com.rc.wishapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class wishList extends AppCompatActivity {
    public Button logout_btn;
    public Logout logout;
    public RefreshTokens refreshTokens;
    final String acT = "access_token";
    final String rfT = "refresh_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        logout = new Logout();
        refreshTokens = new RefreshTokens();

        logout_btn = findViewById(R.id.logout_button);

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(myApp.getAccess(wishList.this, rfT, acT));
                logout.logout(wishList.this);
                myApp.setTokens(null, null, wishList.this, rfT, acT);
                Intent mainIntent = new Intent(wishList.this, MainActivity.class);
                wishList.this.startActivity(mainIntent);
                wishList.this.finish();
                overridePendingTransition(R.anim.slidinrev, R.anim.slideoutrev);


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
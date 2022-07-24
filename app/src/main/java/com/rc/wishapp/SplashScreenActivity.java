package com.rc.wishapp;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.widget.ProgressBar;

import org.json.JSONException;

public class SplashScreenActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    public RefreshTokens rfTokens;
    final String acT = "access_token";
    final String rfT = "refresh_token";
    public String access;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
         rfTokens = new RefreshTokens();
         if (myApp.getAccess(SplashScreenActivity.this, rfT, acT ) != null && myApp.getRefresh(SplashScreenActivity.this, rfT, acT ) != null){
             rfTokens.refresh_tokens(SplashScreenActivity.this);
         }





        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (myApp.getAccess(SplashScreenActivity.this, rfT, acT).isEmpty() == (false && myApp.getRefresh(SplashScreenActivity.this, rfT, acT).isEmpty() == false)){
                    try {
                        VerifyMail.DecodeJWT(myApp.getAccess(SplashScreenActivity.this, rfT, acT));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (myApp.getAccess(SplashScreenActivity.this, rfT, acT ) == null && myApp.getRefresh(SplashScreenActivity.this, rfT, acT ) == null){
                    Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                    SplashScreenActivity.this.finish();
                    overridePendingTransition(R.anim.slidein, R.anim.slideout);
                }else if (myApp.getAccess(SplashScreenActivity.this, rfT, acT ) != null && myApp.getRefresh(SplashScreenActivity.this, rfT, acT ) != null){
                    if (VerifyMail.boolMailVerify()){
                        Intent mainIntent = new Intent(SplashScreenActivity.this, wishList.class);
                        SplashScreenActivity.this.startActivity(mainIntent);
                        SplashScreenActivity.this.finish();
                        overridePendingTransition(R.anim.slidein, R.anim.slideout);
                    } else if (VerifyMail.boolMailVerify() == false){
                        Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        SplashScreenActivity.this.startActivity(mainIntent);
                        SplashScreenActivity.this.finish();
                        overridePendingTransition(R.anim.slidein, R.anim.slideout);
                    }
                }




            }
        }, SPLASH_DISPLAY_LENGTH);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }
}
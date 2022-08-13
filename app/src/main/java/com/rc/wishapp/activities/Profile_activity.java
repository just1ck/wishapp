package com.rc.wishapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.rc.wishapp.R;
import com.rc.wishapp.memory_methods.myApp;
import com.rc.wishapp.requests.Requests;

public class Profile_activity extends AppCompatActivity {

    public ImageView logoutButton;
    final String acT = "access_token";
    final String rfT = "refresh_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proffile);
        final Animation alpha = AnimationUtils.loadAnimation(this, R.anim.button_anim);

        logoutButton = findViewById(R.id.logout_Button);




        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(alpha);
                System.out.println(myApp.getAccess(Profile_activity.this, rfT, acT));
                Requests.logout(Profile_activity.this);
                myApp.setTokens(null, null, Profile_activity.this, rfT, acT);
                Intent mainIntent = new Intent(Profile_activity.this, MainActivity.class);
                Profile_activity.this.startActivity(mainIntent);
                Profile_activity.this.finish();
                overridePendingTransition(R.anim.slidinrev, R.anim.slideoutrev);

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Profile_activity.this, WishList_activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slidein, R.anim.slideout);
        finish();

    }
}
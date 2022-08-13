package com.rc.wishapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.rc.wishapp.R;
import com.rc.wishapp.processing.ShowToast;

public class WishList_activity extends AppCompatActivity {
    public ImageView proffileBtn;
    final String acT = "access_token";
    final String rfT = "refresh_token";
    public ScrollView wishView;
    public Button createBtn;
    public ImageView photo;
    private static final int REQUEST_TAKE_PHOTO = 1;
    public Button createButtonDialog;
    public EditText wishName;
    public boolean takedPhoto;
    public Animation alpha;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        alpha = AnimationUtils.loadAnimation(this, R.anim.button_anim);

        takedPhoto = false;



        createBtn = findViewById(R.id.createWish);
        proffileBtn = findViewById(R.id.profiilePage);
        wishView = findViewById(R.id.wishScroll);






        proffileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(alpha);
                Intent intent = new Intent(WishList_activity.this, Profile_activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slidinrev, R.anim.slideoutrev);
                finish();

            }
        });


        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        //logout_btn = findViewById(R.id.logout_button);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void showDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(WishList_activity.this);
        final View dialogView = getLayoutInflater().inflate(R.layout.create_dialog, null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.show();

        photo = dialogView.findViewById(R.id.take_photo);
        createButtonDialog = dialogView.findViewById(R.id.createButtonDialog);

        wishName = dialogView.findViewById(R.id.wishName);
        wishName.getBackground().setColorFilter(Color.parseColor("#66FFFFFF"), PorterDuff.Mode.SRC_ATOP);

        wishName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wishName.getBackground().setColorFilter(Color.parseColor("#66FFFFFF"), PorterDuff.Mode.SRC_ATOP);
            }
        });

        createButtonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(alpha);
                if (wishName.getText().toString().isEmpty() && takedPhoto == false){
                    wishName.getBackground().setColorFilter(Color.parseColor("#f405fd"), PorterDuff.Mode.SRC_ATOP);
                    ShowToast.showToast("Заполните обязательные поля", WishList_activity.this);
                }else if (wishName.getText().toString().isEmpty()){
                    wishName.getBackground().setColorFilter(Color.parseColor("#f405fd"), PorterDuff.Mode.SRC_ATOP);
                    ShowToast.showToast("Введите название", WishList_activity.this);
                } else if (takedPhoto == false){
                    ShowToast.showToast("Прикрепите фотографию", WishList_activity.this);
                } else if (wishName.getText().toString().isEmpty() == false && takedPhoto == true){
                    dialog.dismiss();
                }
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(alpha);
                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try{
                    startActivityForResult(takePhotoIntent, REQUEST_TAKE_PHOTO);
                }catch (ActivityNotFoundException e){
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap thumbnailBitmap = (Bitmap) extras.get("data");
            photo.setImageBitmap(thumbnailBitmap);
            takedPhoto = true;
        }
    }

}
package com.rc.wishapp.processing;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rc.wishapp.R;

public class ShowToast {

    public static void showToast(String uText, Activity fActivity){
        LayoutInflater inflater = fActivity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.empty_name, fActivity.findViewById(R.id.toastLayout));

        TextView text = layout.findViewById(R.id.errTextView);
        text.setText(uText);
        Toast toast = new Toast(fActivity.getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 30);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}

package com.rc.wishapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.EditText;
import android.widget.Toast;

public class myApp {
    public SharedPreferences sPref;
    public static void saveText(Context context, String SAVED_TEXT, String SAVED_PASS, EditText Login_input, EditText Pass_input ){
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(SAVED_TEXT, Login_input.getText().toString());
        ed.putString(SAVED_PASS, Pass_input.getText().toString());
        ed.commit();

    }

    public static void setTokens(String access_tokenIn, String refresh_tokenIn, Context context, String rfT, String acT){
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(rfT, refresh_tokenIn);
        ed.putString(acT, access_tokenIn);
        ed.commit();

    }

    public static void loadText(Context context, EditText Login_input, EditText Pass_input, String SAVED_TEXT, String SAVED_PASS) {
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);
        String savedLogin = sPref.getString(SAVED_TEXT, "");
        String savedPass = sPref.getString(SAVED_PASS, "");
        Login_input.setText(savedLogin);
        Pass_input.setText(savedPass);
    }

    public static void loadTokens(Context context, String rfT, String acT, String retutnTokenRefresh, String retutnTokenAccess){
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);;
        String refreshTokenPref = sPref.getString(rfT, "");
        String acessTokenPref = sPref.getString(acT, "");
        retutnTokenAccess = acessTokenPref;
        retutnTokenRefresh = refreshTokenPref;
    }

    public static String getRefresh(Context context, String rfT, String acT){
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);;
        String refreshTokenPref = sPref.getString(rfT, "");
        return refreshTokenPref;
    }

    public static String getAccess(Context context, String rfT, String acT){
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);
        String acessTokenPref = sPref.getString(acT, "");
        return acessTokenPref;
    }

    public static void saveRegText(Context context, EditText nameInput, EditText mailInput, EditText passInput, EditText repeatPassInput, String loginText, String mailText, String passText, String repeatPassText){
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(loginText, nameInput.getText().toString());
        ed.putString(mailText, mailInput.getText().toString());
        ed.putString(passText, passInput.getText().toString());
        ed.putString(repeatPassText, repeatPassInput.getText().toString());
        ed.commit();
    }

    public static void loadRegText(Context context, EditText nameInput, EditText mailInput, EditText passInput, EditText repeatPassInput, String loginText, String mailText, String passText, String repeatPassText){
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);
        String savedLogin = sPref.getString(loginText, "");
        String savedMail = sPref.getString(mailText, "");
        String savedPass = sPref.getString(passText, "");
        String savedRepeatPass = sPref.getString(repeatPassText, "");
        nameInput.setText(savedLogin);
        mailInput.setText(savedMail);
        passInput.setText(savedPass);
        repeatPassInput.setText(savedRepeatPass);

    }

    public static String getEmail(Context context, String mailText){
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);
        String email = sPref.getString(mailText, "");
        return email;
    }

    public static String getPassword(Context context, String passText){
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);
        String pass = sPref.getString(passText, "");
        return pass;
    }

    public static String getName(Context context, String loginText){
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);
        String name = sPref.getString(loginText, "");
        return name;
    }




}

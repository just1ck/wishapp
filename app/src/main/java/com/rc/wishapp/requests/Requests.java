package com.rc.wishapp.requests;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rc.wishapp.R;
import com.rc.wishapp.memory_methods.myApp;
import com.rc.wishapp.processing.ShowToast;
import com.rc.wishapp.processing.Tokens;
import com.rc.wishapp.processing.VerifyMail;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Requests {


    private static Tokens tokensClass = new Tokens();
    private static String allObjects;
    public static JSONObject jsonObj = null;
    final static String acT = "access_token";
    final static String rfT = "refresh_token";
    public static String access;
    public static String tokens;
    public static String errorCode;
    public static String errMessage;
    public static JSONObject errObject = null;
    public static String retutnTokenAccess;
    public static String retutnTokenRefresh;
    public static String access_token;
    public static String refresh_token;
    public static Context context;



    public static String oldrefreshToken;


    private void setRefresh_token(String ACtok, String oldrefreshToken){//setTokens
        this.oldrefreshToken = oldrefreshToken;
        context = context.getApplicationContext();
    }

    private static void loadToks(){//loadTokens
        myApp.loadTokens(context.getApplicationContext(), rfT, acT, retutnTokenRefresh, retutnTokenAccess);
    }

    private static String getRefreshToken(){//getRefresh
        return refresh_token;
    }

    private static String getAcessToken(){//getAccess
        return access_token;
    }

    public static void refresh_tokens(Context context){


        System.out.println(tokensClass.access());
        System.out.println(tokensClass.refresh());
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = "https://api.wishapp.ru/auth/refresh";
        JSONObject jsonBody = new JSONObject();


        try {
            jsonBody.put("refresh_token", myApp.getRefresh(context.getApplicationContext(), rfT, acT));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();



        StringRequest stringRequest = new StringRequest(Request.Method.POST,  URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                oldrefreshToken = tokensClass.refresh();
                allObjects = response.toString();
                try {
                    jsonObj = new JSONObject(allObjects);
                    String ACtok = jsonObj.getString("access_token");
                    String RFtok = jsonObj.getString("refresh_token");
//                    access_token = ACtok;
//                    refresh_token = RFtok;
                    myApp.setTokens(ACtok, RFtok, context.getApplicationContext(), rfT, acT);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }


            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-api-version", "1.0");
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        responseString = String.valueOf(jsonString);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        requestQueue.add(stringRequest);
    }//refresh

    public static void logout(Context context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = "https://api.wishapp.ru/auth/logout";
        JSONObject jsonBody = new JSONObject();
        access = myApp.getAccess(context.getApplicationContext(), rfT, acT);


        try {
            jsonBody.put("refresh_token", myApp.getRefresh(context.getApplicationContext(), rfT, acT));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();



        StringRequest stringRequest = new StringRequest(Request.Method.POST,  URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                allObjects = response.toString();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }


            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-api-version", "1.0");
                params.put("Authorization", "Bearer " + access);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        responseString = String.valueOf(jsonString);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        requestQueue.add(stringRequest);
    }//logout

    public static void postAuth(String Login_input, String Pass_input, Context context, Activity activityFirst, Class activitySecond) {

        final Animation alpha = AnimationUtils.loadAnimation(context, R.anim.button_anim);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = "https://api.wishapp.ru/auth/signin";
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("email", Login_input);
            jsonBody.put("password", Pass_input);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST,  URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                tokens = response.toString();


                try {
                    jsonObj = new JSONObject(tokens);
                    String ACtok = jsonObj.getString("access_token");
                    String RFtok = jsonObj.getString("refresh_token");
                    myApp.setTokens(ACtok, RFtok, context, rfT, acT);
                    VerifyMail.DecodeJWT(myApp.getAccess(context, rfT, acT));
                    if (VerifyMail.boolMailVerify() == true){
                        ShowToast.showToast("Вы вторизированы!", activityFirst);

                        Intent intent = new Intent(activityFirst, activitySecond);
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.slidein, R.anim.slideout);
                        activityFirst.finish();
                    } else if (VerifyMail.boolMailVerify() == false){
                        ShowToast.showToast("Подтвердите электронную почту!", activityFirst);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    String jsonErrString = new String(error.networkResponse.data,
                            HttpHeaderParser.parseCharset(error.networkResponse.headers));
                    errorCode = jsonErrString;
                    errObject = new JSONObject(errorCode);
                    int errs = errObject.getInt("code");
//                    errMessage = errs;
                    System.out.println(jsonErrString);
                    if (errs == 1001){
                        ShowToast.showToast("Логин или пароль не верны!", activityFirst);

                    } else if (VerifyMail.boolMailVerify() == false){
                        ShowToast.showToast("Ваша электронная почта не подтверждена!", activityFirst);
                    }
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }


            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-api-version", "1.0");
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        responseString = String.valueOf(jsonString);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        requestQueue.add(stringRequest);
    }//Auth

    public static void registration(Context context, Activity activityFirst, Class activitySecond, String mailTextR, String passTextR, String loginTextR) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = "https://api.wishapp.ru/auth/signup";
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("email", myApp.getEmail(context.getApplicationContext(), mailTextR));
            jsonBody.put("password", myApp.getPassword(context.getApplicationContext(), passTextR));
            jsonBody.put("name", myApp.getName(context.getApplicationContext(), loginTextR));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                allObjects = response.toString();
                postAuth(myApp.getEmail(context, mailTextR), myApp.getPassword(context, passTextR), context, activityFirst, activitySecond);
                Intent intent = new Intent(activityFirst, activitySecond);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.slideout, R.anim.slidein);
                activityFirst.finish();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String jsonErrString = null;
                try {
                    jsonErrString = new String(error.networkResponse.data,
                            HttpHeaderParser.parseCharset(error.networkResponse.headers));
                    errorCode = jsonErrString;
                    errObject = new JSONObject(errorCode);
                    int errs = errObject.getInt("code");
                    if (errs == 1003){
                        ShowToast.showToast("Данный Email уже зарегистрирован", activityFirst);

                    }
                    System.out.println(jsonErrString);

                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }


            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-api-version", "1.0");
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        responseString = String.valueOf(jsonString);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        requestQueue.add(stringRequest);
    }//Registration

    public static void postVerify(String verifyCode, Context context, Activity activityFirst, Class activitySecond) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = "https://api.wishapp.ru/auth/email/verify";
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("code", verifyCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String requestBody = jsonBody.toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST,  URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(activityFirst, activitySecond);
                activityFirst.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.slidinrev, R.anim.slideoutrev);
                activityFirst.finish();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String jsonErrString = null;
                try {
                    jsonErrString = new String(error.networkResponse.data,
                            HttpHeaderParser.parseCharset(error.networkResponse.headers));
                    errorCode = jsonErrString;
                    errObject = new JSONObject(errorCode);
                    int errs = errObject.getInt("code");
                    if (errs == 1002){
                        ShowToast.showToast("Неправильный код", activityFirst);
                    }else if (errs == 1004){
                        ShowToast.showToast("Email уже подтвержден", activityFirst);
                    }else {
                        ShowToast.showToast("Правильный код", activityFirst);
                    }
                    System.out.println(jsonErrString);

                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }

            }

        }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }


            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-api-version", "1.0");
                params.put("Authorization", "Bearer " + myApp.getAccess(context, rfT, acT));
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        responseString = String.valueOf(jsonString);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        requestQueue.add(stringRequest);
    }//VerifyEmail

    public static void sendMessage(Context context) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String URL = "https://api.wishapp.ru/auth/email/resend";
        JSONObject jsonBody = new JSONObject();


        final String requestBody = jsonBody.toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST,  URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }


            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("x-api-version", "1.0");
                params.put("Authorization", "Bearer " + myApp.getAccess(context, rfT, acT));
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        responseString = String.valueOf(jsonString);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        requestQueue.add(stringRequest);
    }//resend
}

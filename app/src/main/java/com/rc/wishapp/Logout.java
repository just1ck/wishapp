package com.rc.wishapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Logout {
    private Tokens tokens = new Tokens();
    private String allObjects;
    public JSONObject jsonObj = null;
    final String acT = "access_token";
    final String rfT = "refresh_token";
    public String access;

    Context context;



    public void logout(Context context){
        System.out.println(tokens.access());
        System.out.println(tokens.refresh());
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
    }
}

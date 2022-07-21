package com.rc.wishapp;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import org.json.XMLTokener;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import static com.rc.wishapp.MainActivity.refreshToken;


public class RefreshTokens<sPre> {
    private Tokens tokens = new Tokens();
    private String allObjects;
    public JSONObject jsonObj = null;
    public String access_token;
    public String refresh_token;
    public SharedPreferences sPre;
    final String acT = "access_token";
    final String rfT = "refresh_token";
    public String retutnTokenAccess;
    public String retutnTokenRefresh;
    Context context;


    public String oldrefreshToken;

    public void setRefresh_token(String ACtok, String oldrefreshToken){
        this.oldrefreshToken = oldrefreshToken;
        context= context.getApplicationContext();
    }

    public void loadToks(){
        myApp.loadTokens(context.getApplicationContext(), rfT, acT, retutnTokenRefresh, retutnTokenAccess);
    }

    public String getRefreshToken(){
        return refresh_token;
    }

    public String getAcessToken(){
        return access_token;
    }

    public void refresh_tokens(Context context){
        System.out.println(tokens.access());
        System.out.println(tokens.refresh());
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
                oldrefreshToken = tokens.refresh();
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
    }

}

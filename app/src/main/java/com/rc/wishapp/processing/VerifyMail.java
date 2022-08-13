package com.rc.wishapp.processing;


import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

public class VerifyMail {
    public static JSONObject jsonObj;
    public static boolean verify;
    public static boolean emailVerify;
    public static void DecodeJWT(String token) throws JSONException {
        String jwtToken = token;
        System.out.println("------------ Decode JWT ------------");
        String[] split_string = jwtToken.split("\\.");
        String base64EncodedHeader = split_string[0];
        String base64EncodedBody = split_string[1];
        String base64EncodedSignature = split_string[2];

        System.out.println("~~~~~~~~~ JWT Header ~~~~~~~");
        Base64 base64Url = new Base64(true);
        String header = new String(base64Url.decode(base64EncodedHeader));
        System.out.println("JWT Header : " + header);



        System.out.println("~~~~~~~~~ JWT Body ~~~~~~~");
        String body = new String(base64Url.decode(base64EncodedBody));
        jsonObj = new JSONObject(body);
        verify = jsonObj.getBoolean("emailVerified");
        System.out.println("JWT Body : "+ verify);
    }

    public static boolean boolMailVerify(){
        if (verify){
            return true;
        }else {
            return false;
        }
    }
}

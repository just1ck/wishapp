package com.rc.wishapp;

public class Tokens {
    private String access_token;
    private String refresh_token;

    public String refresh(){
        return refresh_token;
    }

    public String access(){
        return access_token;
    }

    public void setTokens(String access_token, String refresh_token){
        this.access_token = access_token;
        this.refresh_token = refresh_token;
    }


}

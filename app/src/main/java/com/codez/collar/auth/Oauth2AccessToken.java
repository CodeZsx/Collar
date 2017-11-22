package com.codez.collar.auth;

import android.os.Bundle;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by codez on 2017/11/19.
 * Description:
 */

public class Oauth2AccessToken {
    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String KEY_EXPIRES_IN = "expires_in";
    public static final String KEY_REFRESH_TOKEN = "refresh_token";
    public static final String KEY_UID = "uid";
    public static final String KEY_PHONE_NUM = "phone_num";

    private String accessToken = "";
    private long expiresIn = 0L;
    private String refreshToken = "";
    private String uid = "";
    private String phoneNum = "";



    public Oauth2AccessToken(String accessToken, long expiresIn, String refreshToken, String uid) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.uid = uid;
    }

    public Oauth2AccessToken(String accessToken, long expiresIn, String refreshToken, String uid, String phoneNum) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.uid = uid;
        this.phoneNum = phoneNum;
    }

    //从字符串中解析出accessToken
    public static Oauth2AccessToken parseAccessToken(String responseText) {
        if (responseText != null && responseText.indexOf("{") >= 0) {
            try{
                JSONObject json = new JSONObject(responseText);
                Oauth2AccessToken token = new Oauth2AccessToken(json.optString(KEY_ACCESS_TOKEN),
                        getExpiresInTime(json.optString(KEY_EXPIRES_IN)),
                        json.optString(KEY_REFRESH_TOKEN),
                        json.optString(KEY_UID),
                        json.getString(KEY_PHONE_NUM));
                return token;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return null;
    }
    //从bundle中解析出accessToken
    public static Oauth2AccessToken parseAccessToken(Bundle bundle) {
        if (bundle != null) {
            return new Oauth2AccessToken(bundle.getString(KEY_ACCESS_TOKEN),
                    bundle.getLong(KEY_EXPIRES_IN),
                    bundle.getString(KEY_REFRESH_TOKEN),
                    bundle.getString(KEY_UID),
                    bundle.getString(KEY_PHONE_NUM));
        }
        return null;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ACCESS_TOKEN, this.accessToken);
        bundle.putLong(KEY_EXPIRES_IN, getExpiresIn());
        bundle.putString(KEY_REFRESH_TOKEN, getRefreshToken());
        bundle.putString(KEY_UID, getUid());
        bundle.putString(KEY_PHONE_NUM, getPhoneNum());
        return bundle;
    }

    //返回当前是否为登录状态
    public boolean isSessionsValid() {
        return !TextUtils.isEmpty(this.accessToken);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
    //拼接出结束时间
    public static long getExpiresInTime(String expiresIn){
        if (!TextUtils.isEmpty(expiresIn) && !expiresIn.equals("0")) {
            return System.currentTimeMillis()+Long.parseLong(expiresIn)*1000L;
        }
        return 0L;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public String toString() {
        return "Oauth2AccessToken{" +
                "accessToken='" + accessToken + '\'' +
                ", expiresIn=" + expiresIn +
                ", refreshToken='" + refreshToken + '\'' +
                ", uid='" + uid + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                '}';
    }
}

package com.codez.collar.bean;

/**
 * Created by codez on 2017/11/19.
 * Description:
 */

public class Token {
    private String token;
    private String expiresIn;
    private String refreshToken;
    private String uid;

    public Token(String token, String expiresIn, String refreshToken, String uid) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
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
}

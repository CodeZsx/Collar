package com.codez.collar.bean;

/**
 * Created by codez on 2017/12/3.
 * Description:
 */

public class PostParam {
    private String access_token;
    private String status;

    public PostParam(String access_token, String status) {
        this.access_token = access_token;
        this.status = status;
    }
}

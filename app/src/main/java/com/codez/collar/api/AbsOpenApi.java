package com.codez.collar.api;

import android.content.Context;

import com.codez.collar.auth.Oauth2AccessToken;

/**
 * Created by codez on 2017/11/20.
 * Description:
 */

public abstract class AbsOpenApi {
    private static final String TAG = AbsOpenApi.class.getName();
    /**
     * sina微博接口地址
     */
    protected static final String API_SERVER = "https://api.weibo.com/2";
    /**
     * POST 请求方式
     */
    protected static final String METHOOD_POST = "POST";
    /**
     * GET 请求方式
     */
    protected static final String METHOOD_GET = "GET";
    /**
     * HTTP 参数
     */
    protected static final String ACCESS_TOKEN = "access_token";


    protected Context mContext;
    protected String mAppKey;
    protected Oauth2AccessToken mAccessToken;

    public AbsOpenApi(Context mContext, String mAppKey, Oauth2AccessToken mAccessToken) {
        this.mContext = mContext;
        this.mAppKey = mAppKey;
        this.mAccessToken = mAccessToken;
    }

}

package com.codez.collar.bean;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.codez.collar.auth.AccessTokenKeeper;
import com.codez.collar.utils.L;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by codez on 2017/12/3.
 * Description:
 */

public class HttpParameters {

    private static final String DEFAULT_CHARSET = "UTF-8";
    private LinkedHashMap<String, Object> mParams = new LinkedHashMap<>();


    public HttpParameters() {
    }

    public void setTextStatusParams(String status){
        this.put("access_token", AccessTokenKeeper.getInstance().getAccessToken());
        this.put("status", status);
    }

    public void put(String key, String val) {
        this.mParams.put(key, val);
    }

    public void put(String key, int value) {
        this.mParams.put(key, String.valueOf(value));
    }

    public void put(String key, long value) {
        this.mParams.put(key, String.valueOf(value));
    }

    public void put(String key, Bitmap bitmap) {
        this.mParams.put(key, bitmap);
    }

    public void put(String key, Object val) {
        this.mParams.put(key, val.toString());
    }

    public String urlencode() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        Iterator var4 = this.mParams.keySet().iterator();
        while (var4.hasNext()) {
            String key = (String) var4.next();
            if (first) {
                first = false;
            }else{
                sb.append("&");
            }
            Object value = this.mParams.get(key);
            if (value instanceof String){
                String param = (String) value;
                if (!TextUtils.isEmpty(param)) {
                    try {
                        sb.append(URLEncoder.encode(key, DEFAULT_CHARSET) + "=" + URLEncoder.encode(param, DEFAULT_CHARSET));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        L.e(e.toString());
                    }
                }
                L.e("urlencode:" + sb.toString());
            }
        }
        return sb.toString();
    }
}

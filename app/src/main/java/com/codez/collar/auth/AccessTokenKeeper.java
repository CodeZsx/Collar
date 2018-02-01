package com.codez.collar.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.codez.collar.Config;
import com.codez.collar.manager.ApplicationContext;

/**
 * Created by codez on 2017/11/19.
 * Description:
 */

public class AccessTokenKeeper {
    private static final String PREFERENCES_NAME = Config.PackageName;

    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_EXPIRES_IN = "expires_in";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_UID = "uid";

    private Context mContext;
    private SharedPreferences mAccountPref;

    public AccessTokenKeeper() {
        mContext = ApplicationContext.get();
        init();
    }

    private void init() {
        mAccountPref = mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
    private static class Loader {
        static AccessTokenKeeper INSTANCE = new AccessTokenKeeper();
    }

    public static AccessTokenKeeper getInstance() {
        return Loader.INSTANCE;
    }

    //将token信息写入SharedPreferences
    public void writeAccessToken(Oauth2AccessToken token) {
        if (mAccountPref == null || token == null) {
            return;
        }
        SharedPreferences.Editor editor = mAccountPref.edit();
        editor.putString(KEY_ACCESS_TOKEN, token.getAccessToken());
        editor.putLong(KEY_EXPIRES_IN, token.getExpiresIn());
        editor.putString(KEY_REFRESH_TOKEN, token.getRefreshToken());
        editor.putString(KEY_UID, token.getUid());
        editor.commit();
    }

    //从SharedPreferences读取token信息
    public Oauth2AccessToken readAccessToken() {
        if (mAccountPref == null) {
            return null;
        }
        return new Oauth2AccessToken(mAccountPref.getString(KEY_ACCESS_TOKEN,""),
                mAccountPref.getLong(KEY_EXPIRES_IN, 0L),
                mAccountPref.getString(KEY_REFRESH_TOKEN, ""),
                mAccountPref.getString(KEY_UID, ""));
    }

    public String getAccessToken() {
        if (mAccountPref == null) {
            return null;
        }
        return mAccountPref.getString(KEY_ACCESS_TOKEN, "");
    }

    public String getUid() {
        if (mAccountPref == null) {
            return null;
        }
        return mAccountPref.getString(KEY_UID, "");
    }
    //清除SharedPreferences中的token信息
    public void clear() {
        if (mAccountPref == null) {
            return;
        }

        SharedPreferences.Editor editor = mAccountPref.edit();
        editor.clear();
        editor.commit();

    }
}

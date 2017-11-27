package com.codez.collar.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.codez.collar.Config;

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

    //将token信息写入SharedPreferences
    public static void writeAccessToken(Context context, Oauth2AccessToken token) {
        if (context == null || token == null) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE).edit();
        editor.putString(KEY_ACCESS_TOKEN, token.getAccessToken());
        editor.putLong(KEY_EXPIRES_IN, token.getExpiresIn());
        editor.putString(KEY_REFRESH_TOKEN, token.getRefreshToken());
        editor.putString(KEY_UID, token.getUid());
        editor.commit();
    }

    //从SharedPreferences读取token信息
    public static Oauth2AccessToken readAccessToken(Context context) {
        if (context == null) {
            return null;
        }
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return new Oauth2AccessToken(pref.getString(KEY_ACCESS_TOKEN,""),
                pref.getLong(KEY_EXPIRES_IN, 0L),
                pref.getString(KEY_REFRESH_TOKEN, ""),
                pref.getString(KEY_UID, ""));
    }

    public static String getAccessToken(Context context) {
        if (context == null) {
            return null;
        }
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString(KEY_ACCESS_TOKEN, "");
    }

    public static String getUid(Context context) {
        if (context == null) {
            return null;
        }
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString(KEY_UID, "");
    }
    //清除SharedPreferences中的token信息
    public static void clear(Context context) {
        if (context == null) {
            return;
        }

        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();

    }
}

package com.codez.collar;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by codez on 2017/11/19.
 * Description:
 */

public class Config {
    //weico
//    public static final String APP_KEY = "211160679";
//    public static final String APP_SECRET = "1e6e33db08f9192306c4afa0a61ad56c";
//    public static final String REDIRECT_URL = "http://oauth.weico.cc";
    //welike
    public static final String APP_KEY = "1074487424";
    public static final String APP_SECRET = "99039231ce8713c344b77076d1e2a827";
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    //collar
//    public static final String APP_KEY = "2873958404";
//    public static final String APP_SECRET = "48054075274e02cc513b4c8cf4131e4a";
//    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

//    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write";
    public static final String SCOPE = "direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,invitation_write";

    public static final String PackageName = "com.codez.collar";

    public static final String PREF_NAME_NIGHT_MODE = "night_mode";


    //获取
    public static boolean getCachedNight(Context context) {
        return context.getSharedPreferences(PREF_NAME_NIGHT_MODE, Context.MODE_PRIVATE).getBoolean("night", false);
    }
    //缓存
    public static void cacheNight(Context context,boolean isNight) {
        SharedPreferences.Editor e = context.getSharedPreferences(PREF_NAME_NIGHT_MODE, Context.MODE_PRIVATE).edit();
        e.putBoolean("night", isNight);
        e.commit();
    }
    //获取
    public static String getCachedTheme(Context context) {
        return context.getSharedPreferences(PREF_NAME_NIGHT_MODE, Context.MODE_PRIVATE).getString("theme", "a");
    }
    //缓存
    public static void cacheTheme(Context context,String theme) {
        SharedPreferences.Editor e = context.getSharedPreferences(PREF_NAME_NIGHT_MODE, Context.MODE_PRIVATE).edit();
        e.putString("theme", theme);
        e.commit();
    }
    //获取
    public static boolean getCachedFirstRun(Context context) {
        return context.getSharedPreferences(PREF_NAME_NIGHT_MODE, Context.MODE_PRIVATE).getBoolean("first_run", true);
    }
    //缓存
    public static void cacheFirstRun(Context context,boolean isFirstRun) {
        SharedPreferences.Editor e = context.getSharedPreferences(PREF_NAME_NIGHT_MODE, Context.MODE_PRIVATE).edit();
        e.putBoolean("first_run", isFirstRun);
        e.commit();
    }
}

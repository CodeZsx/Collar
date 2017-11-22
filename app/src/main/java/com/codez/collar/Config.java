package com.codez.collar;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by codez on 2017/11/19.
 * Description:
 */

public class Config {
    public static final String APP_KEY = "211160679";
    public static final String REDIRECT_URL = "http://oauth.weico.cc";
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write";

    public static final String AppSecret = "1e6e33db08f9192306c4afa0a61ad56c";
    public static final String PackageName = "com.codez.collar";

    public static final String PREF_NAME_NIGHT_MODE = "night_mode";


    //获取sex
    public static boolean getCachedNight(Context context) {
        return context.getSharedPreferences(PREF_NAME_NIGHT_MODE, Context.MODE_PRIVATE).getBoolean("night", false);
    }
    //缓存sex
    public static void cacheNight(Context context,boolean isNight) {
        SharedPreferences.Editor e = context.getSharedPreferences(PREF_NAME_NIGHT_MODE, Context.MODE_PRIVATE).edit();
        e.putBoolean("night", isNight);
        e.commit();
    }
}

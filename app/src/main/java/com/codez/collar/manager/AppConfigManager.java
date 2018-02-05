package com.codez.collar.manager;

/**
 * Created by codez on 2018/2/1.
 * Description:
 */

public class AppConfigManager {
    private static final String TAG = "UserManager";

    static class Loader {
        static final AppConfigManager INSTANCE = new AppConfigManager();
    }

    public static AppConfigManager getInstance() {
        return AppConfigManager.Loader.INSTANCE;
    }
}

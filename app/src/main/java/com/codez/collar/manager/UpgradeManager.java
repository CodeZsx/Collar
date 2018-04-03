package com.codez.collar.manager;

import com.codez.collar.BuildConfig;
import com.codez.collar.base.BaseApp;


/**
 * Created by codez on 2018/4/2.
 * Description:
 */

public enum  UpgradeManager {

    mInstance;
    private String mAppVersionName;
    private static final String TAG = "UpgradeManager";

    public static UpgradeManager getInstance() {
        return mInstance;
    }

    public String getAppVersionName() {
        if (BuildConfig.DEBUG) {
            return "0.0.0.180101";
        }
        if (mAppVersionName == null) {
            mAppVersionName = BaseApp.getAppVersionName();
        }
        return mAppVersionName;
    }
}

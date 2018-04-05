package com.codez.collar.manager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.codez.collar.BuildConfig;
import com.codez.collar.base.BaseApp;
import com.codez.collar.utils.SDCardUtil;

import java.io.File;


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
            return "0.0.0.001";
        }
        if (mAppVersionName == null) {
            mAppVersionName = BaseApp.getAppVersionName();
        }
        return mAppVersionName;
    }

    public String getApkPath(String apkPath) {
        return SDCardUtil.getAppCachePath() + "/" + apkPath + ".apk";
    }

    /**
     * 安装apk
     * @param path
     */
    public void installApk(Context context, String path) {
        File file = new File(path);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(BaseApp.sContext,
                    BaseApp.getAppPackageName() + ".fileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }
}

package com.codez.collar.manager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.codez.collar.BuildConfig;
import com.codez.collar.base.BaseApp;
import com.codez.collar.bean.UpgradeInfoBean;
import com.codez.collar.event.NewAppVersionEvent;
import com.codez.collar.event.ToastEvent;
import com.codez.collar.net.HttpUtils;
import com.codez.collar.ui.AppDialog;
import com.codez.collar.utils.EventBusUtils;
import com.codez.collar.utils.SDCardUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;


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

    /**
     * 开启app检查升级的任务
     * @param isActive 是否是主动查询
     * @param pgDialog
     */
    public void startCheckUpgradeTask(final boolean isActive, final AppDialog pgDialog) {
        long checkFrequence = 4 * 60 * 60;
        long delayTime = 0;//暂时不延迟启动
        Observable.interval(delayTime, checkFrequence, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "upgrade task delay start completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (pgDialog != null) {
                            pgDialog.dismiss();
                        }
                        Log.w(TAG, "check task error");
                        Log.w(TAG, "onError:" + ((e == null) ? "onError:" : "onError:" + e.toString()));
                        if (e != null) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(Long aLong) {
                        //从服务器检查更新信息
                        HttpUtils.getInstance().getUpgradeService()
                                .getUpgradeInfo()
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe(new Observer<UpgradeInfoBean>() {
                                    @Override
                                    public void onCompleted() {
                                        Log.i(TAG, "query upgrade info completed");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (pgDialog != null) {
                                            pgDialog.dismiss();
                                        }
                                        Log.w(TAG, "query upgrade info error");
                                        Log.w(TAG, "onError:" + ((e == null) ? "onError:" : "onError:" + e.toString()));
                                        if (isActive) {
                                            EventBusUtils.sendEvent(ToastEvent.newToastEvent("未知错误，操作失败"));
                                        }
                                    }

                                    @Override
                                    public void onNext(UpgradeInfoBean bean) {
                                        if (pgDialog != null) {
                                            pgDialog.dismiss();
                                        }
                                        if (bean == null && isActive) {
                                            EventBusUtils.sendEvent(ToastEvent.newToastEvent("未知错误，操作失败"));
                                            return;
                                        }
                                        Log.i(TAG, "info:" + bean.toString());
                                        if (bean.getVersion_name().compareTo(UpgradeManager.getInstance().getAppVersionName()) <= 0 && isActive) {
                                            EventBusUtils.sendEvent(ToastEvent.newToastEvent("已是最新版本"));
                                            return;
                                        } else {
                                            EventBusUtils.sendEvent(new NewAppVersionEvent(bean, NewAppVersionEvent.MANUAL));
                                            return;
                                        }
                                    }
                                });
                    }
                });
    }
}

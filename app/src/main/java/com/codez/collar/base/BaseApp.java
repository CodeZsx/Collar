package com.codez.collar.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.codez.collar.BuildConfig;
import com.codez.collar.Config;
import com.codez.collar.MainActivity;
import com.codez.collar.manager.ApplicationContext;
import com.codez.collar.service.CoreService;
import com.liulishuo.filedownloader.FileDownloader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import skin.support.SkinCompatManager;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;

/**
 * Created by codez on 2017/11/17.
 * Description:
 */

public class BaseApp extends Application {
    private static final String TAG = "BaseApp";
    public static volatile boolean isAppRunBackground = true;
    private static List<Activity> sActivityList = new LinkedList<>();

    public static Context sContext;

    public BaseApp() {
        super();
        sContext = this;
    }

    //默认是release
    private static boolean appDebug = false;

    public static boolean isAppDebug() {
        return appDebug;
    }

    private void initAppDebug() {
        appDebug = BuildConfig.DEBUG;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "oncreate");
        //开启核心服务
        startCoreService();
        //初始化换肤组件
        SkinCompatManager.withoutActivity(this)
                .addInflater(new SkinMaterialViewInflater())
                .addInflater(new SkinConstraintViewInflater())
                .addInflater(new SkinConstraintViewInflater())
                .setSkinStatusBarColorEnable(false)
                .setSkinWindowBackgroundEnable(false)
                .loadSkin();
        //应用初始运行
        if (Config.getCachedFirstRun(sContext)){
            SkinCompatManager.getInstance().loadSkin("a", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);
            Config.cacheTheme(sContext, "a");
            Config.cacheFirstRun(sContext, false);
        }
        //初始化文件下载组件
        FileDownloader.setup(this);
        Observable.timer(0, TimeUnit.SECONDS, Schedulers.newThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        initAppDebug();
                    }
                });

    }

    private void startCoreService() {
        startService(new Intent(this, CoreService.class));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ApplicationContext.getInstance().init(this);
        registerActivityLifecycleListener();
    }

    /**
     * 获取APP的数字版本号
     * @return
     */
    public static int getAppVersionCode() {
        try {
            PackageInfo info = sContext.getPackageManager()
                    .getPackageInfo(getAppPackageName(), PackageManager.GET_ACTIVITIES);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取APP的版本名
     * @return
     */
    public static String getAppVersionName() {
        try {
            PackageInfo info = sContext.getPackageManager()
                    .getPackageInfo(getAppPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * APP的渠道号
     *
     */
    private static String appChannel = null;

    /**
     * APP的包名
     */
    private static String packageName = null;
    public static String getAppPackageName() {
        if (packageName == null) {
            try {
                packageName = sContext.getApplicationInfo().packageName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return packageName;
    }

    private void registerActivityLifecycleListener() {
        sLifecycleCallback = new CollarLifecycleCallback();
        this.registerActivityLifecycleCallbacks(sLifecycleCallback);
    }
    /**
     * Aty生命周期检测，第三方的Activity不受监控
     */

    static CollarLifecycleCallback sLifecycleCallback;
    class CollarLifecycleCallback implements ActivityLifecycleCallbacks{
        int atyCount = 0;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            //创建主activity时，关闭所有其他aty
            if (activity instanceof MainActivity){
                List<Activity> copyList = new ArrayList<>(sActivityList);
                if (copyList.size() > 0) {
                    for (Activity a : copyList) {
                        if (a != activity ) {
                            a.finish();
                        }
                    }
                }
            }
            sActivityList.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            atyCount++;
            if (isAppRunBackground){
                isAppRunBackground = false;
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            atyCount--;
            if (atyCount <= 0) {
                isAppRunBackground = false;
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            boolean ret = sActivityList.remove(activity);
            Log.i(TAG,"activity destroyed:"+activity.getLocalClassName()+",ret:"+ret);
        }
    }
}

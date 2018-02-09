package com.codez.collar.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.codez.collar.MainActivity;
import com.codez.collar.manager.ApplicationContext;
import com.codez.collar.service.CoreService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "oncreate");
        startCoreService();
        SkinCompatManager.withoutActivity(this)
                .addInflater(new SkinMaterialViewInflater())
                .addInflater(new SkinConstraintViewInflater())
                .addInflater(new SkinConstraintViewInflater())
                .setSkinStatusBarColorEnable(false)
                .setSkinWindowBackgroundEnable(false)
                .loadSkin();

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

package com.codez.collar.base;

import android.app.Application;
import android.content.Context;

import com.codez.collar.manager.ApplicationContext;

import skin.support.SkinCompatManager;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;

/**
 * Created by codez on 2017/11/17.
 * Description:
 */

public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinCompatManager.withoutActivity(this)
                .addInflater(new SkinMaterialViewInflater())
                .addInflater(new SkinConstraintViewInflater())
                .addInflater(new SkinConstraintViewInflater())
                .setSkinStatusBarColorEnable(false)
                .setSkinWindowBackgroundEnable(false)
                .loadSkin();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ApplicationContext.getInstance().init(this);
    }
}

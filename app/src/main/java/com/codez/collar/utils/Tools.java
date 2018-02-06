package com.codez.collar.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.inputmethod.InputMethodManager;

import com.codez.collar.manager.ApplicationContext;

/**
 * Created by codez on 2017/12/10.
 * Description:
 */

public class Tools {
    public static void hiddenKeyboard(Context context){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //得到InputMethodManager的实例
        if (imm.isActive()) {
            //如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
            //关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }
    }
    public static void showKeyboard(Context context){
        //得到InputMethodManager的实例
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static String getAppVersion() {
        try {
            PackageInfo info = ApplicationContext.get().getPackageManager().getPackageInfo(ApplicationContext.get().getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.codez.collar.manager;

import android.content.Context;

public class ApplicationContext {

    private Context appContext;

	private ApplicationContext(){}
	private static String TAG = "ApplicationContext";
    public void init(Context context){
        if(appContext == null){
            appContext = context;
        }
    }

    private Context getContext(){
        return appContext;
    }

    public static Context get(){
        return getInstance().getContext();
    }

    private static volatile ApplicationContext instance;

    public static ApplicationContext getInstance(){
        ApplicationContext i = instance;
        if (i == null) {
            synchronized (ApplicationContext.class) {
                i = instance;
                if (i == null) {
                    i = new ApplicationContext();
                    instance = i;
                }
            }
        }
        return instance;
    }

}
package com.codez.collar.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codez.collar.base.BaseApp;
import com.codez.collar.event.ToastEvent;
import com.codez.collar.utils.EventBusUtils;
import com.codez.collar.utils.T;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by codez on 2018/2/1.
 * Description:
 */

public class CoreService extends Service{
    private static final String TAG = "CoreService";
    private static final String NET_CHANGED = "android.net.conn.CONNECTIVITY_CHANGE";
    public static final String LAUNCH_MODEKEY = "launch-mode";
    public static final String KEY_CHECK_AK_ERVICE = "key.check.self";
    public static final String KEY_CALLBACK = "key.callback";

    private Context mContext;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        super.onCreate();
        mContext = this;

        IntentFilter filter = new IntentFilter();
        filter.addAction(NET_CHANGED);
        registerReceiver(mCoreServiceReceiver, filter);
        EventBusUtils.register(this);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
        unregisterReceiver(mCoreServiceReceiver);
        EventBusUtils.unregister(this);
        startService(new Intent(this, CoreService.class));
    }
    private BroadcastReceiver mCoreServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "broadcast onReceive");
            if (intent == null) {
                Log.i(TAG, "broadcast intent is null");
                return;
            }
            switch (intent.getAction()){
                case NET_CHANGED:
                    Log.i(TAG, "network state has changed");
                    //检测API是不是小于23，因为到了API23以后getNetworkInfo(int networkInfo)方法被弃用
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                        ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
                        //WIFI info
                        NetworkInfo wifiInfo = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        //数据网络 info
                        NetworkInfo dataInfo = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                        if (wifiInfo.isConnected() || dataInfo.isConnected()) {
                            if (wifiInfo.isConnected()){
                                Log.i(TAG, "wifi is connected");
                            }
                            if (dataInfo.isConnected()){
                                Log.i(TAG, "data is connected");
                            }
                            //TODO:网络已连接
                        } else {
                            showToast("网络连接断开");
                            //TODO:网络连接断开
                        }
                    }else {
                        ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
                        Network[] networks = cManager.getAllNetworks();
                        StringBuilder sb = new StringBuilder();
                        Log.i(TAG, "length:" + networks.length);
                        if (networks.length == 0){
                            showToast("网络连接断开");
                            //TODO:网络连接断开
                        }else{
                            for (int i=0; i < networks.length; i++){
                                NetworkInfo networkInfo = cManager.getNetworkInfo(networks[i]);
                                sb.append(networkInfo.getTypeName()+" connect is "+networkInfo.isConnected());
                            }
                            Log.i(TAG, sb.toString());
                            //TODO:网络已连接
                        }

                    }
                    break;
            }

        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onToastEvent(ToastEvent event) {
        if (event.content != null) {
            showToast(event.content);
        }else{
            if (event.resId>0){
                showToast(getString(event.resId));
            }
        }
    }

    private void showToast(String content) {
        if (!BaseApp.isAppRunBackground) {
            T.s(mContext, content);
        }
    }
}

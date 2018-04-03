package com.codez.collar.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by codez on 2018/4/3.
 * Description:
 */

public class UpgradeDownloadService extends Service{
    private static final String TAG = "UpgradeDownloadService";
    private static final String KEY_ACTION = "key_action";
    private static final int ACTION_PAUSE = 1;
    private static final int ACTION_CONTINUE = 2;
    private static final int ACTION_CANCEL = 3;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_NOT_STICKY;
        }
        int action = intent.getIntExtra(KEY_ACTION, 0);
        return super.onStartCommand(intent, flags, startId);
    }
}

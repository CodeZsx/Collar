package com.codez.collar.manager;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by codez on 2018/4/6.
 * Description:
 */

public enum  AttitudesManager {
    instance;
    private static final String TAG = "AttitudesManager";

    public static final int STATE_LIKE = 1;
    public static final int STATE_UNLIKE = 0;

    public static AttitudesManager getInstance() {
        return instance;
    }

    private ConcurrentHashMap<String, Integer> mAttitudeMap = new ConcurrentHashMap<>();

    public void putAttitude(String key, int value) {
        mAttitudeMap.put(key, value);
    }

    public int getAttitude(String key){
        return mAttitudeMap.get(key) == null ? -1 : mAttitudeMap.get(key);
    }
}

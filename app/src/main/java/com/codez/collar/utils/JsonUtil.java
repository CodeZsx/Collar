package com.codez.collar.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by codez on 2018/4/6.
 * Description:
 */

public class JsonUtil {
    public static final int getValueByUncertainKey(byte[] bytes) {
        try {
            JSONObject jsonObject = new JSONObject(new String(bytes));
            Iterator keys = jsonObject.keys();
            if (keys.hasNext()){
                String key = String.valueOf(keys.next());
                return jsonObject.getInt(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }
}

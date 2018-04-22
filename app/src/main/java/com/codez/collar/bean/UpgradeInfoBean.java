package com.codez.collar.bean;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by codez on 2018/4/2.
 * Description:更新信息
 */

public class UpgradeInfoBean implements Serializable{
    private static final String TAG = "UpgradeInfoBean";
    private static final String VERSION_CODE = "version_code";
    private static final String VERSION_NAME = "version_name";
    private static final String FIRMWARE_URL = "firmware_url";
    private static final String DESCRIPTION = "description";
    private static final String FILE_SIZE = "file_size";
    private static final String RELEASE_TIME = "release_time";
    private static final String CHECK_TIME = "check_time";
    private String version_code;
    private String version_name;
    private String firmware_url;
    private String description;
    private String file_size;
    private String release_time;
    private long check_time;

    public UpgradeInfoBean() {
    }

    public String getVersion_code() {
        return version_code;
    }

    public void setVersion_code(String version_code) {
        this.version_code = version_code;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public String getFirmware_url() {
        return firmware_url;
    }

    public void setFirmware_url(String firmware_url) {
        this.firmware_url = firmware_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    public String getRelease_time() {
        return release_time;
    }

    public void setRelease_time(String release_time) {
        this.release_time = release_time;
    }

    public long getCheck_time() {
        return check_time;
    }

    public void setCheck_time(long check_time) {
        this.check_time = check_time;
    }

    @Override
    public String toString() {
        return "UpgradeInfoBean{" +
                "version_code='" + version_code + '\'' +
                ", version_name='" + version_name + '\'' +
                ", firmware_url='" + firmware_url + '\'' +
                ", description='" + description + '\'' +
                ", file_size='" + file_size + '\'' +
                ", release_time='" + release_time + '\'' +
                ", check_time='" + check_time + '\'' +
                '}';
    }
    public static UpgradeInfoBean jsonToBean(String json){
        if (TextUtils.isEmpty(json)){
            Log.w(TAG,"your json is null get null bean");
            return null;
        }
        JSONObject obj = null;
        obj = JSONObject.parseObject(json);
        if (obj.containsKey(VERSION_CODE)){
            UpgradeInfoBean bean = new UpgradeInfoBean();
            bean.setVersion_code(obj.getString(VERSION_CODE));
            bean.setVersion_name(obj.getString(VERSION_NAME));
            bean.setFirmware_url(obj.getString(FIRMWARE_URL));
            bean.setDescription(obj.getString(DESCRIPTION));
            bean.setFile_size(obj.getString(FILE_SIZE));
            bean.setRelease_time(obj.getString(RELEASE_TIME));
            bean.setCheck_time(obj.getLong(CHECK_TIME));
            return bean;
        }
        return null;
    }

    /**
     * 数据转换
     */
    public static JSONObject beanToJson(UpgradeInfoBean b){
        if (b == null){
            Log.w(TAG,"your bean is null get null obj");
            return null;
        }
        JSONObject obj = new JSONObject();
        obj.put(VERSION_CODE, b.getVersion_code());
        obj.put(VERSION_NAME, b.getVersion_name());
        obj.put(FIRMWARE_URL, b.getFirmware_url());
        obj.put(DESCRIPTION, b.getDescription());
        obj.put(FILE_SIZE, b.getFile_size());
        obj.put(RELEASE_TIME, b.release_time);
        obj.put(CHECK_TIME, b.check_time);
        return obj;
    }
}

package com.codez.collar.bean;

/**
 * Created by codez on 2018/4/2.
 * Description:更新信息
 */

public class UpgradeInfoBean {
    private String version_code;
    private String version_name;
    private String firmware_url;
    private String description;
    private String file_size;

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

    @Override
    public String toString() {
        return "UpgradeInfoBean{" +
                "version_code='" + version_code + '\'' +
                ", version_name='" + version_name + '\'' +
                ", firmware_url='" + firmware_url + '\'' +
                ", description='" + description + '\'' +
                ", file_size='" + file_size + '\'' +
                '}';
    }
}

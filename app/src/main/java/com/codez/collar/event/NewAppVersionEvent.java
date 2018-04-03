package com.codez.collar.event;

import com.codez.collar.bean.UpgradeInfoBean;

/**
 * Created by codez on 2018/4/2.
 * Description:
 */

public class NewAppVersionEvent {
    private UpgradeInfoBean info;
    private int type;
    //手动检查
    public static final int AUTOMATIC = 1;
    //自动检查
    public static final int MANUAL = 2;

    public NewAppVersionEvent(UpgradeInfoBean info, int type) {
        this.info = info;
        this.type = type;
    }

    public UpgradeInfoBean getInfo() {
        return info;
    }

    public void setInfo(UpgradeInfoBean info) {
        this.info = info;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

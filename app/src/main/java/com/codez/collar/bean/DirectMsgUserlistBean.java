package com.codez.collar.bean;

import android.databinding.BaseObservable;

/**
 * Created by codez on 2017/12/12.
 * Description:
 */

public class DirectMsgUserlistBean extends BaseObservable{
    private UserBean user;
    private DirectMsgBean direct_message;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public DirectMsgBean getDirect_message() {
        return direct_message;
    }

    public void setDirect_message(DirectMsgBean direct_message) {
        this.direct_message = direct_message;
    }
}

package com.codez.collar.bean;

import android.databinding.BaseObservable;

/**
 * Created by codez on 2017/12/4.
 * Description:
 */

public class FavoriteBean extends BaseObservable{
    private StatusBean status;

    //tags未使用
//    private List<> tags;

    private String favorited_time;

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public String getFavorited_time() {
        return favorited_time;
    }

    public void setFavorited_time(String favorited_time) {
        this.favorited_time = favorited_time;
    }
}

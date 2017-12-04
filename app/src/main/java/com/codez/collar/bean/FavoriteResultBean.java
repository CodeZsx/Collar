package com.codez.collar.bean;

import android.databinding.BaseObservable;

import java.util.List;

/**
 * Created by codez on 2017/12/4.
 * Description:
 */

public class FavoriteResultBean extends BaseObservable {
    private List<FavoriteBean> favorites;
    private int total_number;

    public List<FavoriteBean> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<FavoriteBean> favorites) {
        this.favorites = favorites;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }
}

package com.codez.collar.bean;

import android.databinding.BaseObservable;

import java.util.List;

/**
 * Created by codez on 2017/11/28.
 * Description:
 */

public class TopicResultBean extends BaseObservable{
    private List<StatusBean> statuses;
    private int total_number;

    public List<StatusBean> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<StatusBean> statuses) {
        this.statuses = statuses;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }
}

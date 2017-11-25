package com.codez.collar.bean;

import android.databinding.BaseObservable;

import java.util.List;

/**
 * Created by codez on 2017/11/22.
 * Description:
 */

public class StatusResultBean extends BaseObservable{
    private String next_cursor;
    private String since_id;
    private String max_id;
    private List<StatusBean> statuses;

    public String getNext_cursor() {
        return next_cursor;
    }

    public void setNext_cursor(String next_cursor) {
        this.next_cursor = next_cursor;
    }

    public String getSince_id() {
        return since_id;
    }

    public void setSince_id(String since_id) {
        this.since_id = since_id;
    }

    public String getMax_id() {
        return max_id;
    }

    public void setMax_id(String max_id) {
        this.max_id = max_id;
    }

    public List<StatusBean> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<StatusBean> statuses) {
        this.statuses = statuses;
    }
}

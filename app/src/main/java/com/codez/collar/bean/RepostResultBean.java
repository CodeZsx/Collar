package com.codez.collar.bean;

import android.databinding.BaseObservable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by codez on 2017/11/25.
 * Description:
 */

public class RepostResultBean extends BaseObservable implements Serializable{
    private List<StatusBean> reposts;
    private boolean hasvisible;
    private String previous_cursor;
    private String next_cursor;
    private int total_number;
    private int interval;

    public List<StatusBean> getReposts() {
        return reposts;
    }

    public void setReposts(List<StatusBean> reposts) {
        this.reposts = reposts;
    }

    public boolean isHasvisible() {
        return hasvisible;
    }

    public void setHasvisible(boolean hasvisible) {
        this.hasvisible = hasvisible;
    }

    public String getPrevious_cursor() {
        return previous_cursor;
    }

    public void setPrevious_cursor(String previous_cursor) {
        this.previous_cursor = previous_cursor;
    }

    public String getNext_cursor() {
        return next_cursor;
    }

    public void setNext_cursor(String next_cursor) {
        this.next_cursor = next_cursor;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}

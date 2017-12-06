package com.codez.collar.bean;

import android.databinding.BaseObservable;

import java.util.List;

/**
 * Created by codez on 2017/12/6.
 * Description:
 */

public class FriendsIdsResultBean extends BaseObservable{
    private List<String> ids;
    private int next_cursor;
    private int previous_cursor;
    private int total_number;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public int getNext_cursor() {
        return next_cursor;
    }

    public void setNext_cursor(int next_cursor) {
        this.next_cursor = next_cursor;
    }

    public int getPrevious_cursor() {
        return previous_cursor;
    }

    public void setPrevious_cursor(int previous_cursor) {
        this.previous_cursor = previous_cursor;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    @Override
    public String toString() {
        return "FriendsIdsResultBean{" +
                "ids=" + ids +
                ", next_cursor=" + next_cursor +
                ", previous_cursor=" + previous_cursor +
                ", total_number=" + total_number +
                '}';
    }
}

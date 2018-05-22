package com.codez.collar.bean;

import android.databinding.BaseObservable;

import java.util.List;

/**
 * Created by codez on 2017/12/5.
 * Description:
 */

public class UsersResultBean extends BaseObservable {
    private List<UserBean> users;
    private int next_cursor;
    private int previous_cursor;
    private int total_number;
    private int display_total_number;

    public List<UserBean> getUsers() {
        return users;
    }

    public void setUsers(List<UserBean> users) {
        this.users = users;
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

    public int getDisplay_total_number() {
        return display_total_number;
    }

    public void setDisplay_total_number(int display_total_number) {
        this.display_total_number = display_total_number;
    }

    @Override
    public String toString() {
        return "FriendshipsResultBean{" +
                "users=" + users +
                ", next_cursor=" + next_cursor +
                ", previous_cursor=" + previous_cursor +
                ", total_number=" + total_number +
                ", display_total_number=" + display_total_number +
                '}';
    }
}

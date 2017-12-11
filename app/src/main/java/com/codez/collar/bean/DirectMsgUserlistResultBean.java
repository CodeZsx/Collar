package com.codez.collar.bean;

import java.util.List;

/**
 * Created by codez on 2017/12/12.
 * Description:
 */

public class DirectMsgUserlistResultBean {
    private List<DirectMsgUserlistBean> user_list;
    private String next_cursor;
    private String previous_cursor;
    private int total_number;

    public List<DirectMsgUserlistBean> getUser_list() {
        return user_list;
    }

    public void setUser_list(List<DirectMsgUserlistBean> user_list) {
        this.user_list = user_list;
    }

    public String getNext_cursor() {
        return next_cursor;
    }

    public void setNext_cursor(String next_cursor) {
        this.next_cursor = next_cursor;
    }

    public String getPrevious_cursor() {
        return previous_cursor;
    }

    public void setPrevious_cursor(String previous_cursor) {
        this.previous_cursor = previous_cursor;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }
}

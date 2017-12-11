package com.codez.collar.bean;

import android.databinding.BaseObservable;

import java.util.List;

/**
 * Created by codez on 2017/12/10.
 * Description:
 */

public class DirectMsgConversationResultBean extends BaseObservable{
    private List<DirectMsgBean> direct_messages;
    private String previous_cursor;
    private String next_cursor;
    private int total_number;

    public List<DirectMsgBean> getDirect_messages() {
        return direct_messages;
    }

    public void setDirect_messages(List<DirectMsgBean> direct_messages) {
        this.direct_messages = direct_messages;
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
}

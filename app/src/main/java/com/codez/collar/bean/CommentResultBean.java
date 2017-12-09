package com.codez.collar.bean;

import android.databinding.BaseObservable;

import java.util.List;

/**
 * Created by codez on 2017/11/25.
 * Description:
 */

public class CommentResultBean extends BaseObservable{
    /**
     * comment list
     */
    private List<CommentBean> comments;
    /**
     *
     */
    private boolean hasvisible;
    /**
     *
     */
    private String previous_cursor;
    /**
     *
     */
    private String next_cursor;
    /**
     * 评论条数
     */
    private int total_number;
    /**
     *
     */
    private String since_id;
    /**
     *
     */
    private String max_id;
    /**
     *
     */
    private StatusBean status;

    public List<CommentBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentBean> comments) {
        this.comments = comments;
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

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }
}

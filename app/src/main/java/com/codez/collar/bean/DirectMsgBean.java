package com.codez.collar.bean;

import android.databinding.BaseObservable;

import com.codez.collar.utils.TimeUtil;

/**
 * Created by codez on 2017/12/10.
 * Description:
 */

public class DirectMsgBean extends BaseObservable{
    /**
     * 私信id,int64
     */
    private String id;
    /**
     * 私信创建时间
     */
    private String created_at;
    /**
     * 私信内容
     */
    private String text;
    /**
     * 发送类型
     */
    private int send_type;
    /**
     * 发送者uid
     */
    private String sender_id;
    /**
     * 接受者uid
     */
    private String recipient_id;
    /**
     * 发送者用户昵称
     */
    private String sender_screen_name;
    /**
     * 接受者用户昵称
     */
    private String recipient_screen_name;
    /**
     * 私信mid
     */
    private String mid;
    /**
     * 转发的微博id
     */
    private String status_id;
    /**
     * 发送者用户信息字段
     */
    private UserBean sender;
    /**
     * 接受者用户信息字段
     */
    private UserBean recipient;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return TimeUtil.getWeiboTime(created_at);
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSend_type() {
        return send_type;
    }

    public void setSend_type(int send_type) {
        this.send_type = send_type;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getRecipient_id() {
        return recipient_id;
    }

    public void setRecipient_id(String recipient_id) {
        this.recipient_id = recipient_id;
    }

    public String getSender_screen_name() {
        return sender_screen_name;
    }

    public void setSender_screen_name(String sender_screen_name) {
        this.sender_screen_name = sender_screen_name;
    }

    public String getRecipient_screen_name() {
        return recipient_screen_name;
    }

    public void setRecipient_screen_name(String recipient_screen_name) {
        this.recipient_screen_name = recipient_screen_name;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public UserBean getSender() {
        return sender;
    }

    public void setSender(UserBean sender) {
        this.sender = sender;
    }

    public UserBean getRecipient() {
        return recipient;
    }

    public void setRecipient(UserBean recipient) {
        this.recipient = recipient;
    }
}

package com.codez.collar.bean;

/**
 * Created by codez on 2018/4/6.
 * Description:
 */

public class AttitudeResultBean {
    /**
     * 微博ID
     */
    private String id;
    /**
     * 微博创建时间
     */
    private String created_at;
    /**
     * 点赞状态
     */
    private String attitude;
    /**
     * 上一次点赞状态
     */
    private String source;
    /**
     * 用户
     */
    private UserBean user;
    /**
     * 微博
     */
    private StatusBean status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getAttitude() {
        return attitude;
    }

    public void setAttitude(String attitude) {
        this.attitude = attitude;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AttitudeResultBean{" +
                "id='" + id + '\'' +
                ", created_at='" + created_at + '\'' +
                ", attitude='" + attitude + '\'' +
                ", source='" + source + '\'' +
                ", user=" + user +
                ", status=" + status +
                '}';
    }
}

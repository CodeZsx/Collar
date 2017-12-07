package com.codez.collar.bean;

import android.databinding.BaseObservable;

/**
 * Created by codez on 2017/12/7.
 * Description:存储两个用户之间的详细关注关系情况
 */

public class FriendshipsShowResultBean extends BaseObservable{

    private Relationship source;
    private Relationship target;

    public Relationship getSource() {
        return source;
    }

    public void setSource(Relationship source) {
        this.source = source;
    }

    public Relationship getTarget() {
        return target;
    }

    public void setTarget(Relationship target) {
        this.target = target;
    }

    public class Relationship extends BaseObservable{
        //是否被另一用户关注
        private boolean followed_by;
        //是否正在关注另一用户
        private boolean following;
        //用户id
        private String id;
        private boolean notifications_enabled;
        private String screen_name;

        public boolean isFollowed_by() {
            return followed_by;
        }

        public void setFollowed_by(boolean followed_by) {
            this.followed_by = followed_by;
        }

        public boolean isFollowing() {
            return following;
        }

        public void setFollowing(boolean following) {
            this.following = following;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isNotifications_enabled() {
            return notifications_enabled;
        }

        public void setNotifications_enabled(boolean notifications_enabled) {
            this.notifications_enabled = notifications_enabled;
        }

        public String getScreen_name() {
            return screen_name;
        }

        public void setScreen_name(String screen_name) {
            this.screen_name = screen_name;
        }
    }
}

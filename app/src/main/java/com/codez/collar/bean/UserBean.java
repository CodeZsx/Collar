package com.codez.collar.bean;

import android.databinding.BaseObservable;

/**
 * Created by codez on 2017/11/21.
 * Description: user bean
 */

public class UserBean extends BaseObservable {
    /**
     * 用户UID（int64）
     */
    public String id;
    /**
     * 字符串型的用户UID
     */
    public String idStr;
    /**
     * 用户昵称
     */
    public String screen_name;
    /**
     * 友好显示名称
     */
    public String name;
    /**
     * 省份id
     */
    public int province;
    /**
     * 城市id
     */
    public int city;
    /**
     * 所在地
     */
    public String location;
    /**
     * 个人描述
     */
    public String description;
    /**
     * 博客地址
     */
    public String url;
    /**
     * 用户头像url 50*50
     */
    public String profile_image_url;
    /**
     * 用户头像url 180*180
     */
    public String avatar_large;
    /**
     * 用户头像url 1024*1024
     */
    public String avatar_hd;
    /**
     * 主页背景图url
     */
    public String cover_image_phone;
    /**
     * 用户微博统一URL地址
     */
    public String profile_url;

    /**
     * 用户的个性化URL地址
     */
    public String domain;

    /**
     * 性别 m:male f:female
     */
    public String gender;

    /**
     * 粉丝数
     */
    public int followers_count;

    /**
     * 关注数
     */
    public int friends_count;

    /**
     * 微博数
     */
    public int statuses_count;
    /**
     * 收藏数
     */
    public int favourites_count;

    /**
     * 用户创建时间
     */
    public String created_at;

    /**
     * 暂未支持
     */
    public boolean following;
    /**
     * 是否允许所有人给我发私信
     */
    public boolean allow_all_act_msg;
    /**
     * 是否允许标识用户的地理位置
     */
    public boolean geo_enabled;
    /**
     * 是否是微博认证（加v）用户
     */
    public boolean verified;
    /**
     * 暂未支持
     */
    public boolean veritified_type;
    /**
     * 用户备注信息，只有在查询用户关系时才返回此字段
     */
    public String remark;
    /**
     * 用户的最近一条微博信息字段
     */
    public Object status;
    /**
     * 是否允许所有人对我的微博进行评论
     */
    public boolean allow_all_comment;
    /**
     * 认证原因
     */
    public String vertified_reason;
    /**
     * 该用户是否关注我（当前登录用户）
     */
    public boolean follow_me;
    /**
     * 用户的在线状态 0：不在线，1：在线
     */
    public int online_status;
    /**
     * 用户的互粉数
     */
    public int bi_followers_count;
    /**
     * language zh-cn:简体中文，zh-tw：繁体中文，en：英语
     */
    public String lang;
    /**
     * 注意：以下字段暂时不清楚具体含义，OpenAPI 说明文档暂时没有同步更新对应字段
     */
    public String star;
    public String mbtype;
    public String mbrank;
    public String block_word;


}

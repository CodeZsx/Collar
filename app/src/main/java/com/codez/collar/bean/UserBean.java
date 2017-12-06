package com.codez.collar.bean;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.Serializable;

/**
 * Created by codez on 2017/11/21.
 * Description: user bean
 */

public class UserBean extends BaseObservable implements Serializable{
    /**
     * 用户UID（int64）
     */
    public String id;
    /**
     * 字符串型的用户UID
     */
    public String idstr;
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
    public StatusBean status;
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getAvatar_large() {
        return avatar_large;
    }

    public void setAvatar_large(String avatar_large) {
        this.avatar_large = avatar_large;
    }

    public String getAvatar_hd() {
        return avatar_hd;
    }

    public void setAvatar_hd(String avatar_hd) {
        this.avatar_hd = avatar_hd;
    }

    public String getCover_image_phone() {
        return cover_image_phone;
    }

    public void setCover_image_phone(String cover_image_phone) {
        this.cover_image_phone = cover_image_phone;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public int getFriends_count() {
        return friends_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public int getStatuses_count() {
        return statuses_count;
    }

    public void setStatuses_count(int statuses_count) {
        this.statuses_count = statuses_count;
    }

    public int getFavourites_count() {
        return favourites_count;
    }

    public void setFavourites_count(int favourites_count) {
        this.favourites_count = favourites_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public boolean isAllow_all_act_msg() {
        return allow_all_act_msg;
    }

    public void setAllow_all_act_msg(boolean allow_all_act_msg) {
        this.allow_all_act_msg = allow_all_act_msg;
    }

    public boolean isGeo_enabled() {
        return geo_enabled;
    }

    public void setGeo_enabled(boolean geo_enabled) {
        this.geo_enabled = geo_enabled;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isVeritified_type() {
        return veritified_type;
    }

    public void setVeritified_type(boolean veritified_type) {
        this.veritified_type = veritified_type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public boolean isAllow_all_comment() {
        return allow_all_comment;
    }

    public void setAllow_all_comment(boolean allow_all_comment) {
        this.allow_all_comment = allow_all_comment;
    }

    public String getVertified_reason() {
        return vertified_reason;
    }

    public void setVertified_reason(String vertified_reason) {
        this.vertified_reason = vertified_reason;
    }

    public boolean isFollow_me() {
        return follow_me;
    }

    public void setFollow_me(boolean follow_me) {
        this.follow_me = follow_me;
    }

    public int getOnline_status() {
        return online_status;
    }

    public void setOnline_status(int online_status) {
        this.online_status = online_status;
    }

    public int getBi_followers_count() {
        return bi_followers_count;
    }

    public void setBi_followers_count(int bi_followers_count) {
        this.bi_followers_count = bi_followers_count;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getMbtype() {
        return mbtype;
    }

    public void setMbtype(String mbtype) {
        this.mbtype = mbtype;
    }

    public String getMbrank() {
        return mbrank;
    }

    public void setMbrank(String mbrank) {
        this.mbrank = mbrank;
    }

    public String getBlock_word() {
        return block_word;
    }

    public void setBlock_word(String block_word) {
        this.block_word = block_word;
    }


    @BindingAdapter(value = {"android:imageUrl"},requireAll = false)
    public static void imageLoader(ImageView iv, String coverUrl){
        Glide.with(iv.getContext())
                .load(coverUrl)
                .into(iv);
    }

    private static final String DPI_THUMB = "thumbnail";
    private static final String DPI_MID = "bmiddle";
    private static final String DPI_LARGE = "large";


    @BindingAdapter(value = {"android:albumUrl", "android:definition"}, requireAll = true)
    public static void albumLoader(ImageView iv, String albumUrl, String definition) {
        String url = null;
        if (definition.equals(DPI_THUMB)) {
            url = albumUrl;
        } else if (definition.equals(DPI_LARGE)) {
            url = albumUrl.replace(DPI_THUMB, DPI_LARGE);
        }else{
            url = albumUrl.replace(DPI_THUMB, DPI_MID);
        }
        Glide.with(iv.getContext())
                .load(url)
                .into(iv);
    }

    @BindingAdapter(value = {"android:localUri"},requireAll = false)
    public static void localLoader(ImageView iv, String uri){
        Glide.with(iv.getContext())
                .load("file://" + uri)
                .into(iv);
    }


    @Override
    public String toString() {
        return "UserBean{" +
                "id='" + id + '\'' +
                ", idStr='" + idstr + '\'' +
                ", screen_name='" + screen_name + '\'' +
                ", name='" + name + '\'' +
                ", province=" + province +
                ", city=" + city +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", profile_image_url='" + profile_image_url + '\'' +
                ", avatar_large='" + avatar_large + '\'' +
                ", avatar_hd='" + avatar_hd + '\'' +
                ", cover_image_phone='" + cover_image_phone + '\'' +
                ", profile_url='" + profile_url + '\'' +
                ", domain='" + domain + '\'' +
                ", gender='" + gender + '\'' +
                ", followers_count=" + followers_count +
                ", friends_count=" + friends_count +
                ", statuses_count=" + statuses_count +
                ", favourites_count=" + favourites_count +
                ", created_at='" + created_at + '\'' +
                ", following=" + following +
                ", allow_all_act_msg=" + allow_all_act_msg +
                ", geo_enabled=" + geo_enabled +
                ", verified=" + verified +
                ", veritified_type=" + veritified_type +
                ", remark='" + remark + '\'' +
                ", status=" + status +
                ", allow_all_comment=" + allow_all_comment +
                ", vertified_reason='" + vertified_reason + '\'' +
                ", follow_me=" + follow_me +
                ", online_status=" + online_status +
                ", bi_followers_count=" + bi_followers_count +
                ", lang='" + lang + '\'' +
                ", star='" + star + '\'' +
                ", mbtype='" + mbtype + '\'' +
                ", mbrank='" + mbrank + '\'' +
                ", block_word='" + block_word + '\'' +
                '}';
    }
}

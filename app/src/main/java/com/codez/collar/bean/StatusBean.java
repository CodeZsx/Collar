package com.codez.collar.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.codez.collar.BR;
import com.codez.collar.utils.HandleUtil;
import com.codez.collar.utils.TimeUtil;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by codez on 2017/11/20.
 * Description: weibo bean
 */

public class StatusBean extends BaseObservable implements Serializable{
    public static final String INTENT_SERIALIZABLE = "serializable";

    /**
     * 微博创建时间
     */
    private String created_at;
    /**
     * 微博ID
     */
    private String id;
    /**
     * 微博MID
     */
    private String mid;
    /**
     * 字符串型的微博ID
     */
    private String idstr;
    /**
     * 微博文本内容长度
     */
    private int textLength;
    /**
     * 微博信息内容
     */
    private String text;
    /**
     * 是否是超过140个字的长微博
     */
    private boolean isLongText;
    /**
     * 微博来源类型
     */
    private int source_type;
    /**
     * 微博来源
     */
    private String source;
    /**
     * 是否已收藏，true：是，false：否
     */
    private boolean favorited;
    /**
     * 是否被截断，true：是，false：否
     */
    private boolean truncated;
    /**
     * （暂未支持）回复ID
     */
    private String in_reply_to_status_id;
    /**
     * （暂未支持）回复人UID
     */
    private String in_reply_to_user_id;
    /**
     * （暂未支持）回复人昵称
     */
    private String in_reply_to_screen_name;
    /**
     * 缩略图片地址（小图），没有时不返回此字段
     */
    private String thumbnail_pic;
    /**
     * 中等尺寸图片地址（中图），没有时不返回此字段
     */
    private String bmiddle_pic;
    /**
     * 原始图片地址（原图），没有时不返回此字段
     */
    private String original_pic;
    /**
     * 地理信息字段
     */
    private GeoBean geo;
    /**
     * 微博作者的用户信息字段
     */
    private UserBean user;
    /**
     * 被转发的原微博信息字段，当该微博为转发微博时返回
     */
    private StatusBean retweeted_status;
    /**
     * 转发数
     */
    private int reposts_count;
    /**
     * 评论数
     */
    private int comments_count;
    /**
     * 表态数
     */
    private int attitudes_count;
    /**
     * 暂未支持
     */
    private int mlevel;
    /**
     * 微博的可见性及指定可见分组信息。该 object 中 type 取值，
     * 0：普通微博，1：私密微博，3：指定分组微博，4：密友微博；
     * list_id为分组的组号
     */
    private VisibleBean visible;
    /**
     * 微博来源是否允许点击，如果允许
     */
    private int source_allowclick;

    /**
     * 微博图片字段
     */
    private ArrayList<PicUrlsBean> pic_urls;
    /**
     * 微博图片id，微博接口中，话题搜索返回结果没有pic_urls，只有pic_ids
     * 在gson赋值完成后，需要手动转换为pic_urls
     */
    private ArrayList<String> pic_ids;

    /**
     * 缩略图的url，本地私有的字段，服务器不会返回此字段，在gson赋值完成后，需要手动为此字段赋值
     */

    private ArrayList<String> thumbnail_pic_urls = new ArrayList<>();

    /**
     * 中等质量图片的url，本地私有的字段，服务器不会返回此字段，在gson赋值完成后，需要手动为此字段赋值
     */
    private ArrayList<String> bmiddle_pic_urls = new ArrayList<>();

    /**
     * 原图的url，本地私有的字段，服务器不会返回此字段，在gson赋值完成后，需要手动为此字段赋值
     */
    private ArrayList<String> origin_pic_urls = new ArrayList<>();

    /**
     * 单张微博的尺寸，本地私有的字段，服务器不会返回此字段，在gson赋值完成后，需要手动为此字段赋值
     */
    private String singleImgSizeType;

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    public String getCreated_at() {
        return TimeUtil.getWeiboTime(created_at);
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public int getTextLength() {
        return textLength;
    }

    public void setTextLength(int textLength) {
        this.textLength = textLength;
    }

    public String getText() {
        return this.text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public boolean isLongText() {
        return isLongText;
    }

    public void setLongText(boolean longText) {
        isLongText = longText;
    }

    public int getSource_type() {
        return source_type;
    }

    public void setSource_type(int source_type) {
        this.source_type = source_type;
    }

    public String getSource() {
        return HandleUtil.handleSource(source);
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public String getIn_reply_to_status_id() {
        return in_reply_to_status_id;
    }

    public void setIn_reply_to_status_id(String in_reply_to_status_id) {
        this.in_reply_to_status_id = in_reply_to_status_id;
    }

    public String getIn_reply_to_user_id() {
        return in_reply_to_user_id;
    }

    public void setIn_reply_to_user_id(String in_reply_to_user_id) {
        this.in_reply_to_user_id = in_reply_to_user_id;
    }

    public String getIn_reply_to_screen_name() {
        return in_reply_to_screen_name;
    }

    public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
        this.in_reply_to_screen_name = in_reply_to_screen_name;
    }

    public String getThumbnail_pic() {
        return thumbnail_pic;
    }

    public void setThumbnail_pic(String thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }

    public String getBmiddle_pic() {
        return bmiddle_pic;
    }

    public void setBmiddle_pic(String bmiddle_pic) {
        this.bmiddle_pic = bmiddle_pic;
    }

    public String getOriginal_pic() {
        return original_pic;
    }

    public void setOriginal_pic(String original_pic) {
        this.original_pic = original_pic;
    }

    public GeoBean getGeo() {
        return geo;
    }

    public void setGeo(GeoBean geo) {
        this.geo = geo;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public StatusBean getRetweeted_status() {
        return retweeted_status;
    }

    public void setRetweeted_status(StatusBean retweeted_status) {
        this.retweeted_status = retweeted_status;
    }

    public int getReposts_count() {
        return reposts_count;
    }

    public void setReposts_count(int reposts_count) {
        this.reposts_count = reposts_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getAttitudes_count() {
        return attitudes_count;
    }

    public void setAttitudes_count(int attitudes_count) {
        this.attitudes_count = attitudes_count;
    }

    public int getMlevel() {
        return mlevel;
    }

    public void setMlevel(int mlevel) {
        this.mlevel = mlevel;
    }

    public VisibleBean getVisible() {
        return visible;
    }

    public void setVisible(VisibleBean visible) {
        this.visible = visible;
    }

    public int getSource_allowclick() {
        return source_allowclick;
    }

    public void setSource_allowclick(int source_allowclick) {
        this.source_allowclick = source_allowclick;
    }

    public ArrayList<PicUrlsBean> getPic_urls() {
        if (pic_urls == null) {
            ArrayList<StatusBean.PicUrlsBean> urls = new ArrayList<>();
            if (this.getPic_ids().size() > 0) {
                StatusBean statusBean = new StatusBean();
                for (String url : this.getPic_ids()) {
                    urls.add(statusBean.new PicUrlsBean("http://wx4.sinaimg.cn/thumbnail/"+url+".jpg"));
                }
            }
            return urls;
        }
        return pic_urls;
    }

    public void setPic_urls(ArrayList<PicUrlsBean> pic_urls) {
        this.pic_urls = pic_urls;
    }

    public ArrayList<String> getPic_ids() {
        return pic_ids;
    }

    public void setPic_ids(ArrayList<String> pic_ids) {
        this.pic_ids = pic_ids;
    }

    public ArrayList<String> getThumbnail_pic_urls() {
        return thumbnail_pic_urls;
    }

    public void setThumbnail_pic_urls(ArrayList<String> thumbnail_pic_urls) {
        this.thumbnail_pic_urls = thumbnail_pic_urls;
    }

    public ArrayList<String> getBmiddle_pic_urls() {
        return bmiddle_pic_urls;
    }

    public void setBmiddle_pic_urls(ArrayList<String> bmiddle_pic_urls) {
        this.bmiddle_pic_urls = bmiddle_pic_urls;
    }

    public ArrayList<String> getOrigin_pic_urls() {
        return origin_pic_urls;
    }

    public void setOrigin_pic_urls(ArrayList<String> origin_pic_urls) {
        this.origin_pic_urls = origin_pic_urls;
    }

    public String getSingleImgSizeType() {
        return singleImgSizeType;
    }

    public void setSingleImgSizeType(String singleImgSizeType) {
        this.singleImgSizeType = singleImgSizeType;
    }

    /**
     * 微博的可见性及指定可见分组信息
     */
    public class VisibleBean extends BaseObservable implements Serializable {
        public static final int VISIBLE_NORMAL = 0;
        public static final int VISIBLE_PRIVACY = 1;
        public static final int VISIBLE_GROUPED = 2;
        public static final int VISIBLE_FRIEND = 3;

        /**
         * type 取值，0：普通微博，1：私密微博，3：指定分组微博，4：密友微博
         */
        public int type;
        /**
         * 分组的组号
         */
        public int list_id;

        @Bindable
        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
            notifyPropertyChanged(BR.type);
        }

        @Bindable
        public int getList_id() {
            return list_id;
        }

        public void setList_id(int list_id) {
            this.list_id = list_id;
            notifyPropertyChanged(BR.list_id);
        }
    }

    /**
     * 微博图片
     */
    public class PicUrlsBean extends BaseObservable implements Serializable{
        private String thumbnail_pic;

        public PicUrlsBean(String thumbnail_pic) {
            this.thumbnail_pic = thumbnail_pic;
        }

        @Bindable
        public String getThumbnail_pic() {
            return thumbnail_pic;
        }

        public void setThumbnail_pic(String thumbnail_pic) {
            this.thumbnail_pic = thumbnail_pic;
            notifyPropertyChanged(BR.thumbnail_pic);
        }
    }

    @Override
    public String toString() {
        return "StatusBean{" +
                "created_at='" + created_at + '\'' +
                ", id='" + id + '\'' +
                ", mid='" + mid + '\'' +
                ", idstr='" + idstr + '\'' +
                ", textLength=" + textLength +
                ", text='" + text + '\'' +
                ", isLongText=" + isLongText +
                ", source_type=" + source_type +
                ", source='" + source + '\'' +
                ", favorited=" + favorited +
                ", truncated=" + truncated +
                ", in_reply_to_status_id='" + in_reply_to_status_id + '\'' +
                ", in_reply_to_user_id='" + in_reply_to_user_id + '\'' +
                ", in_reply_to_screen_name='" + in_reply_to_screen_name + '\'' +
                ", thumbnail_pic='" + thumbnail_pic + '\'' +
                ", bmiddle_pic='" + bmiddle_pic + '\'' +
                ", original_pic='" + original_pic + '\'' +
                ", geo=" + geo +
                ", user=" + user +
                ", retweeted_status=" + retweeted_status +
                ", reposts_count=" + reposts_count +
                ", comments_count=" + comments_count +
                ", attitudes_count=" + attitudes_count +
                ", mlevel=" + mlevel +
                ", visible=" + visible +
                ", source_allowclick=" + source_allowclick +
                ", pic_urls=" + pic_urls +
                ", thumbnail_pic_urls=" + thumbnail_pic_urls +
                ", bmiddle_pic_urls=" + bmiddle_pic_urls +
                ", origin_pic_urls=" + origin_pic_urls +
                ", singleImgSizeType='" + singleImgSizeType + '\'' +
                '}';
    }
}

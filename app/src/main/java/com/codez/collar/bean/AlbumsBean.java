package com.codez.collar.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by codez on 2017/11/25.
 * Description:
 */

public class AlbumsBean implements Serializable{
    public static final String INTENT_SERIALIZABLE = "serializable";

    private int curPosition;
    private List<StatusBean.PicUrlsBean> pic_urls;


    public List<StatusBean.PicUrlsBean> getPic_urls() {
        return pic_urls;
    }

    public void setPic_urls(List<StatusBean.PicUrlsBean> pic_urls) {
        this.pic_urls = pic_urls;
    }

    public int getCurPosition() {
        return curPosition;
    }

    public void setCurPosition(int curPosition) {
        this.curPosition = curPosition;
    }
}

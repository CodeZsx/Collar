package com.codez.collar.bean;

import java.io.File;
import java.util.List;

/**
 * Created by codez on 2017/11/30.
 * Description:
 */

public class AlbumFolder {
    /**
     * 目录名
     */
    private String folderName;
    /**
     * 所有图片
     */
    private List<File> imageList;
    /**
     * 第一张图片
     */
    private File cover;

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public List<File> getImageList() {
        return imageList;
    }

    public void setImageList(List<File> imageList) {
        this.imageList = imageList;
    }

    public File getCover() {
        return cover;
    }

    public void setCover(File cover) {
        this.cover = cover;
    }
}

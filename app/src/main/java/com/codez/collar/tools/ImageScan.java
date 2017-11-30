package com.codez.collar.tools;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import com.codez.collar.bean.AlbumFolder;
import com.codez.collar.utils.T;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by codez on 2017/11/30.
 * Description:
 */

public abstract class ImageScan {
    private static final String[] IMAGE_PROJECTION = new String[]{
            MediaStore.Images.Media.DATA,//图片路径
            MediaStore.Images.Media.DISPLAY_NAME,//图片文件,包含后缀
            MediaStore.Images.Media.TITLE//图片文件名，不包含后缀
    };
    private final static int IMAGE_LOADER_ID = 1000;
    private ArrayList<AlbumFolder> mFolderList;

    public ImageScan(Context context, LoaderManager loaderManager) {
        startScanImg(context, loaderManager);
    }

    protected void startScanImg(final Context context, LoaderManager loaderManager){
        mFolderList = new ArrayList<>();
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            T.s(context, "暂无外部存储");
            return;
        }
        LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                CursorLoader imageCursorLoader = new CursorLoader(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION, null, null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
                return imageCursorLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (data.getCount() == 0) {
                    T.s(context,"没有扫描到图片");
                }else{
                    //获取属性列的索引位置
                    int dataColumnIndex = data.getColumnIndex(MediaStore.Images.Media.DATA);
                    //图片目录arraylist
                    ArrayList<File> albumList = new ArrayList<>();
                    //图片目录的hashmap，防止扫描同一目录下的图片，图片目录重复出现
                    //<相册的绝对路径，相册底下的所有图片>
                    HashMap<String, ArrayList<File>> albumImageListMap = new HashMap<>();
                    while (data.moveToNext()) {
                        //图片文件
                        File imageFile = new File(data.getString(dataColumnIndex));
                        //图片的父文件夹
                        File albumFolder = imageFile.getParentFile();
                        //图片目录arraylist中没有这个目录，就加入到里面
                        if (!albumList.contains(albumFolder)) {
                            albumList.add(albumFolder);
                        }
                        //获取绝对路径
                        String albumPath = albumFolder.getAbsolutePath();
                        //获取该相册下的所有图片
                        ArrayList<File> albumImageFiles = albumImageListMap.get(albumPath);
                        //检查此目录是否存在hashmap中，如果没有就添加进去
                        if (albumImageFiles == null) {
                            albumImageFiles = new ArrayList<>();
                            albumImageListMap.put(albumPath, albumImageFiles);
                        }
                        //把当前的图片添加到相册目录下
                        albumImageFiles.add(imageFile);
                    }
                    //对相册目录进行排序
                    sortByFileLastModified(albumList);
                    //对图片目录下所有的图片文件进行排序
                    Set<String> keySet = albumImageListMap.keySet();//取出key列表，即目录列表
                    for (String key : keySet) {
                        ArrayList<File> albumImageList = albumImageListMap.get(key);
                        sortByFileLastModified(albumImageList);
                    }
                    if (albumList != null && albumList.size() > 0 && albumImageListMap != null) {
                        ArrayList<AlbumFolder> folders = new ArrayList<>();
                        //生成一个包含全部的名为"全部"的目录
                        AlbumFolder allFolder = createAllImageAlbum(context, albumImageListMap);
                        if (allFolder != null) {
                            folders.add(allFolder);
                        }
                        int albumFolerSize = albumList.size();
                        for (int pos = 0; pos < albumFolerSize; pos++) {
                            File album = albumList.get(pos);
                            AlbumFolder folder = new AlbumFolder();
                            //设置目录名
                            folder.setFolderName(album.getName());
                            //获取当前目录下的图片list
                            List<File> curList = albumImageListMap.get(album.getAbsolutePath());
                            //设置cover
                            folder.setCover(curList.get(0));
                            //设置目录下的图片list
                            folder.setImageList(curList);
                            //把当前目录的图片list添加到"全部"目录下
                            allFolder.getImageList().addAll(curList);

                            //把设置完的folder添加到list中
                            folders.add(folder);
                        }
                        mFolderList = folders;
                    }
                    onScanFinish(mFolderList);
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };
        //初始化指定id的loader
        loaderManager.initLoader(IMAGE_LOADER_ID, null, loaderCallbacks);
    }

    /**
     * 创建一个包含全部图片的目录
     * @param context
     * @param albumImageListMap
     * @return
     */

    private AlbumFolder createAllImageAlbum(Context context, HashMap<String, ArrayList<File>> albumImageListMap) {
        if (albumImageListMap != null) {
            AlbumFolder albumFolder = new AlbumFolder();
            albumFolder.setFolderName("全部");
            //初始化一个空的list，进行赋值，之后会在主函数中逐目录添加到此"全部"目录中
            albumFolder.setImageList(new ArrayList<File>());
            //是否是第一个目录
            boolean isFirstAlbum = true;
            Set<String> albumKeySet = albumImageListMap.keySet();
            //逐个目录获取
            for (String albumKey : albumKeySet) {
                //获取当前目录下的图片list
                List<File> albumImageList = albumImageListMap.get(albumKey);
                //若是第一个目录，则取出第一张图片作为cover
                if (isFirstAlbum) {
                    File cover = albumImageList.get(0);
                    albumFolder.setCover(cover);
                    isFirstAlbum = false;
                }
            }
            return albumFolder;
        }
        return null;
    }


    /**
     * 按照文件的修改时间进行排序，最近修改的排的靠前
     * @param files
     */

    private void sortByFileLastModified(ArrayList<File> files){
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                if (lhs.lastModified() > rhs.lastModified()) {
                    return -1;
                } else if (lhs.lastModified() < rhs.lastModified()) {
                    return 1;
                }
                return 0;
            }
        });
    }

    public abstract void onScanFinish(ArrayList<AlbumFolder> folders);

}

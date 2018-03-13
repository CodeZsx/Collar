package com.codez.collar.net;

import com.codez.collar.bean.RepostResultBean;
import com.codez.collar.bean.StatusBean;
import com.codez.collar.bean.StatusResultBean;

import java.io.File;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public interface StatusService {

    @GET("statuses/home_timeline.json")
    Observable<StatusResultBean> getHomeStatus(@Query("uid") String uid, @Query("page") int page);

    @GET("statuses/user_timeline.json")
    Observable<StatusResultBean> getUserStatus(@Query("uid") String uid, @Query("screen_name") String screen_name, @Query("page") int page);

    @GET("statuses/public_timeline.json")
    Observable<StatusResultBean> getPublicStatus(@Query("page") int page);

    @GET("statuses/repost_timeline.json")
    Observable<RepostResultBean> getRepostStatus(@Query("id") String id, @Query("page") int page);

    @GET("statuses/mentions.json")
    Observable<StatusResultBean> getStatusMention(@Query("page") int page);

    @GET("statuses/show.json")
    Observable<StatusBean> getStatusInfo(@Query("id") String id);

    @FormUrlEncoded
    @POST("statuses/update.json")
    Observable<StatusBean> createTextStatus(@Field("access_token") String access_token, @Field(value = "status", encoded = true) String status);

    @Multipart
    @POST("statuses/upload.json")
    Observable<StatusBean> uploadStatus(@Part("access_token") String access_token,
                                        @Part(value = "status") String status,
                                        @Part(value = "pic") File file);

    //is_comment是否在转发的同时发表评论，0：否、1：评论给当前微博、2：评论给原微博、3：都评论，默认为0
    @FormUrlEncoded
    @POST("statuses/repost.json")
    Observable<StatusBean> repostStatus(@Field("access_token") String access_token,
                                        @Field(value = "status", encoded = true) String status,
                                        @Field(value = "id", encoded = true) String id,
                                        @Field(value = "is_comment", encoded = true) int is_comment);

    @FormUrlEncoded
    @POST("statuses/destroy.json")
    Observable<StatusBean> destroyStatus(@Field("access_token") String access_token, @Field("id") String id);


}

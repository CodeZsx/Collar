package com.codez.collar.net;

import com.codez.collar.bean.RepostResultBean;
import com.codez.collar.bean.StatusBean;
import com.codez.collar.bean.StatusResultBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @FormUrlEncoded
    @POST("statuses/update.json")
    Observable<StatusBean> postTextStatus(@Field("access_token") String access_token, @Field(value = "status", encoded = true) String status);

    @FormUrlEncoded
    @POST("statuses/update.json")
    Observable<StatusBean> destroyStatus(@Field("access_token") String access_token, @Field("id") String id);


}

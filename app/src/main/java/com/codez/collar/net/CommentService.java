package com.codez.collar.net;

import com.codez.collar.bean.CommentBean;
import com.codez.collar.bean.CommentResultBean;

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

public interface CommentService {

    @GET("comments/show.json")
    Observable<CommentResultBean> getStatusComment(@Query("id") String id, @Query("page") int page);


    @FormUrlEncoded
    @POST("comments/create.json")
    Observable<CommentBean> createComment(@Field("access_token") String access_token, @Field(value = "comment", encoded = true) String comment,
                                           @Field("id") String id, @Field("comment_ori") boolean comment_ori);

    @FormUrlEncoded
    @POST("comments/destroy.json")
    Observable<CommentBean> destroyComment(@Field("access_token") String access_token, @Field("cid") String cid);
}

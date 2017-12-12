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

    /**
     * 发出评论列表
     * @param page
     * @return
     */
    @GET("comments/by_me.json")
    Observable<CommentResultBean> getCommentByMe(@Query("page") int page);

    /**
     * 收到评论列表
     * @param page
     * @return
     */
    @GET("comments/to_me.json")
    Observable<CommentResultBean> getCommentToMe(@Query("page") int page);


    @FormUrlEncoded
    @POST("comments/create.json")
    Observable<CommentBean> createComment(@Field("access_token") String access_token, @Field(value = "comment", encoded = true) String comment,
                                           @Field("id") String id, @Field("comment_ori") int comment_ori);

    @FormUrlEncoded
    @POST("comments/destroy.json")
    Observable<CommentBean> destroyComment(@Field("access_token") String access_token, @Field("cid") String cid);
}

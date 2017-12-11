package com.codez.collar.net;

import com.codez.collar.bean.DirectMsgConversationResultBean;
import com.codez.collar.bean.DirectMsgUserlistResultBean;
import com.codez.collar.bean.FavoriteBean;

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

public interface DirectMsgService {
    /**
     * 获取当前用户最新私信列表
     * @param page
     * @return
     */
    @GET("direct_messages.json")
    Observable<DirectMsgConversationResultBean> getDirectMsg(@Query("page") int page);

    /**
     * 获取私信往来用户列表
     * @param cursor
     * @return
     */
    @GET("direct_messages/user_list.json")
    Observable<DirectMsgUserlistResultBean> getDirectMsgUserlist(@Query("cursor") String cursor);

    /**
     * 获取与指定用户的往来私信列表
     * @param uid
     * @param page
     * @return
     */
    @GET("direct_messages/conversation.json")
    Observable<DirectMsgConversationResultBean> getDirectMsgConversation(@Query("uid") String uid, @Query("page") int page);

    /**
     * 发送一条私信
     * @param access_token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("direct_messages/new.json")
    Observable<FavoriteBean> createDirectMsg(@Field("access_token") String access_token, @Field("id") String id);

    @FormUrlEncoded
    @POST("direct_messages/destroy.json")
    Observable<FavoriteBean> destroyDirectMsg(@Field("access_token") String access_token, @Field("id") String id);


}

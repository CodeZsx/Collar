package com.codez.collar.net;

import com.codez.collar.bean.DirectMsgResultBean;
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
    Observable<DirectMsgResultBean> getDirectMsg(@Query("page") int page);

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

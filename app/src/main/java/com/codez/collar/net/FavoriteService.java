package com.codez.collar.net;

import com.codez.collar.bean.FavoriteBean;
import com.codez.collar.bean.FavoriteResultBean;

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

public interface FavoriteService {
    @GET("favorites.json")
    Observable<FavoriteResultBean> getFavorite(@Query("page") int page);

    @FormUrlEncoded
    @POST("favorites/create.json")
    Observable<FavoriteBean> createFavorite(@Field("access_token") String access_token, @Field("id") String id);

    @FormUrlEncoded
    @POST("favorites/destroy.json")
    Observable<FavoriteBean> destroyFavorite(@Field("access_token") String access_token, @Field("id") String id);


}

package com.codez.collar.net;

import com.codez.collar.bean.WeiboBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public interface WeiboService{
    String BASE_URL = Constants.BASE_URL + "statuses/";
    @GET("home_timeline.json")
    Observable<WeiboBean> getHomeStatus(@Query("uid") String uid, @Query("page") int page);
    @GET("user_timeline.json")
    Observable<WeiboBean> getUserStatus(@Query("uid") String uid, @Query("screen_name") String screen_name, @Query("page") int page);

}

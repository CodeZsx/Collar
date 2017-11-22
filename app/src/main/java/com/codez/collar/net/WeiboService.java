package com.codez.collar.net;

import com.codez.collar.bean.WeiboBean;

import java.util.List;

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
    Observable<Response<List<WeiboBean>>> getWeibo(@Query("uid") String uid, @Query("page") int page);
}

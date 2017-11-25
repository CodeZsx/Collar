package com.codez.collar.net;

import com.codez.collar.bean.CommentResultBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public interface CommentService {
    String BASE_URL = Constants.BASE_URL + "comments/";
    @GET("show.json")
    Observable<CommentResultBean> getStatusComment(@Query("id") String id, @Query("page") int page);

}

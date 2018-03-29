package com.codez.collar.net;

import com.codez.collar.bean.StatusResultBean;
import com.codez.collar.bean.UserBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public interface SearchService {
    @GET("users/search.json")
    Observable<List<UserBean>> searchUsers(@Query("q") String q, @Query("page") int page, @Query("count") int count);

    @GET("statuses/search.json")
    Observable<StatusResultBean> searchStatuses(@Query("q") String q, @Query("page") int page, @Query("count") int count);

}

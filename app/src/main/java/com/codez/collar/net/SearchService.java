package com.codez.collar.net;

import com.codez.collar.bean.TopicResultBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public interface SearchService {
    String BASE_URL = Constants.BASE_URL + "search/";
    @GET("topics.json")
    Observable<TopicResultBean> getSearchTopics(@Query("q") String q, @Query("page") int page);

}
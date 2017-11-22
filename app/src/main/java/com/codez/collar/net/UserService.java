package com.codez.collar.net;


import com.codez.collar.bean.UserBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public interface UserService{
    public static final String BASE_URL = Constants.BASE_URL + "users/";
    @GET("show.json")
    Observable<UserBean> getUserInfo(@Query("uid") String uid);

}

package com.codez.collar.net;


import com.codez.collar.bean.UserBean;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public interface AccountService {

    @FormUrlEncoded
    @POST("/account/login")
    Call<ResponseBody> login(@FieldMap Map<String, Object> p);
    Observable<UserBean> getUserInfo(@Query("uid") String uid, @Query("screen_name") String screen_name);

}

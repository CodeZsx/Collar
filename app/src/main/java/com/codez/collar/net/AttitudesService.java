package com.codez.collar.net;

import com.codez.collar.bean.AttitudeResultBean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by codez on 2018/04/05.
 * Description:
 */

public interface AttitudesService {

    /**
     * 查询点赞状态
     * @param ids
     * @return
     */
    @GET("attitudes/exists.json")
    Call<ResponseBody> existsLike(@Query("ids") String ids);

    /**
     * 点赞
     * @param access_token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("attitudes/create.json")
    Observable<AttitudeResultBean> createLike(@Field("access_token") String access_token, @Field("id") String id);

    /**
     * 取消点赞
     * @param access_token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("attitudes/destroy.json")
    Observable<AttitudeResultBean> destroyLike(@Field("access_token") String access_token, @Field("id") String id);

}

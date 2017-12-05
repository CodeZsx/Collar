package com.codez.collar.net;

import com.codez.collar.bean.FriendshipResultBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public interface FriendshipService {

    @GET("friendships/friends.json")
    Observable<FriendshipResultBean> getFriends(@Query("uid") String uid, @Query("screen_name") String screen_name, @Query("trim_status") int trim_status);

    @GET("friendships/friends.json")
    Observable<FriendshipResultBean> getFollowers(@Query("uid") String uid, @Query("screen_name") String screen_name, @Query("trim_status") int trim_status);

}

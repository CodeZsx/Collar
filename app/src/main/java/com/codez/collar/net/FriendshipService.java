package com.codez.collar.net;

import com.codez.collar.bean.FavoriteBean;
import com.codez.collar.bean.FriendsIdsResultBean;
import com.codez.collar.bean.FriendshipsResultBean;
import com.codez.collar.bean.FriendshipsShowResultBean;
import com.codez.collar.bean.Group;
import com.codez.collar.bean.GroupsResult;
import com.codez.collar.bean.StatusResultBean;
import com.codez.collar.bean.UsersResultBean;

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

public interface FriendshipService {

    @GET("friendships/friends.json")
    Observable<FriendshipsResultBean> getFriends(@Query("uid") String uid, @Query("screen_name") String screen_name, @Query("trim_status") int trim_status);

    @GET("friendships/followers.json")
    Observable<FriendshipsResultBean> getFollowers(@Query("uid") String uid, @Query("screen_name") String screen_name, @Query("trim_status") int trim_status);

    @GET("friendships/friends/ids.json")
    Observable<FriendsIdsResultBean> getFriendsIds(@Query("uid") String uid, @Query("screen_name") String screen_name);

    @GET("friendships/groups.json")
    Observable<GroupsResult> getGroups();

    @GET("friendships/groups/timeline.json")
    Observable<StatusResultBean> getGroupsStatus(@Query("list_id") String list_id, @Query("page") int page);

    @GET("friendships/groups/members.json")
    Observable<UsersResultBean> getGroupsMember(@Query("list_id") String list_id);

    @GET("friendships/groups/members/destory.json")
    Observable<Group> destoryGroupsMember(@Query("list_id") String list_id, @Query("uid") String uid);

    @GET("friendships/show.json")
    Observable<FriendshipsShowResultBean> showFriendships(@Query("source_id") String source_id, @Query("target_id") String target_id);

    @FormUrlEncoded
    @POST("friendships/create.json")
    Observable<FavoriteBean> createFriendships(@Field("access_token") String access_token, @Field("uid") String uid);

    @FormUrlEncoded
    @POST("friendships/destroy.json")
    Observable<FavoriteBean> destroyFriendships(@Field("access_token") String access_token, @Field("uid") String uid);



}

package com.codez.collar.net;

import android.util.Log;

import com.codez.collar.Config;
import com.codez.collar.auth.AccessTokenKeeper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public class HttpUtils {
    private static final String TAG = "HttpUtils";

    private static final int DEFAULT_TIMEOUT = 10;

    private static HttpUtils instance;
    public static HttpUtils getInstance(){
        if (instance == null) {
            synchronized (HttpUtils.class) {
                if (instance == null) {
                    instance = new HttpUtils();
                }
            }
        }
        return instance;
    }


    public StatusService getWeiboService(){
        return getRetrofit(Constants.BASE_URL).create(StatusService.class);
    }

    public UserService getUserService() {
        return getRetrofit(Constants.BASE_URL).create(UserService.class);
    }

    public CommentService getCommentService() {
        return getRetrofit(Constants.BASE_URL).create(CommentService.class);
    }
    public TopicSearchService getTopicSearchService() {
        return getRetrofit(Constants.BASE_URL).create(TopicSearchService.class);
    }
    public FavoriteService getFavoriteService() {
        return getRetrofit(Constants.BASE_URL).create(FavoriteService.class);
    }
    public FriendshipService getFriendshipService() {
        return getRetrofit(Constants.BASE_URL).create(FriendshipService.class);
    }
    public DirectMsgService getDirectMsgService() {
        return getRetrofit(Constants.BASE_URL).create(DirectMsgService.class);
    }

    public SearchService getSearchService() {
        return getRetrofitWithAppKey(Constants.OLD_URL).create(SearchService.class);
    }

    public UpgradeService getUpgradeService() {
        return getBaseRetrofit(Constants.UPGRADE_URL).create(UpgradeService.class);
    }

    private Retrofit getRetrofit(String baseUrl) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request r = addParam(chain.request());
                        Log.i(TAG, r.method()+" "+r.url().toString());
                        return chain.proceed(r);
                    }
                    private Request addParam(Request oldRequest){
                        if (oldRequest.method().equals("POST")){
                            return oldRequest;
                        }
                        HttpUrl.Builder builder = oldRequest.url()
                                .newBuilder()
                                .setEncodedQueryParameter("access_token", AccessTokenKeeper.getInstance().getAccessToken());
                        return oldRequest.newBuilder()
                                .method(oldRequest.method(), oldRequest.body())
                                .url(builder.build())
                                .build();
                    }

                })
                .build();
        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }
    private Retrofit getRetrofitWithAppKey(String baseUrl) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request r = addParam(chain.request());
                        Log.i(TAG, r.method()+" "+r.url().toString());
                        return chain.proceed(r);
                    }
                    private Request addParam(Request oldRequest){
                        if (oldRequest.method().equals("POST")){
                            return oldRequest;
                        }
                        HttpUrl.Builder builder = oldRequest.url()
                                .newBuilder()
                                .setEncodedQueryParameter("source", Config.APP_KEY);
                        return oldRequest.newBuilder()
                                .method(oldRequest.method(), oldRequest.body())
                                .url(builder.build())
                                .build();
                    }

                })
                .build();
        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }
    private Retrofit getBaseRetrofit(String baseUrl) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request r = chain.request();
                        Log.i(TAG, r.method()+" "+r.url().toString());
                        return chain.proceed(r);
                    }
                })
                .build();
        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }
}

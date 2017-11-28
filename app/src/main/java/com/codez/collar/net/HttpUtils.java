package com.codez.collar.net;

import android.content.Context;

import com.codez.collar.auth.AccessTokenKeeper;
import com.codez.collar.utils.L;

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


    public WeiboService getWeiboService(Context context){
        return getRetrofit(context, WeiboService.BASE_URL).create(WeiboService.class);
    }

    public UserService getUserService(Context context) {
        return getRetrofit(context, UserService.BASE_URL).create(UserService.class);
    }

    public CommentService getCommentService(Context context) {
        return getRetrofit(context, CommentService.BASE_URL).create(CommentService.class);
    }
    public SearchService getSearchService(Context context) {
        return getRetrofit(context, SearchService.BASE_URL).create(SearchService.class);
    }


    private Retrofit getRetrofit(final Context context, String baseUrl) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request r = addParam(chain.request());
                        L.e(r.method()+" "+r.url().toString());
                        return chain.proceed(r);
                    }
                    private Request addParam(Request oldRequest){
                        HttpUrl.Builder builder = oldRequest.url()
                                .newBuilder()
                                .setEncodedQueryParameter("access_token", AccessTokenKeeper.getAccessToken(context));
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
}

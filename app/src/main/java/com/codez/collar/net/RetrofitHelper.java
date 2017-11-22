package com.codez.collar.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by codez on 2017/11/21.
 * Description:
 */

public class RetrofitHelper {
    private static final int DEFAULT_TIMEOUT = 10;
    private Retrofit mRetrofit;
    OkHttpClient.Builder mBuilder;

    private WeiboService weiboService;

    private static class Singleton{
        private static final RetrofitHelper INSTANCE = new RetrofitHelper();
    }

    public static RetrofitHelper getInstance() {
        return Singleton.INSTANCE;
    }

    private RetrofitHelper() {
        mBuilder = new OkHttpClient().newBuilder();
        mBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        mRetrofit = new Retrofit.Builder()
                .client(mBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(WeiboService.BASE_URL)
                .build();

        weiboService = mRetrofit.create(WeiboService.class);



    }
}

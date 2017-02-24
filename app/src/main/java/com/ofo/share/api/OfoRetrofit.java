package com.ofo.share.api;


import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class OfoRetrofit {

    private final static class SingletonHolder {
        static final OfoRetrofit instance = new OfoRetrofit();
    }

    public static OfoRetrofit get() {
        return SingletonHolder.instance;
    }

    private Retrofit retrofit;

    private OfoRetrofit() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl("http://139.196.141.95:8181/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()))
                .client(client.build());
        retrofit = retrofitBuilder.build();
    }

    private ShareApi shareApi;

    public ShareApi getShareApi() {
        if (null == shareApi)
            shareApi = retrofit.create(ShareApi.class);
        return shareApi;
    }
}

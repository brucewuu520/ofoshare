package com.ofo.share.api;


import com.google.gson.JsonArray;
import com.ofo.share.model.Result;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ShareApi {

    @POST("save")
    @FormUrlEncoded
    Call<Result> save(@Field("code") String no, @Field("password") String password);

    @POST("saveMultiple")
    Call<Result> saveAll(@Body JsonArray jsonArray);

    @POST("get")
    @FormUrlEncoded
    Call<Result> query(@Field("code") String no);
}

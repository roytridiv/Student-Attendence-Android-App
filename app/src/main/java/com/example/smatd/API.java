package com.example.smatd;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface API {

    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseBody> registration(
            @Field("EMAIL") String email,
            @Field("PASSWORD") String pass,
            @Field("NUMBER") String num
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> login(
            @Field("PASSWORD") String pass,
            @Field("NUMBER") String num
    );

    @FormUrlEncoded
    @POST("check.php")
    Call<ResponseBody> check(
            @Field("id") String id,
            @Field("number") String num
    );
    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> token(
            @Field("DEVICE_TOKEN") String DEVICE_TOKEN
    );
}

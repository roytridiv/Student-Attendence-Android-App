package com.example.smatd;

import androidx.annotation.NonNull;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface API {

    @NonNull
    @FormUrlEncoded
    @POST("isValidUser")
    Call<ResponseBody> check(
            @Field("rfid") @NonNull String id,
            @Field("mobile") @NonNull String num
    );

    @NonNull
    @FormUrlEncoded
    @POST("loginUser")
    Call<ResponseBody> login(
            @Field("rfid") @NonNull String id,
            @Field("mobile") @NonNull String num,
            @Field("password") @NonNull String pass
    );

    @NonNull
    @FormUrlEncoded
    @POST("updatePassword")
    Call<ResponseBody> updatePass(
            @Field("rfid") @NonNull String id,
            @Field("mobile") @NonNull String num,
            @Field("password") @NonNull String pass
    );

    @NonNull
    @FormUrlEncoded
    @POST("studentInfo")
    Call<ResponseBody> studentInfo(
            @Field("rfid") @NonNull String id
    );

    @NonNull
    @FormUrlEncoded
    @POST("attendanceReport")
    Call<ResponseBody> attendanceReport(
            @Field("rfid") @NonNull String id
    );
}

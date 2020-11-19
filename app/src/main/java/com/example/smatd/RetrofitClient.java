package com.example.smatd;

import androidx.annotation.NonNull;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class RetrofitClient {

//    private static  final String BASE_URL = "http://tridivroy.xyz/smatd/v1/public/api/";

    private static final String BASE_URL = "https://smatd.tridivroy.xyz/v1/public/api/" ;
    private static RetrofitClient mInstance;

    private Retrofit retrofit;

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @NonNull
    static synchronized RetrofitClient getInstance(){
        if(mInstance == null){
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    API getApi(){
        return retrofit.create(API.class);
    }
}

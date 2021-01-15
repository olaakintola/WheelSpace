package com.example.wheelspace;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

//created the interface
public interface BusAPICaller {

    @Headers({"x-api-key: 5ed5dc24ea06488c8306965194b075b4", "Cache-Control: no-cache"})
//    @GET("/?format=json")
    @GET("v1/?format=json")
    Call<BusModel> getData();
}

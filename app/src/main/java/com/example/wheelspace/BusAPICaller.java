package com.example.wheelspace;

import retrofit2.Call;
import retrofit2.http.GET;
//created the interface
public interface BusAPICaller {

    @GET("/")
    Call<BusModel> getData();
}

package com.example.wheelspace;

import com.example.wheelspace.busdata.model.BusRoute;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RouteOptionsAPICaller {

    @GET("api/directions/json?mode=transit&transit_mode=bus&alternatives=true&key=AIzaSyB4pfSZwkbrKDasVSw7hmME0sYdV36C2xY")
    Call<BusRoute> getBusOptions(
            @Query("origin") String origin,
            @Query("destination") String destination );

}

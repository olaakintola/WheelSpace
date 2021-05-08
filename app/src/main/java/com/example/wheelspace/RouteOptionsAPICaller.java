package com.example.wheelspace;

import com.example.wheelspace.busdata.model.BusRoute;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RouteOptionsAPICaller {


//    @GET("maps/api/directions/{stops_itinerary}")
//    Call<BusRoute> getBusOptions(@Path(value = "stops_itinerary", encoded = true) String stops_itinerary);

//    @GET
//    Call<BusRoute> getBusOptions(@Url String url);

    @GET("api/directions/json?mode=transit&transit_mode=bus&alternatives=true&key=AIzaSyB4pfSZwkbrKDasVSw7hmME0sYdV36C2xY")
    Call<BusRoute> getBusOptions(
            @Query("origin") String origin,
            @Query("destination") String destination );

//    @GET("maps/api/directions/json")
//    Call<BusRoute> getBusOptions(
//            @Query("origin") String origin,
//            @Query("destination") String destination,
//            @Query("mode") String mode,
//            @Query("transit_mode") String transit_mode,
//            @Query("alternatives") String alternatives,
//            @Query("key") String key );
}


//https://maps.googleapis.com/maps/api/directions/json?origin=Monastery_Rise,stop_1973&destination=Monastery_Heath,stop_1974&mode=transit&transit_mode=bus&alternatives=true&key=AIzaSyB4pfSZwkbrKDasVSw7hmME0sYdV36C2xY
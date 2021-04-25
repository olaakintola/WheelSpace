package com.example.wheelspace;

import com.example.wheelspace.busdata.model.BusRoute;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RouteOptionsAPICaller {


//    @Headers({"key: 5ed5dc24ea06488c8306965194b075b4"})
//    @GET("maps/api/directions/{stops_itinerary}")
//    Call<BusRoute> getBusOptions(@Path(value = "stops_itinerary", encoded = true) String stops_itinerary);

    @GET("maps/api/directions/json")
    Call<BusRoute> getBusOptions(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("mode") String mode,
            @Query("transit_mode") String transit_mode,
            @Query("alternatives") String alternatives,
            @Query("key") String key );
}


//https://maps.googleapis.com/maps/api/directions/json?origin=Monastery_Rise,stop_1973&destination=Monastery_Heath,stop_1974&mode=transit&transit_mode=bus&alternatives=true&key=AIzaSyB4pfSZwkbrKDasVSw7hmME0sYdV36C2xY
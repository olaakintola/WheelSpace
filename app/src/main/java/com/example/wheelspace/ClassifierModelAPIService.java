package com.example.wheelspace;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ClassifierModelAPIService {

//    @POST("/predict")
//    @FormUrlEncoded
//    Call<UserPost> getPrediction(@Field("route") String route,
//                                 @Field("times") String times,
//                                 @Field("days") String days);

//    @POST("/predict")
//    Call<UserPost> getPrediction(@Body UserPost userPost);

    @Headers("Cache-Control: no-cache")
    @POST("/predict")
    Call< UserPost> getPrediction(@Body List<UserPost> userPost);

}

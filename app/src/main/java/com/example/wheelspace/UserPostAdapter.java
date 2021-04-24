package com.example.wheelspace;

import com.squareup.moshi.ToJson;

public class UserPostAdapter {
    public UserPostAdapter() {}

    @ToJson
    UserPost toJson(UserPost userPost){
        UserPost userPost1 = new UserPost();
        userPost1.setRoute(userPost.getRoute());
        userPost1.setTimes(userPost.getTimes());
        userPost1.setDays(userPost.getDays());
        return userPost1;
    }
}

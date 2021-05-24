package com.example.wheelspace;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserPost {

    private String prediction;

    @SerializedName("route")
    @Expose
    private String route;

    @SerializedName("times")
    @Expose
    private String times;

    @SerializedName("days")
    @Expose
    private String days;

    public UserPost(){}

    public UserPost(String route, String times, String days){
        this.route = route;
        this.times = times;
        this.days = days;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    @Override
    public String toString(){
        return "User Response{" + "prediction=" + prediction + "}";
    }

}
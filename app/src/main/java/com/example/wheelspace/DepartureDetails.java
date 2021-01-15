package com.example.wheelspace;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DepartureDetails {
    @SerializedName("delay")
    @Expose
    private int delay;

//    public DepartureDetails(int delay) {
//        this.delay = delay;
//    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}

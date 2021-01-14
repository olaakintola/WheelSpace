package com.example.wheelspace;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArrivalDetails {
    @SerializedName("delay")
    @Expose
    private int delay;

    public ArrivalDetails(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}

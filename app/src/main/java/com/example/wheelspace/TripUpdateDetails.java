package com.example.wheelspace;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class
TripUpdateDetails {

    @SerializedName("trip")
    @Expose
    private TripDetails tripDetails;
    @SerializedName("stop_time_update")
    @Expose
    private List<StopSequenceDetails> stop_time_update = new ArrayList<>();

    public TripDetails getTripDetails() {
        return tripDetails;
    }

    public void setTripDetails(TripDetails tripDetails) {
        this.tripDetails = tripDetails;
    }

    public List<StopSequenceDetails> getStop_time_update() {
        return stop_time_update;
    }

    public void setStop_time_update(List<StopSequenceDetails> stop_time_update) {
        this.stop_time_update = stop_time_update;
    }
}

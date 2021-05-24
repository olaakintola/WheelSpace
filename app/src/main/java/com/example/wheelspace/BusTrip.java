package com.example.wheelspace;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BusTrip {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("trip_update")
    @Expose
    private TripUpdateDetails tripUpdateDetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TripUpdateDetails getTripUpdateDetails() {
        return tripUpdateDetails;
    }

    public void setTripUpdateDetails(TripUpdateDetails tripUpdateDetails) {
        this.tripUpdateDetails = tripUpdateDetails;
    }
}

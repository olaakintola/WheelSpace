package com.example.wheelspace;

public class BusTrip {
    private String id;
    private TripUpdateDetails tripUpdateDetails;

    public BusTrip(String id, TripUpdateDetails tripUpdateDetails) {
        this.id = id;
        this.tripUpdateDetails = tripUpdateDetails;
    }

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

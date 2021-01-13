package com.example.wheelspace;

public class TripUpdateDetails {
    private TripDetails tripDetails;
    private StopSequenceDetails[] stop_time_update;

    public TripUpdateDetails(TripDetails tripDetails, StopSequenceDetails[] stop_time_update) {
        this.tripDetails = tripDetails;
        this.stop_time_update = stop_time_update;
    }

    public TripDetails getTripDetails() {
        return tripDetails;
    }

    public void setTripDetails(TripDetails tripDetails) {
        this.tripDetails = tripDetails;
    }

    public StopSequenceDetails[] getStop_time_update() {
        return stop_time_update;
    }

    public void setStop_time_update(StopSequenceDetails[] stop_time_update) {
        this.stop_time_update = stop_time_update;
    }
}

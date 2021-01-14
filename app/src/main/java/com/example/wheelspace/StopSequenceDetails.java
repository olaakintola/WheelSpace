package com.example.wheelspace;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StopSequenceDetails {
    @SerializedName("stop_sequence")
    @Expose
    private int stop_sequence;
    @SerializedName("departure")
    @Expose
    private DepartureDetails departureDetails;
    @SerializedName("arrival")
    @Expose
    private ArrivalDetails arrivalDetails;
    @SerializedName("stop_id")
    @Expose
    private String stop_id;
    @SerializedName("schedule_relationship")
    @Expose
    private String schedule_relationship;

    public StopSequenceDetails(int stop_sequence, DepartureDetails departureDetails, ArrivalDetails arrivalDetails, String stop_id, String schedule_relationship) {
        this.stop_sequence = stop_sequence;
        this.departureDetails = departureDetails;
        this.arrivalDetails = arrivalDetails;
        this.stop_id = stop_id;
        this.schedule_relationship = schedule_relationship;
    }

    public int getStop_sequence() {
        return stop_sequence;
    }

    public void setStop_sequence(int stop_sequence) {
        this.stop_sequence = stop_sequence;
    }

    public DepartureDetails getDepartureDetails() {
        return departureDetails;
    }

    public void setDepartureDetails(DepartureDetails departureDetails) {
        this.departureDetails = departureDetails;
    }

    public ArrivalDetails getArrivalDetails() {
        return arrivalDetails;
    }

    public void setArrivalDetails(ArrivalDetails arrivalDetails) {
        this.arrivalDetails = arrivalDetails;
    }

    public String getStop_id() {
        return stop_id;
    }

    public void setStop_id(String stop_id) {
        this.stop_id = stop_id;
    }

    public String getSchedule_relationship() {
        return schedule_relationship;
    }

    public void setSchedule_relationship(String schedule_relationship) {
        this.schedule_relationship = schedule_relationship;
    }
}

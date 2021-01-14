package com.example.wheelspace;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TripDetails {
    @SerializedName("trip_id")
    @Expose
    private String trip_id;
    @SerializedName("start_time")
    @Expose
    private String start_time;
    @SerializedName("start_date")
    @Expose
    private String start_date;
    @SerializedName("schedule_relationship")
    @Expose
    private String schedule_relationship;
    @SerializedName("route_id")
    @Expose
    private String route_id;

    public TripDetails(String trip_id, String start_time, String start_date, String schedule_relationship, String route_id) {
        this.trip_id = trip_id;
        this.start_time = start_time;
        this.start_date = start_date;
        this.schedule_relationship = schedule_relationship;
        this.route_id = route_id;
    }

    public String getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getSchedule_relationship() {
        return schedule_relationship;
    }

    public void setSchedule_relationship(String schedule_relationship) {
        this.schedule_relationship = schedule_relationship;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }
}

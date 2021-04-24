package com.example.wheelspace.busdata.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Step {

    @SerializedName("distance")
    @Expose
    private Distance__1 distance;
    @SerializedName("duration")
    @Expose
    private Duration__1 duration;
    @SerializedName("end_location")
    @Expose
    private EndLocation__1 endLocation;
    @SerializedName("html_instructions")
    @Expose
    private String htmlInstructions;
    @SerializedName("polyline")
    @Expose
    private Polyline polyline;
    @SerializedName("start_location")
    @Expose
    private StartLocation__1 startLocation;
    @SerializedName("transit_details")
    @Expose
    private TransitDetails transitDetails;
    @SerializedName("travel_mode")
    @Expose
    private String travelMode;
    @SerializedName("steps")
    @Expose
    private List<Step__1> steps = null;

    public Distance__1 getDistance() {
        return distance;
    }

    public void setDistance(Distance__1 distance) {
        this.distance = distance;
    }

    public Duration__1 getDuration() {
        return duration;
    }

    public void setDuration(Duration__1 duration) {
        this.duration = duration;
    }

    public EndLocation__1 getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(EndLocation__1 endLocation) {
        this.endLocation = endLocation;
    }

    public String getHtmlInstructions() {
        return htmlInstructions;
    }

    public void setHtmlInstructions(String htmlInstructions) {
        this.htmlInstructions = htmlInstructions;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public StartLocation__1 getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(StartLocation__1 startLocation) {
        this.startLocation = startLocation;
    }

    public TransitDetails getTransitDetails() {
        return transitDetails;
    }

    public void setTransitDetails(TransitDetails transitDetails) {
        this.transitDetails = transitDetails;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public List<Step__1> getSteps() {
        return steps;
    }

    public void setSteps(List<Step__1> steps) {
        this.steps = steps;
    }

}

package com.example.wheelspace;

public class WheelBayStatus {
    String tripid;
    String route;
    String departure;
    String destination;
    String time;
    String intermediarystops;

    public WheelBayStatus() {
    }

    public WheelBayStatus(String tripid, String route, String departure, String destination, String time, String intermediarystops) {
        this.tripid = tripid;
        this.route = route;
        this.departure = departure;
        this.destination = destination;
        this.time = time;
        this.intermediarystops = intermediarystops;
    }

    public String getTripid() {
        return tripid;
    }

    public void setTripid(String tripid) {
        this.tripid = tripid;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIntermediarystops() {
        return intermediarystops;
    }

    public void setIntermediarystops(String intermediarystops) {
        this.intermediarystops = intermediarystops;
    }
}

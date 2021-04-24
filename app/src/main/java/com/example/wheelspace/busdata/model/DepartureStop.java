package com.example.wheelspace.busdata.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DepartureStop {

    @SerializedName("location")
    @Expose
    private Location__1 location;
    @SerializedName("name")
    @Expose
    private String name;

    public Location__1 getLocation() {
        return location;
    }

    public void setLocation(Location__1 location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

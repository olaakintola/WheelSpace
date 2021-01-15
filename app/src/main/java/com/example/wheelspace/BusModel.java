package com.example.wheelspace;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

// This class will be as a template for the data that we are going to parse
public class BusModel {
    // making object of class header
    @SerializedName("header")
    @Expose
    private HeaderDetails headerDetails;
    // making object of class entities
    @SerializedName("entity")
    @Expose
    private List<BusTrip> busEntities = new ArrayList<>();

//    public BusModel(HeaderDetails headerDetails, List<BusTrip> busEntities) {
//        this.headerDetails = headerDetails;
//        this.busEntities = busEntities;
//    }

    public HeaderDetails getHeaderDetails() {
        return headerDetails;
    }

    public void setHeaderDetails(HeaderDetails headerDetails) {
        this.headerDetails = headerDetails;
    }

    public List<BusTrip> getBusEntities() {
        return busEntities;
    }

    public void setBusEntities(List<BusTrip> busEntities) {
        this.busEntities = busEntities;
    }
}

package com.example.wheelspace;

// This class will be as a template for the data that we are going to parse
public class BusModel {
    // making object of class header
    private HeaderDetails headerDetails;
    // making object of class entities
    private BusEntities busEntities;

    public BusModel(HeaderDetails headerDetails, BusEntities busEntities) {
        this.headerDetails = headerDetails;
        this.busEntities = busEntities;
    }

    public HeaderDetails getHeaderDetails() {
        return headerDetails;
    }

    public void setHeaderDetails(HeaderDetails headerDetails) {
        this.headerDetails = headerDetails;
    }

    public BusEntities getBusEntities() {
        return busEntities;
    }

    public void setBusEntities(BusEntities busEntities) {
        this.busEntities = busEntities;
    }
}

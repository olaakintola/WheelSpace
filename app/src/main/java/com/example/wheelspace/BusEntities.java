package com.example.wheelspace;

public class BusEntities {

    private BusTrip[] entity;

    public BusEntities(BusTrip[] entity) {
        this.entity = entity;
    }

    public BusTrip[] getEntity() {
        return entity;
    }

    public void setEntity(BusTrip[] entity) {
        this.entity = entity;
    }
}

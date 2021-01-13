package com.example.wheelspace;

public class StopSequenceDetails {
    private int stop_sequence;
    private DepartureDetails departureDetails;
    private ArrivalDetails arrivalDetails;
    private String stop_id;
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

package com.example.wheelspace;

public class Feedback {
    String routeFeedback;
    String issueFeedback;
    String timeFeedback;
    String departureFeedback;
    String destinationFeedback;
    String descriptionFeedback;
    String generatedDate;

    public Feedback() {
    }

    public Feedback(String routeFeedback, String issueFeedback, String timeFeedback, String departureFeedback, String destinationFeedback, String descriptionFeedback, String generatedDate) {
        this.routeFeedback = routeFeedback;
        this.issueFeedback = issueFeedback;
        this.timeFeedback = timeFeedback;
        this.departureFeedback = departureFeedback;
        this.destinationFeedback = destinationFeedback;
        this.descriptionFeedback = descriptionFeedback;
        this.generatedDate = generatedDate;
    }

    public String getRouteFeedback() {
        return routeFeedback;
    }

    public String getIssueFeedback() {
        return issueFeedback;
    }

    public String getTimeFeedback() {
        return timeFeedback;
    }

    public String getDepartureFeedback() {
        return departureFeedback;
    }

    public String getDestinationFeedback() {
        return destinationFeedback;
    }

    public String getDescriptionFeedback() {
        return descriptionFeedback;
    }

    public String getGeneratedDate(){ return generatedDate; }
}

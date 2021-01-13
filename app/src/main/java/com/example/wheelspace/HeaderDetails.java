package com.example.wheelspace;

public class HeaderDetails {
    private String gtfs_realtime_version;
    private Long timestamp;

    public HeaderDetails(String gtfs_realtime_version, Long timestamp) {
        this.gtfs_realtime_version = gtfs_realtime_version;
        this.timestamp = timestamp;
    }

    public String getGtfs_realtime_version() {
        return gtfs_realtime_version;
    }

    public void setGtfs_realtime_version(String gtfs_realtime_version) {
        this.gtfs_realtime_version = gtfs_realtime_version;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}

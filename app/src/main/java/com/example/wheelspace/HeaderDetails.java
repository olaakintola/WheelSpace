package com.example.wheelspace;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HeaderDetails {
    @SerializedName("gtfs_realtime_version")
    @Expose
    private String gtfs_realtime_version;
    @SerializedName("timestamp")
    @Expose
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

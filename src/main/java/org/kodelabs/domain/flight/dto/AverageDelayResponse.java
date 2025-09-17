package org.kodelabs.domain.flight.dto;

public class AverageDelayResponse {
    private String groupKey;
    private double avgDepartureDelayMinutes;
    private double avgArrivalDelayMinutes;

    public AverageDelayResponse(String groupKey, double avgDepartureDelayMinutes, double avgArrivalDelayMinutes) {
        this.groupKey = groupKey;
        this.avgDepartureDelayMinutes = avgDepartureDelayMinutes;
        this.avgArrivalDelayMinutes = avgArrivalDelayMinutes;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public double getAvgDepartureDelayMinutes() {
        return avgDepartureDelayMinutes;
    }

    public void setAvgDepartureDelayMinutes(double avgDepartureDelayMinutes) {
        this.avgDepartureDelayMinutes = avgDepartureDelayMinutes;
    }

    public double getAvgArrivalDelayMinutes() {
        return avgArrivalDelayMinutes;
    }

    public void setAvgArrivalDelayMinutes(double avgArrivalDelayMinutes) {
        this.avgArrivalDelayMinutes = avgArrivalDelayMinutes;
    }
}

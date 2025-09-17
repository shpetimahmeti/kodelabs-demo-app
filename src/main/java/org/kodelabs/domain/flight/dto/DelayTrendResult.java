package org.kodelabs.domain.flight.dto;

public class DelayTrendResult {
    private String id;
    private int delayCount;
    private double avgDepartureDelay;
    private double totalDepartureDelay;

    public DelayTrendResult(String id, int delayCount, double avgDepartureDelay, double totalDepartureDelay) {
        this.id = id;
        this.delayCount = delayCount;
        this.avgDepartureDelay = avgDepartureDelay;
        this.totalDepartureDelay = totalDepartureDelay;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDelayCount() {
        return delayCount;
    }

    public void setDelayCount(int delayCount) {
        this.delayCount = delayCount;
    }

    public double getAvgDepartureDelay() {
        return avgDepartureDelay;
    }

    public void setAvgDepartureDelay(double avgDepartureDelay) {
        this.avgDepartureDelay = avgDepartureDelay;
    }

    public double getTotalDepartureDelay() {
        return totalDepartureDelay;
    }

    public void setTotalDepartureDelay(double totalDepartureDelay) {
        this.totalDepartureDelay = totalDepartureDelay;
    }
}

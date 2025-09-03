package org.kodelabs.domain.airport.model;

import java.util.List;

public class Location {
    private String type;
    private List<Double> coordinates;

    public Location() {
    }

    public String getType() {
        return type;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}

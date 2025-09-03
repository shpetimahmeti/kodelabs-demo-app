package org.kodelabs.domain.flight.dto;

import org.kodelabs.domain.flight.entity.FlightEntity;

import java.util.List;

public class FlightWithConnections extends FlightEntity {
    private List<FlightEntity> connections;

    public List<FlightEntity> getConnections() {
        return connections;
    }

    public void setConnections(List<FlightEntity> connections) {
        this.connections = connections;
    }
}

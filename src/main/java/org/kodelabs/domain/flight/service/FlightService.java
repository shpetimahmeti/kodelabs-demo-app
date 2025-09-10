package org.kodelabs.domain.flight.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kodelabs.domain.flight.dto.FlightDTO;
import org.kodelabs.domain.flight.dto.FlightRouteResponse;
import org.kodelabs.domain.flight.dto.FlightWithConnections;
import org.kodelabs.domain.flight.entity.FlightEntity;
import org.kodelabs.domain.flight.exception.FlightNotFoundException;
import org.kodelabs.domain.flight.mapper.FlightMapper;
import org.kodelabs.domain.flight.repository.FlightRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class FlightService {

    @Inject
    FlightRepository repository;

    public Uni<FlightDTO> findOneByObjectId(String objectId) {
        return repository.findOneByObjectId(objectId)
                .onItem().ifNull().failWith(() -> new FlightNotFoundException(objectId))
                .onItem().transform(FlightMapper::fromEntity);
    }

    public Multi<FlightRouteResponse> findConnectionsFromOriginToDestination(String originIata,
                                                                             String destIata,
                                                                             LocalDate departureDateMin,
                                                                             LocalDate departureDateMax) {

        //TODO sort by price
        return repository.findConnectionsFromOriginToDestination(
                originIata,
                destIata,
                departureDateMin,
                departureDateMax).onItem().transformToMultiAndMerge(flightWithConnections -> {

            Map<String, List<FlightEntity>> entitiesGraph = buildGraph(flightWithConnections);

            List<List<FlightEntity>> fullPathsFromOriginToDest = new ArrayList<>();

            findPathsRecursive(originIata, destIata, entitiesGraph, new ArrayList<>(), fullPathsFromOriginToDest);


            List<FlightRouteResponse> routes = fullPathsFromOriginToDest.stream().map(
                    fullPath -> FlightMapper.buildFlightRouteResponse(originIata, destIata, fullPath)
            ).toList();

            return Multi.createFrom().iterable(routes);
        });
    }

    private Map<String, List<FlightEntity>> buildGraph(FlightWithConnections flightWithConnections) {
        Map<String, List<FlightEntity>> graph = new HashMap<>();

        graph.computeIfAbsent(flightWithConnections.getFrom().getIata(), k -> new ArrayList<>())
                .add(flightWithConnections);

        flightWithConnections.getConnections().forEach(conn ->
                graph.computeIfAbsent(conn.getFrom().getIata(), k -> new ArrayList<>())
                        .add(conn)
        );

        return graph;
    }

    private static void findPathsRecursive(
            String current,
            String end,
            Map<String, List<FlightEntity>> graph,
            List<FlightEntity> currentPath,
            List<List<FlightEntity>> allPaths) {

        if (current.equals(end)) {
            allPaths.add(new ArrayList<>(currentPath));
            return;
        }

        if (!graph.containsKey(current)) {
            return;
        }

        for (FlightEntity flight : graph.get(current)) {
            String to = flight.getTo().getIata();
            if (currentPath.stream().noneMatch(f -> f.getFrom().getIata().equals(to))) {
                currentPath.add(flight);
                findPathsRecursive(to, end, graph, currentPath, allPaths);
                currentPath.removeLast();
            }
        }
    }
}

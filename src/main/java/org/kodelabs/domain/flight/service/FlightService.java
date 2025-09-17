package org.kodelabs.domain.flight.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import org.kodelabs.domain.flight.dto.AverageDelayResponse;
import org.kodelabs.domain.flight.dto.DelayTrendResult;
import org.kodelabs.domain.flight.dto.FlightAvailabilityResponse;
import org.kodelabs.domain.flight.dto.FlightDTO;
import org.kodelabs.domain.flight.dto.FlightRouteResponse;
import org.kodelabs.domain.flight.dto.FlightWithConnections;
import org.kodelabs.domain.flight.dto.UpdateFlightStatusRequest;
import org.kodelabs.domain.flight.entity.FlightEntity;
import org.kodelabs.domain.flight.exception.FlightNotFoundException;
import org.kodelabs.domain.flight.exception.InvalidFlightStatusTransitionException;
import org.kodelabs.domain.flight.mapper.FlightMapper;
import org.kodelabs.domain.flight.model.Seat;
import org.kodelabs.domain.flight.repository.FlightRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.kodelabs.domain.common.mongo.Fields.FlightFields.AIRLINE;
import static org.kodelabs.domain.common.mongo.Fields.FlightFields.PUBLISHED_FLIGHT_NUMBER;

@ApplicationScoped
public class FlightService {

    @Inject
    FlightRepository repository;

    public Uni<FlightDTO> findOneByObjectId(String objectId) {
        return findOneByFlightId(objectId).onItem().transform(FlightMapper::toDto);
    }

    public Uni<FlightAvailabilityResponse> getSeatAvailability(String objectId) {
        return findOneByFlightId(objectId).onItem().ifNotNull().transform(flight -> {

            Map<String, List<Seat>> grouped = flight.getSeats().stream()
                    .collect(Collectors.groupingBy(Seat::getSeatClass));

            List<FlightAvailabilityResponse.SeatAvailability> seatClasses =
                    grouped.entrySet().stream()
                            .map(entry -> {
                                String seatClass = entry.getKey();
                                List<Seat> seats = entry.getValue();

                                int total = seats.size();
                                int booked = (int) seats.stream().filter(s -> !s.isAvailable()).count();
                                int available = total - booked;

                                return new FlightAvailabilityResponse.SeatAvailability(
                                        seatClass, total, booked, available
                                );
                            })
                            .toList();

            return new FlightAvailabilityResponse(flight.get_id(), seatClasses);
        });
    }

    public Uni<FlightDTO> updateFlightStatus(String id, UpdateFlightStatusRequest request) {
        return repository.updateFlightStatus(
                        id,
                        request.getStatus(),
                        request.getNewPlannedArrivalTime(),
                        request.getNewPlannedDepartureTime(),
                        request.getActualDepartureTime(),
                        request.getActualArrivalTime()
                )
                .onItem().ifNull().failWith(() -> new InvalidFlightStatusTransitionException(request.getStatus().toValue()))
                .map(FlightMapper::toDto);
    }

    public Uni<List<AverageDelayResponse>> getAverageDelaysByFlightNumber() {
        return repository.getAverageDelays(PUBLISHED_FLIGHT_NUMBER);
    }

    public Uni<List<AverageDelayResponse>> getAverageDelaysByAirline() {
        return repository.getAverageDelays(AIRLINE);
    }

    public Uni<List<DelayTrendResult>> getDelayTrendsOverTime(String unit) {
        ChronoUnit chronoUnit = switch (unit.toUpperCase()) {
            case "DAYS" -> ChronoUnit.DAYS;
            case "WEEKS" -> ChronoUnit.WEEKS;
            case "MONTHS" -> ChronoUnit.MONTHS;
            default -> throw new BadRequestException("Invalid unit: " + unit);
        };

        return repository.getDelayTrendsOverTime(chronoUnit);
    }

    public Multi<FlightRouteResponse> findConnectionsFromOriginToDestination(String originIata,
                                                                             String destIata,
                                                                             LocalDate departureDateMin,
                                                                             LocalDate departureDateMax) {
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
                }).collect().asList()
                .onItem().transform(sortedRoutes -> {
                    sortedRoutes.sort(Comparator.comparingInt(FlightRouteResponse::getTotalPrice));
                    return sortedRoutes;
                })
                .onItem().transformToMulti(Multi.createFrom()::iterable);
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

    //TODO add depth
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

    private Uni<FlightEntity> findOneByFlightId(String objectId) {
        return repository.findOneByObjectId(objectId).onItem().ifNull().failWith(() -> new FlightNotFoundException(objectId));
    }
}

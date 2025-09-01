package org.kodelabs.domain.route.builder;

import org.kodelabs.domain.flight.dto.FlightSearchResponse;
import org.kodelabs.domain.segment.SegmentEntity;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class FlightSearchResponseBuilder {

    public static FlightSearchResponse buildFlightSearchResponse(
            String origin,
            String destination,
            List<SegmentEntity> segments) {

        FlightSearchResponse response = new FlightSearchResponse();

        response.setOrigin(origin);
        response.setDestination(destination);
        response.setLegs(new ArrayList<>());
        response.setGroups(new ArrayList<>());

        //MOCKED VALUE
        response.setTotalPrice(new FlightSearchResponse.TotalPrice(0, "MOCKED_VALUE"));

        if (segments == null || segments.isEmpty()) {
            return response;
        }

        response.setStops(segments.size() - 1);

        for (int i = 0; i < segments.size(); i++) {
            SegmentEntity segment = segments.get(i);

            FlightSearchResponse.Leg leg = new FlightSearchResponse.Leg();

            leg.setLegId(segment.flightIds.getFirst());
            leg.setRouteReferenceId(segment.routeInstanceId.toString());
            leg.setOrigin(segment.from);
            leg.setDestination(segment.to);
            leg.setPublishedFlightNumber("MOCKED_VALUE");
            leg.setOperatingFlightNumber("MOCKED_VALUE");
            leg.setDepartureTime(segment.departureTime);
            leg.setArrivalTime(segment.arrivalTime);

            if (i < segments.size() - 1) {
                Instant nextDep = segments.get(i + 1).departureTime;
                int minutes = (int) Duration.between(leg.getArrivalTime(), nextDep).toMinutes();
                leg.setLayoverMinutesToNext(minutes);
            } else {
                leg.setLayoverMinutesToNext(null);
            }
            response.getLegs().add(leg);
        }

        String currentRouteRef = null;
        FlightSearchResponse.Group currentGroup = null;

        //map<reference,
        Map<String, FlightSearchResponse.Group> groupMap = new LinkedHashMap<>();

        for (FlightSearchResponse.Leg leg : response.getLegs()) {
            groupMap.computeIfAbsent(leg.getRouteReferenceId(), routeRef -> {
                FlightSearchResponse.Group g = new FlightSearchResponse.Group();
                g.setGroupId("group-" + routeRef);
                g.setRouteReferenceId(routeRef);
                g.setLegs(new ArrayList<>());
                return g;
            }).getLegs().add(leg.getLegId());
        }

        response.setGroups(new ArrayList<>(groupMap.values()));

        response.setSelfTransfer(false);

        for (int i = 0; i < response.getGroups().size(); i++) {
            FlightSearchResponse.Group group = response.getGroups().get(i);

            if (group.getLegs().size() == 1) {
                String type = (response.getGroups().size() == 1) ? "DIRECT" : "DIFFERENT_ROUTE";
                group.setGroupType(type);
            } else {
                group.setGroupType("SAME_ROUTE");
            }

            if (i > 0 && !Objects.equals(group.getRouteReferenceId(), response.getGroups().get(i - 1).getRouteReferenceId())) {

                response.setSelfTransfer(true);
            }
        }


        return response;
    }
}

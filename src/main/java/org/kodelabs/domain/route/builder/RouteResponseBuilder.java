package org.kodelabs.domain.route.builder;

import org.kodelabs.domain.route.dto.RouteSearchResponse;
import org.kodelabs.domain.segment.entity.SegmentEntity;
import org.kodelabs.domain.segment.model.SegmentPrice;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class RouteResponseBuilder {

    public static RouteSearchResponse buildFlightSearchResponse(
            String origin,
            String destination,
            int firstSegmentPrice,
            List<SegmentEntity> segments) {

        RouteSearchResponse response = new RouteSearchResponse();

        response.setOrigin(origin);
        response.setDestination(destination);
        response.setLegs(new ArrayList<>());
        response.setGroups(new ArrayList<>());

        if (segments == null || segments.isEmpty()) {
            return response;
        }

        response.setStops(segments.size() - 1);

        int totalPrice = firstSegmentPrice;

        for (int i = 0; i < segments.size(); i++) {
            SegmentEntity segment = segments.get(i);
            totalPrice += segment.getPrice().getAmount();

            RouteSearchResponse.Leg leg = new RouteSearchResponse.Leg();

            leg.setLegId(segment.getFlightIds().getFirst());
            leg.setRouteReferenceId(segment.getRouteInstanceId().toString());
            leg.setOrigin(segment.getFrom());
            leg.setDestination(segment.getTo());
            leg.setPublishedFlightNumber("MOCKED_VALUE");
            leg.setOperatingFlightNumber("MOCKED_VALUE");
            leg.setDepartureTime(segment.getDepartureTime());
            leg.setArrivalTime(segment.getArrivalTime());

            if (i < segments.size() - 1) {
                Instant nextDep = segments.get(i + 1).getDepartureTime();
                int minutes = (int) Duration.between(leg.getArrivalTime(), nextDep).toMinutes();
                leg.setLayoverMinutesToNext(minutes);
            } else {
                leg.setLayoverMinutesToNext(null);
            }
            response.getLegs().add(leg);
        }

        //MOCKED VALUE
        //TODO remove firstSegmentPrice if only one leg
        response.setTotalPrice(new SegmentPrice(totalPrice, "MOCKED_VALUE"));

        Map<String, RouteSearchResponse.Group> groupMap = new LinkedHashMap<>();

        for (RouteSearchResponse.Leg leg : response.getLegs()) {
            groupMap.computeIfAbsent(leg.getRouteReferenceId(), routeRef -> {
                RouteSearchResponse.Group g = new RouteSearchResponse.Group();
                g.setGroupId("group-" + routeRef);
                g.setRouteReferenceId(routeRef);
                g.setLegs(new ArrayList<>());
                return g;
            }).getLegs().add(leg.getLegId());
        }

        response.setGroups(new ArrayList<>(groupMap.values()));
        response.setSelfTransfer(false);

        if (response.getGroups().size() > 1) {
            response.setSelfTransfer(true);
        }

        return response;
    }
}

package org.kodelabs.domain.route;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kodelabs.domain.segment.SegmentEntity;
import org.kodelabs.domain.segment.SegmentRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@ApplicationScoped
public class RouteService {

    @Inject
    RouteRepository routeRepository;

    @Inject
    SegmentRepository segmentRepository;

    public Uni<List<RouteEntity>> getAllRoutes(String originIata, String destinationIata, LocalDate localDate) {
        return routeRepository.getAllRoutes(originIata, destinationIata, localDate).collect().asList();
    }

    public Uni<RouteEntity> getFirstRoute() {
        return routeRepository.getFirstRoute();
    }

    public Uni<SegmentEntity> getFirstSegment() {
        return segmentRepository.getFirstSegment();
    }

    public Uni<List<SegmentEntity>> getAllSegments(String originIata,
                                                   String destinationIata,
                                                   LocalDate startDepartureDate,
                                                   LocalDate endDepartureDate) {
        return segmentRepository.findSegments(
                originIata,
                destinationIata,
                startDepartureDate,
                endDepartureDate
        ).collect().asList();
    }
}

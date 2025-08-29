package org.kodelabs.domain.route;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kodelabs.domain.segment.SegmentEntity;
import org.kodelabs.domain.segment.SegmentRepository;
import org.kodelabs.domain.segment.SegmentWithConnections;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

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

    public Uni<List<SegmentWithConnections>> getAllSegments(String originIata,
                                                            String destinationIata,
                                                            LocalDate startDepartureDate,
                                                            LocalDate endDepartureDate) {

        return segmentRepository.findSegments(
                originIata,
                destinationIata,
                startDepartureDate,
                endDepartureDate
        ).collect().asList().invoke(allSegments -> {

            allSegments.forEach(segment -> {
                Map<String, List<String>> graph = new HashMap<>();

                graph.put(segment.from, new ArrayList<>(List.of(segment.to)));
                segment.connections.forEach(connection -> {
                    graph.putIfAbsent(connection.from, new ArrayList<>());
                    graph.get(connection.from).add(connection.to);
                });

                List<List<String>> paths = findAllPaths(originIata, destinationIata, graph);
            });
        });
    }

    private static List<List<String>> findAllPaths(String start, String end, Map<String, List<String>> graph) {
        List<List<String>> allPaths = new ArrayList<>();
        List<String> currentPath = new ArrayList<>();
        findPathsRecursive(start, end, graph, currentPath, allPaths);
        return allPaths;
    }

    private static void findPathsRecursive(String current, String end, Map<String, List<String>> graph, List<String> currentPath, List<List<String>> allPaths) {
        currentPath.add(current);

        if (current.equals(end)) {
            allPaths.add(new ArrayList<>(currentPath));
        } else if (graph.containsKey(current)) {
            for (String neighbor : graph.get(current)) {
                if (!currentPath.contains(neighbor)) {
                    findPathsRecursive(neighbor, end, graph, currentPath, allPaths);
                }
            }
        }

        currentPath.removeLast();
    }
}

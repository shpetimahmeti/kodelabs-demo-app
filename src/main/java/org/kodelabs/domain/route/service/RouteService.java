package org.kodelabs.domain.route.service;

import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kodelabs.domain.route.dto.RouteSearchResponse;
import org.kodelabs.domain.route.builder.RouteResponseBuilder;
import org.kodelabs.domain.segment.*;
import org.kodelabs.domain.segment.entity.SegmentEntity;

import java.time.LocalDate;
import java.util.*;

@ApplicationScoped
public class RouteService {

    @Inject
    SegmentRepository segmentRepository;

    public Multi<RouteSearchResponse> findRoutes(String originIata,
                                                 String destinationIata,
                                                 LocalDate startDepartureDate,
                                                 LocalDate endDepartureDate) {

        return segmentRepository.findSegments(
                originIata,
                destinationIata,
                startDepartureDate,
                endDepartureDate
        ).onItem().transformToMultiAndMerge(segmentWithConnections -> {
            Map<String, List<String>> graph = new HashMap<>();
            Map<String, SegmentEntity> segmentsMap = new HashMap<>();

            buildGraphAndSegmentsMap(segmentWithConnections, graph, segmentsMap);

            List<List<String>> paths = findAllPaths(originIata, destinationIata, graph);
            List<List<String>> listOfConnectedPaths = new ArrayList<>();

            for (List<String> arr : paths) {
                List<String> pairs = new ArrayList<>();
                for (int i = 0; i < arr.size() - 1; i++) {
                    pairs.add(arr.get(i) + "-" + arr.get(i + 1));
                }
                listOfConnectedPaths.add(pairs);
            }

            List<RouteSearchResponse> routeSearchResponse = new ArrayList<>();
            listOfConnectedPaths.forEach(path -> {
                List<SegmentEntity> connectedSegmentsThatFormFullPath = path.stream().map(segmentsMap::get).toList();
                RouteSearchResponse response = RouteResponseBuilder.buildFlightSearchResponse(
                        originIata,
                        destinationIata,
                        segmentWithConnections.getPrice().getAmount(),
                        connectedSegmentsThatFormFullPath);
                routeSearchResponse.add(response);
            });

            return Multi.createFrom().iterable(routeSearchResponse);
        });
    }

    private void buildGraphAndSegmentsMap(
            SegmentWithConnections segmentWithConnections,
            Map<String, List<String>> graphToPopulate,
            Map<String, SegmentEntity> segmentsMapToPopulate) {

        graphToPopulate.put(segmentWithConnections.getFrom(), new ArrayList<>(List.of(segmentWithConnections.getTo())));
        segmentsMapToPopulate.put(segmentWithConnections.getSegmentId(), segmentWithConnections);

        segmentWithConnections.connections.forEach(connection -> {
            segmentsMapToPopulate.put(connection.getSegmentId(), connection);

            graphToPopulate.putIfAbsent(connection.getFrom(), new ArrayList<>());
            graphToPopulate.get(connection.getFrom()).add(connection.getTo());
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

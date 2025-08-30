package org.kodelabs.domain.route;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kodelabs.domain.segment.*;

import java.time.LocalDate;
import java.util.*;

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

    public Multi<GroupedSegmentsWrapper> getAllSegments(String originIata,
                                                        String destinationIata,
                                                        LocalDate startDepartureDate,
                                                        LocalDate endDepartureDate) {

        return segmentRepository.findSegments(
                originIata,
                destinationIata,
                startDepartureDate,
                endDepartureDate
        ).onItem().transform(segmentWithConnections -> {
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

            List<GroupedSegments> orderedResult =
                    listOfConnectedPaths.stream()
                            .map(temp -> {
                                GroupedSegments group = new GroupedSegments();

                                temp.forEach(path -> {
                                    SegmentEntity entity = Optional.ofNullable(segmentsMap.get(path))
                                            .orElseThrow(() -> new RuntimeException("Segment " + path + " not found"));

                                    group.getSegmentsGroupedByRouteId()
                                            .computeIfAbsent(entity.routeInstanceId.toString(), k -> new ArrayList<>())
                                            .add(entity);
                                });

                                return group;
                            })
                            .toList();
            return new GroupedSegmentsWrapper(orderedResult);
        });
    }

    private void buildGraphAndSegmentsMap(
            SegmentWithConnections segmentWithConnections,
            Map<String, List<String>> graphToPopulate,
            Map<String, SegmentEntity> segmentsMapToPopulate) {

        graphToPopulate.put(segmentWithConnections.from, new ArrayList<>(List.of(segmentWithConnections.to)));
        segmentsMapToPopulate.put(segmentWithConnections.segmentId, segmentWithConnections);

        segmentWithConnections.connections.forEach(connection -> {
            segmentsMapToPopulate.put(connection.segmentId, connection);

            graphToPopulate.putIfAbsent(connection.from, new ArrayList<>());
            graphToPopulate.get(connection.from).add(connection.to);
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

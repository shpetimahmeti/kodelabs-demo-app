package org.kodelabs.domain.segment;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GroupedSegments {
    private final Map<String, List<SegmentEntity>> segmentsGroupedByRouteId = new LinkedHashMap<>();

    public GroupedSegments() {
    }

    public Map<String, List<SegmentEntity>> getSegmentsGroupedByRouteId(){
        return this.segmentsGroupedByRouteId;
    }
}

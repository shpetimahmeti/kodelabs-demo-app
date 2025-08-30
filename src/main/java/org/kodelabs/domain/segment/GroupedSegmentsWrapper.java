package org.kodelabs.domain.segment;

import java.util.List;

public class GroupedSegmentsWrapper {
    private List<GroupedSegments> groupedSegmentsList;

    public GroupedSegmentsWrapper() {

    }

    public GroupedSegmentsWrapper(List<GroupedSegments> groupedSegmentsList) {
        this.groupedSegmentsList = groupedSegmentsList;
    }


    public List<GroupedSegments> getGroupedSegmentsList() {
        return groupedSegmentsList;

    }

    public void setGroupedSegmentsList(List<GroupedSegments> groupedSegmentsList) {
        this.groupedSegmentsList = groupedSegmentsList;
    }
}

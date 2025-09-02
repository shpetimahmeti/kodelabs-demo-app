package org.kodelabs.domain.segment;

import org.kodelabs.domain.segment.entity.SegmentEntity;

import java.util.List;

public class SegmentWithConnections extends SegmentEntity {
    public List<SegmentEntity> connections;
}

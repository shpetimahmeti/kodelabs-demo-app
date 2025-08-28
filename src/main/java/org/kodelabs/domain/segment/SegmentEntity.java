package org.kodelabs.domain.segment;

import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.List;

public class SegmentEntity {

    public ObjectId id;

    public String segmentId;
    public ObjectId routeInstanceId;
    public String routeTemplateId;

    public Airline airline;

    public String from;
    public String to;

    public Instant departureTime;
    public Instant arrivalTime;

    public List<String> flightIds;

    public int stops;

    public static class Airline {
        public String code;
        public String name;
    }
}

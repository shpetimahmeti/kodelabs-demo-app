package org.kodelabs.domain.common.mongo;

public class Fields {

    public static final String ID = "_id";
    public static final String CREATED_AT = "createdAt";
    public static final String UPDATED_AT = "updatedAt";

    public static class FlightFields {
        public static final String FROM__IATA = "from.iata";
        public static final String TO__IATA = "to.iata";
        public static final String ARRIVAL_TIME = "arrivalTime";
        public static final String DEPARTURE_TIME = "departureTime";
        public static final String FLIGHT_COLLECTION_NAME = "flights";
        public static final String STATUS = "status";
        public static final String SEAT_NUMBER = "seatNumber";
        public static final String AVAILABLE_SEATS_COUNT = "availableSeatsCount";
        public static final String SEATS = "seats";

        public static final String CONNECTIONS = "connections";
        public static final String CONNECTIONS__TO__IATA = "connections.to.iata";

        public static final String __AVAILABLE = "available";
    }

    public static class ReservationFields {
        public static final String USER_ID = "userId";
    }

    public static class AirportFields {
        public static final String IATA = "iata";
    }

    public static class AggregationFacetResultFields {
        public static final String ITEMS = "items";
        public static final String TOTAL_COUNT = "totalCount";
        public static final String __COUNT = "count";
        public static final String TOTAL_COUNT__COUNT = TOTAL_COUNT + "." + __COUNT;
    }

    public static String asFieldRef(String fieldName) {
        return "$" + fieldName;
    }

    public static String positionalField(String arrayField, String nestedField) {
        return arrayField + ".$." + nestedField;
    }
}

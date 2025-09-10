package org.kodelabs.domain.common.util;

public class Fields {

    public static final String ID = "_id";
    public static final String UPDATED_AT = "updatedAt";

    public static class FlightFields {
        public static final String FROM_IATA = "from.iata";
        public static final String TO_IATA = "to.iata";
        public static final String TO_IATA_START_WITH_VALUE = "$" + TO_IATA;
        public static final String DEPARTURE_TIME = "departureTime";
        public static final String FLIGHT_COLLECTION_NAME = "flights";

        public static final String CONNECTIONS_FIELD = "connections";
        public static final String CONNECTION_VALUE = "$" + CONNECTIONS_FIELD;
        public static final String CONNECTIONS_TO_IATA = "connections.to.iata";
    }

    public static class ReservationFields {
        public static final String USER_ID = "userId";
    }

    public static class AirportFields {
        public static final String NAME = "name";
        public static final String IATA = "iata";
    }
}

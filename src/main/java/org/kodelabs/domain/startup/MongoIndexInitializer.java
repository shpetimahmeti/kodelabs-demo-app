package org.kodelabs.domain.startup;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Indexes;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.kodelabs.domain.airport.entity.AirportEntity;
import org.kodelabs.domain.common.mongo.MongoRegistry;
import org.kodelabs.domain.common.mongo.Fields;
import org.kodelabs.domain.flight.entity.FlightEntity;
import org.kodelabs.domain.reservation.entity.ReservationEntity;

@ApplicationScoped
public class MongoIndexInitializer {

    @Inject
    MongoRegistry mongoRegistry;

    public void onStart(@Observes StartupEvent ev) {

        MongoCollection<AirportEntity> airportCollection = mongoRegistry.getNonReactiveCollection(AirportEntity.class);
        MongoCollection<FlightEntity> flightCollection = mongoRegistry.getNonReactiveCollection(FlightEntity.class);
        MongoCollection<ReservationEntity> reservationCollection = mongoRegistry.getNonReactiveCollection(ReservationEntity.class);

        try {
            airportCollection.createIndex(Indexes.ascending(Fields.AirportFields.IATA));
            flightCollection.createIndex(Indexes.ascending(Fields.FlightFields.FROM__IATA, Fields.FlightFields.DEPARTURE_TIME));
            flightCollection.createIndex(Indexes.ascending(Fields.FlightFields.TO__IATA));
            reservationCollection.createIndex(Indexes.ascending(Fields.ReservationFields.USER_ID));
        } catch (Exception ex) {
            throw new RuntimeException("Cannot create MongoDB indexes", ex);
        }
    }
}

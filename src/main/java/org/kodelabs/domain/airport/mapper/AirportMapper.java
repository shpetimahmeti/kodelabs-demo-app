package org.kodelabs.domain.airport.mapper;

import org.kodelabs.domain.airport.entity.AirportEntity;
import org.kodelabs.domain.airport.dto.AirportDTO;

public class AirportMapper {

    public static AirportDTO mapToDTO(AirportEntity entity) {
        AirportDTO dto = new AirportDTO();

        dto.setId(entity.get_id());
        dto.setName(entity.getName());
        dto.setIata(entity.getIata());
        dto.setIcao(entity.getIcao());
        dto.setCity(entity.getCity());
        dto.setState(entity.getState());
        dto.setCountry(entity.getCountry());
        dto.setElevationFt(entity.getElevationFt());
        dto.setTimezone(entity.getTimezone());
        dto.setLoc(entity.getLoc());

        return dto;
    }
}

package org.kodelabs.domain.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CreateReservationDTO {
    @NotBlank(message = "User ID is required")
    public String userId;

    @NotBlank(message = "Flight ID is required")
    public String flightId;

    @Pattern(regexp = "^[1-9][0-9]*[A-F]$", message = "Seat number must be valid (e.g., 1A, 2B)")
    public String seatNumber;
}

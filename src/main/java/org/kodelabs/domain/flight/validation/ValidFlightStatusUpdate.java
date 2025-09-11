package org.kodelabs.domain.flight.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FlightStatusUpdateValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)

public @interface ValidFlightStatusUpdate {
    String message() default "Invalid flight status update";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

package org.kodelabs.domain.flight.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = FlightStatusUpdateValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)

public @interface ValidFlightStatusUpdate {
    String message() default "Invalid flight status update";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

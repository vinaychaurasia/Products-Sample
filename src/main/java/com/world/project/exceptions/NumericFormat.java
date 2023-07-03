package com.world.project.exceptions;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

@NotNull
@ReportAsSingleViolation
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface NumericFormat {
    String message() default "Invalid numeric format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

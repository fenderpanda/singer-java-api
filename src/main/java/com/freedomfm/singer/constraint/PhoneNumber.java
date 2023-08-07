package com.freedomfm.singer.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumber {
    String message() default "{error.phoneNumber}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

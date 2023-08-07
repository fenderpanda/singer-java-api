package com.freedomfm.singer.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = FileSizeValidator.class)
public @interface FileSize {
    int value();

    String message() default "{error.fileSize}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

package com.freedomfm.singer.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = FileExtensionValidator.class)
public @interface FileExtension {
    String[] extensions();

    String message() default "{error.fileExtension}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

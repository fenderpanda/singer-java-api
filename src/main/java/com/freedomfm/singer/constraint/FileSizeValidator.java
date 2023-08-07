package com.freedomfm.singer.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {
    private final long MB = 1024 * 1024;
    private long maxFileSize;

    @Override
    public void initialize(FileSize constraintAnnotation) {
        maxFileSize = constraintAnnotation.value() * MB;
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        return file.getSize() <= maxFileSize;
    }
}

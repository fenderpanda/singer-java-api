package com.freedomfm.singer.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class FileExtensionValidator implements ConstraintValidator<FileExtension, MultipartFile> {
    private List<String> extensions;

    @Override
    public void initialize(FileExtension constraintAnnotation) {
        extensions = Arrays.asList(constraintAnnotation.extensions());
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        if (extension == null) {
            return false;
        }

        return extensions.stream().anyMatch(s -> s.equalsIgnoreCase(extension));
    }
}

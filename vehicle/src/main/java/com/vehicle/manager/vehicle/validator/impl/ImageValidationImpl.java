package com.vehicle.manager.vehicle.validator.impl;

import com.vehicle.manager.vehicle.validator.ImageValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ImageValidationImpl implements ConstraintValidator<ImageValidation, List<MultipartFile>> {
    @SneakyThrows
    @Override
    public boolean isValid(List<MultipartFile> images, ConstraintValidatorContext context) {
        return images.stream()
                .allMatch(image -> {
                    String extension = FilenameUtils.getExtension(image.getOriginalFilename());
                    return "jpg".equals(extension) && 200L >= image.getSize();
                });
    }
}
package ar.edu.itba.paw.webapp.validations;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;

public class FileValidator implements ConstraintValidator<File, MultipartFile> {
    long maxSize;

    @Override
    public void initialize(File constraintAnnotation) {
        maxSize = constraintAnnotation.maxSize();
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        try {
            return value.getBytes().length <= maxSize;
        } catch (IOException e) {
            return false;
        }
    }
}

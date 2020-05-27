package ar.edu.itba.paw.webapp.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotBlankWithSpacesValidator implements ConstraintValidator<NotBlankWithSpaces, String> {

    @Override
    public void initialize(NotBlankWithSpaces notBlankField) { }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && !value.trim().isEmpty();
    }
}
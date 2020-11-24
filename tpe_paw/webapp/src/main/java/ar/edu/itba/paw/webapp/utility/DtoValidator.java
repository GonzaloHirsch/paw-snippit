package ar.edu.itba.paw.webapp.validations;

import javax.validation.Validation;
import javax.validation.Validator;

public class DtoValidator {
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


}

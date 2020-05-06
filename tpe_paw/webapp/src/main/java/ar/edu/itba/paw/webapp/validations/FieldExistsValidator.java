package ar.edu.itba.paw.webapp.validations;

import ar.edu.itba.paw.interfaces.service.LanguageService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.webapp.controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldExistsValidator implements ConstraintValidator<FieldExists, Object> {

    @Autowired private UserService userService;
    @Autowired private LanguageService languageService;
    private static final Logger LOGGER = LoggerFactory.getLogger(FieldExistsValidator.class);


    private String fieldName;
    private final static String EMAIL = "Email";
    private final static String LANGUAGE = "Language";

    @Override
    public void initialize(final FieldExists constraintAnnotation) {
        fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(final Object value, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = true;
        String messageTemplate = "";
        if (fieldName != null ) {
            switch (fieldName) {
                case EMAIL:
                    if (value instanceof String) valid = userService.emailExists((String) value);
                    messageTemplate = "{Exists.notFound.email}";
                    break;
                case LANGUAGE:
                    if (value instanceof Long && ((Long)value) > 0) valid = languageService.languageExists((Long) value);
                    LOGGER.debug("Language id = {}", value);
                    messageTemplate = "{Exists.notFound.language}";
                    break;
            }
        }
        if (!valid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(messageTemplate)
                    .addConstraintViolation();
        }
        return valid;
    }
}

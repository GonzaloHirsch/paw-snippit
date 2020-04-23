package ar.edu.itba.paw.webapp.helpers;

import ar.edu.itba.paw.interfaces.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(UniqueEmail uniqueEmail) { }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && userService.isEmailUnique(value);
    }
}

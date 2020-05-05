package ar.edu.itba.paw.webapp.validations;

import ar.edu.itba.paw.interfaces.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailExistsValidator implements ConstraintValidator<EmailExists, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(EmailExists emailExists) {}

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return userService.emailExists(s);
    }
}

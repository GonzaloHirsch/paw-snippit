package ar.edu.itba.paw.webapp.validations;

import ar.edu.itba.paw.interfaces.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

   @Autowired
   private UserService userService;

   public void initialize(UniqueUsername constraint) { }

   public boolean isValid(String value, ConstraintValidatorContext context) {
      return value != null && userService.isUsernameUnique(value);
   }
}

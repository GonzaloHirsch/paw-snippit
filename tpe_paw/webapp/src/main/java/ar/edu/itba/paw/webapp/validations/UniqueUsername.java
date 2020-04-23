package ar.edu.itba.paw.webapp.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueUsernameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface UniqueUsername {

    String message() default "{Unique.registerForm.username}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default{};
}


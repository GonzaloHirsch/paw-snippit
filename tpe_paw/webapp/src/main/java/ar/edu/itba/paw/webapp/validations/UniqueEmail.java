package ar.edu.itba.paw.webapp.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueEmailValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface UniqueEmail {

    String message() default "{Unique.registerForm.email}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default{};

}

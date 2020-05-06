package ar.edu.itba.paw.webapp.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FieldExistsValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface FieldExists {
    String message() default "{Exists.notFound}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default{};

    String fieldName();
}

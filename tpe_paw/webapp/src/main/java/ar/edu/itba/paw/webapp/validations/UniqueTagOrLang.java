package ar.edu.itba.paw.webapp.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueTagOrLangValidator.class)
@Documented
public @interface UniqueTagOrLang {
    String message() default "{UniqueTagOrLang.default}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String type();
    String[] element();

}


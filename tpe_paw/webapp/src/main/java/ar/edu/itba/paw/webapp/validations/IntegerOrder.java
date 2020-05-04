package ar.edu.itba.paw.webapp.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = IntegerOrderValidator.class)
@Documented
@Repeatable(value = IntegerOrder.IntegerOrders.class)
public @interface IntegerOrder {
    String message() default "{IntegerOrder.exploreForm.order}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String min();
    String max();

    @Target({TYPE})
    @Retention( RetentionPolicy.RUNTIME )
    @Documented
    public @interface IntegerOrders {
        IntegerOrder[] value();
    }
}

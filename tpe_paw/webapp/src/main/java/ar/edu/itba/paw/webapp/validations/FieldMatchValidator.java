package ar.edu.itba.paw.webapp.validations;

import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String firstField;
    private String secondField;
    private String message;

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        firstField = constraintAnnotation.first();
        secondField = constraintAnnotation.second();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        boolean valid = true;
        try
        {
            final Object firstObj = BeanUtils.getProperty(value, firstField);
            final Object secondObj = BeanUtils.getProperty(value, secondField);

            /* Valid if both null OR if the first field isn't null and is equal to the second one */
            valid =  firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
        }
        catch (final Exception e)
        {
            // TODO LOGGER
        }

        if (!valid){
            context.buildConstraintViolationWithTemplate(message)
                    .addNode(secondField)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return valid;
    }
}

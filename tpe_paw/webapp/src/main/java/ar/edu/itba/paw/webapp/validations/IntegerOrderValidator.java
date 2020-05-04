package ar.edu.itba.paw.webapp.validations;

import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IntegerOrderValidator  implements ConstraintValidator<IntegerOrder, Object> {
    private String minField;
    private String maxField;
    private String message;

    @Override
    public void initialize(IntegerOrder constraintAnnotation) {
        this.minField = constraintAnnotation.min();
        this.maxField = constraintAnnotation.max();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean valid = true;
        try
        {
            Integer min = BeanUtils.getProperty(value, this.minField) != null ? Integer.parseInt(BeanUtils.getProperty(value, this.minField)) : null;
            Integer max = BeanUtils.getProperty(value, this.maxField) != null ? Integer.parseInt(BeanUtils.getProperty(value, this.maxField)) : null;

            valid = min == null || max == null || !(min > max);
        }
        catch (final Exception e)
        {
            valid = false;
        }

        if (!valid){
            context.buildConstraintViolationWithTemplate(message)
                    .addNode(minField)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return valid;
    }
}

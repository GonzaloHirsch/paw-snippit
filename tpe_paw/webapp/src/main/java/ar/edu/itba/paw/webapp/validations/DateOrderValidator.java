package ar.edu.itba.paw.webapp.validations;

import ar.edu.itba.paw.webapp.exception.FormErrorException;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateOrderValidator implements ConstraintValidator<DateOrder, Object> {

    @Autowired
    private MessageSource messageSource;
    private String minField;
    private String maxField;
    private String message;

    @Override
    public void initialize(final DateOrder constraintAnnotation) {
        this.minField = constraintAnnotation.min();
        this.maxField = constraintAnnotation.max();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        boolean valid = false;
        SimpleDateFormat fmt = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
        try
        {
            Date min = BeanUtils.getProperty(value, this.minField) != null ? fmt.parse(BeanUtils.getProperty(value, this.minField)) : null;
            Date max = BeanUtils.getProperty(value, this.maxField) != null ? fmt.parse(BeanUtils.getProperty(value, this.maxField)) : null;
            valid = min == null || max == null || max.after(min);
        }
        catch (final Exception e)
        {
            throw new FormErrorException(messageSource.getMessage("error.404.form", null, LocaleContextHolder.getLocale()));
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

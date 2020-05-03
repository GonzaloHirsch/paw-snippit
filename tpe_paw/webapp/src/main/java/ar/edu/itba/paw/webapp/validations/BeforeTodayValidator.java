package ar.edu.itba.paw.webapp.validations;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

public class BeforeTodayValidator implements ConstraintValidator<BeforeToday, Date> {
    @Override
    public void initialize(BeforeToday constraintAnnotation) {

    }

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        return value == null || !value.after(new Date());
    }
}

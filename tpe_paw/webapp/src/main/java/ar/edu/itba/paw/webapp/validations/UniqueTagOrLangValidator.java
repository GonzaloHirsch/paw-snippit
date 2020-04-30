package ar.edu.itba.paw.webapp.validations;


import ar.edu.itba.paw.interfaces.service.LanguageService;
import ar.edu.itba.paw.interfaces.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueTagOrLangValidator implements ConstraintValidator<UniqueTagOrLang, String> {

    @Autowired
    TagService tagService;
    @Autowired
    LanguageService languageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UniqueTagOrLangValidator.class);

    private String message;
    private String type;
    private String[] element;

    private final String TAG = "TAG";

    public void initialize(final UniqueTagOrLang constraint) {
        this.message = constraint.message();
        this.element = constraint.element();
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {

//        String typeValue = (String) new BeanWrapperImpl(value)
//                .getPropertyValue(type);
//
//        StringBuilder error = new StringBuilder(message);
//        boolean valid = true;
//
//        if (typeValue.compareTo(TAG) == 0) {
//            for (String tag : element) {
//                String tagValue = (String) new BeanWrapperImpl(value)
//                        .getPropertyValue(tag);
//                if (tagValue != null && tagValue.compareTo("") != 0) {
//                    error.append(tagService.isUniqueTag(tagValue) ? "" : tagValue + ", ");
//                    valid = false;
//                }
//                LOGGER.debug("Inside TAG = {}", tagValue);
//            }
//        } else {
//            //LOGGER.debug("Inside LANG = {}", elementValue);
//        }
//        if (!valid){
//            context.buildConstraintViolationWithTemplate(error.toString())
//                    .addNode(element[0])
//                    .addConstraintViolation()
//                    .disableDefaultConstraintViolation();
//        }
        return true;
    }
}

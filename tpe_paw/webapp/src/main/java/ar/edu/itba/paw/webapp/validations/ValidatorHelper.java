package ar.edu.itba.paw.webapp.validations;

import ar.edu.itba.paw.interfaces.service.LanguageService;
import ar.edu.itba.paw.interfaces.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Locale;

@Component
public class ValidatorHelper {
    @Autowired
    TagService tagService;
    @Autowired
    LanguageService languageService;
    @Autowired
    MessageSource messageSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorHelper.class);

    public void validateAddedTags (List<String> tags, BindingResult errors, Locale locale){
        StringBuilder error = new StringBuilder("");
        int errorAmount = 0;

        for (String tag : tags) {
            if (!tagService.isUniqueTag(tag)) {
                error.append(tag).append(", ");
                errorAmount++;
            }
        }
        error.setLength(error.length() - 2);

        if (errorAmount != 0) {
            FieldError tagExists = new FieldError("addAdminForm","tags" ,messageSource.getMessage("admin.add.tags.exists",new Object[] {error.toString()}, locale));
            errors.addError(tagExists);
            LOGGER.debug("Tags that already exists = {}", error.toString());
        }
    }

//    public void validateAddedLanguages (List<String> languages, BindingResult errors, Locale locale){
//        StringBuilder error = new StringBuilder("");
//        int errorAmount = 0;
//
//        for (String lang : languages) {
//            if (!languageService.isUniqueLang(lang)) {
//                error.append(lang).append(", ");
//                errorAmount++;
//            }
//        }
//        error.setLength(error.length() - 2);
//
//        if (errorAmount != 0) {
//            FieldError tagExists = new FieldError("addAdminForm","languages" ,messageSource.getMessage("admin.add.lang.exists",new Object[] {error.toString()}, locale));
//            errors.addError(tagExists);
//        }
//    }

}

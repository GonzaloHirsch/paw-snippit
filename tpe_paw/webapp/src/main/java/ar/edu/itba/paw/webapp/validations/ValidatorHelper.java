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

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Component
public class ValidatorHelper {
    @Autowired
    TagService tagService;
    @Autowired
    LanguageService languageService;
    @Autowired
    MessageSource messageSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorHelper.class);

    public void validateAdminAdd (List<String> languages, List<String> tags, BindingResult errors, Locale locale) {
        if (languages.isEmpty() && tags.isEmpty()) {
            FieldError noData = new FieldError("addAdminForm","languages" ,messageSource.getMessage("admin.add.no.data",null, locale));
            errors.addError(noData);
        }
        this.validateAddedLanguages(languages, errors, locale);
        this.validateAddedTags(tags, errors, locale);
    }

    private void validateAddedTags (List<String> tags, BindingResult errors, Locale locale){
        StringBuilder error = new StringBuilder();
        int errorAmount = 0;

        for (String tag : tags) {
            if (tagService.tagExists(tag)) {
                error.append(tag).append(", ");
                errorAmount++;
            }
        }
        if (errorAmount > 0) {
            error.setLength(error.length() - 2);
            FieldError tagExists = new FieldError("addAdminForm","tags" ,messageSource.getMessage("admin.add.tags.exists",new Object[] {error.toString()}, locale));
            errors.addError(tagExists);
            LOGGER.debug("Tags that already exists = {}", error.toString());
        }

        this.checkForDuplicated(tags, "tags", errors, locale);
    }

    private void validateAddedLanguages (List<String> languages, BindingResult errors, Locale locale){
        StringBuilder error = new StringBuilder();
        int errorAmount = 0;

        for (String lang : languages) {
            if (languageService.languageExists(lang)) {
                error.append(lang).append(", ");
                errorAmount++;
            }
        }

        if (errorAmount > 0) {
            error.setLength(error.length() - 2);
            FieldError langExists = new FieldError("addAdminForm","languages" ,messageSource.getMessage("admin.add.lang.exists",new Object[] {error.toString()}, locale));
            errors.addError(langExists);
        }

        this.checkForDuplicated(languages, "languages", errors, locale);
    }

    private void checkForDuplicated(List<String> list, String field, BindingResult errors, Locale locale) {
        Set<String> set = new HashSet<>();

        for (String element : list) {
            if (!set.add(element)) {
                FieldError dupFound = new FieldError("addAdminForm",field ,messageSource.getMessage("admin.add.duplicates.error", null, locale));
                errors.addError(dupFound);
                return;
            }
        }
    }

}

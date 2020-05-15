package ar.edu.itba.paw.webapp.validations;

import ar.edu.itba.paw.interfaces.service.CryptoService;
import ar.edu.itba.paw.interfaces.service.LanguageService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.*;

@Component
public class ValidatorHelper {
    @Autowired TagService tagService;
    @Autowired LanguageService languageService;
    @Autowired MessageSource messageSource;
    @Autowired CryptoService cryptoService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorHelper.class);

    public void validateAdminAdd (List<String> languages, List<String> tags, BindingResult errors, Locale locale) {
        if (tags.isEmpty() && languages.isEmpty()) {
            FieldError noData = new FieldError("addAdminForm","languages" ,messageSource.getMessage("admin.add.no.data",null, locale));
            errors.addError(noData);
            return;
        }
        this.validateAddedLanguages(languages, errors, locale);
        this.validateAddedTags(tags, errors, locale);
    }

    private void validateAddedTags (List<String> tags, BindingResult errors, Locale locale){
        StringBuilder error = new StringBuilder();
        int maxLength = 25;
        int errorAmount = 0;
        boolean trailingSpaces = false;
        boolean tooLong = false;

        for (String tag : tags) {
            if (!tag.isEmpty()) {
                if (tag.charAt(0) == ' ' || tag.charAt(tag.length() - 1) == ' ') {
                    trailingSpaces = true;
                } else if (tag.length() > maxLength) {
                    tooLong = true;
                } else {
                    if (tagService.tagExists(tag)) {
                        error.append(tag).append(", ");
                        errorAmount++;
                    }
                }
            }
        }

        if (errorAmount > 0) {
            error.setLength(error.length() - 2);
            FieldError tagExists = new FieldError("addAdminForm", "tags", messageSource.getMessage("admin.add.tags.exists", new Object[]{error.toString()}, locale));
            errors.addError(tagExists);
            LOGGER.debug("Tags that already exists = {}", error.toString());
        }
        if (trailingSpaces) {
            FieldError trailingSpacesError = new FieldError("addAdminForm", "tags", messageSource.getMessage("admin.add.spaces.error", null, locale));
            errors.addError(trailingSpacesError);
        }
        if (tooLong) {
            FieldError tooLongError = new FieldError("addAdminForm", "tags", messageSource.getMessage("admin.add.tag.length.error", new Object[]{maxLength}, locale));
            errors.addError(tooLongError);
        }
        this.checkForDuplicated(tags, "tags", errors, locale);
    }

    private void validateAddedLanguages (List<String> languages, BindingResult errors, Locale locale){
        StringBuilder error = new StringBuilder();
        int maxLength = 20;
        int errorAmount = 0;
        boolean trailingSpaces = false;
        boolean tooLong = false;

        for (String lang : languages) {
            if (!lang.isEmpty()) {
                if (lang.charAt(0) == ' ' || lang.charAt(lang.length() - 1) == ' ') {
                    trailingSpaces = true;
                } else if (lang.length() > maxLength) {
                    tooLong = true;
                }
                else {
                    if (languageService.languageExists(lang)) {
                        error.append(lang).append(", ");
                        errorAmount++;
                    }
                }
            }
        }

        if (errorAmount > 0) {
            error.setLength(error.length() - 2);
            FieldError langExists = new FieldError("addAdminForm", "languages", messageSource.getMessage("admin.add.lang.exists", new Object[]{error.toString()}, locale));
            errors.addError(langExists);
        }
        if (trailingSpaces) {
            FieldError trailingSpacesError = new FieldError("addAdminForm", "languages", messageSource.getMessage("admin.add.spaces.error", null, locale));
            errors.addError(trailingSpacesError);
        }
        if (tooLong) {
            FieldError tooLongError = new FieldError("addAdminForm", "languages", messageSource.getMessage("admin.add.lang.length.error", new Object[]{maxLength}, locale));
            errors.addError(tooLongError);
        }
        this.checkForDuplicated(languages, "languages", errors, locale);
    }

    private void checkForDuplicated(List<String> list, String field, BindingResult errors, Locale locale) {
        Set<String> set = new HashSet<>();

        for (String element : list) {
            if (!set.add(element)) {
                FieldError dupFound = new FieldError("addAdminForm", field, messageSource.getMessage("admin.add.duplicates.error", null, locale));
                errors.addError(dupFound);
                return;
            }
        }

    }

    public void validateTagsExists(Collection<String> tags, BindingResult errors, Locale locale) {
        StringBuilder error = new StringBuilder();
        int errorAmount = 0;

        if (tags != null) {
            for (String tag : tags) {
                if (!tagService.tagExists(tag)) {
                    error.append(tag).append(", ");
                    errorAmount++;
                }
            }
        }
        if (errorAmount > 0) {
            error.setLength(error.length() - 2);
            FieldError tagExists = new FieldError("snippetCreateForm","tags" ,messageSource.getMessage("Exists.notFound.tags",new Object[] {error.toString()}, locale));
            errors.addError(tagExists);
            LOGGER.debug("Tags that no longer exists = {}", error.toString());
        }
    }

    public boolean checkValidTOTP(User user, String code, BindingResult errors, Locale locale) {
        boolean isCodeValid = this.cryptoService.checkValidTOTP(user, code);
        if (!isCodeValid) {
            FieldError noData = new FieldError("verificationForm", "code", messageSource.getMessage("account.verification.code.invalid", null, locale));
            errors.addError(noData);
            return false;
        }
        return true;
    }
}

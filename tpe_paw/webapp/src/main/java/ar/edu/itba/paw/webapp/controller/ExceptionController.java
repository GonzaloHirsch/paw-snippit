package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.RoleService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.exception.*;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@ControllerAdvice
public class ExceptionController {

    @Autowired private MessageSource messageSource;
    @Autowired private LoginAuthentication loginAuthentication;
    @Autowired private TagService tagService;
    @Autowired private RoleService roleService;

    public static final String DEFAULT_ERROR_VIEW = "errors/default";
    private static final String ERROR_CONTEXT = "error/";

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(Exception e) throws Exception {
        /*
         * If the exception is annotated with @ResponseStatus rethrow it and let
         * the framework handle it
         */
        if (AnnotationUtils.findAnnotation
                (e.getClass(), ResponseStatus.class) != null)
            throw e;

        String errorMessage = messageSource.getMessage("error.404", null, LocaleContextHolder.getLocale());
        String errorName = messageSource.getMessage("error.404.name", null, LocaleContextHolder.getLocale());

        return this.createErrorModel(errorName, errorMessage, 404);
    }

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({PSQLException.class, BadSqlGrammarException.class})
    public ModelAndView internalServerError(Exception e) {
        String errorName = messageSource.getMessage("error.500.name", null, LocaleContextHolder.getLocale());
        String errorMessage = messageSource.getMessage("error.500", null, LocaleContextHolder.getLocale());
        return this.createErrorModel(errorName, errorMessage, 500); }


    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class, LanguageNotFoundException.class, TagNotFoundException.class, SnippetNotFoundException.class})
    public ModelAndView elementNotFound(Exception ex) {
        String errorName = messageSource.getMessage("error.404.name", null, LocaleContextHolder.getLocale());
        return this.createErrorModel(errorName, ex.getMessage(), 404);
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(FormErrorException.class)
    public ModelAndView formError(Exception ex) {
        String errorName = messageSource.getMessage("error.404.name", null, LocaleContextHolder.getLocale());
        return this.createErrorModel(errorName, ex.getMessage(), 404);
    }

    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenAccessException.class)
    public ModelAndView forbiddenAccess(Exception ex) {
        String errorName = messageSource.getMessage("error.403.name", null, LocaleContextHolder.getLocale());
        return this.createErrorModel(errorName, ex.getMessage(), 403);
    }

    @ResponseStatus(code = HttpStatus.CONFLICT)
    @ExceptionHandler({RemovingLanguageInUseException.class, ElementDeletionException.class})
    public ModelAndView cannotRemoveLanguage(RemovingLanguageInUseException ex) {
        String errorName = messageSource.getMessage("error.409.name", null, LocaleContextHolder.getLocale());
        return this.createErrorModel(errorName, ex.getMessage(), 409);
    }

    private ModelAndView createErrorModel(String errorName, String errorMessage, int errorCode) {
        ModelAndView mav = new ModelAndView(DEFAULT_ERROR_VIEW);

        User currentUser = this.loginAuthentication.getLoggedInUser();
        Collection<Tag> userTags = currentUser != null ? this.tagService.getFollowedTagsForUser(currentUser.getId()) : new ArrayList<>();
        Collection<String> userRoles = currentUser != null ? this.roleService.getUserRoles(currentUser.getId()) : new ArrayList<>();
        mav.addObject("currentUser", currentUser);
        mav.addObject("userTags", userTags);
        mav.addObject("searchContext", ERROR_CONTEXT);
        mav.addObject("err", errorCode);
        mav.addObject("errName", errorName);
        mav.addObject("msg", errorMessage);
        mav.addObject("userRoles", userRoles);

        return mav;
    }
}

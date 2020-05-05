package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.RoleService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.exception.RemovingLanguageInUseException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@ControllerAdvice
public class ExceptionController {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private LoginAuthentication loginAuthentication;
    @Autowired
    private TagService tagService;
    @Autowired
    private RoleService roleService;

    public static final String DEFAULT_ERROR_VIEW = "errors/default";
    public static final String NOT_FOUND_ERROR_VIEW = "errors/404";
    private static final String ERROR_CONTEXT = "error/";

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it
        if (AnnotationUtils.findAnnotation
                (e.getClass(), ResponseStatus.class) != null)
            throw e;

        String errorMessage = messageSource.getMessage("error.404", null, LocaleContextHolder.getLocale());

        return this.createErrorModel(NOT_FOUND_ERROR_VIEW, errorMessage, 404);
    }

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({PSQLException.class, BadSqlGrammarException.class})
    public ModelAndView internalServerError(Exception e) { return this.createErrorModel(DEFAULT_ERROR_VIEW, messageSource.getMessage("error.500", null, LocaleContextHolder.getLocale()), 500); }


    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView noSuchUser(UserNotFoundException ex) {
        return this.createErrorModel(NOT_FOUND_ERROR_VIEW, ex.getMessage(), 404);
    }

    @ResponseStatus(code = HttpStatus.CONFLICT)
    @ExceptionHandler(RemovingLanguageInUseException.class)
    public ModelAndView cannotRemoveLanguage(RemovingLanguageInUseException ex) {
        return this.createErrorModel(DEFAULT_ERROR_VIEW, ex.getMessage(), 409);
    }

    private ModelAndView createErrorModel(String modelName, String errorMessage, int errorCode) {
        ModelAndView mav = new ModelAndView(modelName);
        User currentUser = this.loginAuthentication.getLoggedInUser();
        Collection<Tag> userTags = currentUser != null ? this.tagService.getFollowedTagsForUser(currentUser.getId()) : new ArrayList<>();
        Collection<String> userRoles = currentUser != null ? this.roleService.getUserRoles(currentUser.getId()) : new ArrayList<>();
        mav.addObject("currentUser", currentUser);
        mav.addObject("userTags", userTags);
        mav.addObject("searchContext", ERROR_CONTEXT);
        mav.addObject("err", errorCode);
        mav.addObject("msg", errorMessage);
        mav.addObject("userRoles", userRoles);

        return mav;
    }

}

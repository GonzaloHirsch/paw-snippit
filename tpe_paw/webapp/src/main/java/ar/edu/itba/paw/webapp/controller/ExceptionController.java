package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.RoleService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.utility.Constants;
import ar.edu.itba.paw.webapp.exception.*;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

//@ControllerAdvice
@Deprecated
public class ExceptionController {

//    @Autowired
    private MessageSource messageSource;
//    @Autowired
    private LoginAuthentication loginAuthentication;
//    @Autowired
    private TagService tagService;
//    @Autowired
    private RoleService roleService;

    public static final String DEFAULT_ERROR_VIEW = "errors/default";
    private static final String ERROR_CONTEXT = "error/";

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(Exception e, HttpServletRequest request) throws Exception {
        /*
         * If the exception is annotated with @ResponseStatus rethrow it and let
         * the framework handle it
         */
        if (AnnotationUtils.findAnnotation
                (e.getClass(), ResponseStatus.class) != null)
            throw e;

        String errorMessage = messageSource.getMessage("error.404", null, LocaleContextHolder.getLocale());
        String errorName = messageSource.getMessage("error.404.name", null, LocaleContextHolder.getLocale());
        LOGGER.error("[PageNotFound] " + e.getMessage());
        return this.createErrorModel(request, errorName, errorMessage, 404);
    }

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({PSQLException.class, BadSqlGrammarException.class})
    public ModelAndView internalServerError(Exception e, HttpServletRequest request) {
        String errorName = messageSource.getMessage("error.500.name", null, LocaleContextHolder.getLocale());
        String errorMessage = messageSource.getMessage("error.500", null, LocaleContextHolder.getLocale());
        LOGGER.error(e.getMessage());
        return this.createErrorModel(request, errorName, errorMessage, 500); }


    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class, LanguageNotFoundException.class, TagNotFoundException.class, SnippetNotFoundException.class})
    public ModelAndView elementNotFound(Exception ex, HttpServletRequest request) {
        String errorName = messageSource.getMessage("error.404.name", null, LocaleContextHolder.getLocale());
        return this.createErrorModel(request, errorName, ex.getMessage(), 404);
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(FormErrorException.class)
    public ModelAndView formError(Exception ex, HttpServletRequest request) {
        String errorName = messageSource.getMessage("error.404.name", null, LocaleContextHolder.getLocale());
        LOGGER.error(messageSource.getMessage("error.404.form", null, Locale.ENGLISH));
        return this.createErrorModel(request, errorName, ex.getMessage(), 404);
    }

    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenAccessException.class)
    public ModelAndView forbiddenAccess(Exception ex, HttpServletRequest request) {
        String errorName = messageSource.getMessage("error.403.name", null, LocaleContextHolder.getLocale());
        return this.createErrorModel(request, errorName, ex.getMessage(), 403);
    }

    @ResponseStatus(code = HttpStatus.CONFLICT)
    @ExceptionHandler({ElementDeletionException.class})
    public ModelAndView cannotRemoveLanguage(ElementDeletionException ex, HttpServletRequest request) {
        String errorName = messageSource.getMessage("error.409.name", null, LocaleContextHolder.getLocale());
        return this.createErrorModel(request, errorName, ex.getMessage(), 409);
    }

    @ResponseStatus(code = HttpStatus.URI_TOO_LONG)
    @ExceptionHandler(URITooLongException.class)
    public ModelAndView uriTooLong(URITooLongException ex, HttpServletRequest request) {
        String errorName = messageSource.getMessage("error.414.name", null, LocaleContextHolder.getLocale());
        return this.createErrorModel(request, errorName, ex.getMessage(), 414);
    }

    private ModelAndView createErrorModel(HttpServletRequest request, String errorName, String errorMessage, int errorCode) {
        ModelAndView mav = new ModelAndView(DEFAULT_ERROR_VIEW);

        User currentUser = this.loginAuthentication.getLoggedInUser(request);
        Collection<Tag> userTags =  Collections.emptyList();
        Collection<String> userRoles = Collections.emptyList();
        Collection<Tag> allFollowedTags = Collections.emptyList();

        if (currentUser != null) {
            userTags = this.tagService.getMostPopularFollowedTagsForUser(currentUser.getId(), Constants.MENU_FOLLOWING_TAG_AMOUNT);
            userRoles = this.roleService.getUserRoles(currentUser.getId());
            allFollowedTags = this.tagService.getFollowedTagsForUser(currentUser.getId());
        }
        mav.addObject("currentUser", currentUser);
        mav.addObject("userTags", userTags);
        mav.addObject("userTagsCount", userTags.isEmpty() ? 0 : allFollowedTags.size() - userTags.size());
        mav.addObject("searchContext", ERROR_CONTEXT);
        mav.addObject("err", errorCode);
        mav.addObject("errName", errorName);
        mav.addObject("msg", errorMessage);
        mav.addObject("userRoles", userRoles);

        return mav;
    }
}

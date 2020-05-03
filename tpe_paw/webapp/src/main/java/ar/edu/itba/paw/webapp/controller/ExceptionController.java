package ar.edu.itba.paw.webapp.controller;

        import ar.edu.itba.paw.interfaces.service.TagService;
        import ar.edu.itba.paw.models.Tag;
        import ar.edu.itba.paw.models.User;
        import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
        import ar.edu.itba.paw.webapp.exception.RemovingLanguageInUseException;
        import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
        import ar.edu.itba.paw.webapp.form.SearchForm;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.context.MessageSource;
        import org.springframework.context.i18n.LocaleContextHolder;
        import org.springframework.core.annotation.AnnotationUtils;
        import org.springframework.http.HttpStatus;
        import org.springframework.security.core.userdetails.UsernameNotFoundException;
        import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
        import org.springframework.validation.BindingResult;
        import org.springframework.web.bind.annotation.*;
        import org.springframework.web.servlet.ModelAndView;

        import javax.servlet.http.HttpServletRequest;
        import javax.validation.Valid;
        import java.util.*;

@ControllerAdvice
public class ExceptionController {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private LoginAuthentication loginAuthentication;
    @Autowired
    private TagService tagService;

    public static final String DEFAULT_ERROR_VIEW = "errors/default";
    private static final String ERROR_CONTEXT = "error/";

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        // If the exception is annotated with @ResponseStatus rethrow it and let
        // the framework handle it
        if (AnnotationUtils.findAnnotation
                (e.getClass(), ResponseStatus.class) != null)
            throw e;

        String errorMessage = messageSource.getMessage("error.unknown",null, LocaleContextHolder.getLocale());

        //Otherwise setup and send the user to a default error-view.
        ModelAndView mav = new ModelAndView();
        mav.addObject("err", 404);
        mav.addObject("msg", errorMessage);
        mav.addObject("searchContext", ERROR_CONTEXT);
        mav.setViewName(DEFAULT_ERROR_VIEW);

        return mav;
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView noSuchUser(UserNotFoundException ex) {
        return this.createErrorModel(ex.getMessage(), 404);
    }

    @ResponseStatus(code = HttpStatus.CONFLICT)
    @ExceptionHandler(RemovingLanguageInUseException.class)
    public ModelAndView cannotRemoveLanguage(RemovingLanguageInUseException ex) {
        return this.createErrorModel(ex.getMessage(), 409);
    }

    private ModelAndView createErrorModel(String errorMessage, int errorCode) {
        ModelAndView mav = new ModelAndView(DEFAULT_ERROR_VIEW);
        User currentUser = this.loginAuthentication.getLoggedInUser();
        Collection<Tag> userTags = currentUser != null ? this.tagService.getFollowedTagsForUser(currentUser.getId()) : new ArrayList<>();
        mav.addObject("currentUser", currentUser);
        mav.addObject("userTags", userTags);
        mav.addObject("searchContext", ERROR_CONTEXT);
        mav.addObject("err", errorCode);
        mav.addObject("msg", errorMessage);
        return mav;
    }

}

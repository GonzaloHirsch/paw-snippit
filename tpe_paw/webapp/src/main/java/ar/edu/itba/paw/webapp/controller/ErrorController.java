package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.form.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class ErrorController {

    @Autowired
    private MessageSource messageSource;

    private Set<Integer> supportedErrorPages = new HashSet<Integer>(){{
        add(403);
        add(404);
        add(500);
    }};

    @RequestMapping("/error")
    public ModelAndView customError(HttpServletRequest request, @ModelAttribute("searchForm") final SearchForm searchForm) {
        int errorCode = this.getErrorCode(request);
        ModelAndView mav = new ModelAndView("errors/default");
        String message = messageSource.getMessage("error.unknown",null, LocaleContextHolder.getLocale());
        if (this.supportedErrorPages.contains(errorCode)){
            message =  messageSource.getMessage("error." + String.valueOf(errorCode),null, LocaleContextHolder.getLocale());
        }
        mav.addObject("err", errorCode);
        mav.addObject("msg", message);
        return mav;
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest
                .getAttribute("javax.servlet.error.status_code");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchUser() {
        return new ModelAndView("errors/404");
    }

}

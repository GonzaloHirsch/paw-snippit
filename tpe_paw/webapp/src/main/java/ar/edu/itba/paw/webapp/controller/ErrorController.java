package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.form.SearchForm;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ErrorController {

    // TODO --> Pass messages to i18n and use messageSource
    private Map<Integer, String> supportedErrorPages = new HashMap<Integer, String>(){{
        put(403, "You shall not pass!");
        put(404, "Looks like there was an error, why not look for something else or go back to the Home page?");
        put(500, "Looks like there was an error on our side, try again later or go back to the Home page");
    }};

    @RequestMapping("/error")
    public ModelAndView customError(HttpServletRequest request, @ModelAttribute("searchForm") final SearchForm searchForm) {
        int errorCode = this.getErrorCode(request);
        ModelAndView mav = new ModelAndView("errors/default");
        String message = "Unknown Error";
        if (this.supportedErrorPages.containsKey(errorCode)){
            message = this.supportedErrorPages.get(errorCode);
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

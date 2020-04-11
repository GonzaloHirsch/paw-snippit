package ar.edu.itba.paw.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ErrorController {

    private Map<Integer, String> supportedErrorPages = new HashMap<Integer, String>(){{
        put(404, "errors/404");
    }};

    @RequestMapping("/error")
    public ModelAndView customError(HttpServletRequest request) {
        int errorCode = this.getErrorCode(request);
        String page = "errors/default";
        if (this.supportedErrorPages.containsKey(errorCode)){
            page = this.supportedErrorPages.get(errorCode);
        }
        return new ModelAndView(page);
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest
                .getAttribute("javax.servlet.error.status_code");
    }
}

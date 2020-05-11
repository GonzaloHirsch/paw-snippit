package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.RoleService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class ErrorController {

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private LoginAuthentication loginAuthentication;
    @Autowired
    private TagService tagService;
    @Autowired
    private RoleService roleService;

    private Set<Integer> supportedErrorPages = new HashSet<Integer>(){{
        add(404);
        add(413);
        add(414);
        add(500);
    }};

    @RequestMapping("/error")
    public ModelAndView customError(HttpServletRequest request, @ModelAttribute("searchForm") SearchForm searchForm) {
        int errorCode = this.getErrorCode(request);
        ModelAndView mav = new ModelAndView("errors/default");
        String message = messageSource.getMessage("error.unknown",null, LocaleContextHolder.getLocale());
        String errorName = messageSource.getMessage("error.unknown.name",null, LocaleContextHolder.getLocale());

        if (this.supportedErrorPages.contains(errorCode)){
            message =  messageSource.getMessage("error." + String.valueOf(errorCode),null, LocaleContextHolder.getLocale());
            errorName = messageSource.getMessage("error." + String.valueOf(errorCode) +".name",null, LocaleContextHolder.getLocale());
        }
        mav.addObject("err", errorCode);
        mav.addObject("errName", errorName);
        mav.addObject("msg", message);
        return mav;
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest
                    .getAttribute("javax.servlet.error.status_code");
    }

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        User currentUser = this.loginAuthentication.getLoggedInUser(request);
        Collection<Tag> userTags = currentUser != null ? this.tagService.getFollowedTagsForUser(currentUser.getId()) : new ArrayList<>();
        Collection<String> userRoles = currentUser != null ? this.roleService.getUserRoles(currentUser.getId()) : new ArrayList<>();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userTags", userTags);
        model.addAttribute("userRoles", userRoles);
        model.addAttribute("searchContext", "error/");

    }
}

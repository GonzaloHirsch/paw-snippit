package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.LanguageService;
import ar.edu.itba.paw.interfaces.service.RoleService;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.exception.ForbiddenAccessException;
import ar.edu.itba.paw.webapp.exception.FormErrorException;
import ar.edu.itba.paw.webapp.form.SearchForm;
import ar.edu.itba.paw.webapp.form.SnippetCreateForm;
import ar.edu.itba.paw.webapp.validations.ValidatorHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

@Controller
public class SnippetCreateController {

    @Autowired private SnippetService snippetService;
    @Autowired private TagService tagService;
    @Autowired private LanguageService languageService;
    @Autowired private LoginAuthentication loginAuthentication;
    @Autowired private RoleService roleService;
    @Autowired private MessageSource messageSource;
    @Autowired private ValidatorHelper validator;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static final Logger LOGGER = LoggerFactory.getLogger(SnippetCreateController.class);

    @RequestMapping(value = "/snippet/create")
    public ModelAndView snippetCreateDetail(@ModelAttribute("snippetCreateForm") final SnippetCreateForm snippetCreateForm) {
        final ModelAndView mav = new ModelAndView("snippet/snippetCreate");

        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null) {
            throw new ForbiddenAccessException(messageSource.getMessage("error.403.snippet.create", null, LocaleContextHolder.getLocale()));
        }

        mav.addObject("currentUser", currentUser);
        mav.addObject("userTags", this.tagService.getFollowedTagsForUser(currentUser.getId()));
        mav.addObject("tagList", this.tagService.getAllTags());
        mav.addObject("languageList", this.languageService.getAll());
        mav.addObject("searchContext", "");
        mav.addObject("userRoles", this.roleService.getUserRoles(currentUser.getId()));

        return mav;
    }

    @RequestMapping(value = "/snippet/create", method = RequestMethod.POST)
    public ModelAndView snippetCreate( @Valid @ModelAttribute("snippetCreateForm") final SnippetCreateForm snippetCreateForm, final BindingResult errors) {

        this.validator.validateTagsExists(snippetCreateForm.getTags(), errors, LocaleContextHolder.getLocale());

        if (errors.hasErrors()) {
            return snippetCreateDetail(snippetCreateForm);
        }
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String dateCreated = sdf.format(timestamp);

        User currentUser = this.loginAuthentication.getLoggedInUser();
        if(currentUser == null) {
            LOGGER.warn("Creating a snippet when no user is logged in");
            throw new ForbiddenAccessException(this.messageSource.getMessage("error.403.snippet.create", null, LocaleContextHolder.getLocale()));
        }
        Long snippetId = this.snippetService.createSnippet(currentUser,snippetCreateForm.getTitle(),snippetCreateForm.getDescription(), snippetCreateForm.getCode(), dateCreated, snippetCreateForm.getLanguage(),snippetCreateForm.getTags());
        if(snippetId == null){
            LOGGER.warn("Snippet creation was unsuccessful. Return id was null.");
            throw new FormErrorException(this.messageSource.getMessage("error.404.form", null, LocaleContextHolder.getLocale()));
        }

        return new ModelAndView("redirect:/snippet/" + snippetId);
    }

    @ModelAttribute
    public void addAttributes(ModelAndView model, @Valid final SearchForm searchForm) {
        model.addObject("searchForm", searchForm);
    }
}

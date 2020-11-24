package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.LanguageService;
import ar.edu.itba.paw.interfaces.service.RoleService;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.utility.Constants;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import java.time.Instant;
import java.util.Collection;
import java.util.Locale;

@Deprecated
public class SnippetCreateController {

//    @Autowired
    private SnippetService snippetService;
//    @Autowired
    private TagService tagService;
//    @Autowired
    private LanguageService languageService;
//    @Autowired
    private LoginAuthentication loginAuthentication;
//    @Autowired
    private RoleService roleService;
//    @Autowired
    private MessageSource messageSource;
//    @Autowired
    private ValidatorHelper validator;

    private static final Logger LOGGER = LoggerFactory.getLogger(SnippetCreateController.class);

    @RequestMapping(value = "/snippet/create")
    public ModelAndView snippetCreateDetail(@ModelAttribute("snippetCreateForm") final SnippetCreateForm snippetCreateForm) {
        final ModelAndView mav = new ModelAndView("snippet/snippetCreate");

        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null) {
            LOGGER.error(messageSource.getMessage("error.403.snippet.create", null, Locale.ENGLISH));
            throw new ForbiddenAccessException(this.messageSource.getMessage("error.403.snippet.create", null, LocaleContextHolder.getLocale()));
        } else if (this.roleService.isAdmin(currentUser.getId())) {
            LOGGER.error(messageSource.getMessage("error.403.admin.snippet.create", null, Locale.ENGLISH));
            throw new ForbiddenAccessException(this.messageSource.getMessage("error.403.admin.snippet.create", null, LocaleContextHolder.getLocale()));
        }
        Collection<Tag> userTags = this.tagService.getMostPopularFollowedTagsForUser(currentUser.getId(), Constants.MENU_FOLLOWING_TAG_AMOUNT);
        Collection<Tag> allFollowedTags = this.tagService.getFollowedTagsForUser(currentUser.getId());
        mav.addObject("currentUser", currentUser);
        mav.addObject("userTags", userTags);
        mav.addObject("userTagsCount", userTags.isEmpty() ? 0 : allFollowedTags.size() - userTags.size());
        mav.addObject("tagList", this.tagService.getAllTags());
        mav.addObject("languageList", this.languageService.getAllLanguages());
        mav.addObject("searchContext", "");
        mav.addObject("userRoles", this.roleService.getUserRoles(currentUser.getId()));

        return mav;
    }

    @RequestMapping(value = "/snippet/create", method = RequestMethod.POST)
    public ModelAndView snippetCreate(@Valid @ModelAttribute("snippetCreateForm") final SnippetCreateForm snippetCreateForm, final BindingResult errors) {

        this.validator.validateTagsExists(snippetCreateForm.getTags(), errors, LocaleContextHolder.getLocale());

        if (errors.hasErrors()) {
            return snippetCreateDetail(snippetCreateForm);
        }
        Instant dateCreated = Instant.now();
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null) {
            LOGGER.error("Creating a snippet when no user is logged in");
            throw new ForbiddenAccessException(this.messageSource.getMessage("error.403.snippet.create", null, LocaleContextHolder.getLocale()));
        }
        Long snippetId = this.snippetService.createSnippet(currentUser, snippetCreateForm.getTitle(), snippetCreateForm.getDescription(), snippetCreateForm.getCode(), dateCreated, snippetCreateForm.getLanguage(), snippetCreateForm.getTags());
        if (snippetId == null) {
            LOGGER.error("Snippet creation was unsuccessful. Return id was null.");
            throw new FormErrorException(this.messageSource.getMessage("error.404.form", null, LocaleContextHolder.getLocale()));
        }

        return new ModelAndView("redirect:/snippet/" + snippetId);
    }

    @ModelAttribute
    public void addAttributes(ModelAndView model, @Valid final SearchForm searchForm) {
        model.addObject("searchForm", searchForm);
    }
}

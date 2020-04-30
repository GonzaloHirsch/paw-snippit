package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.LanguageService;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.form.SearchForm;
import ar.edu.itba.paw.webapp.form.SnippetCreateForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static final Logger LOGGER = LoggerFactory.getLogger(SnippetCreateController.class);

    @RequestMapping(value = "/snippet/create")
    public ModelAndView snippetCreateDetail(@ModelAttribute("snippetCreateForm") final SnippetCreateForm snippetCreateForm) {
        final ModelAndView mav = new ModelAndView("snippet/snippetCreate");

        User currentUser = this.loginAuthentication.getLoggedInUser();
        Collection<Tag> userTags = currentUser != null ? this.tagService.getFollowedTagsForUser(currentUser.getId()) : new ArrayList<>();

        mav.addObject("currentUser", currentUser);
        mav.addObject("userTags", userTags);
        mav.addObject("tagList",tagService.getAllTags());
        mav.addObject("languageList", languageService.getAll());
        mav.addObject("searchContext", "");

        return mav;
    }

    @RequestMapping(value = "/snippet/create", method = RequestMethod.POST)
    public ModelAndView snippetCreate( @Valid @ModelAttribute("snippetCreateForm") final SnippetCreateForm snippetCreateForm, final BindingResult errors) {

        if (errors.hasErrors())
            return snippetCreateDetail(snippetCreateForm);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String dateCreated = sdf.format(timestamp);

        User currentUser = loginAuthentication.getLoggedInUser();
        if(currentUser == null) {
            LOGGER.warn("[SnippetCreateController] Creating a snippet when no user is logged in");
            // TODO --> what to throw? throw new UserNotFoundException(); ?
        }
        Long snippetId = snippetService.createSnippet(currentUser,snippetCreateForm.getTitle(),snippetCreateForm.getDescription(), snippetCreateForm.getCode(), dateCreated, snippetCreateForm.getLanguage(),snippetCreateForm.getTags());
        if(snippetId == null){
            LOGGER.warn("[SnippetCreateController] Snippet creation was unsuccessful. Return id was null.");
        }

        return new ModelAndView("redirect:/snippet/" + snippetId);
    }

    @ModelAttribute
    public void addAttributes(ModelAndView model, @Valid final SearchForm searchForm) {
        model.addObject("searchForm", searchForm);
    }
}
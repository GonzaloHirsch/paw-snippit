package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.LanguageService;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.models.Language;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Controller
public class LanguagesController {
    @Autowired
    private LanguageService languageService;
    @Autowired
    private SnippetService snippetService;
    @Autowired
    private LoginAuthentication loginAuthentication;
    @Autowired
    private TagService tagService;

    private static final Logger LOGGER = LoggerFactory.getLogger(LanguagesController.class);

    @RequestMapping("/languages")
    public ModelAndView showAllTags() {
        ModelAndView mav = new ModelAndView("tagAndLanguages/languages");

        Collection<Language> allLanguages = languageService.getAll();
        mav.addObject("searchContext","languages/");
        mav.addObject("languages", allLanguages);
        return mav;
    }

    @RequestMapping("/languages/{langId}")
    public ModelAndView showSnippetsForTag(@PathVariable("langId") long langId, final @RequestParam(value = "page", required = false, defaultValue = "1") int page){
        ModelAndView mav = new ModelAndView("tagAndLanguages/languageSnippets");

        /* Retrieve the tag */
        Optional<Language> language = languageService.findById(langId);
        if (!language.isPresent()) {
            LOGGER.warn("No language found with id {}", langId);
            //TODO throw new
        }

        int totalSnippetCount = this.snippetService.getAllSnippetsByLanguageCount(langId);
        int pageSize = this.snippetService.getPageSize();
        mav.addObject("pages", totalSnippetCount/pageSize + (totalSnippetCount % pageSize == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("language", language.get());
        mav.addObject("snippets", snippetService.findSnippetsWithLanguage(langId, page));
        return mav;
    }

    @RequestMapping("/languages/{langId}/delete")
    public ModelAndView followSnippet(@PathVariable("langId") long langId) {
//        User currentUser = loginAuthentication.getLoggedInUser();
//        if ( currentUser != null){
//            tagService.followTag(currentUser.getId(), tagId);
//            LOGGER.debug("User {} followed tag with id {}", currentUser.getUsername(), tagId);
//        } else {
//            LOGGER.warn("No user logged in but tag {} was followed", tagId);
//        }
        return new ModelAndView("redirect:/languages");
    }

    @ModelAttribute
    public void addAttributes(Model model, @Valid final SearchForm searchForm) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        Collection<Tag> userTags = currentUser != null ? this.tagService.getFollowedTagsForUser(currentUser.getId()) : new ArrayList<>();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userTags", userTags);
        model.addAttribute("searchForm", searchForm);
    }

}
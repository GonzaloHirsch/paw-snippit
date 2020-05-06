package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.Language;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.exception.ForbiddenAccessException;
import ar.edu.itba.paw.webapp.exception.LanguageNotFoundException;
import ar.edu.itba.paw.webapp.exception.RemovingLanguageInUseException;
import ar.edu.itba.paw.webapp.form.DeleteForm;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MessageSource messageSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(LanguagesController.class);

    @RequestMapping("/languages")
    public ModelAndView showAllLanguages() {
        ModelAndView mav = new ModelAndView("tagAndLanguages/languages");

        Collection<Language> allLanguages = languageService.getAll();
        mav.addObject("searchContext","languages/");
        mav.addObject("languages", allLanguages);
        return mav;
    }

    @RequestMapping("/languages/{langId}")
    public ModelAndView showSnippetsForLang(@PathVariable("langId") long langId, @ModelAttribute("deleteForm") final DeleteForm deleteForm, final @RequestParam(value = "page", required = false, defaultValue = "1") int page){
        ModelAndView mav = new ModelAndView("tagAndLanguages/languageSnippets");

        /* Retrieve the tag */
        Optional<Language> language = languageService.findById(langId);
        if (!language.isPresent()) {
            LOGGER.warn("No language found with id {}", langId);
            throw new LanguageNotFoundException(messageSource.getMessage("error.404.language", new Object[]{langId}, LocaleContextHolder.getLocale()));
        }

        int totalSnippetCount = this.snippetService.getAllSnippetsByLanguageCount(langId);
        int pageSize = this.snippetService.getPageSize();
        mav.addObject("pages", totalSnippetCount/pageSize + (totalSnippetCount % pageSize == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("language", language.get());
        mav.addObject("snippets", snippetService.findSnippetsWithLanguage(langId, page));
        return mav;
    }

    @Transactional
    @RequestMapping("/languages/{langId}/delete")
    public ModelAndView deleteLanguage (@PathVariable("langId") long langId, @ModelAttribute("deleteForm") final DeleteForm deleteForm) {
        User currentUser = loginAuthentication.getLoggedInUser();
        if ( currentUser != null && roleService.isAdmin(currentUser.getId())){
            /* Language was assigned to a snippet and can no longer be deleted */
            if (this.languageService.languageInUse(langId)) {
                Optional<Language> language = this.languageService.findById(langId);
                Object[] obj = language.map(value -> new Object[]{value.getName().toUpperCase()}).orElseGet(() -> new Object[]{langId});
                throw new RemovingLanguageInUseException(messageSource.getMessage("error.removing.language", obj, LocaleContextHolder.getLocale()));
            }
            this.languageService.removeLanguage(langId);
            LOGGER.debug("Admin removed language with id {}", langId);
        } else {
            LOGGER.warn("No user logged in or logged in user not admin but language {} is trying to be deleted", langId);
            throw new ForbiddenAccessException(messageSource.getMessage("error.403.admin.delete", null, LocaleContextHolder.getLocale()));
        }
        return new ModelAndView("redirect:/languages");
    }

    @ModelAttribute
    public void addAttributes(Model model, @Valid final SearchForm searchForm) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        Collection<Tag> userTags = currentUser != null ? this.tagService.getFollowedTagsForUser(currentUser.getId()) : new ArrayList<>();
        Collection<String> userRoles = currentUser != null ? this.roleService.getUserRoles(currentUser.getId()) : new ArrayList<>();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userTags", userTags);
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("userRoles", userRoles);
    }

}
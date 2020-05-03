package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.service.LanguageService;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.form.ExploreForm;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

@Controller
public class SnippetExploreController {

    @Autowired
    private SnippetService snippetService;
    @Autowired
    private LoginAuthentication loginAuthentication;
    @Autowired
    private TagService tagService;
    @Autowired
    private LanguageService languageService;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @RequestMapping("/explore")
    public ModelAndView explore(@ModelAttribute("searchForm") final SearchForm searchForm, @ModelAttribute("exploreForm") final ExploreForm exploreForm, final @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        final ModelAndView mav = new ModelAndView("snippet/snippetExplore");
        Collection<Snippet> snippets = this.snippetService.getAllSnippets(page);
        int snippetCount = this.snippetService.getAllSnippetsCount();
        this.addModelAttributesHelper(mav, snippetCount, page, snippets, "explore/");
        return mav;
    }

    @RequestMapping("/explore/search")
    public ModelAndView exploreSearch(@Valid @ModelAttribute("searchForm") final SearchForm searchForm, @Valid @ModelAttribute("exploreForm") final ExploreForm exploreForm, final BindingResult errors, final @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        if (errors.hasErrors()) {
            return explore(searchForm, exploreForm, page);
        }
        final ModelAndView mav = new ModelAndView("snippet/snippetExplore");
        String minDate = null;
        String maxDate = null;
        if (exploreForm.getMinDate() != null){
            minDate = sdf.format(new Timestamp(exploreForm.getMinDate().getTime()));
        }
        if (exploreForm.getMaxDate() != null){
            maxDate = sdf.format(new Timestamp(exploreForm.getMaxDate().getTime()));
        }
        Collection<Snippet> snippets = this.snippetService.findSnippetByDeepCriteria(
                minDate, maxDate,
                exploreForm.getMinRep(), exploreForm.getMaxRep(),
                exploreForm.getMinVotes(), exploreForm.getMaxVotes(),
                exploreForm.getLanguage() == -1 ? null : exploreForm.getLanguage(), exploreForm.getTag() == -1 ? null : exploreForm.getTag(),
                exploreForm.getTitle(), exploreForm.getUsername(),
                exploreForm.getField(), exploreForm.getSort(), page);
        int snippetCount =  this.snippetService.getSnippetByDeepCriteriaCount(
                minDate, maxDate,
                exploreForm.getMinRep(), exploreForm.getMaxRep(),
                exploreForm.getMinVotes(), exploreForm.getMaxVotes(),
                exploreForm.getLanguage() == -1 ? null : exploreForm.getLanguage(), exploreForm.getTag() == -1 ? null : exploreForm.getTag(),
                exploreForm.getTitle(), exploreForm.getUsername(),
                exploreForm.getField(), exploreForm.getSort());
        this.addModelAttributesHelper(mav, snippetCount, page, snippets, "explore/");
        return mav;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        Collection<Tag> userTags = currentUser != null ? this.tagService.getFollowedTagsForUser(currentUser.getId()) : new ArrayList<>();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userTags", userTags);
    }

    private void addModelAttributesHelper(ModelAndView mav, int snippetCount, int page, Collection<Snippet> snippets, String searchContext) {
        int pageSize = this.snippetService.getPageSize();
        mav.addObject("pages", snippetCount / pageSize + (snippetCount % pageSize == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("snippetList", snippets);
        mav.addObject("searchContext", searchContext);
        mav.addObject("tagList", this.tagService.getAllTags());
        mav.addObject("languageList", this.languageService.getAll());
    }
}

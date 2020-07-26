package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.utility.Constants;
import ar.edu.itba.paw.webapp.form.ExploreForm;
import ar.edu.itba.paw.webapp.form.FavoriteForm;
import ar.edu.itba.paw.webapp.form.SearchForm;
import ar.edu.itba.paw.webapp.utility.MavHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.Instant;
import java.util.*;

import static ar.edu.itba.paw.webapp.utility.Constants.SNIPPET_PAGE_SIZE;

//@Controller
public class SnippetExploreController {

//    @Autowired
    private SnippetService snippetService;
//    @Autowired
    private LoginAuthentication loginAuthentication;
//    @Autowired
    private TagService tagService;
//    @Autowired
    private LanguageService languageService;
//    @Autowired
    private RoleService roleService;
//    @Autowired
    private UserService userService;

    private final static Map<String, SnippetDao.Types> typesMap;
    static {
        final Map<String, SnippetDao.Types> types = new HashMap<>();
        types.put(null, SnippetDao.Types.TITLE);
        types.put("reputation", SnippetDao.Types.REPUTATION);
        types.put("votes", SnippetDao.Types.VOTES);
        types.put("date", SnippetDao.Types.DATE);
        types.put("title", SnippetDao.Types.TITLE);
        typesMap = Collections.unmodifiableMap(types);
    }

    private final static Map<String, SnippetDao.Orders> ordersMap;
    static {
        final Map<String, SnippetDao.Orders> orders = new HashMap<>();
        orders.put("asc", SnippetDao.Orders.ASC);
        orders.put("desc", SnippetDao.Orders.DESC);
        orders.put("no", SnippetDao.Orders.NO);
        ordersMap = Collections.unmodifiableMap(orders);
    }

    @RequestMapping("/explore")
    public ModelAndView explore(@ModelAttribute("searchForm") final SearchForm searchForm, @ModelAttribute("exploreForm") final ExploreForm exploreForm, final @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        final ModelAndView mav = new ModelAndView("snippet/snippetExplore");
        Collection<Snippet> snippets = this.snippetService.getAllSnippets(page, SNIPPET_PAGE_SIZE);
        int snippetCount = this.snippetService.getAllSnippetsCount();
        this.addModelAttributesHelper(mav, snippetCount, page, snippets, "explore/", false);
        return mav;
    }

    @RequestMapping("/explore/search")
    public ModelAndView exploreSearch(@Valid @ModelAttribute("searchForm") final SearchForm searchForm, @Valid @ModelAttribute("exploreForm") final ExploreForm exploreForm, final BindingResult errors, final @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        if (errors.hasErrors()) {
            return explore(searchForm, exploreForm, page);
        }
        final ModelAndView mav = new ModelAndView("snippet/snippetExplore");
        Instant minDate = null;
        Instant maxDate = null;
        if (exploreForm.getMinDate() != null){
            minDate = exploreForm.getMinDate().toInstant();
        }
        if (exploreForm.getMaxDate() != null){
            maxDate = exploreForm.getMaxDate().toInstant();
        }
        Collection<Snippet> snippets = this.snippetService.findSnippetByDeepCriteria(
                minDate, maxDate,
                exploreForm.getMinRep(), exploreForm.getMaxRep(),
                exploreForm.getMinVotes(), exploreForm.getMaxVotes(),
                exploreForm.getLanguage() == -1 ? null : exploreForm.getLanguage(), exploreForm.getTag() == -1 ? null : exploreForm.getTag(),
                exploreForm.getTitle(), exploreForm.getUsername(),
                ordersMap.get(exploreForm.getSort()), typesMap.get(exploreForm.getField()), exploreForm.getIncludeFlagged(), page, SNIPPET_PAGE_SIZE);
        int snippetCount =  this.snippetService.getSnippetByDeepCriteriaCount(
                minDate, maxDate,
                exploreForm.getMinRep(), exploreForm.getMaxRep(),
                exploreForm.getMinVotes(), exploreForm.getMaxVotes(),
                exploreForm.getLanguage() == -1 ? null : exploreForm.getLanguage(), exploreForm.getTag() == -1 ? null : exploreForm.getTag(),
                exploreForm.getTitle(), exploreForm.getUsername(),
                exploreForm.getIncludeFlagged());
        this.addModelAttributesHelper(mav, snippetCount, page, snippets, "explore/", true);
        return mav;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        MavHelper.addCurrentUserAttributes(model, currentUser, tagService, roleService);
        if (currentUser != null) {
            this.userService.updateLocale(currentUser.getId(), LocaleContextHolder.getLocale());
        }
    }

    private void addModelAttributesHelper(ModelAndView mav, int snippetCount, int page, Collection<Snippet> snippets, String searchContext, boolean searching) {
        mav.addObject("pages", snippetCount / SNIPPET_PAGE_SIZE + (snippetCount % SNIPPET_PAGE_SIZE == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("snippetList", snippets);
        mav.addObject("totalSnippetCount", snippetCount);
        mav.addObject("searchContext", searchContext);
        mav.addObject("searching", searching);
        mav.addObject("tagList", this.tagService.getAllTags());
        mav.addObject("languageList", this.languageService.getAllLanguages());

        User currentUser = this.loginAuthentication.getLoggedInUser();
        MavHelper.addSnippetCardFavFormAttributes(mav, currentUser, snippets);
    }
}

package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.form.ExploreForm;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

import static ar.edu.itba.paw.webapp.constants.Constants.SNIPPET_PAGE_SIZE;

@Controller
public class SnippetExploreController {

    @Autowired private SnippetService snippetService;
    @Autowired private LoginAuthentication loginAuthentication;
    @Autowired private TagService tagService;
    @Autowired private LanguageService languageService;
    @Autowired private RoleService roleService;
    @Autowired private UserService userService;
    @Autowired private MessageSource messageSource;

    private Map<String, SnippetDao.Types> typesMap = new HashMap<String, SnippetDao.Types>() {{
        put(null, SnippetDao.Types.TITLE);
        put("reputation", SnippetDao.Types.REPUTATION);
        put("votes", SnippetDao.Types.VOTES);
        put("date", SnippetDao.Types.DATE);
        put("title", SnippetDao.Types.TITLE);
    }};

    private Map<String, SnippetDao.Orders> ordersMap = new HashMap<String, SnippetDao.Orders>() {{
        put("asc", SnippetDao.Orders.ASC);
        put("desc", SnippetDao.Orders.DESC);
        put("no", SnippetDao.Orders.NO);
    }};

    public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").withLocale(Locale.UK)
            .withZone(ZoneId.systemDefault());

    @RequestMapping("/explore")
    public ModelAndView explore(@ModelAttribute("searchForm") final SearchForm searchForm, @ModelAttribute("exploreForm") final ExploreForm exploreForm, final @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        final ModelAndView mav = new ModelAndView("snippet/snippetExplore");
        Collection<Snippet> snippets = this.snippetService.getAllSnippets(page, SNIPPET_PAGE_SIZE);
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
                this.ordersMap.get(exploreForm.getSort()), this.typesMap.get(exploreForm.getField()), exploreForm.getIncludeFlagged(), exploreForm.isIncludeDeleted(), page, SNIPPET_PAGE_SIZE);
        int snippetCount =  this.snippetService.getSnippetByDeepCriteriaCount(
                minDate, maxDate,
                exploreForm.getMinRep(), exploreForm.getMaxRep(),
                exploreForm.getMinVotes(), exploreForm.getMaxVotes(),
                exploreForm.getLanguage() == -1 ? null : exploreForm.getLanguage(), exploreForm.getTag() == -1 ? null : exploreForm.getTag(),
                exploreForm.getTitle(), exploreForm.getUsername(),
                exploreForm.getIncludeFlagged(),
                exploreForm.isIncludeDeleted());
        this.addModelAttributesHelper(mav, snippetCount, page, snippets, "explore/");
        return mav;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        Collection<Tag> userTags = Collections.emptyList();
        Collection<String> userRoles = Collections.emptyList();

        if (currentUser != null) {
            userTags = this.tagService.getFollowedTagsForUser(currentUser.getId());
            userRoles = this.roleService.getUserRoles(currentUser);
            this.userService.updateLocale(currentUser.getId(), LocaleContextHolder.getLocale());
        }
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userTags", userTags);
        model.addAttribute("userRoles", userRoles);
    }


    private void addModelAttributesHelper(ModelAndView mav, int snippetCount, int page, Collection<Snippet> snippets, String searchContext) {
        mav.addObject("pages", snippetCount / SNIPPET_PAGE_SIZE + (snippetCount % SNIPPET_PAGE_SIZE == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("snippetList", snippets);
        mav.addObject("searchContext", searchContext);
        mav.addObject("tagList", this.tagService.getAllTags());
        mav.addObject("languageList", this.languageService.getAllLanguages());
    }
}

package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.form.SearchForm;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.*;

@Controller
public class SearchController {

    private Map<String, SnippetDao.Types> typesMap = new HashMap<String, SnippetDao.Types>(){{
        put(null, SnippetDao.Types.ALL);
        put("all", SnippetDao.Types.ALL);
        put("tag", SnippetDao.Types.TAG);
        put("title", SnippetDao.Types.TITLE);
        put("content", SnippetDao.Types.CONTENT);
    }};

    private Map<String, SnippetDao.Orders> ordersMap = new HashMap<String, SnippetDao.Orders>(){{
        put("asc", SnippetDao.Orders.ASC);
        put("desc", SnippetDao.Orders.DESC);
        put("no", SnippetDao.Orders.NO);
    }};

    @Autowired
    private SnippetService snippetService;
    @Autowired
    private LoginAuthentication loginAuthentication;
    @Autowired
    private TagService tagService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

    @RequestMapping("/search")
    public ModelAndView searchInHome(@Valid @ModelAttribute("searchForm") final SearchForm searchForm, final @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        final ModelAndView mav = new ModelAndView("index");
        Collection<Snippet> snippets = this.findByCriteria(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.HOME, searchForm.getSort(), null, page);
        int totalSnippetCount = this.getSnippetByCriteriaCount(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.HOME, null);

        this.addModelAttributesHelper(mav, totalSnippetCount, page, snippets, "");
        return mav;
    }

    @RequestMapping("/favorites/search")
    public ModelAndView searchInFavorites(@Valid @ModelAttribute("searchForm") final SearchForm searchForm, final @RequestParam(value = "page", required = false, defaultValue = "1") int page){
        final ModelAndView mav = new ModelAndView("index");
        User currentUser = this.loginAuthentication.getLoggedInUser();
        Collection<Snippet> snippets = this.findByCriteria(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.FAVORITES, searchForm.getSort(), currentUser.getId(), page);
        int totalSnippetCount = this.getSnippetByCriteriaCount(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.FAVORITES, currentUser.getId());

        this.addModelAttributesHelper(mav, totalSnippetCount, page, snippets, "favorites/");
        return mav;
    }

    @RequestMapping("/following/search")
    public ModelAndView searchInFollowing(@Valid @ModelAttribute("searchForm") final SearchForm searchForm, final @RequestParam(value = "page", required = false, defaultValue = "1") int page){
        final ModelAndView mav = new ModelAndView("index");
        User currentUser = this.loginAuthentication.getLoggedInUser();
        Collection<Snippet> snippets = this.findByCriteria(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.FOLLOWING, searchForm.getSort(), currentUser.getId(), page);
        int totalSnippetCount = this.getSnippetByCriteriaCount(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.FOLLOWING, currentUser.getId());

        this.addModelAttributesHelper(mav, totalSnippetCount, page, snippets, "following/");
        return mav;
    }

    private Collection<Snippet> findByCriteria(String type, String query, SnippetDao.Locations location, String sort, Long userId, int page){
        if (!this.typesMap.containsKey(type) || !this.ordersMap.containsKey(sort)){
            return new ArrayList<>();
        } else {
            return this.snippetService.findSnippetByCriteria(
                    this.typesMap.get(type),
                    query == null ? "" : query,
                    location,
                    this.ordersMap.get(sort),
                    userId,
                    page);
        }
    }

    private int getSnippetByCriteriaCount(String type, String query, SnippetDao.Locations location, Long userId){
        return this.snippetService.getSnippetByCriteriaCount(
                this.typesMap.get(type),
                query == null ? "" : query,
                location,
                userId);
    }

    private void addModelAttributesHelper(ModelAndView mav, int snippetCount, int page, Collection<Snippet> snippets, String searchContext) {
        int pageSize = this.snippetService.getPageSize();
        mav.addObject("pages", snippetCount/pageSize + (snippetCount % pageSize == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("snippetList", snippets);
        mav.addObject("searchContext",searchContext);
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        Collection<Tag> userTags = currentUser != null ? this.tagService.getFollowedTagsForUser(currentUser.getId()) : new ArrayList<>();
        if (currentUser != null) LOGGER.debug("Logged in user {} follows -> {}", currentUser.getUsername(), userTags.toString());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userTags", userTags);
    }


}

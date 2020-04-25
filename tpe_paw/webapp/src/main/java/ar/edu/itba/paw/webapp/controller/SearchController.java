package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.form.SearchForm;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.*;

@Controller
public class SearchController {

    private Map<String, SnippetDao.Types> typesMap = new HashMap<String, SnippetDao.Types>(){{
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

    @RequestMapping("/search")
    public ModelAndView searchInHome(@Valid @ModelAttribute("searchForm") final SearchForm searchForm) {
        final ModelAndView mav = new ModelAndView("index");
        Collection<Snippet> snippets = this.findByCriteria(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.HOME, searchForm.getSort(), null);
        mav.addObject("snippetList", snippets);
        mav.addObject("searchContext","");
        return mav;
    }

    @RequestMapping("/favorites/search")
    public ModelAndView searchInFavorites(@Valid @ModelAttribute("searchForm") final SearchForm searchForm){
        final ModelAndView mav = new ModelAndView("index");
        Long currentUserId = this.getCurrentUserId();
        Collection<Snippet> snippets = this.findByCriteria(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.FAVORITES, searchForm.getSort(), currentUserId);
        mav.addObject("snippetList", snippets);
        mav.addObject("searchContext","favorites/");
        return mav;
    }

    @RequestMapping("/following/search")
    public ModelAndView searchInFollowing(@Valid @ModelAttribute("searchForm") final SearchForm searchForm){
        final ModelAndView mav = new ModelAndView("index");
        Long currentUserId = this.getCurrentUserId();
        Collection<Snippet> snippets = this.findByCriteria(searchForm.getType(), searchForm.getQuery(), SnippetDao.Locations.FOLLOWING, searchForm.getSort(), currentUserId);
        mav.addObject("snippetList", snippets);
        mav.addObject("searchContext","following/");
        return mav;
    }

    private Long getCurrentUserId(){
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser != null){
            return currentUser.getUserId();
        } else {
            return null;
        }
    }

    private Collection<Snippet> findByCriteria(String type, String query, SnippetDao.Locations location, String sort, Long userId){
        if (!this.typesMap.containsKey(type) || !this.ordersMap.containsKey(sort)){
            return new ArrayList<>();
        } else {
            return this.snippetService.findSnippetByCriteria(
                    this.typesMap.get(type),
                    query,
                    location,
                    this.ordersMap.get(sort),
                    userId);
        }
    }
}

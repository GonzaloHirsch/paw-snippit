package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class SearchController {

    private Map<String, SnippetDao.Types> typesMap = new HashMap<String, SnippetDao.Types>(){{
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
    private UserService userService;

    @RequestMapping("/search")
    public ModelAndView searchInHome(@RequestParam(value = "q", required = true) String query, @RequestParam(value = "t", required = true) String type, @RequestParam(value = "s", required = true) String sort) {
        final ModelAndView mav = new ModelAndView("snippet/snippetFeed");
        Collection<Snippet> snippets = this.snippetService.findSnippetByCriteria(
                this.typesMap.get(type),
                query,
                SnippetDao.Locations.HOME,
                this.ordersMap.get(sort),
                null);
        mav.addObject("snippetList", snippets);
        return mav;
    }

    @RequestMapping("/favorites/search")
    public ModelAndView searchInFavorites(@RequestParam(value = "q", required = true) String query, @RequestParam(value = "t", required = true) String type, @RequestParam(value = "s", required = true) String sort) {
        final ModelAndView mav = new ModelAndView("snippet/snippetFeed");
        Optional<User> user = this.userService.getCurrentUser();
        Long userId = null;
        if (user.isPresent()){
            userId = user.get().getUserId();
        }
        Collection<Snippet> snippets = this.snippetService.findSnippetByCriteria(
                this.typesMap.get(type),
                query,
                SnippetDao.Locations.FAVORITES,
                this.ordersMap.get(sort),
                userId);
        mav.addObject("snippetList", snippets);
        return mav;
    }

    @RequestMapping("/following/search")
    public ModelAndView searchInFollowing(@RequestParam(value = "q", required = true) String query, @RequestParam(value = "t", required = true) String type, @RequestParam(value = "s", required = true) String sort) {
        final ModelAndView mav = new ModelAndView("snippet/snippetFeed");
        Optional<User> user = this.userService.getCurrentUser();
        Long userId = null;
        if (user.isPresent()){
            userId = user.get().getUserId();
        }
        Collection<Snippet> snippets = this.snippetService.findSnippetByCriteria(
                this.typesMap.get(type),
                query,
                SnippetDao.Locations.FAVORITES,
                this.ordersMap.get(sort),
                userId);
        mav.addObject("snippetList", snippets);
        return mav;
    }
}

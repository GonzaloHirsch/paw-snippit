package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.models.Snippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@Controller
public class SearchController {

    @Autowired
    private SnippetService snippetService;

    @RequestMapping("/search")
    public ModelAndView searchInHome(@RequestParam(value = "q", required = true) String query, @RequestParam(value = "t", required = true) String type) {
        final ModelAndView mav = new ModelAndView("snippet/snippetFeed");
        Collection<Snippet> snippets = null;
        switch (type) {
            case "tag":
                snippets = this.snippetService.findSnippetsByTag(query, "home", null);
                break;
            case "title":
                snippets = this.snippetService.findSnippetsByTitle(query, "home", null);
                break;
            case "content":
                snippets = this.snippetService.findSnippetsByContent(query, "home", null);
                break;
        }
        mav.addObject("snippetList", snippets);
        return mav;
    }

    @RequestMapping("/favorites/search")
    public ModelAndView searchInFavorites(@RequestParam(value = "q", required = true) String query, @RequestParam(value = "t", required = true) String type, @RequestParam(value = "u", required = true) String userId) {
        final ModelAndView mav = new ModelAndView("snippet/snippetFeed");
        Collection<Snippet> snippets = null;
        switch (type) {
            case "tag":
                snippets = this.snippetService.findSnippetsByTag(query, "favorites", userId);
                break;
            case "title":
                snippets = this.snippetService.findSnippetsByTitle(query, "favorites", userId);
                break;
            case "content":
                snippets = this.snippetService.findSnippetsByContent(query, "favorites", userId);
                break;
        }
        mav.addObject("snippetList", snippets);
        return mav;
    }

    @RequestMapping("/following/search")
    public ModelAndView searchInFollowing(@RequestParam(value = "q", required = true) String query, @RequestParam(value = "t", required = true) String type, @RequestParam(value = "u", required = true) String userId) {
        final ModelAndView mav = new ModelAndView("snippet/snippetFeed");
        Collection<Snippet> snippets = null;
        switch (type) {
            case "tag":
                snippets = this.snippetService.findSnippetsByTag(query, "following", userId);
                break;
            case "title":
                snippets = this.snippetService.findSnippetsByTitle(query, "following", userId);
                break;
            case "content":
                snippets = this.snippetService.findSnippetsByContent(query, "following", userId);
                break;
        }
        mav.addObject("snippetList", snippets);
        return mav;
    }
}

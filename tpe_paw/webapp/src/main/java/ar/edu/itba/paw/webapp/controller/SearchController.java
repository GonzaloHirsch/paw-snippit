package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;

@Controller
public class SearchController {

    @Autowired
    private SnippetService snippetService;

    @RequestMapping("/search")
    public ModelAndView searchInHome(@Valid @ModelAttribute("searchForm") final SearchForm searchForm) {
        final ModelAndView mav = new ModelAndView("snippet/snippetFeed");
        Collection<Snippet> snippets = null;
        switch (searchForm.getType()) {
            case "tag":
                snippets = this.snippetService.findSnippetsByTag(searchForm.getQuery(), "home", null);
                break;
            case "title":
                System.out.println("En title");
                snippets = this.snippetService.findSnippetsByTitle(searchForm.getQuery(), "home", null);
                break;
            case "content":
                snippets = this.snippetService.findSnippetsByContent(searchForm.getQuery(), "home", null);
                break;
        }
        mav.addObject("snippetList", snippets);
        return mav;
    }

    @RequestMapping("/favorites/search")
    public ModelAndView searchInFavorites(@Valid @ModelAttribute("searchForm") final SearchForm searchForm){
        final ModelAndView mav = new ModelAndView("snippet/snippetFeed");
        Collection<Snippet> snippets = null;
        switch (searchForm.getType()) {
            case "tag":
                snippets = this.snippetService.findSnippetsByTag(searchForm.getQuery(), "favorites", searchForm.getUserId());
                break;
            case "title":
                snippets = this.snippetService.findSnippetsByTitle(searchForm.getQuery(), "favorites", searchForm.getUserId());
                break;
            case "content":
                snippets = this.snippetService.findSnippetsByContent(searchForm.getQuery(), "favorites", searchForm.getUserId());
                break;
        }
        mav.addObject("snippetList", snippets);
        return mav;
    }

    @RequestMapping("/following/search")
    public ModelAndView searchInFollowing(@Valid @ModelAttribute("searchForm") final SearchForm searchForm){
        final ModelAndView mav = new ModelAndView("snippet/snippetFeed");
        Collection<Snippet> snippets = null;
        switch (searchForm.getType()) {
            case "tag":
                snippets = this.snippetService.findSnippetsByTag(searchForm.getQuery(), "following", searchForm.getUserId());
                break;
            case "title":
                snippets = this.snippetService.findSnippetsByTitle(searchForm.getQuery(), "following", searchForm.getUserId());
                break;
            case "content":
                snippets = this.snippetService.findSnippetsByContent(searchForm.getQuery(), "following", searchForm.getUserId());
                break;
        }
        mav.addObject("snippetList", snippets);
        return mav;
    }

    @RequestMapping(value = "/searchTest/", method={RequestMethod.GET})
    public ModelAndView searchForm(@ModelAttribute("searchForm") final SearchForm searchForm){
        return new ModelAndView("navBar/navBarTop");
    }
}

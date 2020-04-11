package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@Controller
public class SnippetFeedController {

    @Autowired
    private SnippetService snippetService;

    @RequestMapping("/feed")
    public ModelAndView getHomeSnippetFeed(@ModelAttribute("searchForm") final SearchForm searchForm) {
        final ModelAndView mav = new ModelAndView("snippet/snippetFeed");
        Collection<Snippet> snippetsHomeFeed = this.snippetService.getAllSnippets();

        mav.addObject("snippetList", snippetsHomeFeed);
        mav.addObject("searchContext","following/"); // = /search/
        return mav;
    }
}

package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.models.Snippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@Controller
public class SnippetFeedController {

    @Autowired
    private SnippetService snippetService;

    @RequestMapping("/")
    public ModelAndView getHomeSnippetFeed() {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("snippetList", this.snippetService.getAllSnippets());
        return mav;
    }

    @RequestMapping("/favorites")
    public ModelAndView getFavoritesSnippetFeed() {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("snippetList", this.snippetService.getAllSnippets());
        return mav;
    }

    @RequestMapping("/following")
    public ModelAndView getFollowingSnippetFeed() {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("snippetList", this.snippetService.getAllSnippets());
        return mav;
    }
}

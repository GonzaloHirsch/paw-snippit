package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Controller
public class SnippetFeedController {
    @Autowired
    SnippetService snippetService;

    @RequestMapping("/feed")
    public ModelAndView getHomeSnippetFeed() {
        final ModelAndView mav = new ModelAndView("snippet/snippetFeed");

        Date d = new Date();
        User u = new User("lollipop","pass", "myemail", "",1, d);
        Collection<Snippet> snippetsHomeFeed = snippetService.getAllSnippets();

        mav.addObject("snippetList", snippetsHomeFeed);
        return mav;
    }
}

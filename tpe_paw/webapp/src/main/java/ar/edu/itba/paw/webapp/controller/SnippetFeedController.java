package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Snippet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
public class SnippetFeedController {

    @RequestMapping("/feed")
    public ModelAndView getHomeSnippetFeed() {
        final ModelAndView mav = new ModelAndView("snippet/snippetFeed");
        List<Snippet> snippetsHomeFeed = Arrays.asList(
                new Snippet(1, 1, "CODIGO", "TEST CARD", "Just wanna test the card"),
                new Snippet(2, 1, "JAVA ROKS", "TEST CARDS", "")
        );
        mav.addObject("snippetList", snippetsHomeFeed);
        return mav;
    }
}

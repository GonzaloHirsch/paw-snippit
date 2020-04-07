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

    @RequestMapping("/search/tag")
    public ModelAndView searchByTag(@RequestParam(value = "searchTerm", required = true) String searchTerm) {
        final ModelAndView mav = new ModelAndView("snippet/snippetFeed");
        Collection<Snippet> snippets = this.snippetService.getSnippetByTag(searchTerm);
        mav.addObject("greeting", "PAW");
        return mav;
    }
}

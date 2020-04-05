package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.SnippetService;
import ar.edu.itba.paw.models.Snippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloWorldController {

    @Autowired
    private SnippetService snippetService;

    @RequestMapping("/")
    public ModelAndView helloWorld() {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("greeting", "PAW");
        return mav;
    }
    @RequestMapping("/snippet/{id}")
    public ModelAndView snippetDetail(@PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("snippetDetail");
//        mav.addObject("snippet", new Snippet(id, 1234, "code", "title", "description"));
        mav.addObject("snippet", snippetService.getSnippetById(String.valueOf(id)));
        return mav;
    }
}
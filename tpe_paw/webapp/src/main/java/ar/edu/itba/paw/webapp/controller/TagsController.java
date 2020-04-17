package ar.edu.itba.paw.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ar.edu.itba.paw.interfaces.service.TagService;

@Controller
public class TagsController {

    @Autowired
    private TagService tagService;

    @RequestMapping("/tags")
    public ModelAndView showAllTags() {
        ModelAndView mav = new ModelAndView("tags/tags");
        return mav;
    }

}
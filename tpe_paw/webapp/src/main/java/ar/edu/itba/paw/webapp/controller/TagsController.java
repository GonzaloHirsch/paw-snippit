package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@Controller
public class TagsController {

    @Autowired
    private TagService tagService;
    @Autowired
    private SnippetService snippetService;

    @RequestMapping("/tags")
    public ModelAndView showAllTags(@ModelAttribute final SearchForm searchForm) {
        ModelAndView mav = new ModelAndView("tag/tags");
        Collection<Tag> allTags = tagService.getAllTags();
        mav.addObject("tags", allTags); 
        return mav;
    }
    @RequestMapping("/tags/{tagId}")
    public ModelAndView showSnippetsForTag(@PathVariable("tagId") long tagId){
        ModelAndView mav = new ModelAndView("tag/tagSnippets");
        mav.addObject("snippets", snippetService.findSnippetsForTag(tagId));
        return mav;
    }

}
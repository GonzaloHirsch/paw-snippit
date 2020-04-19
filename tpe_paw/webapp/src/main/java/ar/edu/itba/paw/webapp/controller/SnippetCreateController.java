package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.webapp.form.SearchForm;
import ar.edu.itba.paw.webapp.form.SnippetCreateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class SnippetCreateController {
    @Autowired
    private SnippetService snippetService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView snippetCreateDetail(@ModelAttribute("searchForm") final SearchForm searchForm, @ModelAttribute("snippetCreateForm") final SnippetCreateForm snippetCreateForm) {
        final ModelAndView mav = new ModelAndView("snippetCreate");
        mav.addObject("searchContext","");
        return mav;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView snippetCreate(@ModelAttribute("searchForm") final SearchForm searchForm, @Valid @ModelAttribute("snippetCreateForm") final SnippetCreateForm snippetCreateForm){
        final ModelAndView mav = new ModelAndView("snippetCreate");
        mav.addObject("searchContext","");
        return mav;
    }

package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.LanguageService;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.SearchForm;
import ar.edu.itba.paw.webapp.form.SnippetCreateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Optional;

@Controller
public class SnippetCreateController {

    @Autowired private SnippetService snippetService;
    @Autowired private TagService tagService;
    @Autowired private LanguageService languageService;
    @Autowired private LoginAuthentication loginAuthentication;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)


    @RequestMapping(value = "/snippet/create")
    public ModelAndView snippetCreateDetail(@ModelAttribute("snippetCreateForm") final SnippetCreateForm snippetCreateForm) {

        User currentUser = loginAuthentication.getLoggedInUser();
        if(currentUser == null) {
                //Todo LOGGER
        }

        final ModelAndView mav = new ModelAndView("snippet/snippetCreate");
        mav.addObject("currentUser", currentUser);
        mav.addObject("tagList",tagService.getAllTags());
        mav.addObject("languageList", languageService.getAll());
        mav.addObject("searchContext", "");

        return mav;
    }

    @RequestMapping(value = "/snippet/create", method = RequestMethod.POST)
    public ModelAndView snippetCreate( @Valid @ModelAttribute("snippetCreateForm") final SnippetCreateForm snippetCreateForm, final BindingResult errors) {

        if (errors.hasErrors())
            return snippetCreateDetail(snippetCreateForm);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String dateCreated = sdf.format(timestamp);

        User currentUser = loginAuthentication.getLoggedInUser();
        if(currentUser == null)
            throw new UserNotFoundException();

        Long snippetId = snippetService.createSnippet(currentUser,snippetCreateForm.getTitle(),snippetCreateForm.getDescription(), snippetCreateForm.getCode(), dateCreated, snippetCreateForm.getLanguage(),snippetCreateForm.getTags());
        if(snippetId == null){
            //TODO: Throw new exception
        }

        return new ModelAndView("redirect:/snippet/" + snippetId);

    }

    @ModelAttribute
    public void addAttributes(ModelAndView model, @Valid final SearchForm searchForm) {
        model.addObject("searchForm", searchForm);
    }
}

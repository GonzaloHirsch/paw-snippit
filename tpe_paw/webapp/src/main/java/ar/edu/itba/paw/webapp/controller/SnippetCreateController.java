package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
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
    @Autowired
    private SnippetService snippetService;

    @Autowired
    private UserService userService;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)


    @RequestMapping(value = "/create")
    public ModelAndView snippetCreateDetail(@ModelAttribute("searchForm") final SearchForm searchForm, @ModelAttribute("snippetCreateForm") final SnippetCreateForm snippetCreateForm) {
        final ModelAndView mav = new ModelAndView("snippet/snippetCreate");
        mav.addObject("searchContext", "");
        return mav;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView snippetCreate( @Valid @ModelAttribute("snippetCreateForm") final SnippetCreateForm snippetCreateForm, final BindingResult errors,@ModelAttribute("searchForm") final SearchForm searchForm) {
        if (errors.hasErrors())
            return snippetCreateDetail(searchForm,snippetCreateForm);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String dateCreated = sdf.format(timestamp);

        User currentUser = userService.getCurrentUser().orElseThrow(UserNotFoundException::new);


        Snippet snippet = snippetService.createSnippet(currentUser,snippetCreateForm.getTitle(),snippetCreateForm.getDescription(), snippetCreateForm.getCode(), dateCreated, snippetCreateForm.getLanguage(),snippetCreateForm.getTags()).get();

        //TODO: Validar que el snippet se haya creado correctamente

        final ModelAndView mav = new ModelAndView("redirect:/snippet/" + snippet.getId());
        return mav;
    }
}

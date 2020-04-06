package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class SnippetController {
    @Autowired
    private SnippetService snippetService;
    private UserService userService;

    @RequestMapping("/snippet/{id}")
    public ModelAndView snippetDetail(@PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("snippetDetail");
//        mav.addObject("snippet", new Snippet(id, 1234, "code", "title", "description"));
        Optional<Snippet> retrievedSnippet = snippetService.getSnippetById(String.valueOf(id));
        if(retrievedSnippet.isPresent())
            mav.addObject("snippet", retrievedSnippet.get());
        else
            mav.addObject("noSnippet",true);
        //        User user = userService.getCurrentUser();
//        mav.addObject("canVote", userService.checkUserCanVote(user));
        return mav;
    }


}

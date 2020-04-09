package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.interfaces.service.VoteService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class SnippetController {

    @Autowired
    private UserService userService;
    @Autowired
    private SnippetService snippetService;
    @Autowired
    private VoteService voteService;

    @RequestMapping("/snippet/{id}")
    public ModelAndView snippetDetail(@PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("snippetDetail");
        Optional<Snippet> retrievedSnippet = snippetService.getSnippetById(id);
        retrievedSnippet.ifPresent(snippet -> mav.addObject("snippet", snippet));
        Optional<User> user = userService.getCurrentUser();
        Optional<Vote> vote = voteService.getVote(1, retrievedSnippet.get().getId());

        int voteType = -1;
        if (vote.isPresent()) {
            voteType = vote.get().getType();
        }
        mav.addObject("vote", voteType);
        return mav;
    }


}

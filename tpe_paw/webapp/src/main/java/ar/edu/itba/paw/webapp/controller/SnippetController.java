package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.interfaces.service.VoteService;
import ar.edu.itba.paw.models.Snippet;
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
    private VoteService voteService;

    @RequestMapping("/snippet/{id}")
    public ModelAndView snippetDetail(@PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("snippet/snippetDetail");
        Optional<Snippet> retrievedSnippet = snippetService.findSnippetById(id);
        if (retrievedSnippet.isPresent()) {
            mav.addObject("snippet", retrievedSnippet.get());
        }
        else {
            mav.addObject("noSnippet", true);
        }

//        Optional<User> user = userService.getCurrentUser();
//        Optional<Vote> vote = voteService.getVote(user.get().getUserId(), retrievedSnippet.get().getId());
//
//        int voteType = -1;
//        if (vote.isPresent()) {
//            voteType = vote.get().getVoteType();
//        }
//
//        mav.addObject("canVoteUp", voteType != 0);
//        mav.addObject("canVoteDown", voteType );
        return mav;
    }


}

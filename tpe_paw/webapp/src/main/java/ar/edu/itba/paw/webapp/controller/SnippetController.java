package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.interfaces.service.VoteService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.Vote;
import ar.edu.itba.paw.webapp.form.VoteForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    public ModelAndView snippetDetail(@ModelAttribute("snippetId") @PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("snippet/snippetDetail");
        Optional<Snippet> retrievedSnippet = snippetService.findSnippetById(id);
        retrievedSnippet.ifPresent(snippet -> mav.addObject("snippet", snippet));
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            // TODO handle error
        }
        Optional<Vote> vote = voteService.getVote(1, retrievedSnippet.get().getId());
        mav.addObject("user", user.get());
        int voteType = -1;
        if (vote.isPresent()) {
            voteType = vote.get().getType();
        }
        VoteForm voteForm = new VoteForm();
        voteForm.setType(voteType);
        voteForm.setUserId(user.get().getUserId());
        voteForm.setSnippetId(id);
        mav.addObject("vote", voteForm);
        return mav;
    }

    @RequestMapping(value="/snippet/vote", method=RequestMethod.POST)
    public ModelAndView voteFor(@ModelAttribute("vote") final VoteForm voteForm) {
        final ModelAndView mav = new ModelAndView("redirect:/snippet/" + voteForm.getSnippetId());
        voteService.performVote(voteForm.getUserId(), voteForm.getSnippetId(), voteForm.getType());
        return mav;
    }
}

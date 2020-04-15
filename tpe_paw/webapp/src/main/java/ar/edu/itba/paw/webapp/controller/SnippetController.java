package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.FavoriteService;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.interfaces.service.VoteService;
import ar.edu.itba.paw.models.Favorite;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.Vote;
import ar.edu.itba.paw.webapp.form.FavoriteForm;
import ar.edu.itba.paw.webapp.form.SearchForm;
import ar.edu.itba.paw.webapp.form.VoteForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    @Autowired
    private FavoriteService favService;

    @RequestMapping("/snippet/{id}")
    public ModelAndView snippetDetail(
            @ModelAttribute("snippetId") @PathVariable("id") long id,
            @ModelAttribute("searchForm") final SearchForm searchForm,
            @ModelAttribute("favForm") final FavoriteForm favForm,
           @ModelAttribute("voteForm") final VoteForm voteForm
    ) {
        final ModelAndView mav = new ModelAndView("snippet/snippetDetail");
        // Snippet
        Optional<Snippet> retrievedSnippet = this.snippetService.findSnippetById(id);
        retrievedSnippet.ifPresent(snippet -> {
                mav.addObject("snippet", snippet);
        });
        //User
        Optional<User> user = this.userService.getCurrentUser();
        // TODO Do we need to pass user logged in to the detail? Why?
        if (!user.isPresent()) {
            // TODO No user is logged in -> Fav/vote buttons take to login screen
        }
        mav.addObject("user", user.get());
        // Vote
        Optional<Vote> vote = this.voteService.getVote(user.get().getUserId(), retrievedSnippet.get().getId());
        int voteType = 0;
        if (vote.isPresent()) {
            voteType = vote.get().getType();
        }
        voteForm.setType(voteType);
        voteForm.setOldType(voteType);
        voteForm.setUserId(user.get().getUserId());
        // Fav
        Optional<Favorite> fav = this.favService.getFavorite(user.get().getUserId(), retrievedSnippet.get().getId());
        favForm.setFavorite(fav.isPresent());
        favForm.setUserId(user.get().getUserId());
        // Vote Count
        Optional<Integer> voteCount = this.voteService.getVoteBalance(retrievedSnippet.get().getId());
        if (voteCount.isPresent()){
            mav.addObject("voteCount",voteCount.get());
        } else {
            mav.addObject("voteCount",0);
        }
        mav.addObject("searchContext","");
        return mav;
    }

    @RequestMapping(value="/snippet/{id}/vote", method=RequestMethod.POST)
    public ModelAndView voteFor(
            @ModelAttribute("snippetId") @PathVariable("id") long id,
            @ModelAttribute("voteForm") final VoteForm voteForm
    ) {
        final ModelAndView mav = new ModelAndView("redirect:/snippet/" + id);
        this.voteService.performVote(voteForm.getUserId(), id, voteForm.getType(), voteForm.getOldType());
        return mav;
    }

    @RequestMapping(value="/snippet/{id}/fav", method=RequestMethod.POST)
    public ModelAndView favSnippet(
            @ModelAttribute("snippetId") @PathVariable("id") long id,
            @ModelAttribute("favForm") final FavoriteForm favForm
    ) {
        final ModelAndView mav = new ModelAndView("redirect:/snippet/" + id);
        this.favService.updateFavorites(favForm.getUserId(), id, favForm.getFavorite());
        return mav;
    }
}
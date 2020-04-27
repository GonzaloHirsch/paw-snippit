package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.Favorite;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.Vote;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.form.FavoriteForm;
import ar.edu.itba.paw.webapp.form.SearchForm;
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
    private SnippetService snippetService;
    @Autowired
    private VoteService voteService;
    @Autowired
    private FavoriteService favService;
    @Autowired
    private LoginAuthentication loginAuthentication;
    @Autowired
    private TagService tagService;


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

        if (!retrievedSnippet.isPresent()) {
            // TODO --> Logger + REDIRECT TO 500 ERROR CODE!!
            // TODO --> throw new ...
        }
        User currentUser = this.loginAuthentication.getLoggedInUser();
        mav.addObject("currentUser", currentUser);
        if (currentUser != null){
            mav.addObject("userTags", this.tagService.getFollowedTagsForUser(currentUser.getId()));

            // Vote
            Optional<Vote> vote = this.voteService.getVote(currentUser.getId(), retrievedSnippet.get().getId());
            int voteType = 0;
            if (vote.isPresent()) {
                voteType = vote.get().getType();
            }
            voteForm.setType(voteType);
            voteForm.setOldType(voteType);
            voteForm.setVoteUserId(currentUser.getId());

            // Fav
            Optional<Favorite> fav = this.favService.getFavorite(currentUser.getId(), retrievedSnippet.get().getId());
            favForm.setFavorite(fav.isPresent());
            favForm.setFavUserId(currentUser.getId());
        } else {
            // ERROR
        }

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
        this.voteService.performVote(voteForm.getVoteUserId(), id, voteForm.getType(), voteForm.getOldType());
        return mav;
    }

    @RequestMapping(value="/snippet/{id}/fav", method=RequestMethod.POST)
    public ModelAndView favSnippet(
            @ModelAttribute("snippetId") @PathVariable("id") long id,
            @ModelAttribute("favForm") final FavoriteForm favForm
    ) {
        final ModelAndView mav = new ModelAndView("redirect:/snippet/" + id);
        this.favService.updateFavorites(favForm.getFavUserId(), id, favForm.getFavorite());
        return mav;
    }
}

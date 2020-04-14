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

    private Snippet retrSnippet;
    private User currentUser;

    @RequestMapping("/snippet/{id}")
    public ModelAndView snippetDetail(
            @ModelAttribute("snippetId") @PathVariable("id") long id,
            @ModelAttribute("searchForm") final SearchForm searchForm
            //@ModelAttribute("favForm") final FavoriteForm favForm
           // @ModelAttribute("voteForm") final VoteForm voteForm
    ) {

        final ModelAndView mav = new ModelAndView("snippet/snippetDetail");
        // Snippet
        Optional<Snippet> retrievedSnippet = snippetService.findSnippetById(id);
        retrievedSnippet.ifPresent(snippet -> {
                retrSnippet = snippet;
                mav.addObject("snippet", snippet);
        });

        //User
        // TODO Do we need to pass user logged in to the detail? Why?
        Optional<User> user = userService.getCurrentUser();
        if (!user.isPresent()) {
            // TODO No user is logged in
        }
        currentUser = user.get();
        mav.addObject("user", user.get());



        //Vote
//        Optional<Vote> vote = voteService.getVote(user.get().getUserId(), retrievedSnippet.get().getId());
//        int voteType = 0;
//        if (vote.isPresent()) {
//            voteType = vote.get().getType();
//        }
//        voteForm.setType(voteType);
//        voteForm.setOldType(voteType);
//        voteForm.setUserId(user.get().getUserId());
//        voteForm.setSnippetId(id);

        mav.addObject("searchContext","");
        return mav;
    }


    @RequestMapping(value = "/snippet/{id}/fav", method = RequestMethod.GET)
    public ModelAndView getFavForm (@ModelAttribute("snippetId") @PathVariable("id") long id) {
        System.out.println("\nInside FAV GET\n!");
        return new ModelAndView("snippet/favForm", "favForm", new FavoriteForm());
    }

    @RequestMapping(value = "/snippet/{id}/vote", method = RequestMethod.GET)
    public ModelAndView getVoteForm (@ModelAttribute("snippetId") @PathVariable("id") long id) {
        return new ModelAndView("snippet/voteForm");
    }


    @RequestMapping(value="/snippet/{id}/vote", method=RequestMethod.POST)
    public ModelAndView voteFor(
            @ModelAttribute("snippetId") @PathVariable("id") long id,
            @ModelAttribute("voteForm") final VoteForm voteForm
    ) {
        final ModelAndView mav = new ModelAndView("redirect:/snippet/" + voteForm.getSnippetId());

        System.out.println("Vote form 2 " + voteForm);


        // TODO --> hacer estas validaciones en otro lado
        if (voteForm.getOldType() == voteForm.getType()) {
            voteService.withdrawVote(voteForm.getUserId(), voteForm.getSnippetId());
        } else {
            voteService.performVote(voteForm.getUserId(), voteForm.getSnippetId(), voteForm.getType());
        }
        return mav;
    }

    @RequestMapping(value="/snippet/{id}/fav", method=RequestMethod.POST)
    public ModelAndView favSnippet(
            @ModelAttribute("snippetId") @PathVariable("id") long id,
            @ModelAttribute("favForm") final FavoriteForm favForm
    ) {

        final ModelAndView mav = new ModelAndView("redirect:/snippet/" + id);

        //Fav
        Optional<Favorite> fav = favService.getFavorite(currentUser.getUserId(), retrSnippet.getId());
        favForm.setWasFavorite(fav.isPresent());
        favForm.setUserId(currentUser.getUserId());
        favForm.setSnippetId(retrSnippet.getId());


//        System.out.println("Fav form 2 " + favForm);
//
//        System.out.println("BEFORE = " + favForm.getWasFavorite() + favForm.getFavorite());
//        if (favForm.getWasFavorite() == favForm.getFavorite()) {
//            favForm.setFavorite(false);
//            favForm.setWasFavorite(false);
//        } else {
//            favForm.setWasFavorite(true);
//        }
        System.out.println("WAS = " + favForm.getWasFavorite());
        System.out.println("IS = " + favForm.getFavorite());

        //favService.updateFavorites(favForm.getUserId(), favForm.getSnippetId(), favForm.getWasFavorite());

        return mav;
    }
}

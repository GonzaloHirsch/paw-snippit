package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.Favorite;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.Vote;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.exception.ElementDeletionException;
import ar.edu.itba.paw.webapp.exception.ForbiddenAccessException;
import ar.edu.itba.paw.webapp.exception.SnippetNotFoundException;
import ar.edu.itba.paw.webapp.form.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class SnippetController {

    @Autowired private RoleService roleService;
    @Autowired private SnippetService snippetService;
    @Autowired private VoteService voteService;
    @Autowired private FavoriteService favService;
    @Autowired private LoginAuthentication loginAuthentication;
    @Autowired private TagService tagService;
    @Autowired private UserService userService;
    @Autowired private MessageSource messageSource;
    @Autowired private EmailService emailService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SnippetController.class);

    private static boolean wasLoggedIn = false;


    @RequestMapping("/snippet/{id}")
    public ModelAndView snippetDetail(
            @ModelAttribute("snippetId") @PathVariable("id") long id,
            @ModelAttribute("searchForm") final SearchForm searchForm,
            @ModelAttribute("adminFlagForm") final FlagSnippetForm adminFlagForm,
            @ModelAttribute("deleteForm") final DeleteForm deleteForm,
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
            logAndThrow(id);
        }

        User currentUser = this.loginAuthentication.getLoggedInUser();
        mav.addObject("currentUser", currentUser);
        if (currentUser != null){
            wasLoggedIn = true;
            mav.addObject("userTags", this.tagService.getFollowedTagsForUser(currentUser.getId()));

            // Vote
            Optional<Vote> vote = this.voteService.getVote(currentUser.getId(), retrievedSnippet.get().getId());
            int voteType = 0;
            if (vote.isPresent()) {
                voteType = vote.get().getType();
            }
            voteForm.setType(voteType);
            voteForm.setOldType(voteType);

            // Fav
            Optional<Favorite> fav = this.favService.getFavorite(currentUser.getId(), retrievedSnippet.get().getId());
            favForm.setFavorite(fav.isPresent());

            if (roleService.isAdmin(currentUser.getId())) {
                adminFlagForm.setFlagged(retrievedSnippet.get().isFlagged());
                mav.addObject("userRoles", this.roleService.getUserRoles(currentUser.getId()));
            }
        } else {
            wasLoggedIn = false;
            mav.addObject("userRoles", new ArrayList<>());
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

    @RequestMapping(value="/snippet/{id}/delete", method=RequestMethod.POST)
    public ModelAndView deleteSnippet(
            @ModelAttribute("snippetId") @PathVariable("id") long id,
            @ModelAttribute("deleteForm") final DeleteForm deleteForm
    ) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        Optional<Snippet> snippet = this.snippetService.findSnippetById(id);

        if (!snippet.isPresent()) {
            logAndThrow(id);
        }
        if (currentUser == null || currentUser.getUsername().compareTo(snippet.get().getOwner().getUsername()) != 0) {
            throw new ForbiddenAccessException(messageSource.getMessage("error.403.snippet.delete", null, LocaleContextHolder.getLocale()));
        } else {
            /* Want to reverse the voteBalance on the users reputation */
            int voteBalance = this.snippetService.getReputationImportanceBalance(snippet.get());
            if (!this.snippetService.deleteSnippetById(id)) {
                /* Operation was unsuccessful */
                throw new ElementDeletionException(messageSource.getMessage("error.409.deletion.snippet", null, LocaleContextHolder.getLocale()));
            }
            this.userService.changeReputation(currentUser.getId(), voteBalance);
        }
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value="/snippet/{id}/vote", method=RequestMethod.POST)
    public ModelAndView voteFor(
            @ModelAttribute("snippetId") @PathVariable("id") long id,
            @ModelAttribute("voteForm") final VoteForm voteForm
    ) {
        final ModelAndView mav = new ModelAndView("redirect:/snippet/" + id);
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null) {
            throw new ForbiddenAccessException(messageSource.getMessage("error.403.snippet.vote", null, LocaleContextHolder.getLocale()));
        } else if (wasLoggedIn) {
            this.voteService.performVote(currentUser.getId(), id, voteForm.getType(), voteForm.getOldType());
        } else {
            this.voteService.performVote(currentUser.getId(), id, voteForm.getType(), 0);
        }
        return mav;
    }

    @RequestMapping(value="/snippet/{id}/fav", method=RequestMethod.POST)
    public ModelAndView favSnippet(
            @ModelAttribute("snippetId") @PathVariable("id") long id,
            @ModelAttribute("favForm") final FavoriteForm favForm
    ) {
        final ModelAndView mav = new ModelAndView("redirect:/snippet/" + id);
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null) {
            throw new ForbiddenAccessException(messageSource.getMessage("error.403.snippet.fav", null, LocaleContextHolder.getLocale()));
        } else {
            this.favService.updateFavorites(currentUser.getId(), id, favForm.getFavorite());
            LOGGER.debug("User {} updated favorite on snippet {}", currentUser.getUsername(), id);
        }
        return mav;
    }

    @RequestMapping(value="/snippet/{id}/flag", method=RequestMethod.POST)
    public ModelAndView flagSnippet(
            @ModelAttribute("snippetId") @PathVariable("id") long id,
            @ModelAttribute("adminFlagForm") final FlagSnippetForm adminFlagForm
    ) {
        final ModelAndView mav = new ModelAndView("redirect:/snippet/" + id);
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null || !roleService.isAdmin(currentUser.getId())) {
            throw new ForbiddenAccessException(messageSource.getMessage("error.403.snippet.flag", null, LocaleContextHolder.getLocale()));
        } else {
            // Getting the snippet
            Optional<Snippet> snippetOpt = this.snippetService.findSnippetById(id);
            if (!snippetOpt.isPresent()){
                this.logAndThrow(id);
            }
            // Getting the complete owner
            Optional<User> completeOwnerOpt = this.userService.findUserById(snippetOpt.get().getOwner().getId());
            if (!completeOwnerOpt.isPresent()){
                throw new ForbiddenAccessException(messageSource.getMessage("error.403.snippet.delete", null, LocaleContextHolder.getLocale()));
            }
            // Updating the flagged status
            this.snippetService.updateFlagged(id, completeOwnerOpt.get().getId(), adminFlagForm.isFlagged());
            LOGGER.debug("Marked snippet {} as flagged by admin", id);
            // Getting the url of the server
            final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            // Sending flagged email
            this.emailService.sendFlaggedEmail(baseUrl + "/snippet/" + id, snippetOpt.get().getTitle(), completeOwnerOpt.get().getEmail(), completeOwnerOpt.get().getUsername(), adminFlagForm.isFlagged(), completeOwnerOpt.get().getLocale());
        }
        return mav;
    }

    private void logAndThrow(final long snippetId) {
        LOGGER.warn("No snippet found for id {}", snippetId);
        throw new SnippetNotFoundException(messageSource.getMessage("error.404.snippet", new Object[]{snippetId}, LocaleContextHolder.getLocale()));
    }
}

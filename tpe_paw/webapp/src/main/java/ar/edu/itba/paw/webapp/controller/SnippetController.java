package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.utility.Constants;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

@Controller
public class SnippetController {

    @Autowired private RoleService roleService;
    @Autowired private SnippetService snippetService;
    @Autowired private VoteService voteService;
    @Autowired private FavoriteService favService;
    @Autowired private LoginAuthentication loginAuthentication;
    @Autowired private TagService tagService;
    @Autowired private MessageSource messageSource;
    @Autowired private ReportService reportService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SnippetController.class);

    @RequestMapping("/snippet/{id}")
    public ModelAndView snippetDetail(
            @ModelAttribute("snippetId") @PathVariable("id") long id,
            @ModelAttribute("searchForm") final SearchForm searchForm,
            @ModelAttribute("adminFlagForm") final FlagSnippetForm adminFlagForm,
            @ModelAttribute("deleteForm") final DeleteForm deleteForm,
            @ModelAttribute("favForm") final FavoriteForm favForm,
            @ModelAttribute("positiveVoteForm") final VoteForm positiveVoteForm,
            @ModelAttribute("negativeVoteForm") final VoteForm negativeVoteForm,
            @ModelAttribute("dismissReportForm") final DeleteForm dismissReportForm,
            @ModelAttribute("reportForm") final ReportForm reportForm,
            final BindingResult errors
    ) {
        final ModelAndView mav = new ModelAndView("snippet/snippetDetail");
        boolean showFavorite = true;

        Snippet snippet = this.getSnippet(id);

        User currentUser = this.loginAuthentication.getLoggedInUser();
        mav.addObject("currentUser", currentUser);
        if (currentUser != null){
            Collection<Tag> allFollowedTags = this.tagService.getFollowedTagsForUser(currentUser.getId());
            Collection<Tag> userTags = this.tagService.getMostPopularFollowedTagsForUser(currentUser.getId(), Constants.MENU_FOLLOWING_TAG_AMOUNT);
            mav.addObject("userTags", userTags);
            mav.addObject("userTagsCount", userTags.isEmpty() ? 0 : allFollowedTags.size() - userTags.size());
            mav.addObject("userRoles", this.roleService.getUserRoles(currentUser.getId()));

            // Vote
            Optional<Vote> vote = this.voteService.getVote(currentUser.getId(), snippet.getId());
            negativeVoteForm.setVoteSelected(vote.isPresent() && !vote.get().isPositive());
            positiveVoteForm.setVoteSelected(vote.isPresent() && vote.get().isPositive());

            // Fav
            showFavorite = currentUser.getFavorites().contains(snippet);
            favForm.setFavorite(showFavorite);

            //Delete
            deleteForm.setDelete(snippet.isDeleted());

            // Report
            reportForm.setReported(this.reportService.getReport(currentUser.getId(), snippet.getId()).isPresent());
            mav.addObject("displayReportDialog", errors.hasErrors());
            mav.addObject("canReport", this.reportService.canReport(currentUser));

            if (roleService.isAdmin(currentUser.getId())) {
                adminFlagForm.setFlagged(snippet.isFlagged());
            }
        } else {
            mav.addObject("userRoles", Collections.emptyList());
        }

        mav.addObject("snippet", snippet);
        mav.addObject("showReportedWarning", this.reportService.showReportedWarning(snippet, currentUser));
        mav.addObject("showFavorite", showFavorite || !snippet.isDeleted());
        mav.addObject("voteCount", this.voteService.getVoteBalance(snippet.getId()));
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
            LOGGER.error(messageSource.getMessage("error.403.snippet.delete", null, Locale.ENGLISH));
            throw new ForbiddenAccessException(messageSource.getMessage("error.403.snippet.delete", null, LocaleContextHolder.getLocale()));
        } else {
            if (!this.snippetService.deleteOrRestoreSnippet(snippet.get(), currentUser.getId(), deleteForm.isDelete())) {
                /* Operation was unsuccessful */
                LOGGER.error(messageSource.getMessage("error.409.deletion.snippet", null, Locale.ENGLISH));
                throw new ElementDeletionException(messageSource.getMessage("error.409.deletion.snippet", null, LocaleContextHolder.getLocale()));
            }
        }
        return new ModelAndView("redirect:/snippet/" + id);
    }

    @RequestMapping(value="/snippet/{id}/vote/positive", method=RequestMethod.POST)
    public ModelAndView votePositive(
            @ModelAttribute("snippetId") @PathVariable("id") long id,
            @ModelAttribute("positiveVoteForm") final VoteForm positiveVoteForm
    ) {
        final ModelAndView mav = new ModelAndView("redirect:/snippet/" + id);
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null) {
            LOGGER.error(messageSource.getMessage("error.403.snippet.vote", null, Locale.ENGLISH));
            throw new ForbiddenAccessException(messageSource.getMessage("error.403.snippet.vote", null, LocaleContextHolder.getLocale()));
        } else {
            Snippet snippet = this.getSnippet(id);
            this.voteService.performVote(snippet.getOwner().getId(), currentUser.getId(), id, positiveVoteForm.isVoteSelected(), true);
        }
        return mav;
    }

    @RequestMapping(value="/snippet/{id}/vote/negative", method=RequestMethod.POST)
    public ModelAndView voteNegative(
            @ModelAttribute("snippetId") @PathVariable("id") long id,
            @ModelAttribute("negativeVoteForm") final VoteForm negativeVoteForm
    ) {
        final ModelAndView mav = new ModelAndView("redirect:/snippet/" + id);
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null) {
            LOGGER.error(messageSource.getMessage("error.403.snippet.vote", null, Locale.ENGLISH));
            throw new ForbiddenAccessException(messageSource.getMessage("error.403.snippet.vote", null, LocaleContextHolder.getLocale()));
        } else {
            Snippet snippet = this.getSnippet(id);
            this.voteService.performVote(snippet.getOwner().getId(), currentUser.getId(), id, negativeVoteForm.isVoteSelected(), false);
        }
        return mav;
    }

    @RequestMapping(value="/snippet/{id}/fav", method=RequestMethod.POST)
    public ModelAndView favSnippet(
            @ModelAttribute("snippetId") @PathVariable("id") long id,
            @ModelAttribute("favForm") final FavoriteForm favForm,
            HttpServletRequest request
    ) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null) {
            LOGGER.error(messageSource.getMessage("error.403.snippet.fav", null, Locale.ENGLISH));
            throw new ForbiddenAccessException(messageSource.getMessage("error.403.snippet.fav", null, LocaleContextHolder.getLocale()));
        } else {
            this.favService.updateFavorites(currentUser.getId(), id, favForm.getFavorite());
            LOGGER.debug("User {} updated favorite on snippet {}", currentUser.getUsername(), id);
        }
        String referer = request.getHeader(Constants.REFERER);
        String redirect = referer != null ? referer : ("/snippet/" + id);
        return new ModelAndView("redirect:" + redirect);
    }

    @RequestMapping(value="/snippet/{id}/report", method={RequestMethod.POST})
    public ModelAndView reportSnippet(
            @ModelAttribute("snippetId") @PathVariable("id") long id,
            @ModelAttribute("searchForm") final SearchForm searchForm,
            @ModelAttribute("adminFlagForm") final FlagSnippetForm adminFlagForm,
            @ModelAttribute("deleteForm") final DeleteForm deleteForm,
            @ModelAttribute("favForm") final FavoriteForm favForm,
            @ModelAttribute("positiveVoteForm") final VoteForm positiveVoteForm,
            @ModelAttribute("negativeVoteForm") final VoteForm negativeVoteForm,
            @ModelAttribute("dismissReportForm") final DeleteForm dismissReportForm,
            @Valid @ModelAttribute("reportForm") final ReportForm reportForm,
            final BindingResult errors
    ) {
        if (errors.hasErrors()) {
            return snippetDetail(id, searchForm, adminFlagForm, deleteForm, favForm, positiveVoteForm, negativeVoteForm, dismissReportForm, reportForm, errors);
        }
        // Getting the url of the server
        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        Snippet snippet = this.getSnippet(id);
        User currentUser = this.loginAuthentication.getLoggedInUser();

        if (currentUser == null || currentUser.equals(snippet.getOwner())) {
            LOGGER.error(messageSource.getMessage("error.403.snippet.report.owner", null, Locale.ENGLISH));
            throw new ForbiddenAccessException(messageSource.getMessage("error.403.snippet.report.owner", null, LocaleContextHolder.getLocale()));
        } else if (!this.reportService.canReport(currentUser)) {
            LOGGER.error(messageSource.getMessage("error.403.snippet.report.reputation", null, Locale.ENGLISH));
            throw new ForbiddenAccessException(messageSource.getMessage("error.403.snippet.report.reputation", null, LocaleContextHolder.getLocale()));
        }

        try {
            reportService.reportSnippet(currentUser, snippet, reportForm.getReportDetail(), baseUrl);
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + "Failed report snippet: user {} about their snippet {}", snippet.getOwner().getUsername(), snippet.getId());
        }
        LOGGER.debug("User {} reported snippet {} with message {}", currentUser.getUsername(), id, reportForm.getReportDetail());

        return new ModelAndView("redirect:/snippet/" + id);
    }

    @RequestMapping(value="/snippet/{id}/report/dismiss", method={RequestMethod.POST})
    public ModelAndView reportSnippet(
            @ModelAttribute("snippetId") @PathVariable("id") long id,
            @ModelAttribute("dismissReportForm") final DeleteForm dismissReportForm
    ) {
        User currentUser = loginAuthentication.getLoggedInUser();
        Snippet snippet = this.getSnippet(id);

        if (currentUser == null || !currentUser.equals(snippet.getOwner())) {
            LOGGER.error(messageSource.getMessage("error.403.snippet.report.dismiss", null, Locale.ENGLISH));
            throw new ForbiddenAccessException(messageSource.getMessage("error.403.snippet.report.dismiss", null, LocaleContextHolder.getLocale()));
        }
        this.reportService.dismissReportsForSnippet(id);
        return new ModelAndView("redirect:/snippet/" + id);
    }

    @RequestMapping(value="/snippet/{id}/flag", method=RequestMethod.POST)
    public ModelAndView flagSnippet(
            @ModelAttribute("snippetId") @PathVariable("id") long id,
            @ModelAttribute("adminFlagForm") final FlagSnippetForm adminFlagForm
    ) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null || !roleService.isAdmin(currentUser.getId())) {
            LOGGER.error(messageSource.getMessage("error.403.snippet.flag", null, Locale.ENGLISH));
            throw new ForbiddenAccessException(messageSource.getMessage("error.403.snippet.flag", null, LocaleContextHolder.getLocale()));
        } else {
            Snippet snippet = this.getSnippet(id);

            // Getting the url of the server
            final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            try {
                // Updating the flagged variable of snippet
                this.snippetService.updateFlagged(snippet, snippet.getOwner(), adminFlagForm.isFlagged(), baseUrl);
            } catch (Exception e) {
                LOGGER.error(e.getMessage() + "Failed to flag snippet {}", snippet.getId());
            }
            LOGGER.debug("Marked snippet {} as flagged by admin", id);

        }
        return new ModelAndView("redirect:/snippet/" + id);
    }

    private Snippet getSnippet(final long snippetId) {
        Optional<Snippet> retrievedSnippet = this.snippetService.findSnippetById(snippetId);
        if (!retrievedSnippet.isPresent()) {
            logAndThrow(snippetId);
        }
        return retrievedSnippet.get();
    }

    private void logAndThrow(final long snippetId) {
        LOGGER.error("No snippet found for id {}", snippetId);
        throw new SnippetNotFoundException(messageSource.getMessage("error.404.snippet", new Object[]{snippetId}, LocaleContextHolder.getLocale()));
    }
}

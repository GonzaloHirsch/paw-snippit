package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.RoleService;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.utility.Constants;
import ar.edu.itba.paw.webapp.exception.ForbiddenAccessException;
import ar.edu.itba.paw.webapp.exception.TagNotFoundException;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.webapp.utility.MavHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static ar.edu.itba.paw.webapp.utility.Constants.SNIPPET_PAGE_SIZE;
import static ar.edu.itba.paw.webapp.utility.Constants.TAG_PAGE_SIZE;

@Controller
public class TagsController {

    @Autowired private TagService tagService;
    @Autowired private SnippetService snippetService;
    @Autowired private LoginAuthentication loginAuthentication;
    @Autowired private RoleService roleService;
    @Autowired private UserService userService;
    @Autowired private MessageSource messageSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(TagsController.class);

    @RequestMapping("/tags")
    public ModelAndView showAllTags(@ModelAttribute("itemSearchForm") final ItemSearchForm searchForm, final @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        ModelAndView mav = new ModelAndView("tagAndLanguages/tags");
        User currentUser = loginAuthentication.getLoggedInUser();
        Collection<Tag> allTags = tagService.getAllTags(searchForm.isShowEmpty(), searchForm.isShowOnlyFollowing(), currentUser != null ? currentUser.getId() : null, page, TAG_PAGE_SIZE);
        int tagCount = this.tagService.getAllTagsCount(searchForm.isShowEmpty(), searchForm.isShowOnlyFollowing(), currentUser != null ? currentUser.getId() : null);
        return this.setTagsWithPage(mav, allTags, currentUser, tagCount, page, false);
    }

    @RequestMapping("/tags/search")
    public ModelAndView searchInAllTags(@ModelAttribute("itemSearchForm") final ItemSearchForm searchForm, final @RequestParam(value = "page", required = false, defaultValue = "1") int page){
        final ModelAndView mav = new ModelAndView("tagAndLanguages/tags");
        User currentUser = loginAuthentication.getLoggedInUser();
        Collection<Tag> allTags = this.tagService.findTagsByName(searchForm.getName(), searchForm.isShowEmpty(), searchForm.isShowOnlyFollowing(), currentUser != null ? currentUser.getId() : null, page, TAG_PAGE_SIZE);
        int tagCount = this.tagService.getAllTagsCountByName(searchForm.getName(), searchForm.isShowEmpty(), searchForm.isShowOnlyFollowing(), currentUser != null ? currentUser.getId() : null);
        return this.setTagsWithPage(mav, allTags, currentUser, tagCount, page, true);
    }

    private ModelAndView setTagsWithPage(ModelAndView mav, Collection<Tag> allTags, User currentUser, int tagCount, int page, boolean searching) {
        mav.addObject("pages", (tagCount/TAG_PAGE_SIZE) + (tagCount % TAG_PAGE_SIZE == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("searchContext","tags/");
        mav.addObject("tags", allTags);
        mav.addObject("itemSearchContext", "tags/");
        mav.addObject("loggedUser", currentUser);
        mav.addObject("searching", searching);
        mav.addObject("totalTagsCount", tagCount);

        for (Tag tag : allTags) {
            if (currentUser != null) {
                /* Follow form quick action */
                FollowForm followForm = new FollowForm();
                followForm.setFollows(currentUser.getFollowedTags().contains(tag));
                mav.addObject("followIconForm" + tag.getId().toString(), followForm);
                /* Tag snippet amount count */
            }
            this.snippetService.analizeSnippetsUsing(tag);
        }
        this.addAttributes(mav);
        return mav;
    }

    @RequestMapping("/tags/{tagId}")
    public ModelAndView showSnippetsForTag(@PathVariable("tagId") long tagId,
                                           @ModelAttribute("followForm") final FollowForm followForm,
                                           @ModelAttribute("deleteForm") final DeleteForm deleteForm,
                                           final @RequestParam(value = "page", required = false, defaultValue = "1") int page){

        ModelAndView mav = new ModelAndView("tagAndLanguages/tagSnippets");

        /* Retrieve the tag */
        Optional<Tag> tag = this.tagService.findTagById(tagId);
        if (!tag.isPresent()) {
            LOGGER.warn("No tag found with id {}", tagId);
            throw new TagNotFoundException(messageSource.getMessage("error.404.tag", new Object[]{tagId}, LocaleContextHolder.getLocale()));
        }
        /* If user is logged in, check if they follow the tag */
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser != null){
            followForm.setFollows(this.tagService.userFollowsTag(currentUser.getId(), tagId));
        }

        Collection<Snippet> snippets = snippetService.findSnippetsForTag(tagId, page, SNIPPET_PAGE_SIZE);
        int totalSnippetCount = this.snippetService.getAllSnippetsByTagCount(tag.get().getId());
        mav.addObject("pages", totalSnippetCount/SNIPPET_PAGE_SIZE + (totalSnippetCount % SNIPPET_PAGE_SIZE == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("tag", tag.get());
        mav.addObject("searching", false);
        mav.addObject("totalSnippetCount", totalSnippetCount);
        mav.addObject("searchContext","tags/"+tagId+"/");
        mav.addObject("snippetList", snippets);
        this.addAttributes(mav);
        MavHelper.addSnippetCardFavFormAttributes(mav, currentUser, snippets);
        return mav;
    }

    @RequestMapping(value="/tags/{tagId}/follow", method= RequestMethod.POST)
    public ModelAndView followTag(
            HttpServletRequest request,
            @ModelAttribute("tagId") @PathVariable("tagId") long tagId,
            @ModelAttribute("followForm") final FollowForm followForm
    ) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser != null) {
            this.tagService.updateFollowing(currentUser.getId(), tagId, followForm.isFollows());
        } else {
            LOGGER.warn("Inside the follow form of tag {} without a logged in user", tagId);
            throw new ForbiddenAccessException(messageSource.getMessage("error.403.follow", null, LocaleContextHolder.getLocale()));
        }
        String referer = request.getHeader(Constants.REFERER);
        String redirect = referer != null ? referer : ("/tags/" + tagId);
        return new ModelAndView("redirect:" + redirect);
    }
    
    @RequestMapping(value = "/tags/{tagId}/delete",  method= RequestMethod.POST)
    public ModelAndView deleteTag(@PathVariable("tagId") long tagId, @ModelAttribute("deleteForm") final DeleteForm deleteForm) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if ( currentUser != null && roleService.isAdmin(currentUser.getId())){
            this.tagService.removeTag(tagId);
            LOGGER.debug("Admin deleted tag with id {}", tagId);
        } else {
            LOGGER.warn("No user logged in or logged in user not admin but attempting to delete tag {}", tagId);
            throw new ForbiddenAccessException(messageSource.getMessage("error.403.admin", null, LocaleContextHolder.getLocale()));
        }
        return new ModelAndView("redirect:/tags");
    }

    /* Are not in the @ModelAttribute method because I do not want the values in the URL */
    private void addAttributes(ModelAndView mav) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        Collection<Tag> userTags =  Collections.emptyList();
        Collection<Tag> allFollowedTags = Collections.emptyList();
        Collection<String> userRoles = Collections.emptyList();

        if (currentUser != null) {
            userTags = this.tagService.getMostPopularFollowedTagsForUser(currentUser.getId(), Constants.MENU_FOLLOWING_TAG_AMOUNT);
            allFollowedTags = this.tagService.getFollowedTagsForUser(currentUser.getId());
            this.userService.updateLocale(currentUser.getId(), LocaleContextHolder.getLocale());
            userRoles = this.roleService.getUserRoles(currentUser.getId());
        }
        mav.addObject("currentUser", currentUser);
        mav.addObject("userTags", userTags);
        mav.addObject("userTagsCount", userTags.isEmpty() ? 0 : allFollowedTags.size() - userTags.size());
        mav.addObject("userRoles", userRoles);
    }

    @ModelAttribute
    public void addAttributes(Model model, @Valid final SearchForm searchForm) {
        model.addAttribute("searchForm", searchForm);
    }
}
package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class TagsController {

    @Autowired
    private TagService tagService;
    @Autowired
    private SnippetService snippetService;
    @Autowired
    private LoginAuthentication loginAuthentication;

    private static final Logger LOGGER = LoggerFactory.getLogger(TagsController.class);

    @RequestMapping("/tags")
    public ModelAndView showAllTags() {
        ModelAndView mav = new ModelAndView("tag/tags");

        Collection<Tag> allTags = tagService.getAllTags();
        mav.addObject("searchContext","tags/");
        mav.addObject("tags", allTags); 
        return mav;
    }

    @RequestMapping("/tags/{tagId}")
    public ModelAndView showSnippetsForTag(@PathVariable("tagId") long tagId, final @RequestParam(value = "page", required = false, defaultValue = "1") int page){
        ModelAndView mav = new ModelAndView("tag/tagSnippets");

        /* Retrieve the tag */
        Optional<Tag> tag = tagService.findTagById(tagId);
        if (!tag.isPresent()) {
            LOGGER.warn("No tag found with id {}", tagId);
            //TODO throw new
        }
        /* If user is logged in, check if they follow the tag */
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser != null){
            Collection<Tag> followedTags = tagService.getFollowedTagsForUser(currentUser.getId());
            mav.addObject("follows", followedTags.stream().map(Tag::getId).collect(Collectors.toList()).contains(tagId));
        }

        int totalSnippetCount = this.snippetService.getAllSnippetsByTagCount(tag.get().getId());
        int pageSize = this.snippetService.getPageSize();
        mav.addObject("pages", totalSnippetCount/pageSize + (totalSnippetCount % pageSize == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("tag", tag.get());
        mav.addObject("snippets", snippetService.findSnippetsForTag(tagId));
        return mav;
    }

    @RequestMapping("/tags/{tagId}/follow")
    public ModelAndView followSnippet(@PathVariable("tagId") long tagId) {
        User currentUser = loginAuthentication.getLoggedInUser();
        if ( currentUser != null){
            tagService.followTag(currentUser.getId(), tagId);
            LOGGER.debug("User {} followed tag with id {}", currentUser.getUsername(), tagId);
        } else {
            LOGGER.warn("No user logged in but tag {} was followed", tagId);
        }
        return new ModelAndView("redirect:/tags/" + tagId);
    }

    @RequestMapping("/tags/{tagId}/unfollow")
    public ModelAndView unfollowSnippet(@PathVariable("tagId") long tagId) {
        User currentUser = loginAuthentication.getLoggedInUser();
        if ( currentUser != null){
            tagService.unfollowTag(currentUser.getId(), tagId);
            LOGGER.debug("User {} unfollowed tag with id {}", currentUser.getUsername(), tagId);
        } else {
            LOGGER.warn("No user logged in but tag {} was unfollowed", tagId);
        }
        return new ModelAndView("redirect:/tags/" + tagId);
    }

    @ModelAttribute
    public void addAttributes(Model model, @Valid final SearchForm searchForm) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        Collection<Tag> userTags = currentUser != null ? this.tagService.getFollowedTagsForUser(currentUser.getId()) : new ArrayList<>();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userTags", userTags);
        model.addAttribute("searchForm", searchForm);
    }
}
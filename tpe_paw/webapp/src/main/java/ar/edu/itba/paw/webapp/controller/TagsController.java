package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
    private UserService userService;
    @Autowired
    private LoginAuthentication loginAuthentication;

    @RequestMapping("/tags")
    public ModelAndView showAllTags(@ModelAttribute final SearchForm searchForm) {
        ModelAndView mav = new ModelAndView("tag/tags");
        User currentUser = this.loginAuthentication.getLoggedInUser();
        mav.addObject("currentUser", currentUser);
        if (currentUser != null){
            mav.addObject("userTags", this.tagService.getFollowedTagsForUser(currentUser.getId()));
        } else {
            // ERROR
        }
        Collection<Tag> allTags = tagService.getAllTags();
        mav.addObject("searchContext","tags/");
        mav.addObject("tags", allTags); 
        return mav;
    }

    @RequestMapping("/tags/{tagId}")
    public ModelAndView showSnippetsForTag(@PathVariable("tagId") long tagId, @ModelAttribute final SearchForm searchForm, final @RequestParam(value = "page", required = false, defaultValue = "1") int page){
        ModelAndView mav = new ModelAndView("tag/tagSnippets");
        User currentUser = this.loginAuthentication.getLoggedInUser();
        mav.addObject("currentUser", currentUser);
        boolean follows = false;
        if (currentUser != null){
            mav.addObject("userTags", this.tagService.getFollowedTagsForUser(currentUser.getId()));
            Collection<Tag> followedTags = tagService.getFollowedTagsForUser(currentUser.getId());
            follows = followedTags.stream().map(Tag::getId).collect(Collectors.toList()).contains(tagId);
        } else {
            // ERROR
        }
        Optional<Tag> tag = tagService.findTagById(tagId);
        if (!tag.isPresent()) {
            // TODO customize error msg
        }
        int totalSnippetCount = this.snippetService.getAllSnippetsByTagCount(tag.get().getId());
        int pageSize = this.snippetService.getPageSize();
        mav.addObject("pages", totalSnippetCount/pageSize + (totalSnippetCount % pageSize == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("follows", follows);
        mav.addObject("tag", tag.get());
        mav.addObject("snippets", snippetService.findSnippetsForTag(tagId));
        return mav;
    }

    @RequestMapping("/tags/{tagId}/follow")
    public ModelAndView followSnippet(@PathVariable("tagId") long tagId) {
        User currentUserOpt = loginAuthentication.getLoggedInUser();
        if ( currentUserOpt != null){
            // TODO LOGGER + customize error msg
        } else {
            tagService.followTag(currentUserOpt.getId(), tagId);
        }
        return new ModelAndView("redirect:/tags/" + tagId);
    }

    @RequestMapping("/tags/{tagId}/unfollow")
    public ModelAndView unfollowSnippet(@PathVariable("tagId") long tagId) {
        User currentUserOpt = loginAuthentication.getLoggedInUser();
        if ( currentUserOpt != null){
            // TODO LOGGER + customize error msg
        }else {
            tagService.unfollowTag(currentUserOpt.getId(), tagId);
        }
        return new ModelAndView("redirect:/tags/" + tagId);
    }
}
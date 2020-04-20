package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("/tags")
    public ModelAndView showAllTags(@ModelAttribute final SearchForm searchForm) {
        ModelAndView mav = new ModelAndView("tag/tags");
        Collection<Tag> allTags = tagService.getAllTags();
        mav.addObject("tags", allTags); 
        return mav;
    }
    @RequestMapping("/tags/{tagId}")
    public ModelAndView showSnippetsForTag(@PathVariable("tagId") long tagId, @ModelAttribute final SearchForm searchForm){
        ModelAndView mav = new ModelAndView("tag/tagSnippets");
        Optional<User> currentUserOpt = userService.getCurrentUser();
        if (!currentUserOpt.isPresent()){
            // TODO customize error msg
        }
        Collection<Tag> followedTags = tagService.getFollowedTagsForUser(currentUserOpt.get().getUserId());
        boolean follows = followedTags.stream().map(Tag::getId).collect(Collectors.toList()).contains(tagId);
        Optional<Tag> tag = tagService.findTagById(tagId);
        if (!tag.isPresent()) {
            // TODO customize error msg
        }
        mav.addObject("follows", follows);
        mav.addObject("tag", tag.get());
        mav.addObject("snippets", snippetService.findSnippetsForTag(tagId));
        return mav;
    }
    @RequestMapping("/tags/follow/{tagId}")
    public ModelAndView followSnippet(@PathVariable("tagId") long tagId) {
        Optional<User> currentUserOpt = userService.getCurrentUser();
        if (!currentUserOpt.isPresent()){
            // TODO customize error msg
        } else {
            tagService.followTag(currentUserOpt.get().getUserId(), tagId);
        }
        return new ModelAndView("redirect:/tags/" + tagId);
    }
    @RequestMapping("/tags/unfollow/{tagId}")
    public ModelAndView unfollowSnippet(@PathVariable("tagId") long tagId) {
        Optional<User> currentUserOpt = userService.getCurrentUser();
        if (!currentUserOpt.isPresent()){
            // TODO customize error msg
        } else {
            tagService.unfollowTag(currentUserOpt.get().getUserId(), tagId);
        }
        return new ModelAndView("redirect:/tags/" + tagId);
    }
}
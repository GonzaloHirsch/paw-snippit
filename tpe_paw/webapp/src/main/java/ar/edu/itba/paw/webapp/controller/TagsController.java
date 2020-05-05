package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.form.DeleteForm;
import ar.edu.itba.paw.webapp.form.FollowForm;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TagsController.class);

    @RequestMapping("/tags")
    public ModelAndView showAllTags() {
        ModelAndView mav = new ModelAndView("tagAndLanguages/tags");

        Collection<Tag> allTags = tagService.getAllTags();
        mav.addObject("searchContext","tags/");
        mav.addObject("tags", allTags); 
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
            //TODO throw new
        }
        /* If user is logged in, check if they follow the tag */
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser != null){
            followForm.setFollows(this.tagService.userFollowsTag(currentUser.getId(), tagId));
        }

        int totalSnippetCount = this.snippetService.getAllSnippetsByTagCount(tag.get().getId());
        int pageSize = this.snippetService.getPageSize();
        mav.addObject("pages", totalSnippetCount/pageSize + (totalSnippetCount % pageSize == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("tag", tag.get());
        mav.addObject("snippets", snippetService.findSnippetsForTag(tagId));
        return mav;
    }

    @RequestMapping(value="/tags/{tagId}/follow", method= RequestMethod.POST)
    public ModelAndView favSnippet(
            @ModelAttribute("tagId") @PathVariable("tagId") long tagId,
            @ModelAttribute("followForm") final FollowForm followForm
    ) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null) {
            LOGGER.warn("Inside the follow form of tag {} without a logged in user", tagId);
        } else {
            this.tagService.updateFollowing(currentUser.getId(), tagId, followForm.isFollows());
        }
        return new ModelAndView("redirect:/tags/" + tagId);
    }


    @RequestMapping(value = "/tags/{tagId}/delete",  method= RequestMethod.POST)
    public ModelAndView deleteTag(@PathVariable("tagId") long tagId, @ModelAttribute("deleteForm") final DeleteForm deleteForm) {
        User currentUser = loginAuthentication.getLoggedInUser();
        if ( currentUser != null && userService.isAdmin(currentUser)){
            this.tagService.removeTag(tagId);
            LOGGER.debug("Admin deleted tag with id {}", tagId);
        } else {
            LOGGER.warn("No user logged in or logged in user not admin but tag {} was deleted", tagId);
        }
        return new ModelAndView("redirect:/tags");
    }

    @ModelAttribute
    public void addAttributes(Model model, @Valid final SearchForm searchForm) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        Collection<Tag> userTags = currentUser != null ? this.tagService.getFollowedTagsForUser(currentUser.getId()) : new ArrayList<>();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userTags", userTags);
        model.addAttribute("searchForm", searchForm);
    }

//    @ModelAttribute
//    public FollowForm followForm(final FollowForm followForm) {
//        return followForm;
//    }
}
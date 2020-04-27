package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;

@Controller
public class SnippetFeedController {

    @Autowired
    private SnippetService snippetService;
    @Autowired
    private UserService userService;
    @Autowired
    private LoginAuthentication loginAuthentication;
    @Autowired
    private TagService tagService;

    @RequestMapping("/")
    public ModelAndView getHomeSnippetFeed(/*@ModelAttribute("pageForm") final PageForm pageForm*/) {
        final ModelAndView mav = new ModelAndView("index");
        User currentUser = this.loginAuthentication.getLoggedInUser();
        mav.addObject("currentUser", currentUser);
        if (currentUser != null){
            mav.addObject("tagList", this.tagService.getFollowedTagsForUser(currentUser.getId()));
        }
//        Collection<Snippet> snippets = this.snippetService.getAllSnippets(pageForm.getPage(), 5);
        Collection<Snippet> snippets = this.snippetService.getAllSnippets(1, 30);
        int totalSnippetCount = this.snippetService.getAllSnippetsCount();
//        pageForm.setTotalPages(totalSnippetCount/ 5 + (totalSnippetCount % 5 == 0 ? 0 : 1));
        mav.addObject("snippetList", snippets);
        mav.addObject("searchContext","");
        return mav;
    }

    @RequestMapping("/favorites")
    public ModelAndView getFavoritesSnippetFeed() {
        final ModelAndView mav = new ModelAndView("index");
        User currentUser = this.loginAuthentication.getLoggedInUser();
        mav.addObject("currentUser", currentUser);
        if (currentUser != null){
            mav.addObject("tagList", this.tagService.getFollowedTagsForUser(currentUser.getId()));
        } else {
            // ERROR
        }
        mav.addObject("snippetList", this.snippetService.getAllFavoriteSnippets(currentUser.getId()));
        mav.addObject("searchContext","favorites/");
        return mav;
    }

    @RequestMapping("/following")
    public ModelAndView getFollowingSnippetFeed(/*, @ModelAttribute("pageForm") final PageForm pageForm*/) {
        final ModelAndView mav = new ModelAndView("index");
        User currentUser = this.loginAuthentication.getLoggedInUser();
        mav.addObject("currentUser", currentUser);
        if (currentUser != null){
            mav.addObject("tagList", this.tagService.getFollowedTagsForUser(currentUser.getId()));
        } else {
            // ERROR
        }
        mav.addObject("snippetList", this.snippetService.getAllFollowingSnippets(currentUser.getId()));
        mav.addObject("searchContext","following/");
        return mav;
    }

    @ModelAttribute
    public void addAttributes(ModelAndView model, @Valid final SearchForm searchForm) {
        model.addObject("searchForm", searchForm);
    }
}

package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.PageForm;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.Optional;

@Controller
public class SnippetFeedController {

    @Autowired
    private SnippetService snippetService;

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public ModelAndView getHomeSnippetFeed(@ModelAttribute("searchForm") final SearchForm searchForm/*, @ModelAttribute("pageForm") final PageForm pageForm*/) {
        final ModelAndView mav = new ModelAndView("index");
//        Collection<Snippet> snippets = this.snippetService.getAllSnippets(pageForm.getPage(), 5);
        Collection<Snippet> snippets = this.snippetService.getAllSnippets(1, 30);
        int totalSnippetCount = this.snippetService.getAllSnippetsCount();
//        pageForm.setTotalPages(totalSnippetCount/ 5 + (totalSnippetCount % 5 == 0 ? 0 : 1));
        mav.addObject("snippetList", snippets);
        mav.addObject("searchContext","");
        return mav;
    }

    @RequestMapping("/favorites")
    public ModelAndView getFavoritesSnippetFeed(@ModelAttribute("searchForm") final SearchForm searchForm) {
        final ModelAndView mav = new ModelAndView("index");
        Optional<User> user = this.userService.getCurrentUser();
        if (!user.isPresent()){
            // REddirect to error
        }
        mav.addObject("snippetList", this.snippetService.getAllFavoriteSnippets(user.get().getUserId()));
        mav.addObject("searchContext","favorites/");
        return mav;
    }

    @RequestMapping("/following")
    public ModelAndView getFollowingSnippetFeed(@ModelAttribute("searchForm") final SearchForm searchForm/*, @ModelAttribute("pageForm") final PageForm pageForm*/) {
        final ModelAndView mav = new ModelAndView("index");
        Optional<User> user = this.userService.getCurrentUser();
        if (!user.isPresent()){
            // REddirect to error
        }
        mav.addObject("snippetList", this.snippetService.getAllFollowingSnippets(user.get().getUserId()));
        mav.addObject("searchContext","following/");
        return mav;
    }
}

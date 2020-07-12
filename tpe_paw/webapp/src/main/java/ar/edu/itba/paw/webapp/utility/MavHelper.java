package ar.edu.itba.paw.webapp.utility;

import ar.edu.itba.paw.interfaces.service.RoleService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.FavoriteForm;
import ar.edu.itba.paw.webapp.form.FollowForm;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.Collections;

public final class MavHelper {

    public static void addSnippetCardFavFormAttributes(ModelAndView mav, User currentUser, Collection<Snippet> snippets) {
        for (Snippet snippet : snippets) {
            if (currentUser != null) {
                /* Fav form quick action */
                FavoriteForm favForm = new FavoriteForm();
                favForm.setFavorite(currentUser.getFavorites().contains(snippet));
                mav.addObject("favoriteForm" + snippet.getId().toString(), favForm);
            }
        }
    }

    public static void addTagChipUnfollowFormAttributes(ModelAndView mav, Collection<Tag> followingTags, int followingAmount) {
        for (Tag tag : followingTags) {
            FollowForm followForm = new FollowForm();
            followForm.setFollows(true);
            mav.addObject("unfollowForm" + tag.getId().toString(), followForm);
        }
        mav.addObject("followingTags", followingTags);
        mav.addObject("maxChipAmount", Constants.FOLLOWING_FEED_TAG_AMOUNT);
        mav.addObject("followingAmount", followingAmount);
    }

    /*
     * Cannot be in @ControllerAdvice since it will add the @ModelAttribute to all controllers and we dont want this.
     * How we retrieve the currentUser depends on the controller
     */
    public static void addCurrentUserAttributes(Model model, User currentUser, TagService tagService, RoleService roleService) {
        Collection<Tag> userTags =  Collections.emptyList();
        Collection<String> userRoles = Collections.emptyList();
        Collection<Tag> allFollowedTags = Collections.emptyList();

        if (currentUser != null) {
            userTags = tagService.getMostPopularFollowedTagsForUser(currentUser.getId(), Constants.MENU_FOLLOWING_TAG_AMOUNT);
            userRoles = roleService.getUserRoles(currentUser.getId());
            allFollowedTags = tagService.getFollowedTagsForUser(currentUser.getId());
        }
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("userTags", userTags);
        model.addAttribute("userTagsCount", userTags.isEmpty() ? 0 : allFollowedTags.size() - userTags.size());
        model.addAttribute("userRoles", userRoles);
    }

    /*
     * "The AssertionError isn’t strictly required, but it provides insurance in case the
     *  constructor is accidentally invoked from within the class" - Page 19, Effective Java
     */
    private MavHelper() {
        throw new AssertionError();
    }
}
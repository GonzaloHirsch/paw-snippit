package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.constants.Constants;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.DescriptionForm;
import ar.edu.itba.paw.webapp.form.ProfilePhotoForm;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static ar.edu.itba.paw.webapp.constants.Constants.SNIPPET_PAGE_SIZE;

@Controller
public class  UserController {
    
    @Autowired
    private UserService userService;
    @Autowired
    private SnippetService snippetService;
    @Autowired
    private LoginAuthentication loginAuthentication;
    @Autowired
    private TagService tagService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MessageSource messageSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private static final String OWNER_DELETED_SNIPPETS = "deleted";
    private static final String OWNER_ACTIVE_SNIPPETS = "active";

    @RequestMapping(value = "/user/{id}/active")
    public ModelAndView activeSnippetUserProfile(
            final @PathVariable("id") long id,
            @ModelAttribute("profilePhotoForm") final ProfilePhotoForm profilePhotoForm,
            @ModelAttribute("descriptionForm") final DescriptionForm descriptionForm,
            final @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            final @RequestParam(value = "editing", required = false, defaultValue = "false") boolean editing
    ) {

        Optional<User> user = this.userService.findUserById(id);
        if (!user.isPresent() || this.roleService.isAdmin(user.get().getId())) {
            this.logAndThrow(id);
        }
        Collection<Snippet> snippets = this.snippetService.getAllSnippetsByOwner(user.get().getId(), page, SNIPPET_PAGE_SIZE);
        int totalSnippetCount = this.snippetService.getAllSnippetsByOwnerCount(user.get().getId());
        return profileMav(id, user.get(), descriptionForm, OWNER_ACTIVE_SNIPPETS, snippets, totalSnippetCount, page, editing);
    }

    @RequestMapping(value = "/user/{id}/deleted")
    public ModelAndView deletedSnippetUserProfile(
            final @PathVariable("id") long id,
            @ModelAttribute("profilePhotoForm") final ProfilePhotoForm profilePhotoForm,
            @ModelAttribute("descriptionForm") final DescriptionForm descriptionForm,
            final @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            final @RequestParam(value = "editing", required = false, defaultValue = "false") boolean editing
    ) {

        Optional<User> user = this.userService.findUserById(id);
        if (!user.isPresent() || this.roleService.isAdmin(user.get().getId())) {
            this.logAndThrow(id);
        }

        Collection<Snippet> snippets = this.snippetService.getAllDeletedSnippetsByOwner(user.get().getId(), page, SNIPPET_PAGE_SIZE);
        int totalSnippetCount = this.snippetService.getAllDeletedSnippetsByOwnerCount(user.get().getId());
        return profileMav(id, user.get(), descriptionForm, OWNER_DELETED_SNIPPETS, snippets, totalSnippetCount, page, editing);
    }

    @RequestMapping(value = "/user/{id}")
    public ModelAndView userProfile(
            final @PathVariable("id") long id,
            @ModelAttribute("profilePhotoForm") final ProfilePhotoForm profilePhotoForm,
            @ModelAttribute("descriptionForm") final DescriptionForm descriptionForm,
            final @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            final @RequestParam(value = "editing", required = false, defaultValue = "false") boolean editing
    ) {
        return this.activeSnippetUserProfile(id, profilePhotoForm, descriptionForm, page, editing);
    }

    private ModelAndView profileMav(long id, User user, DescriptionForm descriptionForm, String tabContext, Collection<Snippet> snippets, final int totalSnippetCount, final int page, final boolean editing) {
        final ModelAndView mav = new ModelAndView("user/profile");

        /* Set the current user and its following tags */
        User currentUser = this.loginAuthentication.getLoggedInUser();
        Collection<Tag> userTags =  Collections.emptyList();
        Collection<String> userRoles = Collections.emptyList();
        Collection<Tag> allFollowedTags = Collections.emptyList();

        if (currentUser != null) {
            userTags = this.tagService.getMostPopularFollowedTagsForUser(currentUser.getId(), Constants.MENU_FOLLOWING_TAGS_AMOUNT);
            userRoles = this.roleService.getUserRoles(currentUser.getId());
            allFollowedTags = this.tagService.getFollowedTagsForUser(currentUser.getId());
        }
        mav.addObject("currentUser", currentUser);
        mav.addObject("userTags", userTags);
        mav.addObject("userTagsCount", userTags.isEmpty() ? 0 : allFollowedTags.size() - userTags.size());
        mav.addObject("userRoles", userRoles);

        descriptionForm.setDescription(user.getDescription());
        mav.addObject("followedTags", this.tagService.getFollowedTagsForUser(user.getId()));
        mav.addObject("pages", totalSnippetCount / SNIPPET_PAGE_SIZE + (totalSnippetCount % SNIPPET_PAGE_SIZE == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("editing", editing);
        mav.addObject("isEdit", false);
        mav.addObject("user", user);
        mav.addObject("snippets", snippets);
        mav.addObject("snippetsCount", totalSnippetCount);
        mav.addObject("searchContext", "user/"+id+"/");
        mav.addObject("tabContext", tabContext);
        return mav;
    }

    @RequestMapping(value = "/user/{id}/{context}", method = {RequestMethod.POST})
    public ModelAndView endEditPhoto(
            final @PathVariable("id") long id,
            final @PathVariable("context") String context,
            @ModelAttribute("profilePhotoForm") @Valid final ProfilePhotoForm profilePhotoForm,
            final BindingResult errors,
            @ModelAttribute("descriptionForm") final DescriptionForm descriptionForm
    ){
        if (errors.hasErrors()){
            return userProfile(id, profilePhotoForm, descriptionForm, 1, false);
        }

        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser != null && currentUser.getId() == id) {
            try {
                this.userService.changeProfilePhoto(id, profilePhotoForm.getFile().getBytes());
                LOGGER.debug("User {} changed their profile picture", id);
            } catch (IOException e) {
                LOGGER.error("Exception changing profile photo for user {}", id);
                FieldError photoError = new FieldError("profilePhotoForm","file" , messageSource.getMessage("profile.photo.error", null, LocaleContextHolder.getLocale()));
                errors.addError(photoError);
                return userProfile(id, profilePhotoForm, descriptionForm, 1, false);
            }
        }
        return new ModelAndView("redirect:/user/" + id + "/" + context);
    }

    @RequestMapping(value = "/user/{id}/image", produces = "image/jpeg")
    @ResponseBody
    public ResponseEntity<byte[]> getUserImage(final @PathVariable("id") long id) {
        Optional<User> user = this.userService.findUserById(id);
        if (!user.isPresent()) {
            this.logAndThrow(id);
        }
        CacheControl cacheControl = CacheControl.maxAge(60, TimeUnit.SECONDS)
                .noTransform()
                .mustRevalidate();
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .body(user.map(User::getIcon).orElse(null));
    }

    @RequestMapping(value = "/user/{id}/{context}/edit", method = {RequestMethod.POST})
    public ModelAndView endEditUserProfile(
            final @PathVariable("id") long id,
            final @PathVariable("context") String context,
            @Valid @ModelAttribute("descriptionForm") final DescriptionForm descriptionForm,
            final BindingResult errors,
            @ModelAttribute("profilePhotoForm") final ProfilePhotoForm profilePhotoForm,
            final @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            final @RequestParam(value = "editing", required = false, defaultValue = "false") boolean editing
    ) {
        if (errors.hasErrors()) {
            return userProfile(id, profilePhotoForm, descriptionForm, page, editing);
        }
        User currentUser = this.loginAuthentication.getLoggedInUser();
        Optional<User> user = this.userService.findUserById(id);
        if (!user.isPresent()){
            this.logAndThrow(id);
        }
        if (currentUser != null && currentUser.getId().equals(user.get().getId())) {
            this.userService.changeDescription(id, descriptionForm.getDescription());
        }
        return new ModelAndView("redirect:/user/" + id + "/" + context);
    }

    private void logAndThrow(long id) {
        LOGGER.warn("User with id {} doesn't exist", id);
        throw new UserNotFoundException(messageSource.getMessage("error.404.user", new Object[]{id}, LocaleContextHolder.getLocale()));
    }

    @ModelAttribute
    public void addAttributes(Model model, @Valid final SearchForm searchForm) {
        model.addAttribute("searchForm", searchForm);
    }

}

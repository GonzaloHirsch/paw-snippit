package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
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

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
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
    
    @RequestMapping(value = "/user/{id}")
    public ModelAndView userProfile(
            final @PathVariable("id") long id,
            @ModelAttribute("profilePhotoForm") final ProfilePhotoForm profilePhotoForm,
            @ModelAttribute("descriptionForm") final DescriptionForm descriptionForm,
            final @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            final @RequestParam(value = "editing", required = false, defaultValue = "false") boolean editing
           ) {
        final ModelAndView mav = new ModelAndView("user/profile");

        Optional<User> user = this.userService.findUserById(id);
        if (!user.isPresent() || this.roleService.isAdmin(user.get().getId())) {
            this.logAndThrow(id);
        }

        /* Set the current user and its following tags */
        User currentUser = this.loginAuthentication.getLoggedInUser();
        Collection<Tag> userTags = Collections.emptyList();
        Collection<String> userRoles = Collections.emptyList();

        if (currentUser != null) {
            userTags = this.tagService.getFollowedTagsForUser(currentUser.getId());
            userRoles = this.roleService.getUserRoles(currentUser.getId());
            this.userService.updateLocale(currentUser.getId(), LocaleContextHolder.getLocale());
        }
        mav.addObject("currentUser", currentUser);
        mav.addObject("userTags", userTags);
        mav.addObject("userRoles", userRoles);

        descriptionForm.setDescription(user.get().getDescription());
        Collection<Snippet> snippets = this.snippetService.findAllSnippetsByOwner(user.get().getId(), page, SNIPPET_PAGE_SIZE);
        int totalSnippetCount = this.snippetService.getAllSnippetsByOwnerCount(user.get().getId());
        mav.addObject("followedTags", this.tagService.getFollowedTagsForUser(user.get().getId()));
        mav.addObject("pages", totalSnippetCount / SNIPPET_PAGE_SIZE + (totalSnippetCount % SNIPPET_PAGE_SIZE == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("editing", editing);
        mav.addObject("isEdit", false);
        mav.addObject("user", user.get());
        mav.addObject("snippets", snippets);
        mav.addObject("snippetsCount", totalSnippetCount);
        mav.addObject("searchContext", "user/"+id+"/");
        return mav;
    }

    @RequestMapping(value = "/user/{id}", method = {RequestMethod.POST})
    public ModelAndView endEditPhoto(final @PathVariable("id") long id, @ModelAttribute("profilePhotoForm") @Valid final ProfilePhotoForm profilePhotoForm, final BindingResult errors, @ModelAttribute("descriptionForm") final DescriptionForm descriptionForm) {
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
        return new ModelAndView("redirect:/user/" + id);
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

    @RequestMapping(value = "/user/{id}/edit", method = {RequestMethod.POST})
    public ModelAndView endEditUserProfile(final @PathVariable("id") long id,
                                           @Valid @ModelAttribute("descriptionForm") final DescriptionForm descriptionForm,
                                           final BindingResult errors,
                                           @ModelAttribute("profilePhotoForm") final ProfilePhotoForm profilePhotoForm,
                                           final @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                           final @RequestParam(value = "editing", required = false, defaultValue = "false") boolean editing) {
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
        return new ModelAndView("redirect:/user/" + id);
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

package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.utility.Constants;
import ar.edu.itba.paw.webapp.exception.ForbiddenAccessException;
import ar.edu.itba.paw.webapp.exception.InvalidUrlException;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.DescriptionForm;
import ar.edu.itba.paw.webapp.form.ProfilePhotoForm;
import ar.edu.itba.paw.webapp.form.SearchForm;
import ar.edu.itba.paw.webapp.utility.MavHelper;
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
import java.util.*;
import java.util.concurrent.TimeUnit;

import static ar.edu.itba.paw.webapp.utility.Constants.SNIPPET_PAGE_SIZE;

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

    @RequestMapping(value = "/user/{id}/active", method = {RequestMethod.GET})
    public ModelAndView activeSnippetUserProfile(
            final @PathVariable("id") long id,
            @ModelAttribute("profilePhotoForm") final ProfilePhotoForm profilePhotoForm,
            @ModelAttribute("descriptionForm") final DescriptionForm descriptionForm,
            final @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            final @RequestParam(value = "editing", required = false, defaultValue = "false") boolean editing
    ) {
        User user = this.getUserWithId(id);
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null || !user.equals(currentUser)) {
            throw new ForbiddenAccessException(messageSource.getMessage("error.403.profile.owner", null, LocaleContextHolder.getLocale()));
        }

        Collection<Snippet> snippets = this.snippetService.getAllSnippetsByOwner(user.getId(), page, SNIPPET_PAGE_SIZE);
        int totalSnippetCount = this.snippetService.getAllSnippetsByOwnerCount(user.getId());
        return profileMav(id, currentUser, user, "user/"+id+"/active/", descriptionForm, Constants.OWNER_ACTIVE_CONTEXT, snippets, totalSnippetCount, totalSnippetCount, page, editing);
    }

    @RequestMapping(value = "/user/{id}/deleted", method = {RequestMethod.GET})
    public ModelAndView deletedSnippetUserProfile(
            final @PathVariable("id") long id,
            @ModelAttribute("profilePhotoForm") final ProfilePhotoForm profilePhotoForm,
            @ModelAttribute("descriptionForm") final DescriptionForm descriptionForm,
            final @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            final @RequestParam(value = "editing", required = false, defaultValue = "false") boolean editing
    ) {
        User user = this.getUserWithId(id);
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser == null || !currentUser.equals(user)) {
            throw new ForbiddenAccessException(messageSource.getMessage("error.403.profile.owner", null, LocaleContextHolder.getLocale()));
        }

        Collection<Snippet> snippets = this.snippetService.getAllDeletedSnippetsByOwner(user.getId(), page, SNIPPET_PAGE_SIZE);
        int totalSnippetCount = this.snippetService.getAllDeletedSnippetsByOwnerCount(user.getId());
        int userTotalSnippetCount = this.snippetService.getAllSnippetsByOwnerCount(user.getId());
        return profileMav(id, currentUser, user, "user/"+id+"/deleted/", descriptionForm, Constants.OWNER_DELETED_CONTEXT, snippets, totalSnippetCount, userTotalSnippetCount, page, editing);
    }

    @RequestMapping(value = "/user/{id}", method = {RequestMethod.GET})
    public ModelAndView userProfile(
            final @PathVariable("id") long id,
            @ModelAttribute("profilePhotoForm") final ProfilePhotoForm profilePhotoForm,
            @ModelAttribute("descriptionForm") final DescriptionForm descriptionForm,
            final @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            final @RequestParam(value = "editing", required = false, defaultValue = "false") boolean editing
    ) {
        User user = this.getUserWithId(id);
        User currentUser = this.loginAuthentication.getLoggedInUser();
        StringBuilder searchContext = new StringBuilder("user/").append(id).append("/");
        String tabContext = "";

        //The context is "" but it is my profile --> change it to active
        if (currentUser != null && currentUser.equals(user)){
            searchContext.append(Constants.OWNER_ACTIVE_CONTEXT).append("/");
            tabContext = Constants.OWNER_ACTIVE_CONTEXT;
        }
        Collection<Snippet> snippets = this.snippetService.getAllSnippetsByOwner(user.getId(), page, SNIPPET_PAGE_SIZE);
        int totalSnippetCount = this.snippetService.getAllSnippetsByOwnerCount(user.getId());
        return profileMav(id, currentUser, user, searchContext.toString(), descriptionForm, tabContext, snippets, totalSnippetCount, totalSnippetCount, page, editing);
    }

    @RequestMapping(value = "/user/{id}/{context}", method = {RequestMethod.POST})
    public ModelAndView endEditPhoto(
            final @PathVariable("id") long id,
            final @PathVariable("context") String context,
            @ModelAttribute("profilePhotoForm") @Valid final ProfilePhotoForm profilePhotoForm,
            final BindingResult errors,
            @ModelAttribute("descriptionForm") final DescriptionForm descriptionForm
    ){
        List<String> possibleContexts = new ArrayList<>();
        possibleContexts.add(Constants.OWNER_DELETED_CONTEXT);
        possibleContexts.add(Constants.OWNER_ACTIVE_CONTEXT);
        possibleContexts.add(Constants.USER_PROFILE_CONTEXT);

        if (!possibleContexts.contains(context)) {
            throw new InvalidUrlException();
        }
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
        if (!(context.equals(Constants.OWNER_DELETED_CONTEXT) || context.equals(Constants.OWNER_ACTIVE_CONTEXT) || context.equals(Constants.USER_PROFILE_CONTEXT))) {
            throw new InvalidUrlException();
        }
        if (errors.hasErrors()) {
            return context.equals(Constants.OWNER_DELETED_CONTEXT) ?
                    deletedSnippetUserProfile(id, profilePhotoForm, descriptionForm, page, true) :
                    activeSnippetUserProfile(id, profilePhotoForm, descriptionForm, page, true);
        }
        User currentUser = this.loginAuthentication.getLoggedInUser();
        User user = this.getUserWithId(id);
        if (currentUser != null && currentUser.equals(user)) {
            this.userService.changeDescription(id, descriptionForm.getDescription());
        } else {
            throw new ForbiddenAccessException(messageSource.getMessage("error.403.profile.owner", null, LocaleContextHolder.getLocale()));
        }
        return new ModelAndView("redirect:/user/" + id + "/" + context);
    }

    private void logAndThrow(long id) {
        LOGGER.warn("User with id {} doesn't exist", id);
        throw new UserNotFoundException(messageSource.getMessage("error.404.user", new Object[]{id}, LocaleContextHolder.getLocale()));
    }

    private User getUserWithId(final long id) {
        Optional<User> user = this.userService.findUserById(id);
        if (!user.isPresent() || this.roleService.isAdmin(user.get().getId())) {
            this.logAndThrow(id);
        }
        return user.get();
    }

    private ModelAndView profileMav(long id, User currentUser, User user, String searchContext, DescriptionForm descriptionForm, String tabContext, Collection<Snippet> snippets, final int totalSnippetCount, final int totalUserSnippetCount, final int page, final boolean editing) {
        final ModelAndView mav = new ModelAndView("user/profile");

        MavHelper.addSnippetCardFavFormAttributes(mav, this.loginAuthentication.getLoggedInUser(), snippets);

        descriptionForm.setDescription(user.getDescription());
        mav.addObject("followedTags", this.tagService.getFollowedTagsForUser(user.getId()));
        mav.addObject("pages", totalSnippetCount / SNIPPET_PAGE_SIZE + (totalSnippetCount % SNIPPET_PAGE_SIZE == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("editing", editing);
        mav.addObject("isEdit", false);
        mav.addObject("user", user);
        mav.addObject("snippets", snippets);
        mav.addObject("snippetsCount", totalUserSnippetCount);
        mav.addObject("tabSnippetCount", totalSnippetCount);
        mav.addObject("searchContext", searchContext);
        mav.addObject("tabContext", tabContext);
        return mav;
    }

    @ModelAttribute
    public void addAttributes(Model model, @Valid final SearchForm searchForm) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        MavHelper.addCurrentUserAttributes(model, currentUser, tagService, roleService);
        if (currentUser != null) {
            this.userService.updateLocale(currentUser.getId(), LocaleContextHolder.getLocale());
        }
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("searching", false);
    }

}

package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.DescriptionForm;
import ar.edu.itba.paw.webapp.form.ProfilePhotoForm;
import ar.edu.itba.paw.webapp.form.RegisterForm;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller
public class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private SnippetService snippetService;
    @Autowired
    private LoginAuthentication loginAuthentication;
    @Autowired
    private TagService tagService;

    private static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/signup", method = {RequestMethod.GET})
    public ModelAndView signUpForm(@ModelAttribute("registerForm") final RegisterForm form) {
        return new ModelAndView("user/signUpForm");
    }

    @RequestMapping(value = "/signup", method = {RequestMethod.POST})
    public ModelAndView signUp(@Valid @ModelAttribute("registerForm") final RegisterForm registerForm, final BindingResult errors, HttpServletRequest request, HttpServletResponse response) {
        if (errors.hasErrors()) {
            return signUpForm(registerForm);
        }

        long userId = userService.register(
                registerForm.getUsername(),
                passwordEncoder.encode(registerForm.getPassword()),
                registerForm.getEmail(),
                DATE.format(Calendar.getInstance().getTime().getTime())
        );

        final Collection<? extends GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER")
        );

        loginAuthentication.authWithAuthManager(request, registerForm.getUsername(), registerForm.getPassword());

        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/user/{id}")
    public ModelAndView userProfile(
            final @PathVariable("id") long id,
            @ModelAttribute("profilePhotoForm") final ProfilePhotoForm profilePhotoForm,
            @ModelAttribute("descriptionForm") final DescriptionForm descriptionForm,
            final @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            final @RequestParam(value = "editing", required = false, defaultValue = "false") boolean editing
           ) {
        final ModelAndView mav = new ModelAndView("user/profile");

        /* Set the current user and its following tags */
        User currentUser = this.loginAuthentication.getLoggedInUser();
        Collection<Tag> userTags = currentUser != null ? this.tagService.getFollowedTagsForUser(currentUser.getId()) : new ArrayList<>();
        mav.addObject("currentUser", currentUser);
        mav.addObject("userTags", userTags);

        Optional<User> user = this.userService.findUserById(id);
        if (!user.isPresent()) {
            this.logAndThrow(id);
        }
        descriptionForm.setDescription(user.get().getDescription());
        if (currentUser == null || (currentUser.getId() != user.get().getId() && editing)) {
            // ERROR
        }
        Collection<Snippet> snippets = this.snippetService.findAllSnippetsByOwner(user.get().getId(), page);
        int totalSnippetCount = this.snippetService.getAllSnippetsByOwnerCount(user.get().getId());
        int pageSize = this.snippetService.getPageSize();
        mav.addObject("followedTags", this.tagService.getFollowedTagsForUser(user.get().getId()));
        mav.addObject("pages", totalSnippetCount / pageSize + (totalSnippetCount % pageSize == 0 ? 0 : 1));
        mav.addObject("page", page);
        mav.addObject("editing", editing);
        mav.addObject("isEdit", false);
        mav.addObject("user", user.get());
        mav.addObject("snippets", snippets);
        return mav;
    }

    @RequestMapping(value = "/user/{id}", method = {RequestMethod.POST})
    public ModelAndView endEditPhoto(final @PathVariable("id") long id, @ModelAttribute("profilePhotoForm") @Valid final ProfilePhotoForm profilePhotoForm, final BindingResult errors) {
        if (errors.hasErrors()){
            return userProfile(id, profilePhotoForm, new DescriptionForm(), 1, false);
        }

        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser != null && currentUser.getId() == id) {
            try {
                this.userService.changeProfilePhoto(id, profilePhotoForm.getFile().getBytes());
                LOGGER.debug("User {} changed their profile picture", id);
            } catch (IOException e) {
                // TODO: ERROR
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
                                           @ModelAttribute("profilePhotoForm") final ProfilePhotoForm profilePhotoForm,
                                           final @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                           final @RequestParam(value = "editing", required = false, defaultValue = "false") boolean editing,
                                           final BindingResult errors) {
        if (errors.hasErrors()) {
            return userProfile(id, profilePhotoForm, descriptionForm, page, editing);
        }
        User currentUser = this.loginAuthentication.getLoggedInUser();
        Optional<User> user = this.userService.findUserById(id);
        if (currentUser != null && currentUser.getId() == user.get().getId()) {
            this.userService.changeDescription(id, descriptionForm.getDescription());
        }
        return new ModelAndView("redirect:/user/" + id);
    }

    @RequestMapping(value = "/login")
    public ModelAndView login() {
        final ModelAndView mav = new ModelAndView("user/login");
        mav.addObject("error", false);
        return mav;
    }

    @RequestMapping(value = "/login_error")
    public ModelAndView loginError() {
        final ModelAndView mav = new ModelAndView("user/login");
        mav.addObject("error", true);
        return mav;
    }

    @RequestMapping(value = "/goodbye")
    public ModelAndView logout() {
        return new ModelAndView("user/logout");
    }

    @ModelAttribute
    public void addAttributes(Model model, @Valid final SearchForm searchForm) {
        model.addAttribute("searchForm", searchForm);
    }

    private void logAndThrow(long id) {
        LOGGER.warn("User with id {} doesn't exist", id);
        throw new UserNotFoundException();
    }
}

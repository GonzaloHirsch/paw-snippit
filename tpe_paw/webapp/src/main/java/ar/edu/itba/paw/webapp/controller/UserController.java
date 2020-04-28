package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.form.ProfilePhotoForm;
import ar.edu.itba.paw.webapp.form.RegisterForm;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
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

    @RequestMapping(value = "/signup", method = { RequestMethod.GET })
    public ModelAndView signUpForm(@ModelAttribute("registerForm") final RegisterForm form) {
        return new ModelAndView("user/signUpForm");
    }

    @RequestMapping(value = "/signup",method = { RequestMethod.POST })
    public ModelAndView signUp(@Valid @ModelAttribute("registerForm") final RegisterForm registerForm, final BindingResult errors, HttpServletRequest request, HttpServletResponse response) {
        if (errors.hasErrors()) {
            return signUpForm(registerForm);
        }

        User user = userService.register(
                registerForm.getUsername(),
                passwordEncoder.encode(registerForm.getPassword()),
                registerForm.getEmail(),
                new Date()                  //TODO how to getTimeZone, switch to calendar
        );

        final Collection<? extends GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER")
        );

        loginAuthentication.authWithAuthManager(request, registerForm.getUsername(), registerForm.getPassword());

        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/user/{id}")
    public ModelAndView userProfile(final @PathVariable("id") long id, @ModelAttribute("profilePhotoForm") final ProfilePhotoForm profilePhotoForm) {
        final ModelAndView mav = new ModelAndView("user/profile");
        User currentUser = this.loginAuthentication.getLoggedInUser();
        mav.addObject("currentUser", currentUser);
        if (currentUser != null){
            mav.addObject("userTags", this.tagService.getFollowedTagsForUser(currentUser.getId()));
        } else {
            // ERROR
        }
        Optional<User> user = this.userService.findUserById(id);
        if (!user.isPresent()){
            // TODO: handle error
        }
        Collection<Snippet> snippets = this.snippetService.findAllSnippetsByOwner(user.get().getId());
        mav.addObject("isEdit", false);
        mav.addObject("user", user.get());
        mav.addObject("snippets", snippets);
        return mav;
    }

    @RequestMapping(value = "/user/{id}/edit")
    public ModelAndView startEditUserProfile(final @PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("user/profile");
        User currentUser = this.loginAuthentication.getLoggedInUser();
        mav.addObject("currentUser", currentUser);
        if (currentUser != null){
            mav.addObject("userTags", this.tagService.getFollowedTagsForUser(currentUser.getId()));
        } else {
            // ERROR
        }
        Optional<User> user = this.userService.findUserById(id);
        if (!user.isPresent()){
            // TODO: handle error
        }
        Collection<Snippet> snippets = this.snippetService.findAllSnippetsByOwner(user.get().getId());
        mav.addObject("isEdit", true);
        mav.addObject("user", user.get());
        mav.addObject("snippets", snippets);
        return mav;
    }

    @RequestMapping(value = "/user/{id}/photo", method = {RequestMethod.POST})
    public ModelAndView endEditPhoto(final @PathVariable("id") long id, @Valid @ModelAttribute("profilePhotoForm") final ProfilePhotoForm profilePhotoForm) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        if (currentUser.getId() != id){
            throw new RuntimeException();
        } else {
            try {
                this.userService.changeProfilePhoto(id, profilePhotoForm.getFile().getBytes());
            } catch (IOException e){
                // TODO: ERROR
            }
        }
        return new ModelAndView("redirect:/user/" + id);
    }

    @RequestMapping(value = "/user/{id}/image", produces ="image/jpeg")
    @ResponseBody
    public ResponseEntity<byte[]> getUserImage(final @PathVariable("id") long id) {
        Optional<User> user = this.userService.findUserById(id);
        if (!user.isPresent()){
            // TODO: handle error
        }
        CacheControl cacheControl = CacheControl.maxAge(60, TimeUnit.SECONDS)
                .noTransform()
                .mustRevalidate();
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .body(user.map(User::getIcon).orElse(null));
    }

    @RequestMapping(value = "/user/{id}/edit", method = {RequestMethod.PUT})
    public ModelAndView endEditUserProfile(final @PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("user/profile");
        User currentUser = this.loginAuthentication.getLoggedInUser();
        mav.addObject("currentUser", currentUser);
        if (currentUser != null){
            mav.addObject("userTags", this.tagService.getFollowedTagsForUser(currentUser.getId()));
        } else {
            // ERROR
        }
        Optional<User> user = this.userService.findUserById(id);
        if (!user.isPresent()){
            // TODO: handle error
        }
        Collection<Snippet> snippets = this.snippetService.findAllSnippetsByOwner(user.get().getId());
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
        return mav;    }

    @RequestMapping(value = "/goodbye")
    public ModelAndView logout() {
        return new ModelAndView("user/logout");
    }

    @ModelAttribute
    public void addAttributes(ModelAndView model, @Valid final SearchForm searchForm) {
        model.addObject("searchForm", searchForm);
    }

}

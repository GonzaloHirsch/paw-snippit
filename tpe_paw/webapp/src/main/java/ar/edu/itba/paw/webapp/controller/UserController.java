package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.crypto.HashGenerator;
import ar.edu.itba.paw.webapp.crypto.WebappCrypto;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Controller
public class  UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
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

        roleService.assignUserRole(userId);

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
        Collection<String> userRoles = currentUser != null ? this.roleService.getUserRoles(currentUser.getId()) : new ArrayList<>();

        mav.addObject("currentUser", currentUser);
        mav.addObject("userTags", userTags);
        mav.addObject("userRoles", userRoles);

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

    @RequestMapping(value = "recover-password")
    public ModelAndView recoverPassword(@ModelAttribute("recoveryForm") final RecoveryForm recoveryForm, BindingResult errors) {
        final ModelAndView mav  = new ModelAndView("user/recoverPassword");
        return mav;
    }

    @RequestMapping(value = "/send-email", method = RequestMethod.POST)
    public ModelAndView sendEmail(@Valid @ModelAttribute("recoveryForm") final RecoveryForm recoveryForm, BindingResult errors) {
        if (errors.hasErrors()){
            return recoverPassword(recoveryForm, errors);
        }
        LOGGER.debug("RecoveryForm Successful");
        User searchedUser = userService.findUserByEmail(recoveryForm.getEmail()).get();
        /*if (!searchedUser.isPresent()) {
            // this SHOULD NOT happen, Exists validation SHOULD prevent it
        }*/
        String currentPass = searchedUser.getPassword();
        String otp = WebappCrypto.generateOtp(WebappCrypto.TEST_KEY);
        String base64Token = HashGenerator.getInstance().generateRecoveryHash(recoveryForm.getEmail(), currentPass, otp);
        emailService.sendRecoveryEmail(searchedUser.getId(), recoveryForm.getEmail(), searchedUser.getUsername(), base64Token);

        // TODO create dedicated view
        return new ModelAndView("user/emailSent");
    }

    @RequestMapping(value = "/reset-password", method = RequestMethod.GET)
    public ModelAndView resetPassword(final @RequestParam(value="id") long id,
                                        final @RequestParam(value="token") String token,
                                        @ModelAttribute("resetPasswordForm") final ResetPasswordForm resetPasswordForm) {
        Optional<User> userOpt = userService.findUserById(id);
        if(!userOpt.isPresent()) {
            // TODO Resource Not Found
        }
        User user = userOpt.get();
        resetPasswordForm.setEmail(user.getEmail());
        String[] otps = WebappCrypto.generateOtps(WebappCrypto.TEST_KEY);
        String base64Token;
        boolean pass = false;
        for (int i = 0; i < 3; i++) {
            base64Token = HashGenerator.getInstance().generateRecoveryHash(user.getEmail(), user.getPassword(), otps[i]);
            pass = pass || token.compareTo(base64Token) == 0;
        }
        if (!pass) {
            return new ModelAndView("errors/404");
        }
        resetPasswordForm.setEmail(user.getEmail());
        return new ModelAndView("user/resetPassword");
    }

    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public ModelAndView endResetPassword (final @RequestParam(value="id") long id,
                                          final @RequestParam(value="token") String token,
                                          @ModelAttribute("resetPasswordForm") @Valid final ResetPasswordForm resetPasswordForm,
                                          BindingResult errors){
        // TODO redirect to previous page KEEPING pathVariables
        if(errors.hasErrors()) {
            return resetPassword(id, token, resetPasswordForm);
        }
        userService.changePassword(resetPasswordForm.getEmail(), resetPasswordForm.getNewPassword());
        // TODO inform user everything went fine
        return new ModelAndView("user/passwordReset");
    }


    private void logAndThrow(long id) {
        LOGGER.warn("User with id {} doesn't exist", id);
        throw new UserNotFoundException(messageSource.getMessage("error.user.notFound", new Object[]{id}, LocaleContextHolder.getLocale()));
    }
}

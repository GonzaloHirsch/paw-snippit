package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.auth.SignUpAuthentication;
import ar.edu.itba.paw.webapp.form.RecoveryForm;
import ar.edu.itba.paw.webapp.form.RegisterForm;
import ar.edu.itba.paw.webapp.form.ResetPasswordForm;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;

@Controller
public class RegistrationController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private SignUpAuthentication signUpAuthentication;
    @Autowired
    private RoleService roleService;
    @Autowired
    private CryptoService cryptoService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);
    private static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @RequestMapping(value = "/login")
    public ModelAndView login(HttpServletRequest request) {
        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("url_prior_login", referrer);

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

    @RequestMapping(value = "/signup", method = {RequestMethod.GET})
    public ModelAndView signUpForm(HttpServletRequest request, @ModelAttribute("registerForm") final RegisterForm form) {
        String referrer = request.getHeader("Referer");
        request.getSession().setAttribute("url_prior_login", referrer);

        return new ModelAndView("user/signUpForm");
    }

    @RequestMapping(value = "/signup", method = {RequestMethod.POST})
    public ModelAndView signUp(@Valid @ModelAttribute("registerForm") final RegisterForm registerForm, final BindingResult errors, HttpServletRequest request, HttpServletResponse response) {
        if (errors.hasErrors()) {
            return signUpForm(request, registerForm);
        }

        long userId = this.userService.register(
                registerForm.getUsername(),
                this.passwordEncoder.encode(registerForm.getPassword()),
                registerForm.getEmail(),
                DATE.format(Calendar.getInstance().getTime().getTime())
        );
        try {
            this.emailService.sendRegistrationEmail(registerForm.getEmail(), registerForm.getUsername());
        } catch (MailException e) {
            LOGGER.warn("Failed to send registration email to user {}", registerForm.getUsername());
        }

        this.roleService.assignUserRole(userId);
        this.signUpAuthentication.authWithAuthManager(request, registerForm.getUsername(), registerForm.getPassword());
        String redirectUrl = this.signUpAuthentication.redirectionAuthenticationSuccess(request);
        return new ModelAndView("redirect:" + redirectUrl);
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
        emailService.sendRecoveryEmail(recoveryForm.getEmail());
        return new ModelAndView("user/emailSent");
    }

    @RequestMapping(value = "/reset-password", method = RequestMethod.GET)
    public ModelAndView resetPassword(final @RequestParam(value="id") long id,
                                      final @RequestParam(value="token") String token,
                                      @ModelAttribute("resetPasswordForm") final ResetPasswordForm resetPasswordForm) {
        Optional<User> userOpt = userService.findUserById(id);
        if(!userOpt.isPresent()) {
            return new ModelAndView("errors/404");
        }
        User user = userOpt.get();
        boolean pass = cryptoService.checkValidRecoveryToken(id, token);
        if (!pass)
            return new ModelAndView("errors/404");

        // TODO pass email instead of id in recovery link?
        //in order to avoid calling db twice for user email
        resetPasswordForm.setEmail(user.getEmail());
        return new ModelAndView("user/resetPassword");
    }

    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public ModelAndView endResetPassword (final @RequestParam(value="id") long id,
                                          final @RequestParam(value="token") String token,
                                          @ModelAttribute("resetPasswordForm") @Valid final ResetPasswordForm resetPasswordForm,
                                          BindingResult errors){
        if(errors.hasErrors()) {
            return resetPassword(id, token, resetPasswordForm);
        }
        userService.changePassword(resetPasswordForm.getEmail(), resetPasswordForm.getNewPassword());
        return new ModelAndView("user/passwordReset");
    }

    @ModelAttribute
    public void addAttributes(Model model, @Valid final SearchForm searchForm) {
        model.addAttribute("searchForm", searchForm);
    }
}

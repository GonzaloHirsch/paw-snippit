package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
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
    private LoginAuthentication loginAuthentication;
    @Autowired
    private RoleService roleService;
    @Autowired
    private CryptoService cryptoService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);
    private static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

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
        try {
            this.emailService.sendRegistrationEmail(registerForm.getEmail(), registerForm.getUsername());
        } catch (MailException e) {
            LOGGER.warn("Failed to send registration email to user ?", registerForm.getUsername());
        }

        roleService.assignUserRole(userId);
        loginAuthentication.authWithAuthManager(request, registerForm.getUsername(), registerForm.getPassword());
        return new ModelAndView("redirect:/");
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

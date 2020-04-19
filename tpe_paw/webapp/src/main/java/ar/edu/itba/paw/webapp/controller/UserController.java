package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;

@Controller
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/signup", method = { RequestMethod.GET })
    public ModelAndView signUpForm(@ModelAttribute("signUpForm") final RegisterForm form) {
        return new ModelAndView("user/signUpForm");
    }

    @RequestMapping(value = "/signup",method = { RequestMethod.POST })
    public ModelAndView signUp(@Valid @ModelAttribute("signUpForm") final RegisterForm registerForm, final BindingResult errors) {
        if (errors.hasErrors()) {
            return signUpForm(registerForm);
        }

        User user = userService.register(
                registerForm.getUsername(),
                passwordEncoder.encode(registerForm.getPassword()),
                registerForm.getEmail(),
                new Date()                  //TODO how to getTimeZone, switch to calendar
        );

        // TODO set as current user --> will redirect to login for now

        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "/user/{id}")
    public ModelAndView userProfile(@PathVariable("id") long id) {
        // TODO
        return new ModelAndView("user/userProfile");
    }

    @RequestMapping(value = "/login")
    public ModelAndView login() {
        return new ModelAndView("user/login");
    }


//    @ModelAttribute("loggedUser")
//    public User loggedUser(HttpSession session) {
//         Long userId = (Long) session.getAttribute("userId");
//         if (userId == null) {
//             return null;
//         }
//         return userService.findById(userId.longValue()).orElseGet(() -> null);
//    }

}

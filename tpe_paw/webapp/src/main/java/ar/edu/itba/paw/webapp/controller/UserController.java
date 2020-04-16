package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    UserService userService;

    @RequestMapping(value = "/signUp", method = { RequestMethod.GET })
    public ModelAndView signUpForm(@ModelAttribute("signUpForm") final RegisterForm form) {
        return new ModelAndView("user/signUpForm");
    }

    @RequestMapping(value = "/signUp",method = { RequestMethod.POST })
    public ModelAndView signUp(@Valid @ModelAttribute("signUpForm") final RegisterForm registerForm, final BindingResult errors) {
        if (errors.hasErrors()) {
            System.out.println("INSIDE ERROR");
            return signUpForm(registerForm);
        }

        User user = userService.createUser(
                registerForm.getUsername(),
                registerForm.getPassword(),
                registerForm.getEmail(),
                "",
                0,
                new Date()     //TODO how to getTimeZone, switch to calendar
        );

        // TODO set as current user

        return new ModelAndView("redirect:/");
    }

    /* Llevarlo a un error controller
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView noSuchUser() {
        return new ModelAndView("errors/404");
    }
    */

    @RequestMapping(value = "/user/{id}")
    public ModelAndView userProfile(@PathVariable("id") long id) {
        // TODO
        return new ModelAndView("user/userProfile");
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

package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.webapp.form.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

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

//        User user = userService.createUser(
//                registerForm.getUsername(),
//                registerForm.getPassword(),
//                registerForm.getEmail(),
//                "",
//                0,
//                new Date()     //TODO how to getTimeZone, switch to calendar
//        );

        // TODO set as current user

        return new ModelAndView("redirect:/");
    }
}

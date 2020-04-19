package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.RegisterForm;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private SnippetService snippetService;

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
    public ModelAndView userProfile(@ModelAttribute("searchForm") final SearchForm searchForm, final @PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("user/profile");
        Optional<User> user = this.userService.findUserById(id);
        if (!user.isPresent()){
            // TODO: handle error
        }
        Collection<Snippet> snippets = this.snippetService.findAllSnippetsByOwner(user.get().getUserId());
        mav.addObject("user", user.get());
        mav.addObject("snippets", snippets);
        return mav;
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

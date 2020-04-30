package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.LanguageService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.LoginAuthentication;
import ar.edu.itba.paw.webapp.form.AdminAddForm;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Controller
public class AdminController {

    @Autowired
    LoginAuthentication loginAuthentication;
    @Autowired
    LanguageService languageService;
    @Autowired
    TagService tagService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @RequestMapping("/admin/add")
    public ModelAndView adminAddForm(@ModelAttribute("adminAddForm") final AdminAddForm adminAddForm) {
        return new ModelAndView("admin/adminAdd");
    }

    @RequestMapping(value="/admin/add", method= RequestMethod.POST)
    public ModelAndView adminAdd(@Valid @ModelAttribute("adminAddForm") final AdminAddForm adminAddForm, final BindingResult errors) {
        if (errors.hasErrors()) {
            return adminAddForm(adminAddForm);
        }
        User currentUser = this.loginAuthentication.getLoggedInUser();

        if (currentUser == null || currentUser.getUsername().compareTo("admin") != 0) {
            LOGGER.warn("In admin add form and the user is not admin");
        }

        /* Language List */
        List<String> languages = adminAddForm.getLanguages() != null ? adminAddForm.getLanguages() : new ArrayList<>();
        languages.removeAll(Arrays.asList("", null));
        if (!languages.isEmpty()) languageService.addLanguages(languages);

        /* Tag List */
        List<String> tags = adminAddForm.getTags() != null ? adminAddForm.getTags() : new ArrayList<>();
        tags.removeAll(Arrays.asList("", null));
        if (!tags.isEmpty()) tagService.addTags(tags);

        LOGGER.debug("Admin added languages and tags -> {} and {}", languages.toString(), tags.toString());

        return new ModelAndView("redirect:/");
    }

    @ModelAttribute
    public void addAttributes(Model model, @Valid final SearchForm searchForm) {
        User currentUser = this.loginAuthentication.getLoggedInUser();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("searchForm", searchForm);
    }
}

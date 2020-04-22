package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.helpers.FieldMatch;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@FieldMatch(first = "password", second = "repeatPassword" /*message = messageSource.getMessage("FieldMatch.registerForm",null, LocaleContextHolder.getLocale())*/)
public class RegisterForm {

    @Size(min=6, max=50)
    @Pattern(regexp = "^[a-zA-Z0-9-_.]+$")
    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @Size(min=8)
    @NotBlank
    private String password;

    private String repeatPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}

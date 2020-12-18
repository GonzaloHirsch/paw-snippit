package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.validations.FieldMatch;
import ar.edu.itba.paw.webapp.validations.UniqueEmail;
import ar.edu.itba.paw.webapp.validations.UniqueUsername;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

@FieldMatch(first = "password", second = "repeatPassword", message = "{FieldMatch.registerForm.passwords}")
public class RegisterDto {

    @Size(min=6, max=50, message = "{Size.registerForm.username}")
    @Pattern(regexp = "^[a-zA-Z0-9-_.]+$", message = "{Pattern.registerForm.username}")
    @NotBlank(message = "{NotBlank.registerForm.username}")
    @UniqueUsername(message = "{Unique.registerForm.username}")
    private String username;

    @Email(message = "{Email.registerForm.email}")
    @NotBlank(message = "{NotBlank.registerForm.email}")
    @UniqueEmail(message = "{Unique.registerForm.email}")
    private String email;

    @Size(min=8, message = "{Size.registerForm.password}")
    @NotBlank(message = "{NotBlank.registerForm.password}")
    @Pattern(regexp = "^\\S*$", message = "{Pattern.password.spaces}")
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

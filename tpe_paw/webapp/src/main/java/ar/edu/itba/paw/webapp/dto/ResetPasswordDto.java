package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.validations.FieldMatch;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@FieldMatch(first = "newPassword", second = "repeatNewPassword", message = "{FieldMatch.registerForm.passwords}")
public class ResetPasswordDto {
    @Size(min = 8, message = "{Size.resetPasswordForm.newPassword}")
    @NotBlank(message = "{NotBlank.resetPasswordForm.newPassword}")
    @Pattern(regexp = "^\\S*$", message = "{Pattern.password.spaces}")
    private String newPassword;

    private String repeatNewPassword;

    @NotBlank(message = "{NotBlank.resetPasswordForm.token}")
    private String token;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatNewPassword() {
        return repeatNewPassword;
    }

    public void setRepeatNewPassword(String repeatNewPassword) {
        this.repeatNewPassword = repeatNewPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

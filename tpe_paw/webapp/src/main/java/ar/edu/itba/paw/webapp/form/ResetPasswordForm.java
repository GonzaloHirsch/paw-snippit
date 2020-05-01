package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validations.FieldMatch;

@FieldMatch(first = "newPassword", second = "repeatNewPassword", message = "{FieldMatch.resetPasswordForm.passwords}")
public class ResetPasswordForm {

    private String newPassword;
    private String repeatNewPassword;
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

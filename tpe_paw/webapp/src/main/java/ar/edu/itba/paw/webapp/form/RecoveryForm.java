package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validations.FieldExists;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class RecoveryForm {
    @Email
    @NotBlank
    @FieldExists(fieldName = "Email")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

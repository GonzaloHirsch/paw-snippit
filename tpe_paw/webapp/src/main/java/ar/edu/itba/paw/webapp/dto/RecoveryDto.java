package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.validations.FieldExists;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class RecoveryDto {
    @Email(message = "{Email.recoveryForm.email}")
    @NotBlank(message = "{NotBlank.recoveryForm.email}")
    @FieldExists(fieldName = "Email", message = "{Exists.notFound.email}")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

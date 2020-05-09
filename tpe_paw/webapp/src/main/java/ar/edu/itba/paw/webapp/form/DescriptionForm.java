package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class DescriptionForm {

    @Size(max=300, message = "{Size.profileForm.description}")
    @Pattern(regexp = "^[^\\s]+(\\s+[^\\s]+)*$", message = "{form.error.spaces}")
    private String description;

    public void setDescription(String description){ this.description = description; }

    public String getDescription(){ return this.description; }
}

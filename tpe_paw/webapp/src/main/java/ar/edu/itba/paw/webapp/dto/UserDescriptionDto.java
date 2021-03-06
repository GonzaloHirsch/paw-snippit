package ar.edu.itba.paw.webapp.dto;

import javax.validation.constraints.Size;

public class UserDescriptionDto {
    @Size(max=500, message = "{Size.profileForm.description}")
    private String description;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package ar.edu.itba.paw.webapp.dto;

import javax.validation.constraints.Size;

public class ItemCreateDto {
    @Size(min=1, max=30, message = "{Size.verification.code}")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

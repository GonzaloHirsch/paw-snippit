package ar.edu.itba.paw.webapp.dto;

import org.hibernate.validator.constraints.NotBlank;

public class TokenDto {
    @NotBlank(message = "{NotBlank.tokenValidForm.token}")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

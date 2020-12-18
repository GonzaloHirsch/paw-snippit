package ar.edu.itba.paw.webapp.dto;

import org.hibernate.validator.constraints.NotBlank;

import javax.ws.rs.QueryParam;

public class TokenDto {
    @NotBlank(message = "{NotBlank.tokenValidForm.token}")
    @QueryParam("token")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

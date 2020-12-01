package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.User;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class SnippetUserInfoDto {

    private String username;
    private long id;
    private URI picture;
    private boolean hasPicture;

    public static SnippetUserInfoDto fromUser(User user, UriInfo uriInfo) {
        final SnippetUserInfoDto dto = new SnippetUserInfoDto();

        dto.username = user.getUsername();
        dto.id = user.getId();
        dto.picture = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(user.getId())).path("profile_photo").build();
        dto.hasPicture = user.getIcon() != null;

        return dto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public URI getPicture() {
        return picture;
    }

    public void setPicture(URI picture) {
        this.picture = picture;
    }

    public boolean isHasPicture() {
        return hasPicture;
    }

    public void setHasPicture(boolean hasPicture) {
        this.hasPicture = hasPicture;
    }
}

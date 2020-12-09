package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.Instant;

public class UserDto {
    private Long id;
    private String username;
    private String description;
    private boolean isVerified;
    private URI activeSnippets;
    private URI deletedSnippets;
    private String password;
    private String email;
    private URI picture;
    private boolean hasPicture;
    private Instant dateJoined;
    private UserStatsDto stats;
    private URI url;

    public static UserDto fromUser(User user, UriInfo uriInfo) {
        final UserDto dto = new UserDto();

        dto.id = user.getId();
        dto.username = user.getUsername();
        dto.description = user.getDescription();
        dto.dateJoined = user.getDateJoined();
        dto.isVerified = user.isVerified();
        dto.activeSnippets = uriInfo.getAbsolutePathBuilder().path("active_snippets").build();
        dto.deletedSnippets = uriInfo.getAbsolutePathBuilder().path("deleted_snippets").build();
        // DO NOT SET PASSWORD -> Sensitive
        // DO NOT SET EMAIL -> Sensitive
        dto.picture = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(user.getId())).path("profile_photo").build();
        dto.hasPicture = user.getIcon() != null;
        dto.stats = UserStatsDto.fromUser(user);
        dto.url = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(user.getId())).build();

        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public URI getActiveSnippets() {
        return activeSnippets;
    }

    public void setActiveSnippets(URI activeSnippets) {
        this.activeSnippets = activeSnippets;
    }

    public URI getDeletedSnippets() {
        return deletedSnippets;
    }

    public void setDeletedSnippets(URI deletedSnippets) {
        this.deletedSnippets = deletedSnippets;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    public UserStatsDto getStats() {
        return stats;
    }

    public void setStats(UserStatsDto stats) {
        this.stats = stats;
    }

    public Instant getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Instant dateJoined) {
        this.dateJoined = dateJoined;
    }
}

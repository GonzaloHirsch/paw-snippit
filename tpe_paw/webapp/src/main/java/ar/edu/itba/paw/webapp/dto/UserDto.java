package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class UserDto {
    private Long id;
    private String username;
    private String description;
    private boolean isVerified;
    private int reputation;
    private URI activeSnippets;
    private URI deletedSnippets;
    private String password;
    private String email;

    public static UserDto fromUser(User user, UriInfo uriInfo) {
        final UserDto dto = new UserDto();

        dto.id = user.getId();
        dto.username = user.getUsername();
        dto.description = user.getDescription();
        dto.isVerified = user.isVerified();
        dto.reputation = user.getReputation();
        dto.activeSnippets = uriInfo.getAbsolutePathBuilder().path("active_snippets").build();
        dto.deletedSnippets = uriInfo.getAbsolutePathBuilder().path("deleted_snippets").build();
        // DO NOT SET PASSWORD -> Sensitive
        // DO NOT SET EMAIL -> Sensitive

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

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
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
}

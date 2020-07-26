package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Snippet;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class SnippetDto {

    private Long id;
    private String code;
    private String title;
    private String description;
    private URI creator;

    public static SnippetDto fromSnippet(Snippet snippet, UriInfo uriInfo) {
        final SnippetDto dto = new SnippetDto();

        dto.id = snippet.getId();
        dto.code = snippet.getCode();
        dto.title = snippet.getTitle();
        dto.description = snippet.getDescription();
        dto.creator = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(snippet.getOwner().getId())).build();

        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URI getCreator() {
        return creator;
    }

    public void setCreator(URI creator) {
        this.creator = creator;
    }
}

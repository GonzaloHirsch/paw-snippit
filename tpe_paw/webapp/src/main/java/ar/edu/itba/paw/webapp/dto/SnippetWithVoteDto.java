package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Snippet;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class SnippetWithVoteDto {

    private Long id;
    private String code;
    private String title;
    private String description;
    private URI creator;
    private URI language;
    private int voteCount;
    private boolean isFlagged;
    private boolean isDeleted;

    public static SnippetWithVoteDto fromSnippetAndVote(Snippet snippet, int voteCount, UriInfo uriInfo) {
        final SnippetWithVoteDto dto = new SnippetWithVoteDto();

        dto.id = snippet.getId();
        dto.code = snippet.getCode();
        dto.title = snippet.getTitle();
        dto.description = snippet.getDescription();
        dto.isFlagged = snippet.isFlagged();
        dto.isDeleted = snippet.isDeleted();
        dto.voteCount = voteCount;
        dto.creator = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(snippet.getOwner().getId())).build();
        dto.language = uriInfo.getBaseUriBuilder().path("languages").path(String.valueOf(snippet.getLanguage().getId())).build();

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

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public URI getLanguage() {
        return language;
    }

    public void setLanguage(URI language) {
        this.language = language;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}

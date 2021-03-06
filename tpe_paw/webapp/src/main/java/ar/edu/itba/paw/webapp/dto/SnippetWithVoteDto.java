package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Vote;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.Instant;
import java.util.Locale;
import java.util.Optional;

public class SnippetWithVoteDto {

    private Long id;
    private String code;
    private String title;
    private String description;
    private int voteCount;
    private SnippetUserInfoDto creator;
    private LanguageDto language;
    private Instant createdDate;
    private URI tags;
    private boolean isFlagged;
    private boolean isDeleted;
    private boolean isUserReported;
    private boolean isReported;
    private boolean isFavorite;
    private boolean isUserVotedPositive;
    private boolean isUserVotedNegative;

    public static SnippetWithVoteDto fromSnippetAndVote(Snippet snippet, int voteCount, UriInfo uriInfo, long currentUserId, Locale locale) {
        final SnippetWithVoteDto dto = new SnippetWithVoteDto();

        dto.id = snippet.getId();
        dto.code = snippet.getCode();
        dto.title = snippet.getTitle();
        dto.description = snippet.getDescription();
        dto.isFlagged = snippet.isFlagged();
        dto.isDeleted = snippet.isDeleted();
        dto.voteCount = voteCount;
        dto.creator = SnippetUserInfoDto.fromUser(snippet.getOwner(), uriInfo);
        dto.language = LanguageDto.fromLanguage(snippet.getLanguage(), uriInfo);
        dto.tags = uriInfo.getBaseUriBuilder().path("snippets").path(String.valueOf(snippet.getId())).path("tags").build();
        dto.createdDate = snippet.getDateCreated();
        // Indicates if the logged user reported the snippet
        dto.isUserReported = snippet.getReports().stream().anyMatch(r -> r.getReportedBy().getId().equals(currentUserId));
        dto.isFavorite = snippet.getUserFavorites().stream().anyMatch(u -> u.getId().equals(currentUserId));
        // Indicates if the snippet has any active reports
        dto.isReported = snippet.getReports().stream().anyMatch(r -> !r.isOwnerDismissed());
        // Inicates the type of vote by the user
        Optional<Vote> vote = snippet.getVotes().stream().filter(v -> v.getUser().getId().equals(currentUserId)).findFirst();
        if (vote.isPresent()){
            if (vote.get().isPositive()){
                dto.isUserVotedPositive = true;
                dto.isUserVotedNegative = false;
            } else {
                dto.isUserVotedPositive = false;
                dto.isUserVotedNegative = true;
            }
        } else {
            dto.isUserVotedPositive = false;
            dto.isUserVotedNegative = false;
        }

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

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public SnippetUserInfoDto getCreator() {
        return creator;
    }

    public void setCreator(SnippetUserInfoDto creator) {
        this.creator = creator;
    }

    public LanguageDto getLanguage() {
        return language;
    }

    public void setLanguage(LanguageDto language) {
        this.language = language;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public URI getTags() {
        return tags;
    }

    public void setTags(URI tags) {
        this.tags = tags;
    }

    public boolean isUserReported() {
        return isUserReported;
    }

    public void setUserReported(boolean userReported) {
        isUserReported = userReported;
    }

    public boolean isReported() {
        return isReported;
    }

    public void setReported(boolean reported) {
        isReported = reported;
    }

    public boolean isUserVotedPositive() {
        return isUserVotedPositive;
    }

    public void setUserVotedPositive(boolean userVotedPositive) {
        isUserVotedPositive = userVotedPositive;
    }

    public boolean isUserVotedNegative() {
        return isUserVotedNegative;
    }

    public void setUserVotedNegative(boolean userVotedNegative) {
        isUserVotedNegative = userVotedNegative;
    }
}

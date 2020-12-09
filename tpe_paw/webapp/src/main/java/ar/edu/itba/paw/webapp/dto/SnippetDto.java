package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Snippet;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.LocaleResolver;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.Date;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Locale;

public class SnippetDto {
    private Long id;
    private String code;
    private String title;
    private String description;
    private SnippetUserInfoDto creator;
    private LanguageDto language;
    private String createdDate;
    private boolean isFlagged;
    private boolean isFavorite;

    public static SnippetDto fromSnippet(Snippet snippet, long currentUserId, UriInfo uriInfo, Locale locale) {
        final SnippetDto dto = new SnippetDto();

        dto.id = snippet.getId();
        dto.code = snippet.getCode();
        dto.title = snippet.getTitle();
        dto.description = snippet.getDescription();
        dto.isFlagged = snippet.isFlagged();
        dto.creator = SnippetUserInfoDto.fromUser(snippet.getOwner(), uriInfo);
        dto.language = LanguageDto.fromLanguage(snippet.getLanguage(), uriInfo);
        dto.createdDate = DateFormat.getDateInstance(DateFormat.SHORT, locale).format(Date.from(snippet.getDateCreated()));
        dto.isFavorite = snippet.getUserFavorites().stream().anyMatch(u -> u.getId().equals(currentUserId));
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

    public SnippetUserInfoDto getCreator() {
        return creator;
    }

    public void setCreator(SnippetUserInfoDto creator) {
        this.creator = creator;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public LanguageDto getLanguage() {
        return language;
    }

    public void setLanguage(LanguageDto language) {
        this.language = language;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}

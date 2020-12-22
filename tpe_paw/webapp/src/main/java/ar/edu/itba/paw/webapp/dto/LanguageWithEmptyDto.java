package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Language;

import javax.ws.rs.core.UriInfo;

public class LanguageWithEmptyDto {
    private String name;
    private Long id;
    private boolean isEmpty;

    public static LanguageWithEmptyDto fromLanguage(Language language, UriInfo uriInfo){
        final LanguageWithEmptyDto dto = new LanguageWithEmptyDto();

        dto.id = language.getId();
        dto.name = language.getName();
        dto.isEmpty = language.getSnippetsUsingIsEmpty();

        return dto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }
}
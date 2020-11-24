package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Language;

import javax.ws.rs.core.UriInfo;

public class LanguageDto {
    private String name;
    private Long id;

    public static LanguageDto fromLanguage(Language language, UriInfo uriInfo){
        final LanguageDto dto = new LanguageDto();

        dto.id = language.getId();
        dto.name = language.getName();

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
}

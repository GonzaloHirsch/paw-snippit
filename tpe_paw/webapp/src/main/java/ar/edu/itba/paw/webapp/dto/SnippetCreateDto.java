package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.validations.FieldExists;
import ar.edu.itba.paw.webapp.validations.NotBlankWithSpaces;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Collection;

public class SnippetCreateDto {
    @Size(min=5, max=50, message = "{Size.snippetCreateForm.title}")
    @NotBlankWithSpaces(message = "NotBlankWithSpaces.message")
    private String title;

    @Size(max=500, message = "{Size.snippetCreateForm.description}")
    private String description;

    @Size(min=5, max=30000, message = "{Size.snippetCreateForm.code}")
    @NotBlankWithSpaces(message = "NotBlankWithSpaces.message")
    private String code;

    @Min(value=1, message = "{Min.snippetCreateForm.language}")
    @FieldExists(fieldName = "Language", message = "Exists.notFound.language")
    private Long language;

    private Collection<String> tags;


    public void setCode(String code) {
        this.code = code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLanguage(Long language) {
        this.language = language;
    }

    public void setTags(Collection<String> tags) {
        this.tags = tags;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getLanguage() {
        return language;
    }

    public Collection<String> getTags() {
        return tags;
    }
}

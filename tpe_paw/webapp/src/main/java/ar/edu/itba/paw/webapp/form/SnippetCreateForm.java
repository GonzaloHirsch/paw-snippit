package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validations.FieldExists;
import ar.edu.itba.paw.webapp.validations.NotBlankWithSpaces;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;

public class SnippetCreateForm {
    @Size(min=5, max=50)
    @NotBlankWithSpaces
    private String title;

    @Size(max=500)
    private String description;

    @Size(min=5, max=30000)
    @NotBlankWithSpaces
    private String code;

    @Min(value=1)
    @FieldExists(fieldName = "Language")
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

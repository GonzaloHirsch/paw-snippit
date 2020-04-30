package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Collection;

public class SnippetCreateForm {
    @Size(min=5, max=50)
    private String title;

    @Size(max=500)
    private String description;

    @Size(min=5, max=30000)
    private String code;

    @Min(value=1)
    private Long language;

    private Collection<Long> tags;


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

    public void setTags(Collection<Long> tags) {
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

    public Collection<Long> getTags() {
        return tags;
    }
}

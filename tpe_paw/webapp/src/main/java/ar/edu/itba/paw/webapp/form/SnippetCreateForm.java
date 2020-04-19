package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;

import javax.validation.constraints.Size;
import java.util.Collection;

public class SnippetCreateForm {
    @Size(min=5, max=50)
    private String title;

    @Size(max=500)
    private String description;

    @Size(min=5,max=30000)
    private String code;

    private String language;
    private Collection<Tag> tags;


    public void setCode(String code) {
        this.code = code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setTags(Collection<Tag> tags) {
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

    public String getLanguage() {
        return language;
    }

    public Collection<Tag> getTags() {
        return tags;
    }
}

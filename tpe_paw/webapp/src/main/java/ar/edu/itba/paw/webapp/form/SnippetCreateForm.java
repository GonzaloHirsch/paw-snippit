package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;

import java.util.Collection;

public class SnippetCreateForm {

    private long id;
    private User owner;
    private String code;
    private String title;
    private String description;
    private String dateCreated;
    private String language;
    private Collection<Tag> tags;

    public void setId(long id) {
        this.id = id;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
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

    public String getDateCreated() {
        return dateCreated;
    }

    public String getLanguage() {
        return language;
    }

    public Collection<Tag> getTags() {
        return tags;
    }
}

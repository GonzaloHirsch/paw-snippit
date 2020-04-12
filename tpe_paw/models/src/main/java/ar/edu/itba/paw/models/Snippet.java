package ar.edu.itba.paw.models;

import org.omg.IOP.TAG_CODE_SETS;

import java.util.Calendar;
import java.util.Collection;

public class Snippet {
    private long id;
    private User owner;
    private String code;
    private String title;
    private String description;
    private String dateCreated;
    private String language;
    private Collection<Tag> tags;

    public Snippet(long id, User owner, String code, String title, String description, String dateCreated, String language, Collection<Tag> tags) {
        this.id = id;
        this.owner = owner;
        this.code = code;
        this.title = title;
        this.description = description;
        this.dateCreated = dateCreated;
        this.language = language;
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

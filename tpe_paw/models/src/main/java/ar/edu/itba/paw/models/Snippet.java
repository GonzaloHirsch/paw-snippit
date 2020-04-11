package ar.edu.itba.paw.models;

import java.util.Calendar;

public class Snippet {
    private long id;
    private User owner;
    private String code;
    private String title;
    private String description;
    private String dateCreated;
    private String language;

    public Snippet(long id, User owner, String code, String title, String description, String dateCreated, String language) {
        this.id = id;
        this.owner = owner;
        this.code = code;
        this.title = title;
        this.description = description;
        this.dateCreated = dateCreated;
        this.language = language;
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
}

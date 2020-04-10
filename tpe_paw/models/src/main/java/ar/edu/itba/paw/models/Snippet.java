package ar.edu.itba.paw.models;

import java.util.Calendar;

public class Snippet {
    private long id;
    private User owner;
    private String code;
    private String title;
    private String description;
    private Calendar date_created;
    private String language;

    public Snippet(long id, User owner, String code, String title, String description, Calendar date_created, String language) {
        this.id = id;
        this.owner = owner;
        this.code = code;
        this.title = title;
        this.description = description;
        this.date_created = date_created;
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

    public Calendar getDate_created() {
        return date_created;
    }

    public String getLanguage() {
        return language;
    }
}

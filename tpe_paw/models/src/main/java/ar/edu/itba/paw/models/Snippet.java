package ar.edu.itba.paw.models;

import java.util.Date;

public class Snippet {
    private long id;
    private User owner;
    private String code;
    private String title;
    private String description;
    private Date dateCreated;

    public Snippet(long id, User owner, String code, String title, String description, Date dateCreated) {
        this.id = id;
        this.owner = owner;
        this.code = code;
        this.title = title;
        this.description = description;
        this.dateCreated = dateCreated;
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

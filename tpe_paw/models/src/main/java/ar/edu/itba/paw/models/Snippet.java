package ar.edu.itba.paw.models;

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
    private int votes;
    private boolean flagged;

    public Snippet(long id, User owner, String code, String title, String description, String dateCreated, String language, Collection<Tag> tags, int votes, boolean flagged) {
        this.id = id;
        this.owner = owner;
        this.code = code;
        this.title = title;
        this.description = description;
        this.dateCreated = dateCreated;
        this.language = language;
        this.tags = tags;
        this.votes = votes;
        this.flagged = flagged;
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

    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }

    public int getVotes(){ return this.votes;}

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }
}

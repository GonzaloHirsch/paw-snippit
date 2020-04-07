package ar.edu.itba.paw.models;

public class Snippet {
    private long id;
    private User owner;
    private String code;
    private String title;
    private String description;

    public Snippet(long id, User owner, String code, String title, String description) {
        this.id = id;
        this.ownerId = ownerId;
        this.code = code;
        this.title = title;
        this.description = description;
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

    public void setDescription(String description) {
        this.description = description;
    }
}

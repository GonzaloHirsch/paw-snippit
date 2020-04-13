package ar.edu.itba.paw.models;

public class Favorite {
    private long userId;
    private long snippetId;
    private boolean isSelected;

    public Favorite(long user, long snippet, boolean isSelected) {
        this.userId = user;
        this.snippetId = snippet;
        this.isSelected = isSelected;
    }

    public long getUser() {
        return userId;
    }

    public long getSnippet() {
        return snippetId;
    }

    public boolean isSelected() {
        return isSelected;
    }

}

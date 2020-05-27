package ar.edu.itba.paw.models;

@Deprecated
public class Favorite {
    private long userId;
    private long snippetId;

    public Favorite(long user, long snippet) {
        this.userId = user;
        this.snippetId = snippet;
    }

    public long getUser() {
        return userId;
    }

    public long getSnippet() {
        return snippetId;
    }

}

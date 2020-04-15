package ar.edu.itba.paw.models;

public class Vote {


    private User user;
    private Snippet snippet;
    private int type;

    public Vote(User user, Snippet snippet, int type) {
        this.user = user;
        this.snippet = snippet;
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public Snippet getSnippet() {
        return snippet;
    }

    public int getType() {
        return type;
    }

}

package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "votes_for")
public class Vote {

    @EmbeddedId
    VoteId id;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn("user_id")
    private User user;

    @ManyToOne
    @MapsId("snippet_id")
    @JoinColumn("snippet_id")
    private Snippet snippet;

    //TODO: add constraint(-1,1)
    @Column
    private int type;

    // For Hibernate
    public Vote(){}

    /*
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
    */
}

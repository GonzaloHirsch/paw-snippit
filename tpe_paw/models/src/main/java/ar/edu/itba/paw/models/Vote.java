package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "votes_for")
public class Vote {

    @EmbeddedId
    VoteId id;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("snippet_id")
    @JoinColumn(name = "snippet_id")
    private Snippet snippet;

    @Column(name = "type", columnDefinition = "INT CHECK (type IN (-1, 1))")
    private int type;

    public Vote(){
        // Hibernate constructor
    }

    public int getType() {
        return type;
    }

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

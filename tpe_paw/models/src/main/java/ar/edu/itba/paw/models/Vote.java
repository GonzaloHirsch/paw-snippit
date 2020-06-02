package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "votes_for")
public class Vote {

    /**
     * Method to set the Embedded Id of the vote object
     */
    @PrePersist
    private void prePersiste() {
        if (this.id == null) {
            this.id = new VoteId(this.user.getId(), this.snippet.getId());
        }
    }

    @EmbeddedId
    VoteId id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @MapsId("snippet_id")
    @JoinColumn(name = "snippet_id")
    private Snippet snippet;

    @Column(name = "is_positive")
    private boolean isPositive;

    public Vote(){
        // Hibernate constructor
    }

    public Vote(User user, Snippet snippet, boolean isPositive) {
        this.user = user;
        this.snippet = snippet;
        this.isPositive = isPositive;
    }


    public User getUser() {
        return user;
    }

    public Snippet getSnippet() {
        return snippet;
    }

    public boolean isPositive() {
        return isPositive;
    }

    public void setPositive(boolean positive) {
        isPositive = positive;
    }

    public int getVoteWeight() {
        return isPositive ? 1 : -1;
    }
}

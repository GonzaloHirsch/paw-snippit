package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "votes_for")
public class Vote {

    /**
     * Method to set the Embedded Id of the vote object
     */
    @PrePersist
    private void prePersist() {
        if (this.id == null) {
            this.id = new VoteId(this.user.getId(), this.snippet.getId());
        }
    }

    @EmbeddedId
    private VoteId id;

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
    

    /**
     * Returns the string representation of this Vote.
     * The string consists of the Class name, the id of the
     * user that voted, the snippet id the vote was directed at,
     * and a boolean to specify if the vote was positive or negative.
     * The exact details of the representation are unspecified
     * and subject to change, but the following may be regarded as typical:
     *
     * Example: "[Vote: userId=12, snippetId=30, isPositive=true]"
     */
    @Override public String toString() {
        return String.format("[Vote: user=%d, snippet=%d, isPositive=%b]", this.getUser().getId(), this.getSnippet().getId(), this.isPositive());
    }
}

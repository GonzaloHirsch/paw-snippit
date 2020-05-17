package ar.edu.itba.paw.models;

import org.hibernate.annotations.Check;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VoteId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "snippet_id")
    private Long snippetId;


    // For Hibernate
    public VoteId(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteId voteId = (VoteId) o;
        return userId.equals(voteId.userId) &&
                snippetId.equals(voteId.snippetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, snippetId);
    }
}

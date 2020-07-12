package ar.edu.itba.paw.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class VoteId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "snippet_id")
    private Long snippetId;

    public VoteId(){
        // Hibernate constructor
    }

    public VoteId(Long userId, Long snippetId){
        this.userId = userId;
        this.snippetId = snippetId;
    }

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
        int result = userId.hashCode();
        result = 31 * result + snippetId.hashCode();
        return result;
    }
}

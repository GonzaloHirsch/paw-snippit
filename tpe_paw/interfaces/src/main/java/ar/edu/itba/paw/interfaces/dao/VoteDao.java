package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Vote;

import java.util.Collection;
import java.util.Optional;

public interface VoteDao {
    Collection<Vote> getUserVotes(long userId);
    Optional<Vote> getVote(long userId, long snippetId);
    void performVote(long userId, long snippetId, int voteType);
    void withdrawVote(long userId, long snippetId);
}

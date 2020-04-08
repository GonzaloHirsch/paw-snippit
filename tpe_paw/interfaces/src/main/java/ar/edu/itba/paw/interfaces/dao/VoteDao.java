package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Vote;

import java.util.Collection;
import java.util.Optional;

public interface VoteDao {
    Collection<Vote> getUserVotes(long userId);
    Optional<Vote> getVote(long userId, long voteId);
    void performVote(long userId, long voteId, int voteType);
}

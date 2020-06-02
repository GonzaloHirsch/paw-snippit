package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Vote;

import java.util.Collection;
import java.util.Optional;

public interface VoteDao {
    Collection<Vote> getUserVotes(final long userId);
    Optional<Vote> getVote(final long userId, final long snippetId);
    void addVote(final long userId, final long snippetId, boolean isPositive);
    void withdrawVote(final long userId, final long snippetId);
    Optional<Integer> getVoteBalance(final long snippetId);
}

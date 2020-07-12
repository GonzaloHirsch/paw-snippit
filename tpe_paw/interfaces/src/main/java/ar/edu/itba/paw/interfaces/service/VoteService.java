package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Vote;

import java.util.Collection;
import java.util.Optional;

public interface VoteService {
    int POSITIVE_WEIGHT = 1;
    int NEGATIVE_WEIGHT = -1;

    Collection<Vote> getUserVotes(final long userId);
    Optional<Vote> getVote(final long userId, final long snippetId);
    void performVote(final long ownerId, final long userId, final long snippetId, boolean voteSelected, boolean isPositive);
    void withdrawVote(final long userId, final long snippetId);
    int addVote(final long userId, final long snippetId, boolean isPositive);
    int getVoteBalance(final long snippetId);
}

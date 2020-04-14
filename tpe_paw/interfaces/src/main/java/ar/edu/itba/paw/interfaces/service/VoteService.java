package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Vote;

import java.util.Collection;
import java.util.Optional;

public interface VoteService {
    Collection<Vote> getUserVotes(final long userId);
    Optional<Vote> getVote(final long userId, final long snippetId);
    void performVote(final long userId, final long snippetId, final int voteType, final int oldVoteType);
    void withdrawVote(final long userId, final long snippetId);
    void addVote(final long userId, final long snippetId, final int voteType);
    Optional<Integer> getVoteCount(final long snippetId);
}

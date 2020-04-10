package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Vote;

import java.util.Collection;
import java.util.Optional;

public interface VoteService {
    Collection<Vote> getUserVotes(long userId);
    Optional<Vote> getVote(long userId, long snippetId);
    void performVote(long userId, long snippetId, int voteType);
}

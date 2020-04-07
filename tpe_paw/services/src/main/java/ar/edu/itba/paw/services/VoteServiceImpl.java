package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.VoteService;
import ar.edu.itba.paw.models.Vote;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class VoteServiceImpl implements VoteService {
    @Override
    public Optional<Collection<Vote>> getUserVotes(long userId) {
        return Optional.empty();
    }

    @Override
    public Optional<Vote> getVote(long userId, long voteId) {
        return Optional.empty();
    }

    @Override
    public void performVote(long userId, long voteId, int voteType) {

    }
}

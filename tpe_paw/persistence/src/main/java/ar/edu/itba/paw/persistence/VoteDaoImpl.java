package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.VoteDao;
import ar.edu.itba.paw.models.Vote;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class VoteDaoImpl implements VoteDao {

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

package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.VoteDao;
import ar.edu.itba.paw.interfaces.service.VoteService;
import ar.edu.itba.paw.models.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    VoteDao voteDao;

    @Override
    public Collection<Vote> getUserVotes(long userId) {
        return voteDao.getUserVotes(userId);
    }

    @Override
    public Optional<Vote> getVote(long userId, long snippetId) {
        return voteDao.getVote(userId, snippetId);
    }

    @Override
    public void performVote(long userId, long snippetId, int voteType) {
        voteDao.performVote(userId, snippetId, voteType);
    }

    @Override
    public void withdrawVote(long userId, long snippetId) {
        voteDao.withdrawVote(userId, snippetId);
    }
}

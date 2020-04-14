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
    private VoteDao voteDao;

    @Override
    public Collection<Vote> getUserVotes(final long userId) {
        return voteDao.getUserVotes(userId);
    }

    @Override
    public Optional<Vote> getVote(final long userId, final long snippetId) {
        return this.voteDao.getVote(userId, snippetId);
    }

    @Override
    public void performVote(final long userId, final long snippetId, final int voteType, final int oldVoteType) {
        if (oldVoteType == voteType){
            this.withdrawVote(userId, snippetId);
        } else {
            this.addVote(userId, snippetId, voteType);
        }
    }

    @Override
    public void withdrawVote(final long userId, final long snippetId) {
        this.voteDao.withdrawVote(userId, snippetId);
    }

    @Override
    public void addVote(final long userId, final long snippetId, final int voteType) {
        this.voteDao.addVote(userId, snippetId, voteType);
    }

    @Override
    public Optional<Integer> getVoteCount(long snippetId) {
        return voteDao.getVoteCount(snippetId);
    }
}

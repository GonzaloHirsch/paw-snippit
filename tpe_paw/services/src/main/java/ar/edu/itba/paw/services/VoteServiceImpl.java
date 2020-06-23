package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.VoteDao;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.interfaces.service.VoteService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteDao voteDao;

    @Autowired
    private UserService userService;

    @Override
    public Collection<Vote> getUserVotes(final long userId) {
        return voteDao.getUserVotes(userId);
    }

    @Override
    public Optional<Vote> getVote(final long userId, final long snippetId) {
        return this.voteDao.getVote(userId, snippetId);
    }

    @Override
    public int getVoteWeight(final long userId, final long snippetId) {
        Optional<Vote> vote = this.getVote(userId, snippetId);
        return vote.map(Vote::getVoteWeight).orElse(0);
    }

    @Transactional
    @Override
    public void performVote(final long ownerId, final long userId, final long snippetId, final int voteType, final int oldVoteType) {
        boolean newIsPositive = voteType == 1;
        Optional<User> owner = this.userService.findUserById(ownerId);
        Optional<User> currentUser = this.userService.findUserById(userId);
        if (owner.isPresent() && currentUser.isPresent()){
            int newReputation = owner.get().getReputation();
            // Removing a vote
            if (oldVoteType == voteType){
                newReputation -= voteType;
            }
            // Adding a vote, the new type will always be != 0
            else if (oldVoteType == 0){
                newReputation += voteType;
            }
            // Updating a vote
            else {
                newReputation += voteType - oldVoteType;
            }
            this.userService.changeReputation(ownerId, newReputation);
        }
        if (oldVoteType == voteType){
            this.withdrawVote(userId, snippetId);
        } else {
            this.addVote(userId, snippetId, newIsPositive);
        }
    }

    @Transactional
    @Override
    public void withdrawVote(final long userId, final long snippetId) {
        this.voteDao.withdrawVote(userId, snippetId);
    }

    @Transactional
    @Override
    public void addVote(final long userId, final long snippetId, boolean isPositive) {
        this.voteDao.addVote(userId, snippetId, isPositive);
    }

    @Override
    public int getVoteBalance(final long snippetId) {
        return voteDao.getVoteBalance(snippetId);
    }
}

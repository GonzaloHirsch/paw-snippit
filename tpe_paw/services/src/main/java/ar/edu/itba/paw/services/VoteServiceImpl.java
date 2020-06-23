package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.VoteDao;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.interfaces.service.VoteService;
import ar.edu.itba.paw.models.Snippet;
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
    private SnippetService snippetService;

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

    @Transactional
    @Override
    public void performVote(final long ownerId, final long userId, final long snippetId, boolean voteSelected, boolean isPositive) {
        Optional<User> owner = this.userService.findUserById(ownerId);
        Optional<User> currentUser = this.userService.findUserById(userId);

        if (owner.isPresent() && currentUser.isPresent()){
            int newReputation = owner.get().getReputation();

            if (voteSelected){
                newReputation += this.addVote(userId, snippetId, isPositive);
            } else {
                this.withdrawVote(userId, snippetId);
                newReputation -= isPositive ? POSITIVE_WEIGHT : NEGATIVE_WEIGHT;
            }
            this.userService.changeReputation(ownerId, newReputation);
        }
    }

    @Transactional
    @Override
    public void withdrawVote(final long userId, final long snippetId) {
        this.voteDao.withdrawVote(userId, snippetId);
    }

    @Transactional
    @Override
    public int addVote(final long userId, final long snippetId, boolean isPositive) {
        return this.voteDao.addVote(userId, snippetId, isPositive, POSITIVE_WEIGHT, NEGATIVE_WEIGHT);
    }

    @Override
    public int getVoteBalance(long snippetId) {
        Optional<Snippet> snippet = this.snippetService.findSnippetById(snippetId);
        if(snippet.isPresent()){
            Collection<Vote> votes = snippet.get().getVotes();
            return votes.stream().mapToInt(vote -> vote.isPositive() ? POSITIVE_WEIGHT : NEGATIVE_WEIGHT).sum();
        }
        return 0;
    }
}

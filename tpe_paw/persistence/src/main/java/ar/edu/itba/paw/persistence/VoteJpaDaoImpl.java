package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.VoteDao;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.Vote;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Repository
public class VoteJpaDaoImpl implements VoteDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Collection<Vote> getUserVotes(long userId) {
        User user = this.em.find(User.class, userId);
        if (user != null)
            return user.getVotes();

        return Collections.emptyList();
    }

    @Override
    public Optional<Vote> getVote(long userId, long snippetId) {
        final TypedQuery<Vote> query = this.em.createQuery("from Vote as v where v.user.id = :user_id and v.snippet.id = :snippet_id", Vote.class)
                .setParameter("user_id", userId)
                .setParameter("snippet_id", snippetId);
        return query.getResultList().stream().findFirst();
    }

    /**
     * Method to add or update the vote on a snippet
     * @param userId id of the user that voted
     * @param snippetId snippet which was voted
     * @param isPositive whether the vote was an upvote or a downvote
     * @param positiveVoteWeight number by which the reputation is affected due to an upvote
     * @param negativeVoteWeight number by which the reputation is affected due to a downvote
     * @return the number by which the reputation of the user will be affected due to the voting
     */
    @Override
    public int addVote(long userId, long snippetId, boolean isPositive, int positiveVoteWeight, int negativeVoteWeight) {
        Optional<Vote> maybeVote = this.getVote(userId, snippetId);
        int reputationChange = 0;
        /* Vote will be updated, must update reputation accordingly */
        if (maybeVote.isPresent()) {
            Vote vote = maybeVote.get();
            if (vote.isPositive() && !isPositive) {
                reputationChange = negativeVoteWeight - positiveVoteWeight;
            } else if (!vote.isPositive() && isPositive) {
                reputationChange = positiveVoteWeight - negativeVoteWeight;
            }
            vote.setPositive(isPositive);
            this.em.persist(vote);

        /* Vote will be added, must update reputation accordingly */
        } else {
            User user = this.em.find(User.class, userId);
            Snippet snippet = this.em.find(Snippet.class, snippetId);

            if (user != null && snippet != null) {
                reputationChange = isPositive ? positiveVoteWeight : negativeVoteWeight;
                Vote vote = new Vote(user, snippet, isPositive);
                snippet.getVotes().add(vote);
                user.getVotes().add(vote);
                this.em.persist(vote);
            }
        }
        return reputationChange;
    }

    @Override
    public void withdrawVote(long userId, long snippetId) {
        Optional<Vote> maybeVote = this.getVote(userId, snippetId);
        if (maybeVote.isPresent()) {
            Vote vote = maybeVote.get();
            vote.getUser().getVotes().remove(vote);
            vote.getSnippet().getVotes().remove(vote);
            this.em.remove(vote);
        }
    }
}

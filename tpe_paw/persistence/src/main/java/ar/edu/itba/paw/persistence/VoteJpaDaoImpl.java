package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.dao.VoteDao;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
        if(user != null)
            return user.getVotes();

        return Collections.emptyList();
    }

    @Override
    public Optional<Vote> getVote(long userId, long snippetId) {
        final TypedQuery<Vote> query = this.em.createQuery("from Vote as v where v.user.id = :user_id and v.snippet.id = :snippet_id", Vote.class)
                .setParameter("user_id", userId)
                .setParameter("snippet_id",snippetId);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void addVote(long userId, long snippetId, int voteType) {
        User user = this.em.find(User.class, userId);
        Snippet snippet = this.em.find(Snippet.class, snippetId);

        if(user != null && snippet !=null){
            Vote vote = new Vote(user, snippet, voteType);
            snippet.getVotes().add(vote);
            user.getVotes().add(vote);
            this.em.persist(vote);
        }

    }

    @Override
    public void withdrawVote(long userId, long snippetId) {
        Optional<Vote> maybeVote = this.getVote(userId, snippetId);
        if(maybeVote.isPresent()){
            Vote vote = maybeVote.get();
            vote.getUser().getVotes().remove(vote);
            vote.getSnippet().getVotes().remove(vote);
            this.em.remove(vote);
        }
    }

    @Override
    public Optional<Integer> getVoteBalance(long snippetId) {
        Snippet snippet = this.em.find(Snippet.class, snippetId);
        if(snippet != null){
            Collection<Vote> votes = snippet.getVotes();
            Integer result = votes.stream().mapToInt(Vote::getType).sum();
            return Optional.of(result);
        }

        return Optional.empty();
    }
}

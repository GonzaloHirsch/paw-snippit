package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.FavoriteDao;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class FavoriteJpaDaoImpl implements FavoriteDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public void addToFavorites(long userId, long snippetId) {
        Optional<User> user = Optional.ofNullable(this.em.find(User.class, userId));
        Optional<Snippet> snippet = Optional.ofNullable(this.em.find(Snippet.class, snippetId));

        if (user.isPresent() && snippet.isPresent()) {
            user.get().addFavorite(snippet.get()); //TODO check if okay
            this.em.persist(user.get());
        }
    }
    @Transactional
    @Override
    public void removeFromFavorites(long userId, long snippetId) {
        Optional<User> user = Optional.ofNullable(this.em.find(User.class, userId));
        Optional<Snippet> snippet = Optional.ofNullable(this.em.find(Snippet.class, snippetId));

        if (user.isPresent() && snippet.isPresent()) {
            user.get().removeFavorite(snippet.get()); //TODO check if okay
            this.em.persist(user.get());
        }
    }
}

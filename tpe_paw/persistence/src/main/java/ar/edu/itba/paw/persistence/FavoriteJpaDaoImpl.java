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

    @Override
    public void addToFavorites(long userId, long snippetId) {
        Optional<User> user = Optional.ofNullable(this.em.find(User.class, userId));
        Optional<Snippet> snippet = Optional.ofNullable(this.em.find(Snippet.class, snippetId));

        if (user.isPresent() && snippet.isPresent() && !user.get().getFavorites().contains(snippet.get())) {
            user.get().addFavorite(snippet.get());
            this.em.persist(user.get());
        }
    }

    @Override
    public void removeFromFavorites(long userId, long snippetId) {
        Optional<User> user = Optional.ofNullable(this.em.find(User.class, userId));
        Optional<Snippet> snippet = Optional.ofNullable(this.em.find(Snippet.class, snippetId));

        if (user.isPresent() && snippet.isPresent()) {
            user.get().removeFavorite(snippet.get());
            this.em.persist(user.get());
        }
    }
}

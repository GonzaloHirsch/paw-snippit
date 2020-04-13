package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Favorite;

import java.util.Collection;
import java.util.Optional;

public interface FavoriteDao {

    Optional<Favorite> getFavorite(long userId, long snippetId);

    Collection<Favorite> getUserFavorites(long userId);

    void addToFavorites(long userId, long snippetId);

    void removeFromFavorites(long userId, long snippetId);
}
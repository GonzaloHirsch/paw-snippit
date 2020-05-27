package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Favorite;

import java.util.Collection;
import java.util.Optional;

public interface FavoriteDao {

    void addToFavorites(long userId, long snippetId);

    void removeFromFavorites(long userId, long snippetId);
}
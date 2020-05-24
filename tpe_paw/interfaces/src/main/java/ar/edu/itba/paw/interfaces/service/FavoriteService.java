package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Favorite;

import java.util.Collection;
import java.util.Optional;

public interface FavoriteService {

    void addToFavorites(long userId, long snippetId);

    void removeFromFavorites(long userId, long snippetId);

    void updateFavorites(long userId, long snippetId, boolean isFav);
}


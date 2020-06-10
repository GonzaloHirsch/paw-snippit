package ar.edu.itba.paw.interfaces.service;


public interface FavoriteService {

    void addToFavorites(long userId, long snippetId);

    void removeFromFavorites(long userId, long snippetId);

    void updateFavorites(long userId, long snippetId, boolean isFav);
}


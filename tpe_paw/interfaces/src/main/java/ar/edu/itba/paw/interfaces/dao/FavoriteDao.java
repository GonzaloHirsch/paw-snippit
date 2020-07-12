package ar.edu.itba.paw.interfaces.dao;

public interface FavoriteDao {

    void addToFavorites(long userId, long snippetId);

    void removeFromFavorites(long userId, long snippetId);
}
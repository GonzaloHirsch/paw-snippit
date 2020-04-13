package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.FavoriteDao;
import ar.edu.itba.paw.interfaces.service.FavoriteService;
import ar.edu.itba.paw.models.Favorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    FavoriteDao favDao;

    @Override
    public Collection<Favorite> getUserFavorites(long userId) {
        return favDao.getUserFavorites(userId);
    }

    @Override
    public Optional<Favorite> getFavorite(long userId, long snippetId) {
        return favDao.getFavorite(userId, snippetId);
    }

    @Override
    public void addToFavorites(long userId, long snippetId) {
        favDao.addToFavorites(userId, snippetId);
    }

    @Override
    public void removeFromFavorites(long userId, long snippetId) {
        favDao.removeFromFavorites(userId, snippetId);
    }

}


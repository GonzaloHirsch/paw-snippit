package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.FavoriteDao;
import ar.edu.itba.paw.interfaces.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    FavoriteDao favDao;

    @Override
    public void addToFavorites(long userId, long snippetId) {
        favDao.addToFavorites(userId, snippetId);
    }

    @Override
    public void removeFromFavorites(long userId, long snippetId) {
        favDao.removeFromFavorites(userId, snippetId);
    }

    @Override
    @Transactional
    public void updateFavorites(long userId, long snippetId, boolean isFav) {
        if (isFav) {
            addToFavorites(userId, snippetId);
        } else {
            removeFromFavorites(userId, snippetId);
        }
    }

}


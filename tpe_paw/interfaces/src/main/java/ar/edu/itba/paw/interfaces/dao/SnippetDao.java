package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Snippet;

import java.util.Collection;
import java.util.Optional;

public interface SnippetDao {
    enum Locations {
        HOME,
        FAVORITES,
        FOLLOWING
    }
    enum Types {
        TAG,
        CONTENT,
        TITLE
    }
    enum Orders {
        NO,
        ASC,
        DESC
    }

    Collection<Snippet> findSnippetByCriteria(SnippetDao.Types type, String term, SnippetDao.Locations location, SnippetDao.Orders order, Long userId);
    Collection<Snippet> getAllSnippets();
    Optional<Snippet> findSnippetById(long id);
}

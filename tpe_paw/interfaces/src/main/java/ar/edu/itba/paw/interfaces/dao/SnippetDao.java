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
        ALL,
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
    Collection<Snippet> getAllFavoriteSnippets(Long userId);
    Collection<Snippet> getAllFollowingSnippets(Long userId);
    Collection<Snippet> findAllSnippetsByOwner(final long userId);
    Optional<Snippet> findSnippetById(long id);
    Collection<Snippet> findSnippetsForTag(long tagId);
}

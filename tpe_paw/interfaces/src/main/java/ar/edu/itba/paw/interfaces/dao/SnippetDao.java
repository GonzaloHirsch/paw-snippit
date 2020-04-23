package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;

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
    Collection<Snippet> getAllFavoriteSnippets(Long userId);
    Collection<Snippet> getAllFollowingSnippets(Long userId);
    Optional<Snippet> findSnippetById(long id);
    Optional<Snippet> createSnippet(User owner, String title, String description, String code, String dateCreated, Long language);
    Collection<Snippet> findSnippetsForTag(long tagId);
}

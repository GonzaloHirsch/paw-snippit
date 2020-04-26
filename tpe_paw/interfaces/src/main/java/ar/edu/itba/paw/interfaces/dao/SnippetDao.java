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
    enum QueryTypes {
        SEARCH,
        COUNT
    }

    Collection<Snippet> findSnippetByCriteria(QueryTypes queryType, SnippetDao.Types type, String term, SnippetDao.Locations location, SnippetDao.Orders order, Long userId, int page, int pageSize);
    Collection<Snippet> getAllSnippets(int page, int pageSize);
    Collection<Snippet> getAllFavoriteSnippets(Long userId);
    Collection<Snippet> getAllFollowingSnippets(Long userId);
    Collection<Snippet> findAllSnippetsByOwner(final long userId);
    Optional<Snippet> findSnippetById(long id);
    Optional<Snippet> createSnippet(User owner, String title, String description, String code, String dateCreated, Long language);
    Collection<Snippet> findSnippetsForTag(long tagId);
    int getAllSnippetsCount();
    int getSnippetByCriteriaCount(QueryTypes queryType, SnippetDao.Types type, String term, SnippetDao.Locations location, Long userId);
}

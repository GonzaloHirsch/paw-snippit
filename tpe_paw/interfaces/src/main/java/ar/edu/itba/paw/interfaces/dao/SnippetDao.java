package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;

import java.util.Calendar;
import java.util.Collection;
import java.util.Optional;

public interface SnippetDao {
    int PAGE_SIZE = 6;
    enum Locations {
        HOME,
        FAVORITES,
        FOLLOWING,
        UPVOTED
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

    Collection<Snippet> findSnippetByCriteria(QueryTypes queryType, SnippetDao.Types type, String term, SnippetDao.Locations location, SnippetDao.Orders order, Long userId, int page);
    Collection<Snippet> findSnippetByDeepCriteria(String dateMin, String dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, String order, String sort, int page);
    Collection<Snippet> getAllSnippets(int page);
    Collection<Snippet> getAllFavoriteSnippets(final long userId, int page);
    Collection<Snippet> getAllFollowingSnippets(final long userId, int page);
    Collection<Snippet> getAllUpVotedSnippets(final long userId, int page);
    Collection<Snippet> getAllFlaggedSnippets(int page);
    Collection<Snippet> findAllSnippetsByOwner(final long userId, int page);
    Optional<Snippet> findSnippetById(long id);
    Long createSnippet(User owner, String title, String description, String code, String dateCreated, Long language);
    Collection<Snippet> findSnippetsForTag(long tagId);
    void flagSnippet(long snippetId);
    void unflagSnippet(long snippetId);
    int getPageSize();
    int getAllSnippetsCount();
    int getAllFavoriteSnippetsCount(final long userId);
    int getAllFollowingSnippetsCount(final long userId);
    int getAllUpvotedSnippetsCount(final long userId);
    int getAllFlaggedSnippetsCount();
    int getAllSnippetsByOwnerCount(final long userId);
    int getAllSnippetsByTagCount(final long tagId);
    int getSnippetByCriteriaCount(QueryTypes queryType, SnippetDao.Types type, String term, SnippetDao.Locations location, Long userId);
    int getSnippetByDeepCriteriaCount(String dateMin, String dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, String order, String sort);
}

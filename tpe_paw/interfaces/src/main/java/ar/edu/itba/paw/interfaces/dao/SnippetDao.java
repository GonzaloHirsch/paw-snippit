package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;

import java.util.Collection;
import java.util.Optional;

public interface SnippetDao {
    enum Locations {
        HOME,
        FAVORITES,
        FOLLOWING,
        UPVOTED,
        FLAGGED,
        LANGUAGES,
        TAGS,
        USER
    }
    enum Types {
        ALL,
        TAG,
        CONTENT,
        TITLE,
        USER,
        LANGUAGE
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

    Collection<Snippet> findSnippetByCriteria(QueryTypes queryType, SnippetDao.Types type, String term, SnippetDao.Locations location, SnippetDao.Orders order, Long userId, Long resourceId, int page, int pageSize);
    Collection<Snippet> findSnippetByDeepCriteria(String dateMin, String dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, String order, String sort, Boolean includeFlagged, int page, int pageSize);
    Collection<Snippet> getAllSnippets(int page, int pageSize);
    Collection<Snippet> getAllFavoriteSnippets(final long userId, int page, int pageSize);
    Collection<Snippet> getAllFollowingSnippets(final long userId, int page, int pageSize);
    Collection<Snippet> getAllUpVotedSnippets(final long userId, int page, int pageSize);
    Collection<Snippet> getAllFlaggedSnippets(int page, int pageSize);
    Collection<Snippet> findAllSnippetsByOwner(final long userId, int page, int pageSize);
    Collection<Snippet> findSnippetsWithLanguage(final long langId, int page, int pageSize);
    Optional<Snippet> findSnippetById(final long id);
    boolean deleteSnippetById(final long id);
    int getNewSnippetsForTagsCount(String dateMin, Collection<Tag> tags, long userId);
    Long createSnippet(long ownerId, String title, String description,String code, String dateCreated, Long languageId);
    Collection<Snippet> findSnippetsForTag(long tagId);
    void flagSnippet(long snippetId);
    void unflagSnippet(long snippetId);
    int getAllSnippetsCount();
    int getAllFavoriteSnippetsCount(final long userId);
    int getAllFollowingSnippetsCount(final long userId);
    int getAllUpvotedSnippetsCount(final long userId);
    int getAllFlaggedSnippetsCount();
    int getAllSnippetsByOwnerCount(final long userId);
    int getAllSnippetsByTagCount(final long tagId);
    int getAllSnippetsByLanguageCount(final long langId);
    int getSnippetByCriteriaCount(QueryTypes queryType, SnippetDao.Types type, String term, SnippetDao.Locations location, Long userId, Long resourceId);
    int getSnippetByDeepCriteriaCount(String dateMin, String dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, String order, String sort, Boolean includeFlagged);
}

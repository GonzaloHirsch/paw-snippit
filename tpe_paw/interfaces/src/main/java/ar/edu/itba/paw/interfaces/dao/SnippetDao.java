package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;

import java.sql.Timestamp;
import java.time.Instant;
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
        USER,
        DELETED
    }
    enum Types {
        ALL,
        TAG,
        CONTENT,
        TITLE,
        USER,
        LANGUAGE,
        REPUTATION,
        VOTES,
        DATE
    }
    enum Orders {
        NO,
        ASC,
        DESC
    }

    Optional<Snippet> findSnippetById(final long id);
    boolean deleteSnippetById(final long id);
    boolean restoreSnippetById(final long id);
    Long createSnippet(long ownerId, String title, String description, String code, Instant dateCreated, Long languageId, Collection<String> tags);
    boolean unflagSnippet(long snippetId);
    boolean flagSnippet(long snippetId);
    int getAllSnippetsCount();
    int getAllFlaggedSnippetsCount();
    int getAllDeletedSnippetsByOwnerCount(final long userId);
    int getAllSnippetsByLanguageCount(final long langId);
    int getAllFollowingSnippetsCount(final long userId);
    int getAllFavoriteSnippetsCount(final long userId);
    int getAllSnippetsByOwnerCount(final long userId);
    int getAllSnippetsByTagCount(final long tagId);
    int getNewSnippetsForTagsCount(Instant dateMin, Collection<Tag> tags, long userId);
    int getAllUpvotedSnippetsCount(final long userId);

    /* Collection Methods */
    Collection<Snippet> getAllSnippets(int page, int pageSize);
    Collection<Snippet> getAllFavoriteSnippets(final long userId, int page, int pageSize);
    Collection<Snippet> getSnippetsWithLanguage(final long langId, int page, int pageSize);
    Collection<Snippet> getAllDeletedSnippetsByOwner(final long userId, int page, int pageSize);
    Collection<Snippet> getAllSnippetsByOwner(final long userId, int page, int pageSize);
    Collection<Snippet> getAllFlaggedSnippets(int page, int pageSize);
    Collection<Snippet> getAllUpVotedSnippets(final long userId, int page, int pageSize);
    Collection<Snippet> getAllFollowingSnippets(final long userId, int page, int pageSize);
    Collection<Snippet> findSnippetsForTag(long tagId, int page, int pageSize);
    Collection<Snippet> findSnippetByCriteria(SnippetDao.Types type, String term, SnippetDao.Locations location, SnippetDao.Orders order, Long userId, Long resourceId, int page, int pageSize);

    Collection<Snippet> findSnippetByDeepCriteria(Instant dateMin, Instant dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, SnippetDao.Orders order, SnippetDao.Types type, Boolean includeFlagged, int page, int pageSize);
    int getSnippetByCriteriaCount(SnippetDao.Types type, String term, SnippetDao.Locations location, Long userId, Long resourceId);
    int getSnippetByDeepCriteriaCount(Instant dateMin, Instant dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, Boolean includeFlagged);
}

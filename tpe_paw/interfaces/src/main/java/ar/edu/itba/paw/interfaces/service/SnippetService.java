package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

public interface SnippetService {
    int FLAGGED_SNIPPET_REP_VALUE = 10;

    Collection<Snippet> getAllSnippets(int page, int pageSize);
    Collection<Snippet> getAllFavoriteSnippets(final long userId, int page, int pageSize);
    Collection<Snippet> getAllFollowingSnippets(final long userId, int page, int pageSize);
    Collection<Snippet> getAllUpVotedSnippets(final long userId, int page, int pageSize);
    Collection<Snippet> getAllFlaggedSnippets(int page, int pageSize);
    Optional<Snippet> findSnippetById(long id);
    Collection<Snippet> getAllSnippetsByOwner(final long userId, int page, int pageSize);
    Collection<Snippet> getAllDeletedSnippetsByOwner(final long userId, int page, int pageSize);
    Collection<Snippet> findSnippetByCriteria(SnippetDao.Types type, String term, SnippetDao.Locations location, SnippetDao.Orders order, Long userId, Long resourceId, int page, int pageSize);
    Collection<Snippet> findSnippetsForTag(long tagId, int page, int pageSize);
    Collection<Snippet> getSnippetsWithLanguage(long langId, int page, int pageSize);
    boolean deleteOrRestoreSnippet(final Snippet snippet, final long userId, boolean delete);
    void updateFlagged(final Snippet snippet, final User owner, boolean isFlagged, final String baseUrl);
    int getNewSnippetsForTagsCount(Instant dateMin, Collection<Tag> tags, long userId);
    Collection<Snippet> findSnippetByDeepCriteria(Instant dateMin, Instant dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, SnippetDao.Orders order, SnippetDao.Types type, Boolean includeFlagged, int page, int pageSize);
    int getAllSnippetsCount();
    int getAllFavoriteSnippetsCount(final long userId);
    int getAllFollowingSnippetsCount(final long userId);
    int getAllUpvotedSnippetsCount(final long userId);
    int getAllFlaggedSnippetsCount();
    int getAllSnippetsByOwnerCount(final long userId);
    int getAllDeletedSnippetsByOwnerCount(final long userId);
    int getAllSnippetsByTagCount(final long tagId);
    int getAllSnippetsByLanguageCount(final long langId);
    int getSnippetByCriteriaCount(SnippetDao.Types type, String term, SnippetDao.Locations location, Long userId, Long resourceId);
    int getSnippetByDeepCriteriaCount(Instant dateMin, Instant dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, Boolean includeFlagged);
    Long createSnippet(User owner, String title, String description, String code, Instant dateCreated, Long language, Collection<String> tags);
    boolean reportSnippet(Long userId, Long snippetId, String reportDetail);

}

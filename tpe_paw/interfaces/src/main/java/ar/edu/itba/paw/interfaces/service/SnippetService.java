package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;

import java.util.Calendar;
import java.util.Collection;
import java.util.Optional;

public interface SnippetService {
    Collection<Snippet> getAllSnippets(int page, int pageSize);
    Collection<Snippet> getAllFavoriteSnippets(final long userId, int page, int pageSize);
    Collection<Snippet> getAllFollowingSnippets(final long userId, int page, int pageSize);
    Collection<Snippet> getAllUpVotedSnippets(final long userId, int page, int pageSize);
    Collection<Snippet> getAllFlaggedSnippets(int page, int pageSize);
    Optional<Snippet> findSnippetById(long id);
    Collection<Snippet> findAllSnippetsByOwner(final long userId, int page, int pageSize);
    Collection<Snippet> findSnippetByCriteria(SnippetDao.Types type, String term, SnippetDao.Locations location, SnippetDao.Orders order, Long userId, Long resourceId, int page, int pageSize);
    Collection<Snippet> findSnippetsForTag(long tagId);
    Collection<Snippet> findSnippetsWithLanguage(long langId, int page, int pageSize);
    int getReputationImportanceBalance(final Snippet snippet);
    boolean isFlaggedByAdmin(final Snippet snippet);
    boolean deleteSnippetById(final long id);
    void updateFlagged(long snippetId, long userId, boolean isFlagged);
    int getNewSnippetsForTagsCount(String dateMin, Collection<Tag> tags, long userId);
    Collection<Snippet> findSnippetByDeepCriteria(String dateMin, String dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, String order, String sort, Boolean includeFlagged, int page, int pageSize);
    int getAllSnippetsCount();
    int getAllFavoriteSnippetsCount(final long userId);
    int getAllFollowingSnippetsCount(final long userId);
    int getAllUpvotedSnippetsCount(final long userId);
    int getAllFlaggedSnippetsCount();
    int getAllSnippetsByOwnerCount(final long userId);
    int getAllSnippetsByTagCount(final long tagId);
    int getAllSnippetsByLanguageCount(final long langId);
    int getSnippetByCriteriaCount(SnippetDao.Types type, String term, SnippetDao.Locations location, Long userId, Long resourceId);
    int getSnippetByDeepCriteriaCount(String dateMin, String dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, String order, String sort, Boolean includeFlagged);
    Long createSnippet(User owner, String title, String description, String code, String dateCreated, Long language, Collection<String> tags);
}

package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;

import java.util.Calendar;
import java.util.Collection;
import java.util.Optional;

public interface SnippetService {
    int getPageSize();
    Collection<Snippet> getAllSnippets(int page);
    Collection<Snippet> getAllFavoriteSnippets(final long userId, int page);
    Collection<Snippet> getAllFollowingSnippets(final long userId, int page);
    Collection<Snippet> getAllUpVotedSnippets(final long userId, int page);
    Collection<Snippet> getAllFlaggedSnippets(int page);
    Optional<Snippet> findSnippetById(long id);
    Collection<Snippet> findAllSnippetsByOwner(final long userId, int page);
    Collection<Snippet> findSnippetByCriteria(SnippetDao.Types type, String term, SnippetDao.Locations location, SnippetDao.Orders order, Long userId, int page);
    Collection<Snippet> findSnippetsForTag(long tagId);
    Collection<Snippet> findSnippetByDeepCriteria(String dateMin, String dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, String order, String sort, int page);
    boolean isFlaggedByAdmin(final Snippet snippet);
    void updateFlagged(long snippetId, long userId, boolean isFlagged);
    int getAllSnippetsCount();
    int getAllFavoriteSnippetsCount(final long userId);
    int getAllFollowingSnippetsCount(final long userId);
    int getAllUpvotedSnippetsCount(final long userId);
    int getAllFlaggedSnippetsCount();
    int getAllSnippetsByOwnerCount(final long userId);
    int getAllSnippetsByTagCount(final long tagId);
    int getSnippetByCriteriaCount(SnippetDao.Types type, String term, SnippetDao.Locations location, Long userId);
    int getSnippetByDeepCriteriaCount(String dateMin, String dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, String order, String sort);
    Long createSnippet(User owner, String title, String description, String code, String dateCreated, Long language, Collection<Long> tags);
}

package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.models.Snippet;

import java.util.Collection;
import java.util.Optional;

public interface SnippetService {
    Collection<Snippet> getAllSnippets(int page, int pageSize);
    Collection<Snippet> getAllFavoriteSnippets(Long userId);
    Collection<Snippet> getAllFollowingSnippets(Long userId);
    Optional<Snippet> findSnippetById(long id);
    Collection<Snippet> findAllSnippetsByOwner(final long userId);
    Collection<Snippet> findSnippetByCriteria(SnippetDao.Types type, String term, SnippetDao.Locations location, SnippetDao.Orders order, Long userId, int page, int pageSize);
    Collection<Snippet> findSnippetsForTag(long tagId);
    int getAllSnippetsCount();
    int getSnippetByCriteriaCount(SnippetDao.Types type, String term, SnippetDao.Locations location, Long userId);
}

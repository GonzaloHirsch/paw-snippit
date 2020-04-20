package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;

import java.util.Collection;
import java.util.Optional;

public interface SnippetService {
    Collection<Snippet> getAllSnippets();
    Collection<Snippet> getAllFavoriteSnippets(Long userId);
    Collection<Snippet> getAllFollowingSnippets(Long userId);
    Optional<Snippet> findSnippetById(long id);
    Collection<Snippet> findSnippetByCriteria(SnippetDao.Types type, String term, SnippetDao.Locations location, SnippetDao.Orders order, Long userId);
    Optional<Snippet> createSnippet(User owner, String title, String description, String code, String dateCreated, String language, Collection<Tag> tags);
    Collection<Snippet> findSnippetsForTag(long tagId);
}

package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.models.Snippet;

import java.util.Collection;
import java.util.Optional;

public interface SnippetService {
    Collection<Snippet> getAllSnippets();
    Optional<Snippet> findSnippetById(long id);
    Collection<Snippet> findSnippetByCriteria(SnippetDao.Types type, String term, SnippetDao.Locations location, SnippetDao.Orders order, Long userId);
//    Collection<Snippet> findSnippetsByTag(String tag, String source, String userId);
//    Collection<Snippet> findSnippetsByTitle(String title, String source, String userId);
//    Collection<Snippet> findSnippetsByContent(String content, String source, String userId);
}

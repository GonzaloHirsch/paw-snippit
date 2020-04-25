package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.dao.TagDao;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.models.Snippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class SnippetServiceImpl implements SnippetService {

    @Autowired
    private SnippetDao snippetDao;

    @Autowired
    private TagDao tagDao;

    @Override
    public Collection<Snippet> findSnippetByCriteria(SnippetDao.Types type, String term, SnippetDao.Locations location, SnippetDao.Orders order, Long userId, int page, int pageSize) {
        return this.snippetDao.findSnippetByCriteria(SnippetDao.QueryTypes.SEARCH, type, term, location, order, userId, page, pageSize);
    }

    @Override
    public Collection<Snippet> findSnippetsForTag(long tagId) {
        return snippetDao.findSnippetsForTag(tagId);
    }

    @Override
    public Optional<Snippet> findSnippetById(long id) {
        return snippetDao.findSnippetById(id);
    }

    @Override
    public Collection<Snippet> findAllSnippetsByOwner(final long userId) {
        return this.snippetDao.findAllSnippetsByOwner(userId);
    }

    @Override
    public Collection<Snippet> getAllSnippets(int page, int pageSize) { return snippetDao.getAllSnippets(page, pageSize); }

    @Override
    public int getAllSnippetsCount() {
        return this.snippetDao.getAllSnippetsCount();
    }

    @Override
    public int getSnippetByCriteriaCount(SnippetDao.Types type, String term, SnippetDao.Locations location, Long userId) {
        return this.snippetDao.getSnippetByCriteriaCount(SnippetDao.QueryTypes.COUNT, type, term, location, userId);
    }

    @Override
    public Collection<Snippet> getAllFavoriteSnippets(Long userId) {
        return snippetDao.getAllFavoriteSnippets(userId);
    }

    @Override
    public Collection<Snippet> getAllFollowingSnippets(Long userId) {
        return snippetDao.getAllFollowingSnippets(userId);
    }
}

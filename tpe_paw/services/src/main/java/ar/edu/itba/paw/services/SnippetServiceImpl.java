package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class SnippetServiceImpl implements SnippetService {

    @Autowired
    private SnippetDao snippetDao;

    @Override
    public Collection<Snippet> findSnippetByCriteria(SnippetDao.Types type, String term, SnippetDao.Locations location, SnippetDao.Orders order, Long userId) {
        return this.snippetDao.findSnippetByCriteria(type, term, location, order, userId);
    }
    @Override
    public Optional<Snippet> findSnippetById(long id) { return snippetDao.findSnippetById(id);}

    @Override
    public Collection<Snippet> findAllSnippetsByOwner(final long userId) {
        return this.snippetDao.findAllSnippetsByOwner(userId);
    }

    @Override
    public Collection<Snippet> getAllSnippets() { return snippetDao.getAllSnippets(); }

    @Override
    public Collection<Snippet> getAllFavoriteSnippets(Long userId) {
        return snippetDao.getAllFavoriteSnippets(userId);
    }

    @Override
    public Collection<Snippet> getAllFollowingSnippets(Long userId) {
        return snippetDao.getAllFollowingSnippets(userId);
    }
}

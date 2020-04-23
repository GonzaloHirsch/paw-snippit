package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.dao.TagDao;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class SnippetServiceImpl implements SnippetService {

    @Autowired
    private SnippetDao snippetDao;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private TagService tagService;

    @Override
    public Collection<Snippet> findSnippetByCriteria(SnippetDao.Types type, String term, SnippetDao.Locations location, SnippetDao.Orders order, Long userId) {
        return this.snippetDao.findSnippetByCriteria(type, term, location, order, userId);
    }

    @Override
    public Collection<Snippet> findSnippetsForTag(long tagId) {
        return snippetDao.findSnippetsForTag(tagId);
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

    @Override
    public Optional<Snippet> createSnippet(User owner, String title, String description, String code, String dateCreated, Long language, Collection<Long> tags) {
        Optional<Snippet> snippet =  snippetDao.createSnippet(owner,title,description,code,dateCreated,language);
        //TODO: See if its better to call TagService instead of TagDao directly.
        snippet.ifPresent(s ->
                s.setTags(tagService.addTagsToSnippet(snippet.get().getId(), tags)));

        return snippet;
    }
}

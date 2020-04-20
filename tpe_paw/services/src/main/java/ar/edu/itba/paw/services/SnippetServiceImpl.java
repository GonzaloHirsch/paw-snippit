package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.dao.TagDao;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
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
    public Optional<Snippet> createSnippet(User owner, String title, String description, String code, String dateCreated, String language, Collection<Tag> tags) {
        Optional<Snippet> snippet =  snippetDao.createSnippet(owner,title,description,code,dateCreated,language,tags);

        //Add the tags to the snippet_tag table
        //TODO: See if its better to call TagService instead of TagDao directly.
        if(snippet.isPresent()){
            for(Tag tag : tags) {
                tagDao.addSnippetTag(snippet.get(), tag);
            }
        }

        return snippet;
    }
}

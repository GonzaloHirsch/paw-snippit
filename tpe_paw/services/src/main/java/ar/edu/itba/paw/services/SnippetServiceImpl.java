package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
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

//    @Override
//    public Collection<Snippet> findSnippetsByTag(String tag, String source, String userId) {
//        return this.snippetDao.findSnippetsByTag(tag, source, userId);
//    }
//
//    @Override
//    public Collection<Snippet> findSnippetsByTitle(String title, String source, String userId) {
//        return this.snippetDao.findSnippetsByTitle(title, source, userId);
//    }
//
//    @Override
//    public Collection<Snippet> findSnippetsByContent(String content, String source, String userId) {
//        return this.snippetDao.findSnippetsByContent(content, source, userId);
//    }


    @Override
    public Optional<Snippet> getSnippetById(long id) { return snippetDao.getSnippetById(id);}

    @Override
    public Collection<Snippet> findSnippetByCriteria(SnippetDao.Types type, String term, SnippetDao.Locations location, SnippetDao.Orders order, Long userId) {
        return this.snippetDao.findSnippetByCriteria(type, term, location, order, userId);
    }
}

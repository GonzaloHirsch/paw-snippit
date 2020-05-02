package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.dao.TagDao;
import ar.edu.itba.paw.interfaces.service.SnippetService;
import ar.edu.itba.paw.interfaces.service.TagService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Collection;
import java.util.Optional;

@Service
public class SnippetServiceImpl implements SnippetService {

    @Autowired
    private SnippetDao snippetDao;

    @Autowired
    private UserService userService;

    @Autowired
    private TagService tagService;

    @Override
    public Collection<Snippet> findSnippetByCriteria(SnippetDao.Types type, String term, SnippetDao.Locations location, SnippetDao.Orders order, Long userId, int page) {
        return this.snippetDao.findSnippetByCriteria(SnippetDao.QueryTypes.SEARCH, type, term, location, order, userId, page);
    }

    @Override
    public Collection<Snippet> findSnippetsForTag(long tagId) {
        return this.snippetDao.findSnippetsForTag(tagId);
    }

    @Override
    public Collection<Snippet> findSnippetByDeepCriteria(String dateMin, String dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, String order, String sort, int page) {
        return this.snippetDao.findSnippetByDeepCriteria(dateMin, dateMax, repMin, repMax, voteMin, voteMax, languageId, tagId, title, username, order, sort, page);
    }

    @Override
    public Collection<Snippet> findSnippetsWithLanguage(long langId, int page) {
        return this.snippetDao.findSnippetsWithLanguage(langId, page);
    }

    @Override
    public boolean isFlaggedByAdmin(final Snippet snippet) {
        return snippet.isFlagged();
    }

    @Override
    public Optional<Snippet> findSnippetById(long id) { return this.snippetDao.findSnippetById(id);}

    @Override
    public Collection<Snippet> findAllSnippetsByOwner(final long userId, int page) {
        return this.snippetDao.findAllSnippetsByOwner(userId, page);
    }

    @Override
    public int getPageSize() {
        return this.snippetDao.getPageSize();
    }

    @Override
    public Collection<Snippet> getAllSnippets(int page) { return this.snippetDao.getAllSnippets(page); }

    @Override
    public int getAllSnippetsCount() {
        return this.snippetDao.getAllSnippetsCount();
    }

    @Override
    public int getAllFavoriteSnippetsCount(long userId) {
        return this.snippetDao.getAllFavoriteSnippetsCount(userId);
    }

    @Override
    public int getAllFollowingSnippetsCount(long userId) {
        return this.snippetDao.getAllFollowingSnippetsCount(userId);
    }

    @Override
    public int getAllUpvotedSnippetsCount(long userId) {
        return snippetDao.getAllUpvotedSnippetsCount(userId);
    }

    @Override
    public int getAllFlaggedSnippetsCount() {
        return snippetDao.getAllFlaggedSnippetsCount();
    }

    @Override
    public int getAllSnippetsByOwnerCount(long userId) {
        return this.snippetDao.getAllSnippetsByOwnerCount(userId);
    }

    @Override
    public int getAllSnippetsByTagCount(long tagId) {
        return this.snippetDao.getAllSnippetsByTagCount(tagId);
    }

    @Override
    public int getAllSnippetsByLanguageCount(long langId) {
        return this.snippetDao.getAllSnippetsByLanguageCount(langId);
    }

    @Override
    public int getSnippetByCriteriaCount(SnippetDao.Types type, String term, SnippetDao.Locations location, Long userId) {
        return this.snippetDao.getSnippetByCriteriaCount(SnippetDao.QueryTypes.COUNT, type, term, location, userId);
    }

    @Override
    public int getSnippetByDeepCriteriaCount(String dateMin, String dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, String order, String sort) {
        return this.snippetDao.getSnippetByDeepCriteriaCount(dateMin, dateMax, repMin, repMax, voteMin, voteMax, languageId, tagId, title, username, order, sort);
    }

    @Override
    public Collection<Snippet> getAllFavoriteSnippets(final long userId, int page) {
        return this.snippetDao.getAllFavoriteSnippets(userId, page);
    }

    @Override
    public Collection<Snippet> getAllFollowingSnippets(final long userId, int page) {
        return this.snippetDao.getAllFollowingSnippets(userId, page);
    }

    @Override
    public Collection<Snippet> getAllUpVotedSnippets(long userId, int page) {
        return this.snippetDao.getAllUpVotedSnippets(userId, page);
    }

    @Override
    public Collection<Snippet> getAllFlaggedSnippets(int page) {
        return this.snippetDao.getAllFlaggedSnippets(page);
    }

    @Override
    public Long createSnippet(User owner, String title, String description, String code, String dateCreated, Long language, Collection<Long> tags) {
        Long snippetId =  snippetDao.createSnippet(owner,title,description,code,dateCreated,language);
        //TODO: See if its better to call TagService instead of TagDao directly.
        if(snippetId != null)
            this.tagService.addTagsToSnippet(snippetId, tags);

        return snippetId;
    }

    @Override
    public void updateFlagged(long snippetId, long userId, boolean isFlagged) {
        if (isFlagged) {
            this.snippetDao.flagSnippet(snippetId);
            this.userService.changeReputationForFlaggedSnippet(userId, false);
        } else {
            this.snippetDao.unflagSnippet(snippetId);
            this.userService.changeReputationForFlaggedSnippet(userId, true);
        }
    }
}

package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
public class SnippetServiceImpl implements SnippetService {

    @Autowired private SnippetDao snippetDao;
    @Autowired private UserService userService;
    @Autowired private TagService tagService;
    @Autowired private VoteService voteService;
    @Autowired private EmailService emailService;

    @Override
    public Collection<Snippet> findSnippetByCriteria(SnippetDao.Types type, String term, SnippetDao.Locations location, SnippetDao.Orders order, Long userId, Long resourceId, int page, int pageSize) {
        return this.snippetDao.findSnippetByCriteria(SnippetDao.QueryTypes.SEARCH, type, term, location, order, userId, resourceId, page, pageSize);
    }

    @Override
    public Collection<Snippet> findSnippetsForTag(long tagId) {
        return this.snippetDao.findSnippetsForTag(tagId);
    }

    @Override
    public int getNewSnippetsForTagsCount(String dateMin, Collection<Tag> tags, long userId) {
        return this.snippetDao.getNewSnippetsForTagsCount(dateMin, tags, userId);
    }

    @Override
    public Collection<Snippet> findSnippetByDeepCriteria(String dateMin, String dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, String order, String sort, Boolean includeFlagged, int page, int pageSize) {
        return this.snippetDao.findSnippetByDeepCriteria(dateMin, dateMax, repMin, repMax, voteMin, voteMax, languageId, tagId, title, username, order, sort, includeFlagged, page, pageSize);
    }

    @Override
    public Collection<Snippet> findSnippetsWithLanguage(long langId, int page, int pageSize) {
        return this.snippetDao.findSnippetsWithLanguage(langId, page, pageSize);
    }

    @Override
    public int getReputationImportanceBalance(Snippet snippet) {
        int voteBalance = (-1) * this.voteService.getVoteBalance(snippet.getId()).orElse(0);
        voteBalance += this.isFlaggedByAdmin(snippet) ? FLAGGED_SNIPPET_REP_VALUE : 0;
        return voteBalance;
    }

    @Override
    public boolean isFlaggedByAdmin(final Snippet snippet) {
        return snippet.isFlagged();
    }

    @Override
    public boolean deleteSnippet(Snippet snippet, long userId) {
        int voteBalance = this.getReputationImportanceBalance(snippet);
        boolean success = snippetDao.deleteSnippetById(snippet.getId());
        if (success) {
            this.userService.changeReputation(userId, voteBalance);
        }
        return success;
    }

    @Override
    public Optional<Snippet> findSnippetById(long id) { return this.snippetDao.findSnippetById(id);}

    @Override
    public Collection<Snippet> findAllSnippetsByOwner(final long userId, int page, int pageSize) {
        return this.snippetDao.findAllSnippetsByOwner(userId, page, pageSize);
    }

    @Override
    public Collection<Snippet> getAllSnippets(int page, int pageSize) { return this.snippetDao.getAllSnippets(page, pageSize); }

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
    public int getSnippetByCriteriaCount(SnippetDao.Types type, String term, SnippetDao.Locations location, Long userId, Long resourceId) {
        return this.snippetDao.getSnippetByCriteriaCount(SnippetDao.QueryTypes.COUNT, type, term, location, userId, resourceId);
    }

    @Override
    public int getSnippetByDeepCriteriaCount(String dateMin, String dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, String order, String sort, Boolean includeFlagged) {
        return this.snippetDao.getSnippetByDeepCriteriaCount(dateMin, dateMax, repMin, repMax, voteMin, voteMax, languageId, tagId, title, username, order, sort, includeFlagged);
    }

    @Override
    public Collection<Snippet> getAllFavoriteSnippets(final long userId, int page, int pageSize) {
        return this.snippetDao.getAllFavoriteSnippets(userId, page, pageSize);
    }

    @Override
    public Collection<Snippet> getAllFollowingSnippets(final long userId, int page, int pageSize) {
        return this.snippetDao.getAllFollowingSnippets(userId, page, pageSize);
    }

    @Override
    public Collection<Snippet> getAllUpVotedSnippets(long userId, int page, int pageSize) {
        return this.snippetDao.getAllUpVotedSnippets(userId, page, pageSize);
    }

    @Override
    public Collection<Snippet> getAllFlaggedSnippets(int page, int pageSize) {
        return this.snippetDao.getAllFlaggedSnippets(page, pageSize);
    }

    @Transactional
    @Override
    public Long createSnippet(User owner, String title, String description, String code, String dateCreated, Long language, Collection<String> tags) {
        Long snippetId =  snippetDao.createSnippet(owner.getId(),title,description,code,dateCreated,language);
        if(snippetId != null) {
            this.tagService.addTagsToSnippet(snippetId, tags);
        }
        return snippetId;
    }

    @Transactional
    @Override
    public void updateFlagged(final Snippet snippet, final User owner, boolean isFlagged, final String baseUrl) {
        if (isFlagged) {
            this.snippetDao.flagSnippet(snippet.getId());
            this.userService.changeReputation(owner.getId(), FLAGGED_SNIPPET_REP_VALUE * (-1));
        } else {
            this.snippetDao.unflagSnippet(snippet.getId());
            this.userService.changeReputation(owner.getId(), FLAGGED_SNIPPET_REP_VALUE);
        }
        // Getting the url of the server
            this.emailService.sendFlaggedEmail(baseUrl + "/snippet/" + snippet.getId(), snippet.getTitle(), owner.getEmail(), owner.getUsername(), isFlagged, owner.getLocale());

    }
}

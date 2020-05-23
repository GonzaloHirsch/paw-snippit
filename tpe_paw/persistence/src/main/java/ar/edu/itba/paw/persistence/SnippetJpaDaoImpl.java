package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SnippetJpaDaoImpl implements SnippetDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Collection<Snippet> findSnippetByCriteria(QueryTypes queryType, Types type, String term, Locations location, Orders order, Long userId, Long resourceId, int page, int pageSize) {
        return null;
    }

    @Override
    public Collection<Snippet> findSnippetByDeepCriteria(String dateMin, String dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, String order, String sort, Boolean includeFlagged, int page, int pageSize) {
        return null;
    }

    @Override
    public Collection<Snippet> getAllSnippets(int page, int pageSize) {
        Query nativeQuery = this.em.createNativeQuery("SELECT id FROM snippets");
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<Long> filteredIds = ((List<Integer>) nativeQuery.getResultList())
                .stream().map(i -> i.longValue()).collect(Collectors.toList());
        final TypedQuery<Snippet> query = this.em.createQuery("from Snippet where id IN :filteredIds", Snippet.class);
        query.setParameter("filteredIds", filteredIds);
        return query.getResultList();
    }

    @Override
    public Collection<Snippet> getAllFavoriteSnippets(long userId, int page, int pageSize) {
        Query nativeQuery = this.em.createNativeQuery("SELECT fav.snippet_id FROM favorites AS fav WHERE fav.user_id = :id");
        nativeQuery.setParameter("id", userId);
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<Long> filteredIds = ((List<Integer>) nativeQuery.getResultList())
                .stream().map(i -> i.longValue()).collect(Collectors.toList());
        final TypedQuery<Snippet> query = this.em.createQuery("from Snippet where id IN :filteredIds", Snippet.class);
        query.setParameter("filteredIds", filteredIds);
        return query.getResultList();
    }

    @Override
    public Collection<Snippet> getAllFollowingSnippets(long userId, int page, int pageSize) {
        return null;
    }

    @Override
    public Collection<Snippet> getAllUpVotedSnippets(long userId, int page, int pageSize) {
        return null;
    }

    @Override
    public Collection<Snippet> getAllFlaggedSnippets(int page, int pageSize) {
        return null;
    }

    @Override
    public Collection<Snippet> findAllSnippetsByOwner(long userId, int page, int pageSize) {
        return null;
    }

    @Override
    public Collection<Snippet> findSnippetsWithLanguage(long langId, int page, int pageSize) {
        return null;
    }

    @Override
    public Optional<Snippet> findSnippetById(long id) {
        return Optional.empty();
    }

    @Override
    public boolean deleteSnippetById(long id) {
        return false;
    }

    @Override
    public int getNewSnippetsForTagsCount(String dateMin, Collection<Tag> tags, long userId) {
        return 0;
    }

    @Override
    public Long createSnippet(long ownerId, String title, String description, String code, String dateCreated, Long languageId) {
        return null;
    }

    @Override
    public Collection<Snippet> findSnippetsForTag(long tagId, int page, int pageSize) {
        return null;
    }

    @Override
    public void flagSnippet(long snippetId) {

    }

    @Override
    public void unflagSnippet(long snippetId) {

    }

    @Override
    public int getAllSnippetsCount() {
        Query nativeQuery = this.em.createNativeQuery("SELECT id FROM snippets");
        return nativeQuery.getResultList().size();
    }

    @Override
    public int getAllFavoriteSnippetsCount(long userId) {
        Query nativeQuery = this.em.createNativeQuery("SELECT fav.snippet_id FROM favorites AS fav WHERE fav.user_id = :id");
        nativeQuery.setParameter("id", userId);
        return nativeQuery.getResultList().size();
    }

    @Override
    public int getAllFollowingSnippetsCount(long userId) {
        return 0;
    }

    @Override
    public int getAllUpvotedSnippetsCount(long userId) {
        return 0;
    }

    @Override
    public int getAllFlaggedSnippetsCount() {
        return 0;
    }

    @Override
    public int getAllSnippetsByOwnerCount(long userId) {
        return 0;
    }

    @Override
    public int getAllSnippetsByTagCount(long tagId) {
        return 0;
    }

    @Override
    public int getAllSnippetsByLanguageCount(long langId) {
        return 0;
    }

    @Override
    public int getSnippetByCriteriaCount(QueryTypes queryType, Types type, String term, Locations location, Long userId, Long resourceId) {
        return 0;
    }

    @Override
    public int getSnippetByDeepCriteriaCount(String dateMin, String dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, String order, String sort, Boolean includeFlagged) {
        return 0;
    }
}

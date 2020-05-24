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
        return getSnippetsByPage(page, pageSize, nativeQuery);
    }

    @Override
    public Collection<Snippet> getAllFavoriteSnippets(long userId, int page, int pageSize) {
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT fav.snippet_id FROM favorites AS fav WHERE fav.user_id = :id");
        nativeQuery.setParameter("id", userId);
        return getSnippetsByPage(page, pageSize, nativeQuery);
    }

    @Override
    public Collection<Snippet> getAllFollowingSnippets(long userId, int page, int pageSize) {
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT sn.id FROM snippets AS sn LEFT OUTER JOIN snippet_tags AS st ON st.snippet_id = sn.id LEFT OUTER JOIN follows AS fol ON fol.tag_id = st.tag_id WHERE fol.user_id = :id ORDER BY sn.id");
        nativeQuery.setParameter("id", userId);
        return getSnippetsByPage(page, pageSize, nativeQuery);
    }

    @Override
    public Collection<Snippet> getAllUpVotedSnippets(long userId, int page, int pageSize) {
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT vf.snippet_id FROM votes_for AS vf WHERE vf.user_id = :id AND vf.type = 1 ORDER BY vf.snippet_id");
        nativeQuery.setParameter("id", userId);
        return this.getSnippetsByPage(page, pageSize, nativeQuery);
    }

    @Override
    public Collection<Snippet> getAllFlaggedSnippets(int page, int pageSize) {
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT sn.id FROM snippets AS sn WHERE sn.flagged = true ORDER BY sn.id");
        return this.getSnippetsByPage(page, pageSize, nativeQuery);
    }

    @Override
    public Collection<Snippet> findAllSnippetsByOwner(long userId, int page, int pageSize) {
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT sn.id FROM snippets AS sn WHERE sn.user_id = :id ORDER BY sn.id");
        nativeQuery.setParameter("id", userId);
        return this.getSnippetsByPage(page, pageSize, nativeQuery);
    }

    @Override
    public Collection<Snippet> findSnippetsForTag(long tagId, int page, int pageSize) {
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT sn.id FROM snippets AS sn LEFT OUTER JOIN snippet_tags AS st ON sn.id = st.snippet_id WHERE st.tag_id = :id ORDER BY sn.id");
        nativeQuery.setParameter("id", tagId);
        return this.getSnippetsByPage(page, pageSize, nativeQuery);
    }

    @Override
    public Collection<Snippet> findSnippetsWithLanguage(long langId, int page, int pageSize) {
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT sn.id FROM snippets AS sn WHERE sn.language_id = :id ORDER BY sn.id");
        nativeQuery.setParameter("id", langId);
        return this.getSnippetsByPage(page, pageSize, nativeQuery);
    }

    @Override
    public Optional<Snippet> findSnippetById(long id) {
        final TypedQuery<Snippet> query = this.em.createQuery("from Snippet where id = :id", Snippet.class);
        query.setParameter("id", id);
        return query.getResultList().stream().findFirst();
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
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT fav.snippet_id FROM favorites AS fav WHERE fav.user_id = :id");
        nativeQuery.setParameter("id", userId);
        return nativeQuery.getResultList().size();
    }

    @Override
    public int getAllFollowingSnippetsCount(long userId) {
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT sn.id FROM snippets AS sn LEFT OUTER JOIN snippet_tags AS st ON st.snippet_id = sn.id LEFT OUTER JOIN follows AS fol ON fol.tag_id = st.tag_id WHERE fol.user_id = :id");
        nativeQuery.setParameter("id", userId);
        return nativeQuery.getResultList().size();
    }

    @Override
    public int getAllUpvotedSnippetsCount(long userId) {
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT vf.snippet_id FROM votes_for AS vf WHERE vf.user_id = :id AND vf.type = 1");
        nativeQuery.setParameter("id", userId);
        return nativeQuery.getResultList().size();
    }

    @Override
    public int getAllFlaggedSnippetsCount() {
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT sn.id FROM snippets AS sn WHERE sn.flagged = true");
        return nativeQuery.getResultList().size();
    }

    @Override
    public int getAllSnippetsByOwnerCount(long userId) {
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT sn.id FROM snippets AS sn WHERE sn.user_id = :id");
        nativeQuery.setParameter("id", userId);
        return nativeQuery.getResultList().size();
    }

    @Override
    public int getAllSnippetsByTagCount(long tagId) {
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT sn.id FROM snippets AS sn LEFT OUTER JOIN snippet_tags AS st ON sn.id = st.snippet_id WHERE st.tag_id = :id ORDER BY sn.id");
        nativeQuery.setParameter("id", tagId);
        return nativeQuery.getResultList().size();
    }

    @Override
    public int getAllSnippetsByLanguageCount(long langId) {
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT sn.id FROM snippets AS sn WHERE sn.language_id = :id");
        nativeQuery.setParameter("id", langId);
        return nativeQuery.getResultList().size();
    }

    @Override
    public int getSnippetByCriteriaCount(QueryTypes queryType, Types type, String term, Locations location, Long userId, Long resourceId) {
        return 0;
    }

    @Override
    public int getSnippetByDeepCriteriaCount(String dateMin, String dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, String order, String sort, Boolean includeFlagged) {
        return 0;
    }

    private Collection<Snippet> getSnippetsByPage(int page, int pageSize, Query nativeQuery) {
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<Long> filteredIds = ((List<Integer>) nativeQuery.getResultList())
                .stream().map(i -> i.longValue()).collect(Collectors.toList());
        final TypedQuery<Snippet> query = this.em.createQuery("from Snippet where id IN :filteredIds", Snippet.class);
        query.setParameter("filteredIds", filteredIds);
        return query.getResultList();
    }
}

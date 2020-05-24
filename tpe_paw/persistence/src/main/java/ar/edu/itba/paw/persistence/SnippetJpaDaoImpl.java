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
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class SnippetJpaDaoImpl implements SnippetDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Collection<Snippet> findSnippetByCriteria(Types type, String term, Locations location, Orders order, Long userId, Long resourceId, int page, int pageSize) {
        SnippetSearchQuery searchQuery = new SnippetSearchQuery.Builder(location, userId, type, term, resourceId).setOrder(order, type).build();
        Query nativeQuery = this.em.createNativeQuery(searchQuery.getQuery());
        this.setSearchQueryParameters(searchQuery.getParams(), nativeQuery);
        return this.getSearchSnippetsByPage(page, pageSize, nativeQuery, order, type);
    }

    @Override
    public Collection<Snippet> findSnippetByDeepCriteria(String dateMin, String dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, SnippetDao.Orders order, SnippetDao.Types type, Boolean includeFlagged, int page, int pageSize) {
        SnippetDeepSearchQuery searchQuery = this.createDeepQuery(dateMin, dateMax, repMin, repMax, voteMin, voteMax, languageId, tagId, title, username, order, type, includeFlagged);
        Query nativeQuery = this.em.createNativeQuery(searchQuery.getQuery());
        this.setSearchQueryParameters(searchQuery.getParams(), nativeQuery);
        return this.getDeepSearchSnippetsByPage(page, pageSize, nativeQuery, order, type);
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
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT sn.id FROM snippets AS sn LEFT OUTER JOIN snippet_tags AS st ON st.snippet_id = sn.id LEFT OUTER JOIN follows AS fol ON fol.tag_id = st.tag_id WHERE fol.user_id = :id ORDER BY sn.id ASC");
        nativeQuery.setParameter("id", userId);
        return getSnippetsByPage(page, pageSize, nativeQuery);
    }

    @Override
    public Collection<Snippet> getAllUpVotedSnippets(long userId, int page, int pageSize) {
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT vf.snippet_id FROM votes_for AS vf WHERE vf.user_id = :id AND vf.type = 1 ORDER BY vf.snippet_id ASC");
        nativeQuery.setParameter("id", userId);
        return this.getSnippetsByPage(page, pageSize, nativeQuery);
    }

    @Override
    public Collection<Snippet> getAllFlaggedSnippets(int page, int pageSize) {
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT sn.id FROM snippets AS sn WHERE sn.flagged = true ORDER BY sn.id ASC");
        return this.getSnippetsByPage(page, pageSize, nativeQuery);
    }

    @Override
    public Collection<Snippet> findAllSnippetsByOwner(long userId, int page, int pageSize) {
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT sn.id FROM snippets AS sn WHERE sn.user_id = :id ORDER BY sn.id ASC");
        nativeQuery.setParameter("id", userId);
        return this.getSnippetsByPage(page, pageSize, nativeQuery);
    }

    @Override
    public Collection<Snippet> findSnippetsForTag(long tagId, int page, int pageSize) {
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT sn.id FROM snippets AS sn LEFT OUTER JOIN snippet_tags AS st ON sn.id = st.snippet_id WHERE st.tag_id = :id ORDER BY sn.id ASC");
        nativeQuery.setParameter("id", tagId);
        return this.getSnippetsByPage(page, pageSize, nativeQuery);
    }

    @Override
    public Collection<Snippet> findSnippetsWithLanguage(long langId, int page, int pageSize) {
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT sn.id FROM snippets AS sn WHERE sn.language_id = :id ORDER BY sn.id ASC");
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
        Optional<Snippet> maybeSnippet = this.findSnippetById(id);
        maybeSnippet.ifPresent(snippet -> this.em.remove(snippet));
        return maybeSnippet.isPresent();
    }

    @Override
    public int getNewSnippetsForTagsCount(String dateMin, Collection<Tag> tags, long userId) {
        Collection<Long> tagIds = tags.stream().mapToLong(Tag::getId).boxed().collect(Collectors.toList());
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT sn.id FROM snippets AS sn LEFT OUTER JOIN snippet_tags AS st ON st.snippet_id = sn.id WHERE st.tag_id IN :ids AND sn.date_created::date >= :cdate::date AND sn.user_id != :id");
        nativeQuery.setParameter("ids", tagIds);
        nativeQuery.setParameter("cdate", dateMin);
        nativeQuery.setParameter("id", userId);
        return nativeQuery.getResultList().size();
    }

    @Override
    public Long createSnippet(long ownerId, String title, String description, String code, String dateCreated, Long languageId) {
        return null;
    }

    @Override
    public void flagSnippet(long snippetId) {
        Optional<Snippet> maybeSnippet = this.findSnippetById(snippetId);
        maybeSnippet.ifPresent(snippet -> {
            snippet.setFlagged(true);
            this.em.persist(snippet);
        });
    }

    @Override
    public void unflagSnippet(long snippetId) {
        Optional<Snippet> maybeSnippet = this.findSnippetById(snippetId);
        maybeSnippet.ifPresent(snippet -> {
            snippet.setFlagged(false);
            this.em.persist(snippet);
        });
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
    public int getSnippetByCriteriaCount(Types type, String term, Locations location, Long userId, Long resourceId) {
        SnippetSearchQuery searchQuery = new SnippetSearchQuery.Builder(location, userId, type, term, resourceId)
                .build();
        Query nativeQuery = this.em.createNativeQuery(searchQuery.getQuery());
        this.setSearchQueryParameters(searchQuery.getParams(), nativeQuery);
        return nativeQuery.getResultList().size();
    }

    @Override
    public int getSnippetByDeepCriteriaCount(String dateMin, String dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, Boolean includeFlagged) {
        SnippetDeepSearchQuery searchQuery = this.createDeepQuery(dateMin, dateMax, repMin, repMax, voteMin, voteMax, languageId, tagId, title, username, null, null, includeFlagged);
        Query nativeQuery = this.em.createNativeQuery(searchQuery.getQuery());
        this.setSearchQueryParameters(searchQuery.getParams(), nativeQuery);
        return nativeQuery.getResultList().size();
    }

    private SnippetDeepSearchQuery createDeepQuery(String dateMin, String dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, SnippetDao.Orders order, SnippetDao.Types type, Boolean includeFlagged) {
        SnippetDeepSearchQuery.Builder queryBuilder = new SnippetDeepSearchQuery.Builder();
        if (dateMin == null && dateMax == null && repMin == null && repMax == null && voteMin == null && voteMax == null && languageId == null && tagId == null && title == null && username == null && includeFlagged == null) {
            return queryBuilder.setOrder(Types.TITLE, Orders.ASC).build();
        } else {
            boolean isFirst = true;
            queryBuilder = queryBuilder.where();
            if (dateMin != null || dateMax != null) {
                queryBuilder = queryBuilder.addDateRange(dateMin, dateMax);
                isFirst = false;
            }
            if (repMin != null || repMax != null) {
                if (!isFirst) {
                    queryBuilder = queryBuilder.and();
                } else {
                    isFirst = false;
                }
                queryBuilder = queryBuilder.addReputationRange(repMin, repMax);
            }
            if (voteMin != null || voteMax != null) {
                if (!isFirst) {
                    queryBuilder = queryBuilder.and();
                } else {
                    isFirst = false;
                }
                queryBuilder = queryBuilder.addVotesRange(voteMin, voteMax);
            }
            if (languageId != null) {
                if (!isFirst) {
                    queryBuilder = queryBuilder.and();
                } else {
                    isFirst = false;
                }
                queryBuilder = queryBuilder.addLanguage(languageId);
            }
            if (tagId != null) {
                if (!isFirst) {
                    queryBuilder = queryBuilder.and();
                } else {
                    isFirst = false;
                }
                queryBuilder = queryBuilder.addTag(tagId);
            }
            if (title != null && !title.isEmpty()) {
                if (!isFirst) {
                    queryBuilder = queryBuilder.and();
                } else {
                    isFirst = false;
                }
                queryBuilder = queryBuilder.addTitle(title);
            }
            if (username != null && !username.isEmpty()) {
                if (!isFirst) {
                    queryBuilder = queryBuilder.and();
                } else {
                    isFirst = false;
                }
                queryBuilder = queryBuilder.addUsername(username);
            }
            if (includeFlagged != null && !includeFlagged) {
                if (!isFirst) {
                    queryBuilder = queryBuilder.and();
                } else {
                    isFirst = false;
                }
                queryBuilder = queryBuilder.addIncludeFlagged(includeFlagged);
            }
            return queryBuilder.setOrder(type, order).build();
        }
    }

    /**
     * Extracts a page of Snippet results given a query for filtered ids
     *
     * @param page        Number of page
     * @param pageSize    Size of the page
     * @param nativeQuery Query searching for filtered IDs
     * @return Collection<Snippet> with the results of the query
     */
    private Collection<Snippet> getSnippetsByPage(int page, int pageSize, Query nativeQuery) {
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<Long> filteredIds = ((List<Integer>) nativeQuery.getResultList())
                .stream().map(i -> i.longValue()).collect(Collectors.toList());
        if (filteredIds.size() > 0) {
            final TypedQuery<Snippet> query = this.em.createQuery("from Snippet where id IN :filteredIds", Snippet.class);
            query.setParameter("filteredIds", filteredIds);
            return query.getResultList();
        }
        return Collections.emptyList();
    }

    /**
     * Extracts a page of Snippet results given a native query that sorts results
     *
     * @param page        Number of page
     * @param pageSize    Size of the page
     * @param nativeQuery Query searching for filtered IDs
     * @return Collection<Snippet> with the results of the query
     */
    private Collection<Snippet> getSearchSnippetsByPage(int page, int pageSize, Query nativeQuery, Orders order, Types type) {
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<Long> filteredIds = ((List<Object[]>) nativeQuery.getResultList())
                .stream().map(i -> ((Integer)i[0]).longValue()).collect(Collectors.toList());
        if (filteredIds.size() > 0) {
            final TypedQuery<Snippet> query = this.getSortedSearchQuery(order, type);
            query.setParameter("filteredIds", filteredIds);
            return query.getResultList();
        }
        return Collections.emptyList();
    }

    private Collection<Snippet> getDeepSearchSnippetsByPage(int page, int pageSize, Query nativeQuery, Orders order, Types type) {
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<Long> filteredIds = ((List<Object[]>) nativeQuery.getResultList())
                .stream().map(i -> ((Integer)i[0]).longValue()).collect(Collectors.toList());
        if (filteredIds.size() > 0) {
            final TypedQuery<Snippet> query = this.getSortedDeepSearchQuery(order, type);
            query.setParameter("filteredIds", filteredIds);
            return query.getResultList();
        }
        return Collections.emptyList();
    }

    /**
     * Generates a sorted query for the Snippet object
     * @param order Type of order to be used
     * @param type The field to order by
     * @return TypedQuery<Snippet> with the sorted query
     */
    private TypedQuery<Snippet> getSortedDeepSearchQuery(Orders order, Types type){
        if (!order.equals(Orders.NO)){
            StringBuilder query = new StringBuilder();
            query.append("from Snippet WHERE id IN :filteredIds");
            switch (type){
                case ALL:
                case TAG:
                case TITLE:
                    query.append(" ORDER BY title ");
                    break;
                case USER:
                    query.append(" ORDER BY owner.username ");
                    break;
                case CONTENT:
                    query.append(" ORDER BY code ");
                    break;
                case LANGUAGE:
                    query.append(" ORDER BY language.name ");
                    break;
            }
            switch (order){
                case ASC:
                    query.append("ASC");
                    break;
                case DESC:
                    query.append("DESC");
                    break;
            }
            return this.em.createQuery(query.toString(), Snippet.class);
        } else {
            return this.em.createQuery("from Snippet WHERE id IN :filteredIds", Snippet.class);
        }
    }

    /**
     * Generates a sorted query for the Snippet object
     * @param order Type of order to be used
     * @param type The field to order by
     * @return TypedQuery<Snippet> with the sorted query
     */
    private TypedQuery<Snippet> getSortedSearchQuery(Orders order, Types type){
        if (!order.equals(Orders.NO)){
            StringBuilder query = new StringBuilder();
            query.append("from Snippet WHERE id IN :filteredIds");
            switch (type){
                case ALL:
                case TAG:
                case TITLE:
                    query.append(" ORDER BY title ");
                    break;
                case USER:
                    query.append(" ORDER BY owner.username ");
                    break;
                case CONTENT:
                    query.append(" ORDER BY code ");
                    break;
                case LANGUAGE:
                    query.append(" ORDER BY language.name ");
                    break;
            }
            switch (order){
                case ASC:
                    query.append("ASC");
                    break;
                case DESC:
                    query.append("DESC");
                    break;
            }
            return this.em.createQuery(query.toString(), Snippet.class);
        } else {
            return this.em.createQuery("from Snippet WHERE id IN :filteredIds", Snippet.class);
        }
    }

    /**
     * Adds all parameters to a search query
     *
     * @param params      Named parameters for query
     * @param nativeQuery Query for snippet search
     */
    private void setSearchQueryParameters(Map<String, Object> params, Query nativeQuery) {
        for (String key : params.keySet()) {
            nativeQuery.setParameter(key, params.get(key));
        }
    }
}

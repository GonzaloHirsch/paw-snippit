package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.dao.TagDao;
import ar.edu.itba.paw.models.Language;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class SnippetJpaDaoImpl implements SnippetDao {

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private TagDao tagJpaDaoImpl;

    @Override
    public Collection<Snippet> findSnippetByCriteria(Types type, String term, Locations location, Orders order, Long userId, Long resourceId, int page, int pageSize) {
        SnippetSearchQuery searchQuery = new SnippetSearchQuery.Builder(location, userId, type, term, resourceId).setOrder(order, type).build();
        Query nativeQuery = this.em.createNativeQuery(searchQuery.getQuery());
        this.setSearchQueryParameters(searchQuery.getParams(), nativeQuery);
        return this.getSearchSnippetsByPage(page, pageSize, nativeQuery, order, type, false);
    }

    @Override
    public Collection<Snippet> findSnippetByDeepCriteria(Instant dateMin, Instant dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, SnippetDao.Orders order, SnippetDao.Types type, Boolean includeFlagged, int page, int pageSize) {
        SnippetDeepSearchQuery searchQuery = this.createDeepQuery(dateMin, dateMax, repMin, repMax, voteMin, voteMax, languageId, tagId, title, username, order, type, includeFlagged);
        Query nativeQuery = this.em.createNativeQuery(searchQuery.getQuery());
        this.setSearchQueryParameters(searchQuery.getParams(), nativeQuery);
        return this.getSearchSnippetsByPage(page, pageSize, nativeQuery, order, type, true);
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
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT sn.id FROM snippets AS sn WHERE sn.flagged = 1 ORDER BY sn.id ASC");
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
    public int getNewSnippetsForTagsCount(Instant dateMin, Collection<Tag> tags, long userId) {
        ZonedDateTime zdt = ZonedDateTime.ofInstant(dateMin, ZoneId.systemDefault());
        Calendar min = GregorianCalendar.from(zdt);
        // Extracting the tag ids form the list
        Collection<Long> tagIds = tags.stream().mapToLong(Tag::getId).boxed().collect(Collectors.toList());
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT sn.id FROM snippets AS sn LEFT OUTER JOIN snippet_tags AS st ON st.snippet_id = sn.id WHERE st.tag_id IN :ids AND sn.date_created\\:\\:date >= :cdate\\:\\:date AND sn.user_id != :id");
        // TODO ver si hay algo más rápido
//        if (tagIds.isEmpty()) {
//            tagIds.add(-1L);
//        }
        nativeQuery.setParameter("ids", tagIds);
        nativeQuery.setParameter("cdate", min, TemporalType.TIMESTAMP);
        nativeQuery.setParameter("id", userId);
        return nativeQuery.getResultList().size();
    }

    @Override
    public Long createSnippet(long ownerId, String title, String description, String code, Timestamp dateCreated, Long languageId, Collection<String> tags) {
        User owner = em.find(User.class, ownerId);
        Language lang = em.find(Language.class, languageId);
        Collection<Tag> tagEntities = tagJpaDaoImpl.findSpecificTagsByName(tags);
        Snippet createdSnippet = new Snippet(owner, code, title, description, dateCreated, lang, tagEntities, false);
        em.persist(createdSnippet);
        return createdSnippet.getId();
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
        Query nativeQuery = this.em.createNativeQuery("SELECT DISTINCT sn.id FROM snippets AS sn WHERE sn.flagged = 1");
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
    public int getSnippetByDeepCriteriaCount(Instant dateMin, Instant dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, Boolean includeFlagged) {
        SnippetDeepSearchQuery searchQuery = this.createDeepQuery(dateMin, dateMax, repMin, repMax, voteMin, voteMax, languageId, tagId, title, username, null, null, includeFlagged);
        Query nativeQuery = this.em.createNativeQuery(searchQuery.getQuery());
        this.setSearchQueryParameters(searchQuery.getParams(), nativeQuery);
        return nativeQuery.getResultList().size();
    }

    private SnippetDeepSearchQuery createDeepQuery(Instant dateMin, Instant dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, SnippetDao.Orders order, SnippetDao.Types type, Boolean includeFlagged) {
        SnippetDeepSearchQuery.Builder queryBuilder = new SnippetDeepSearchQuery.Builder();
        if (dateMin == null && dateMax == null && repMin == null && repMax == null && voteMin == null && voteMax == null && languageId == null && tagId == null && title == null && username == null && includeFlagged == null) {
            return queryBuilder.setOrder(Types.TITLE, Orders.ASC).build();
        } else {
            if (dateMin != null || dateMax != null) {
                Calendar min = null;
                Calendar max = null;
                if (dateMin != null) {
                    ZonedDateTime zdt = ZonedDateTime.ofInstant(dateMin, ZoneId.systemDefault());
                    min = GregorianCalendar.from(zdt);
                }
                if (dateMax != null) {
                    ZonedDateTime zdt = ZonedDateTime.ofInstant(dateMax, ZoneId.systemDefault());
                    max = GregorianCalendar.from(zdt);
                }
                queryBuilder = queryBuilder.addDateRange(min, max);
            }
            if (repMin != null || repMax != null) {
                queryBuilder = queryBuilder.addReputationRange(repMin, repMax);
            }
            if (voteMin != null || voteMax != null) {
                queryBuilder = queryBuilder.addVotesRange(voteMin, voteMax);
            }
            if (languageId != null) {
                queryBuilder = queryBuilder.addLanguage(languageId);
            }
            if (tagId != null) {
                queryBuilder = queryBuilder.addTag(tagId);
            }
            if (title != null && !title.isEmpty()) {
                queryBuilder = queryBuilder.addTitle(title);
            }
            if (username != null && !username.isEmpty()) {
                queryBuilder = queryBuilder.addUsername(username);
            }
            if (includeFlagged != null) {
                queryBuilder = queryBuilder.addIncludeFlagged(includeFlagged);
            }
            return type != null && order != null ? queryBuilder.setOrder(type, order).build() : queryBuilder.build();
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
     * Extracts a page of Snippet deep search results given a native query that sorts results
     * @param page Number of the page
     * @param pageSize Size of the page
     * @param nativeQuery NativeQuery to be used to filter IDs
     * @param order Sort type of the order
     * @param type Order type of the order
     * @param isDeepSearch Boolean representing if the query is a deep query
     * @return Collection<Snippet> with the results
     */
    private Collection<Snippet> getSearchSnippetsByPage(int page, int pageSize, Query nativeQuery, Orders order, Types type, boolean isDeepSearch) {
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<Long> filteredIds = ((List<Object[]>) nativeQuery.getResultList())
                .stream().map(i -> ((Integer) i[0]).longValue()).collect(Collectors.toList());
        if (filteredIds.size() > 0) {
            final TypedQuery<Snippet> query = isDeepSearch ? this.getSortedDeepSearchQuery(order, type) : this.getSortedDeepSearchQuery(order, type);
            query.setParameter("filteredIds", filteredIds);
            return query.getResultList();
        }
        return Collections.emptyList();
    }

    /**
     * Generates a sorted query for the Snippet object
     *
     * @param order Type of order to be used
     * @param type  The field to order by
     * @return TypedQuery<Snippet> with the sorted query
     */
    private TypedQuery<Snippet> getSortedDeepSearchQuery(Orders order, Types type) {
        if (!order.equals(Orders.NO)) {
            StringBuilder query = new StringBuilder();
            query.append("from Snippet WHERE id IN :filteredIds");
            switch (type) {
                case VOTES:
                    query.append(" ORDER BY SUM(votes.type) ");
                    break;
                case DATE:
                    query.append(" ORDER BY dateCreated ");
                    break;
                case REPUTATION:
                    query.append(" ORDER BY owner.reputation ");
                    break;
                case ALL:
                case TAG:
                case TITLE:
                case LANGUAGE:
                case CONTENT:
                case USER:
                default:
                    query.append(" ORDER BY title ");
                    break;
            }
            switch (order) {
                case ASC:
                    query.append("ASC");
                    break;
                case DESC:
                default:
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
     *
     * @param order Type of order to be used
     * @param type  The field to order by
     * @return TypedQuery<Snippet> with the sorted query
     */
    private TypedQuery<Snippet> getSortedSearchQuery(Orders order, Types type) {
        if (!order.equals(Orders.NO)) {
            StringBuilder query = new StringBuilder();
            query.append("from Snippet WHERE id IN :filteredIds");
            switch (type) {
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
            switch (order) {
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
        params.forEach((key, value) -> {
            // Checking if instance of Calendar because Hibernate needs to be told it's a calendar type in a different way
            // If not checking Calendar instance, fails with SQL error
            if (value instanceof Calendar) {
                nativeQuery.setParameter(key, (Calendar) value, TemporalType.TIMESTAMP);
            } else {
                nativeQuery.setParameter(key, value);
            }
        });
    }
}

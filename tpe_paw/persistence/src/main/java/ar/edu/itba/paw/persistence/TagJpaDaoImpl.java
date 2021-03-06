package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.TagDao;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TagJpaDaoImpl implements TagDao {

    @PersistenceContext
    private EntityManager em;


    @Override
    public Optional<Tag> findById(final long id) {
        return Optional.ofNullable(this.em.find(Tag.class, id));
    }


    @Override
    public Optional<Tag> findByName(final String name) {
        final TypedQuery<Tag> query = this.em.createQuery("from Tag as t where LOWER(t.name) LIKE LOWER(:name)", Tag.class);
        query.setParameter("name", SearchUtils.EscapeSpecialCharacters(name));
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Tag addTag(String name) {
        Optional<Tag> maybeTag = this.findByName(name);
        if (!maybeTag.isPresent()) {
            final Tag tag = new Tag(name);
            em.persist(tag);
            return tag;
        }
        return maybeTag.get();
    }

    @Override
    public void addTags(List<String> tags) {
        if (tags != null) {
            for (String name : tags) {
                if (name != null)
                    this.addTag(name);
            }
        }
    }

    @Override
    public void removeTag(long tagId) {
        Optional<Tag> maybeTag = findById(tagId);
        if(maybeTag.isPresent()){
            Tag tag = maybeTag.get();
            for(Snippet s : tag.getSnippetsUsing()){
                s.getTags().remove(tag);
            }
            for(User u : tag.getFollowingUsers()){
                u.getFollowedTags().remove(tag);
            }
            this.em.remove(tag);
        }
    }

    @Override
    public Collection<Tag> getAllTags(boolean showEmpty, boolean showOnlyFollowing, Long userId, int page, int pageSize) {
        Query nativeQuery;
        if (showEmpty){
            if (showOnlyFollowing){
                nativeQuery = this.em.createNativeQuery("SELECT DISTINCT t.id, t.name FROM follows AS f LEFT OUTER JOIN tags AS t ON t.id = f.tag_id WHERE f.user_id = :id ORDER BY t.name ASC");
                nativeQuery.setParameter("id", userId);
            } else {
                nativeQuery = this.em.createNativeQuery("SELECT DISTINCT id, name FROM tags ORDER BY name ASC");
            }
        } else {
            if (showOnlyFollowing){
                nativeQuery = this.em.createNativeQuery("SELECT DISTINCT t.id, t.name FROM follows AS f LEFT OUTER JOIN tags AS t ON f.tag_id = t.id INNER JOIN snippet_tags AS st ON st.tag_id = t.id INNER JOIN snippets AS sn ON st.snippet_id = sn.id WHERE deleted = FALSE AND f.user_id = :id ORDER BY t.name ASC");
                nativeQuery.setParameter("id", userId);
            } else {
                nativeQuery = this.em.createNativeQuery("SELECT DISTINCT t.id, t.name FROM tags AS t INNER JOIN snippet_tags AS st ON st.tag_id = t.id INNER JOIN snippets AS sn ON st.snippet_id = sn.id WHERE deleted = FALSE ORDER BY t.name ASC");
            }
        }
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<Long> filteredIds = ((List<Object[]>) nativeQuery.getResultList())
                .stream().map(i -> ((Number) i[0]).longValue()).collect(Collectors.toList());
        if (!filteredIds.isEmpty()){
            final TypedQuery<Tag> query = this.em.createQuery("from Tag where id IN :filteredIds ORDER BY name ASC", Tag.class);
            query.setParameter("filteredIds", filteredIds);
            return query.getResultList();
        }
        return Collections.emptyList();
    }

    @Override
    public Collection<Tag> getAllTags() {
        return this.em.createQuery("from Tag ORDER BY name", Tag.class).getResultList();
    }

    @Override
    public int getAllTagsCountByName(String name, boolean showEmpty, boolean showOnlyFollowing, Long userId) {
        Query nativeQuery;
        if (showEmpty){
            if (showOnlyFollowing){
                nativeQuery = this.em.createNativeQuery("SELECT COUNT(DISTINCT t.id) FROM follows AS f LEFT OUTER JOIN tags AS t ON f.tag_id = t.id WHERE " + SearchUtils.TranslateField("t.name") + " LIKE " + SearchUtils.TranslateField(":term") + " AND f.user_id = :id")
                        .setParameter("term", "%"+SearchUtils.EscapeSpecialCharacters(name)+"%").setParameter("id", userId);
            } else {
                nativeQuery = this.em.createNativeQuery("SELECT COUNT(DISTINCT id) FROM tags WHERE " + SearchUtils.TranslateField("name") + " LIKE " + SearchUtils.TranslateField(":term"))
                        .setParameter("term", "%"+SearchUtils.EscapeSpecialCharacters(name)+"%");
            }
        } else {
            if (showOnlyFollowing){
                nativeQuery = this.em.createNativeQuery("SELECT COUNT(DISTINCT t.id) FROM follows AS f LEFT OUTER JOIN tags AS t ON f.tag_id = t.id INNER JOIN snippet_tags AS st ON st.tag_id = t.id INNER JOIN snippets AS sn ON st.snippet_id = sn.id WHERE deleted = FALSE AND " + SearchUtils.TranslateField("t.name") + " LIKE " + SearchUtils.TranslateField(":term") + " AND f.user_id = :id")
                        .setParameter("term", "%"+SearchUtils.EscapeSpecialCharacters(name)+"%").setParameter("id", userId);
            } else {
                nativeQuery = this.em.createNativeQuery("SELECT COUNT(DISTINCT t.id) FROM tags AS t INNER JOIN snippet_tags AS st ON st.tag_id = t.id INNER JOIN snippets AS sn ON st.snippet_id = sn.id WHERE deleted = FALSE AND " + SearchUtils.TranslateField("t.name") + " LIKE " + SearchUtils.TranslateField(":term"))
                        .setParameter("term", "%"+SearchUtils.EscapeSpecialCharacters(name)+"%");
            }
        }
        return ((BigInteger) nativeQuery.getSingleResult()).intValue();
    }

    @Override
    public int getAllTagsCount(boolean showEmpty, boolean showOnlyFollowing, Long userId) {
        Query nativeQuery;
        if (showEmpty){
            if (showOnlyFollowing){
                nativeQuery = this.em.createNativeQuery("SELECT COUNT(DISTINCT tag_id) FROM follows WHERE user_id = :id");
                nativeQuery.setParameter("id", userId);
            } else {
                nativeQuery = this.em.createNativeQuery("SELECT COUNT(id) FROM tags");
            }
        } else {
            if (showOnlyFollowing){
                nativeQuery = this.em.createNativeQuery("SELECT COUNT(DISTINCT tag_id) FROM follows AS f LEFT OUTER JOIN snippet_tags AS st ON f.tag_id = st.tag_id INNER JOIN snippets AS sn ON st.snippet_id = sn.id WHERE sn.deleted = FALSE AND f.user_id = :id");
                nativeQuery.setParameter("id", userId);
            } else {
                nativeQuery = this.em.createNativeQuery("SELECT COUNT(DISTINCT tag_id) FROM snippet_tags AS st INNER JOIN snippets AS sn ON st.snippet_id = sn.id WHERE sn.deleted = FALSE");
            }
        }
        return ((BigInteger) nativeQuery.getSingleResult()).intValue();
    }

    @Override
    public Collection<Tag> findSpecificTagsByName(Collection<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return Collections.emptyList();
        }
        TypedQuery<Tag> query = this.em.createQuery("FROM Tag WHERE name IN :tagNames", Tag.class)
                .setParameter("tagNames", tags);
        return query.getResultList();
    }

    @Override
    public Collection<Tag> findTagsByName(String name, boolean showEmpty, boolean showOnlyFollowing, Long userId, int page, int pageSize) {
        Query nativeQuery;
        if (showEmpty){
            if (showOnlyFollowing){
                nativeQuery = this.em.createNativeQuery("SELECT DISTINCT t.id, t.name FROM follows AS f LEFT OUTER JOIN tags AS t ON f.tag_id = t.id WHERE f.user_id = :id AND " + SearchUtils.TranslateField("t.name") + " LIKE " + SearchUtils.TranslateField(":term") + " ORDER BY t.name ASC")
                        .setParameter("term", "%"+SearchUtils.EscapeSpecialCharacters(name)+"%").setParameter("id", userId);
            } else {
                nativeQuery = this.em.createNativeQuery("SELECT DISTINCT id, name FROM tags WHERE " + SearchUtils.TranslateField("name") + " LIKE " + SearchUtils.TranslateField(":term") + " ORDER BY name ASC")
                        .setParameter("term", "%"+SearchUtils.EscapeSpecialCharacters(name)+"%");
            }
        } else {
            if (showOnlyFollowing){
                nativeQuery = this.em.createNativeQuery("SELECT DISTINCT t.id, t.name FROM follows AS f LEFT OUTER JOIN tags AS t ON f.tag_id = t.id INNER JOIN snippet_tags AS st ON st.tag_id = t.id INNER JOIN snippets AS sn ON st.snippet_id = sn.id WHERE f.user_id = :id AND sn.deleted = FALSE AND " + SearchUtils.TranslateField("t.name") + " LIKE " + SearchUtils.TranslateField(":term") + " ORDER BY t.name ASC")
                        .setParameter("term", "%"+SearchUtils.EscapeSpecialCharacters(name)+"%").setParameter("id", userId);
            } else {
                nativeQuery = this.em.createNativeQuery("SELECT DISTINCT t.id, t.name FROM tags AS t INNER JOIN snippet_tags AS st ON st.tag_id = t.id INNER JOIN snippets AS sn ON st.snippet_id = sn.id WHERE deleted = FALSE AND " + SearchUtils.TranslateField("t.name") + " LIKE " + SearchUtils.TranslateField(":term") + " ORDER BY t.name ASC")
                        .setParameter("term", "%"+SearchUtils.EscapeSpecialCharacters(name)+"%");
            }
        }
        nativeQuery.setParameter("term", "%"+SearchUtils.EscapeSpecialCharacters(name)+"%");
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<Long> filteredIds = ((List<Object[]>) nativeQuery.getResultList())
                .stream().map(i -> ((Number) i[0]).longValue()).collect(Collectors.toList());
        if (!filteredIds.isEmpty()){
            final TypedQuery<Tag> query = this.em.createQuery("from Tag where id IN :filteredIds ORDER BY name ASC", Tag.class);
            query.setParameter("filteredIds", filteredIds);
            return query.getResultList();
        }
        return Collections.emptyList();
    }

}

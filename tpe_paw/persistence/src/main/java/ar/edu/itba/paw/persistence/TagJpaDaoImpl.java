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
        query.setParameter("name", name);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Tag addTag(String name) {
        final Tag tag = new Tag(name);
        em.persist(tag);
        return tag;
    }

    // TODO REMOVE
    @Override
    public Collection<Tag> findTagsForSnippet(long snippetId) {
        return null;
    }

    @Override
    public void addSnippetTag(long snippetId, long tagId) {
        Snippet snippet = this.em.find(Snippet.class, snippetId);
        Tag tag = this.em.find(Tag.class, tagId);

        snippet.getTags().add(tag);
        tag.getSnippetsUsing().add(snippet);

        this.em.persist(snippet);
        this.em.persist(tag);
    }

    @Override
    public void addTags(List<String> tags) {
        for(String name : tags){
            if (name != null)
                this.addTag(name);
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
    public Collection<Tag> getAllTags(boolean showEmpty, int page, int pageSize) {
        Query nativeQuery;
        if (showEmpty){
            nativeQuery = this.em.createNativeQuery("SELECT DISTINCT id, name FROM tags ORDER BY name ASC");
        } else {
            nativeQuery = this.em.createNativeQuery("SELECT DISTINCT t.id, t.name FROM tags AS t INNER JOIN snippet_tags AS st ON st.tag_id = t.id INNER JOIN snippets AS sn ON st.snippet_id = sn.id WHERE deleted = FALSE ORDER BY t.name ASC");
        }
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<Long> filteredIds = ((List<Object[]>) nativeQuery.getResultList())
                .stream().map(i -> ((Integer) i[0]).longValue()).collect(Collectors.toList());
        if (!filteredIds.isEmpty()){
            final TypedQuery<Tag> query = this.em.createQuery("from Tag where id IN :filteredIds ORDER BY name ASC", Tag.class);
            query.setParameter("filteredIds", filteredIds);
            return query.getResultList();
        }
        return Collections.emptyList();
    }

    @Override
    public Collection<Tag> getAllTags() {
        return this.em.createQuery("from Tag", Tag.class).getResultList();
    }

    @Override
    public int getAllTagsCountByName(String name, boolean showEmpty) {
        Query nativeQuery;
        if (showEmpty){
            nativeQuery = this.em.createNativeQuery("SELECT COUNT(DISTINCT id) FROM tags WHERE LOWER(name) LIKE LOWER(:term)")
                    .setParameter("term", "%"+name+"%");
        } else {
            nativeQuery = this.em.createNativeQuery("SELECT COUNT(DISTINCT t.id) FROM tags AS t INNER JOIN snippet_tags AS st ON st.tag_id = t.id INNER JOIN snippets AS sn ON st.snippet_id = sn.id WHERE deleted = FALSE AND LOWER(t.name) LIKE LOWER(:term)")
                    .setParameter("term", "%"+name+"%");
        }
        return ((Number) nativeQuery.getSingleResult()).intValue();
    }

    @Override
    public int getAllTagsCount(boolean showEmpty) {
        Query nativeQuery;
        if (showEmpty){
            nativeQuery = this.em.createNativeQuery("SELECT id FROM tags");
        } else {
            nativeQuery = this.em.createNativeQuery("SELECT DISTINCT tag_id FROM snippet_tags AS st INNER JOIN snippets AS sn ON st.snippet_id = sn.id WHERE deleted = FALSE");
        }
        return nativeQuery.getResultList().size();
    }

    @Override
    public Collection<Tag> findSpecificTagsByName(Collection<String> tags) {
        TypedQuery<Tag> query = this.em.createQuery("FROM Tag WHERE name IN :tagNames", Tag.class)
                .setParameter("tagNames", tags);
        return query.getResultList();
    }

    @Override
    public Collection<Tag> findTagsByName(String name, boolean showEmpty, int page, int pageSize) {
        Query nativeQuery;
        if (showEmpty){
            nativeQuery = this.em.createNativeQuery("SELECT DISTINCT id, name FROM tags WHERE LOWER(name) LIKE LOWER(:term) ORDER BY name ASC")
                    .setParameter("term", "%"+name+"%");
        } else {
            nativeQuery = this.em.createNativeQuery("SELECT DISTINCT t.id, t.name FROM tags AS t INNER JOIN snippet_tags AS st ON st.tag_id = t.id INNER JOIN snippets AS sn ON st.snippet_id = sn.id WHERE deleted = FALSE AND LOWER(t.name) LIKE LOWER(:term) ORDER BY t.name ASC")
                    .setParameter("term", "%"+name+"%");
        }
        nativeQuery.setParameter("term", "%"+name+"%");
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<Long> filteredIds = ((List<Object[]>) nativeQuery.getResultList())
                .stream().map(i -> ((Integer) i[0]).longValue()).collect(Collectors.toList());
        if (!filteredIds.isEmpty()){
            final TypedQuery<Tag> query = this.em.createQuery("from Tag where id IN :filteredIds ORDER BY name ASC", Tag.class);
            query.setParameter("filteredIds", filteredIds);
            return query.getResultList();
        }
        return Collections.emptyList();
    }

}

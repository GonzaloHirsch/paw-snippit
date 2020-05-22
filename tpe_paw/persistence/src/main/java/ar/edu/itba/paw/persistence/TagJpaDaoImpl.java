package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.TagDao;
import ar.edu.itba.paw.models.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
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
        final TypedQuery<Tag> query = this.em.createQuery("from Tag as t where t.name = :name", Tag.class);
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

    }

    @Override
    public void addTags(List<String> tags) {

    }

    @Override
    public void removeTag(long tagId) {

    }


    @Override
    public Collection<Tag> getAllTags(int page, int pageSize) {
        Query nativeQuery = this.em.createNativeQuery("SELECT id FROM tags");
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<Long> filteredIds = ((List<Integer>) nativeQuery.getResultList())
                .stream().map(i -> i.longValue()).collect(Collectors.toList());
        final TypedQuery<Tag> query = this.em.createQuery("from Tag where id IN :filteredIds", Tag.class);
        query.setParameter("filteredIds", filteredIds);
        return query.getResultList();
    }

    @Override
    public Collection<Tag> getAllTags() {
        return this.em.createQuery("from Tag", Tag.class).getResultList(); //TODO CHECK
    }

    @Override
    public int getAllTagsCountByName(String name) {
        return 0;
    }

    @Override
    public int getAllTagsCount() {
        Query nativeQuery = this.em.createNativeQuery("SELECT id FROM tags");
        return nativeQuery.getResultList().size();
    }

    @Override
    public Collection<Tag> findTagsByName(String name, int page, int pageSize) {
        return null;
    }
}

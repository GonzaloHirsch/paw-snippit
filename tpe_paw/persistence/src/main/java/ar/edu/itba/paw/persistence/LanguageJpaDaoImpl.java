package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.LanguageDao;
import ar.edu.itba.paw.models.Language;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class LanguageJpaDaoImpl implements LanguageDao {

    @PersistenceContext
    private EntityManager em;


    @Override
    public Optional<Language> findById(final long id) {
        return Optional.ofNullable(this.em.find(Language.class, id));
    }

    @Override
    public Optional<Language> findByName(final String name) {
        final TypedQuery<Language> query = this.em.createQuery("FROM Language AS l WHERE LOWER(l.name) LIKE LOWER(:name)", Language.class);
        query.setParameter("name", name);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Collection<Language> getAllLanguages() {
        return this.em.createQuery("FROM Language WHERE deleted = FALSE", Language.class).getResultList();
    }

    @Override
    public Collection<Language> getAllLanguages(boolean showEmpty, int page, int pageSize) {
        Query nativeQuery;
        if (showEmpty){
            nativeQuery = this.em.createNativeQuery("SELECT DISTINCT id, name FROM languages WHERE deleted = FALSE ORDER BY name ASC");
        } else {
            nativeQuery = this.em.createNativeQuery("SELECT DISTINCT s.language_id, l.name FROM snippets AS s INNER JOIN languages AS l ON s.language_id = l.id WHERE s.deleted = FALSE AND l.deleted = FALSE ORDER BY l.name ASC");
        }
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<Long> filteredIds = ((List<Object[]>) nativeQuery.getResultList())
                .stream().map(i -> ((Number) i[0]).longValue()).collect(Collectors.toList());
        if (!filteredIds.isEmpty()){
            final TypedQuery<Language> query = this.em.createQuery("FROM Language WHERE id IN :filteredIds ORDER BY name ASC", Language.class);
            query.setParameter("filteredIds", filteredIds);
            return query.getResultList();
        }
        return Collections.emptyList();
    }

    @Override
    public Collection<Language> findAllLanguagesByName(String name, boolean showEmpty, int page, int pageSize) {
        Query nativeQuery;
        if (showEmpty){
            nativeQuery = this.em.createNativeQuery("SELECT DISTINCT id, name FROM languages WHERE LOWER(name) LIKE LOWER(:name) AND deleted = FALSE ORDER BY name ASC")
                    .setParameter("name", "%"+name+"%");
        } else {
            nativeQuery = this.em.createNativeQuery("SELECT DISTINCT s.language_id, l.name FROM snippets AS s INNER JOIN languages AS l ON s.language_id = l.id WHERE s.deleted = FALSE AND LOWER(l.name) LIKE LOWER(:name) AND l.deleted = FALSE ORDER BY l.name ASC")
                    .setParameter("name", "%"+name+"%");
        }
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<Long> filteredIds = ((List<Object[]>) nativeQuery.getResultList())
                .stream().map(i -> ((Number) i[0]).longValue()).collect(Collectors.toList());
        if (!filteredIds.isEmpty()){
            final TypedQuery<Language> query = this.em.createQuery("FROM Language WHERE id IN :filteredIds ORDER BY name ASC", Language.class);
            query.setParameter("filteredIds", filteredIds);
            return query.getResultList();
        }
        return Collections.emptyList();
    }

    private List<Long> filterIdsForPaging(Query nativeQuery, int page, int pageSize) {
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        return ((List<Integer>) nativeQuery.getResultList())
                .stream().map(i -> i.longValue()).collect(Collectors.toList());
    }

    @Override
    public int getAllLanguagesCountByName(String name, boolean showEmpty) {
        Query nativeQuery;
        if (showEmpty){
            nativeQuery = this.em.createNativeQuery("SELECT COUNT(DISTINCT id) FROM languages WHERE deleted = FALSE AND LOWER(name) LIKE LOWER(:name)")
                    .setParameter("name", "%"+name+"%");
        } else {
            nativeQuery = this.em.createNativeQuery("SELECT COUNT(DISTINCT s.language_id) FROM snippets AS s INNER JOIN languages AS l ON s.language_id = l.id WHERE s.deleted = FALSE AND l.deleted = FALSE AND LOWER(l.name) LIKE LOWER(:name)")
                    .setParameter("name", "%"+name+"%");
        }
        return ((BigInteger) nativeQuery.getSingleResult()).intValue();
    }

    @Override
    public int getAllLanguagesCount(boolean showEmpty) {
        Query nativeQuery;
        if (showEmpty){
            nativeQuery = this.em.createNativeQuery("SELECT COUNT(id) FROM languages WHERE deleted = FALSE");
        } else {
            nativeQuery = this.em.createNativeQuery("SELECT COUNT(DISTINCT s.language_id) FROM snippets AS s INNER JOIN languages AS l ON s.language_id = l.id WHERE s.deleted = FALSE AND l.deleted = FALSE");
        }
        return ((BigInteger) nativeQuery.getSingleResult()).intValue();
    }

    @Override
    public Language addLanguage(String name) {
        Language language;
        Optional<Language> maybeLang = findByName(name);
        if (maybeLang.isPresent()) {
            language = maybeLang.get();
            language.setDeleted(false);
        } else {
            language = new Language(name);
        }
        this.em.persist(language);
        return language;
    }

    @Override
    public void addLanguages(List<String> languages) {
        if (languages != null) {
            for (String name : languages) {
                this.addLanguage(name);
            }
        }
    }

    @Override
    public void removeLanguage(final long langId) {
        Optional<Language> lang = this.findById(langId);
        if (lang.isPresent()) {
            lang.get().setDeleted(true);
            this.em.persist(lang.get());
        }
    }

}

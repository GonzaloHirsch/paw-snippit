package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.LanguageDao;
import ar.edu.itba.paw.models.Language;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
        final TypedQuery<Language> query = this.em.createQuery("FROM Language AS t WHERE t.name = :name", Language.class);
        query.setParameter("name", name);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Collection<Language> getAllLanguages() {
        return this.em.createQuery("FROM Language", Language.class).getResultList();
    }

    @Override
    public Collection<Language> getAllLanguages(boolean showEmpty, int page, int pageSize) {
        Query nativeQuery;
        if (showEmpty){
            nativeQuery = this.em.createNativeQuery("SELECT DISTINCT id, name FROM languages ORDER BY name ASC");
        } else {
            nativeQuery = this.em.createNativeQuery("SELECT DISTINCT s.language_id, l.name FROM snippets AS s INNER JOIN languages AS l ON s.language_id = l.id ORDER BY l.name ASC");
        }
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<Long> filteredIds = ((List<Object[]>) nativeQuery.getResultList())
                .stream().map(i -> ((Integer) i[0]).longValue()).collect(Collectors.toList());
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
            nativeQuery = this.em.createNativeQuery("SELECT DISTINCT id, name FROM languages WHERE LOWER(name) LIKE LOWER(:name) ORDER BY name ASC")
                    .setParameter("name", "%"+name+"%");
        } else {
            nativeQuery = this.em.createNativeQuery("SELECT DISTINCT s.language_id, l.name FROM snippets AS s INNER JOIN languages AS l ON s.language_id = l.id WHERE LOWER(l.name) LIKE LOWER(:name) ORDER BY l.name ASC")
                    .setParameter("name", "%"+name+"%");
        }
        nativeQuery.setFirstResult((page - 1) * pageSize);
        nativeQuery.setMaxResults(pageSize);
        List<Long> filteredIds = ((List<Object[]>) nativeQuery.getResultList())
                .stream().map(i -> ((Integer) i[0]).longValue()).collect(Collectors.toList());
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
            nativeQuery = this.em.createNativeQuery("SELECT DISTINCT id FROM languages WHERE LOWER(name) LIKE LOWER(:name)")
                    .setParameter("name", "%"+name+"%");
        } else {
            nativeQuery = this.em.createNativeQuery("SELECT DISTINCT s.language_id FROM snippets AS s INNER JOIN languages AS l ON s.language_id = l.id WHERE LOWER(l.name) LIKE LOWER(:name)")
                    .setParameter("name", "%"+name+"%");
        }
        return nativeQuery.getResultList().size();
    }

    @Override
    public int getAllLanguagesCount(boolean showEmpty) {
        Query nativeQuery;
        if (showEmpty){
            nativeQuery = this.em.createNativeQuery("SELECT id FROM languages");
        } else {
            nativeQuery = this.em.createNativeQuery("SELECT DISTINCT language_id FROM snippets");
        }
        return nativeQuery.getResultList().size();
    }

    @Override
    public Language addLanguage(String name) {
        final Language language = new Language(name);
        em.persist(language);
        return language;
    }

    @Override
    public void addLanguages(List<String> languages) {
        for (String name : languages) {
            this.addLanguage(name);
        }
    }

    @Override
    public void removeLanguage(final long langId) {
        Optional<Language> lang = this.findById(langId);
        if (lang.isPresent() && lang.get().getSnippetsUsing().isEmpty()) {
            this.em.remove(lang.get());
        }
    }

    @Override
    public boolean languageInUse(final long langId) {
        Optional<Language> lang = this.findById(langId);
        return lang.filter(language -> !language.getSnippetsUsing().isEmpty()).isPresent();
    }
}

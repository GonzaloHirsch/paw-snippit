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
    public Collection<Language> getAllLanguages(int page, int pageSize) {
        Query nativeQuery = this.em.createNativeQuery("SELECT id FROM languages ORDER BY name ASC");
        final List<Long> filteredIds = this.filterIdsForPaging(nativeQuery, page, pageSize);
        if (!filteredIds.isEmpty()){
            final TypedQuery<Language> query = this.em.createQuery("FROM Language WHERE id IN :filteredIds ORDER BY name ASC", Language.class);
            query.setParameter("filteredIds", filteredIds);
            return query.getResultList();
        }
        return Collections.emptyList();
    }

    @Override
    public Collection<Language> findAllLanguagesByName(String name, int page, int pageSize) {
        Query nativeQuery = this.em.createNativeQuery("SELECT id FROM languages WHERE LOWER(name) LIKE LOWER(:name) ORDER BY name ASC");
        nativeQuery.setParameter("name", "%"+name+"%");
        final List<Long> filteredIds = this.filterIdsForPaging(nativeQuery, page, pageSize);
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
    public int getAllLanguagesCountByName(String name) {
        final Query query = this.em.createNativeQuery("SELECT id FROM languages WHERE LOWER(name) LIKE LOWER(:name)");
        query.setParameter("name", "%"+name+"%");
        return query.getResultList().size();
    }

    @Override
    public int getAllLanguagesCount() {
        Query nativeQuery = this.em.createNativeQuery("SELECT id FROM languages");
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

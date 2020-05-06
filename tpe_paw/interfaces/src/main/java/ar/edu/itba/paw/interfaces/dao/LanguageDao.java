package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Language;
import ar.edu.itba.paw.models.Tag;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LanguageDao {
    int PAGE_SIZE = 15;
    Optional<Language> findById(long id);
    Optional<Language> findByName(String name);
    Collection<Language> getAllLanguages();
    Collection<Language> getAllLanguages(int page);
    Collection<Language> findAllLanguagesByName(String name, int page);
    int getAllLanguagesCountByName(String name);
    int getAllLanguagesCount();
    Language addLanguage(String lang);
    void addLanguages(List<String> languages);
    void removeLanguage(final long langId);
    boolean languageInUse(final long langId);
}

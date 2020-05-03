package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Language;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LanguageDao {
    Optional<Language> findById(long id);
    Optional<Language> findByName(String name);
    Collection<Language> getAll();
    Language addLanguage(String lang);
    void addLanguages(List<String> languages);
    void removeLanguage(final long langId);
    boolean languageInUse(final long langId);
}

package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Language;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LanguageService {
    Optional<Language> findById(long id);
    Collection<Language> getAllLanguages();
    Collection<Language> getAllLanguages(int page, int pageSize);
    Collection<Language> findAllLanguagesByName(String name, int page, int pageSize);
    int getAllLanguagesCountByName(String name);
    int getAllLanguagesCount();
    void addLanguages(List<String> languages);
    boolean languageExists(final String language);
    boolean languageExists(final long id);
    void removeLanguage(final long langId);
    boolean languageInUse(final long langId);
}

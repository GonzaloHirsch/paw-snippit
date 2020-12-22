package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Language;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LanguageService {
    Optional<Language> findById(long id);
    Collection<Language> getAllLanguages();
    Collection<Language> getAllLanguages(boolean showEmpty, int page, int pageSize);
    Collection<Language> findAllLanguagesByName(String name, boolean showEmpty, int page, int pageSize);
    int getAllLanguagesCountByName(String name, boolean showEmpty);
    int getAllLanguagesCount(boolean showEmpty);
    void addLanguages(List<String> languages);
    long addLanguage(String language);
    boolean languageExists(final String language);
    boolean languageExists(final long id);
    void removeLanguage(final long langId);
}

package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Language;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LanguageService {
    Optional<Language> findById(long id);
    Collection<Language> getAll();
    void addLanguages(List<String> languages);
    boolean languageExists(final String language);
    boolean languageExists(final long id);
    void removeLanguage(final long langId);
    boolean languageInUse(final long langId);
}

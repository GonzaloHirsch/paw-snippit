package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Language;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LanguageService {
    Optional<Language> findById(long id);
    Collection<Language> getAll();
    void addLanguages(List<String> languages);
    boolean isUnique(String language);
    void removeLanguage(final long langId);
    boolean languageInUse(final long langId);
}

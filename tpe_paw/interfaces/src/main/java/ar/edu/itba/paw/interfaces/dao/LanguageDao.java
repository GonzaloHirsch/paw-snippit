package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Language;
import ar.edu.itba.paw.models.Tag;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LanguageDao {
    Optional<Language> findById(long id);
    Optional<Language> findByName(String name);
    Collection<Language> getAllLanguages();
    Collection<Language> getAllLanguages(boolean showEmpty, int page, int pageSize);
    Collection<Language> findAllLanguagesByName(String name, boolean showEmpty, int page, int pageSize);
    int getAllLanguagesCountByName(String name, boolean showEmpty);
    int getAllLanguagesCount(boolean showEmpty);
    Language addLanguage(String lang);
    void addLanguages(List<String> languages);
    void removeLanguage(final long langId);
    boolean languageInUse(final long langId);
}

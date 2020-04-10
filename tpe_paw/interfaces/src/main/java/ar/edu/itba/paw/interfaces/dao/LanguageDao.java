package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Language;

import java.util.Collection;
import java.util.Optional;

public interface LanguageDao {
    Optional<Language> findById(long id);
    Collection<Language> getAll();
}

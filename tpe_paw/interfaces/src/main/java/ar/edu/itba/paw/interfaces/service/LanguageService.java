package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Language;

import java.util.Collection;
import java.util.Optional;

public interface LanguageService {
    Optional<Language> findById(long id);
    Collection<Language> getAll();
}

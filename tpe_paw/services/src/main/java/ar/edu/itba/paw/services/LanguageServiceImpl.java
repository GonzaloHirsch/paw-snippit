package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.LanguageDao;
import ar.edu.itba.paw.interfaces.service.LanguageService;
import ar.edu.itba.paw.models.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class LanguageServiceImpl implements LanguageService {

    @Autowired
    private LanguageDao languageDao;

    @Override
    public Optional<Language> findById(long id) {
        return languageDao.findById(id);
    }

    @Override
    public Collection<Language> getAllLanguages() {
        return languageDao.getAllLanguages();
    }

    @Override
    public Collection<Language> getAllLanguages(int page, int pageSize) {
        return this.languageDao.getAllLanguages(page, pageSize);
    }

    @Override
    public Collection<Language> findAllLanguagesByName(String name, int page, int pageSize) {
        return this.languageDao.findAllLanguagesByName(name, page, pageSize);
    }

    @Override
    public int getAllLanguagesCountByName(String name) {
        return this.languageDao.getAllLanguagesCountByName(name);
    }

    @Override
    public int getAllLanguagesCount() {
        return this.languageDao.getAllLanguagesCount();
    }

    @Override
    public void addLanguages(List<String> languages) {
        languageDao.addLanguages(languages);
    }

    @Override
    public boolean languageExists(String language) {
        return languageDao.findByName(language).isPresent();
    }

    @Override
    public boolean languageExists(long id) {
        return languageDao.findById(id).isPresent();
    }

    @Override
    public void removeLanguage(final long langId) {
        this.languageDao.removeLanguage(langId);
    }

    @Override
    public boolean languageInUse(final long langId) {
        return this.languageDao.languageInUse(langId);
    }

}

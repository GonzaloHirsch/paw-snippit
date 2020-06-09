package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.LanguageDao;
import ar.edu.itba.paw.models.Language;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class LanguageDaoTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private LanguageDao languageDao;

    @Before
    public void setUp() {
    }

    @Test
    public void testAddLanguage(){
        Language language = TestMethods.insertLanguage(this.em, TestConstants.LANGUAGE);
        assertTrue(language.getId() > 0);
    }

    @Test
    public void testAddLanguageS(){
        List<String> languages = Arrays.asList(TestConstants.LANGUAGE, TestConstants.LANGUAGE2, TestConstants.LANGUAGE3);
        this.languageDao.addLanguages(languages);

        // TODO: VER COMO HACER
    }


    @Test
    public void testAddLanguagesEmpty(){
        List<String> stringList = Collections.emptyList();
        this.languageDao.addLanguages(stringList);

        // TODO: VER COMO HACER
    }

    @Test
    public void testGetAll(){
        Language lang1 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE);
        Language lang2 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        Language lang3 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE3);
        Collection<Language> langs = this.languageDao.getAllLanguages();

        assertFalse(langs.isEmpty());
        assertEquals(3, langs.size());
        List<Long> langIds = langs.stream().mapToLong(Language::getId).boxed().collect(Collectors.toList());
        assertTrue(langIds.contains(lang1.getId()));
        assertTrue(langIds.contains(lang2.getId()));
        assertTrue(langIds.contains(lang3.getId()));
    }

    @Test
    public void testGetAllEmpty(){
        Collection<Language> maybeLanguages = this.languageDao.getAllLanguages();
        assertTrue(maybeLanguages.isEmpty());
    }

    @Test
    public void testFindById(){
        Language language = TestMethods.insertLanguage(this.em, TestConstants.LANGUAGE);

        Optional<Language> maybeLanguage = languageDao.findById(language.getId());

        assertTrue(maybeLanguage.isPresent());
        assertEquals(language.getId(), maybeLanguage.get().getId());
        assertEquals(TestConstants.LANGUAGE, maybeLanguage.get().getName());
    }

    @Test
    public void testFindByNonExistentId(){
        Language language = TestMethods.insertLanguage(this.em, TestConstants.LANGUAGE);
        Optional<Language> maybeLanguage = languageDao.findById(TestConstants.INVALID_LANGUAGE_ID);
        assertFalse(maybeLanguage.isPresent());
    }

    @Test
    public void testFindByName(){
        Language language = TestMethods.insertLanguage(this.em, TestConstants.LANGUAGE);
        Optional<Language> maybeLanguage = languageDao.findByName(TestConstants.LANGUAGE);

        assertTrue(maybeLanguage.isPresent());
        assertEquals(language.getId(), maybeLanguage.get().getId());
        assertEquals(TestConstants.LANGUAGE, maybeLanguage.get().getName());
    }

    @Test
    public void testFindByNonExistentName(){
        Language language = TestMethods.insertLanguage(this.em, TestConstants.LANGUAGE);
        Optional<Language> maybeLanguage = languageDao.findByName(TestConstants.INVALID_LANGUAGE_NAME);
        assertFalse(maybeLanguage.isPresent());
    }

    @Test
    public void testFindAllLanguagesByName(){
        Language lang1 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE);
        Language lang2 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        Language lang3 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE3);
        Collection<Language> maybeCollection = languageDao.findAllLanguagesByName(TestConstants.VALID_LANGUAGE_NAME_SEARCH, true,1, 20);
        assertNotNull(maybeCollection);
        List<Long> maybeIdList = maybeCollection.stream().mapToLong(Language::getId).boxed().collect(Collectors.toList());
        assertTrue(maybeIdList.contains(lang1.getId()));
        assertTrue(maybeIdList.contains(lang2.getId()));
        assertTrue(maybeIdList.contains(lang3.getId()));
    }

    @Test
    public void testFindAllLanguagesByNonExistentName(){
        Language lang1 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE);
        Language lang2 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        Language lang3 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE3);
        Collection<Language> maybeCollection = languageDao.findAllLanguagesByName(TestConstants.INVALID_LANGUAGE_NAME_SEARCH, true,1, 20);
        assertNotNull(maybeCollection);
        assertTrue(maybeCollection.isEmpty());
    }

    @Test
    public void testRemoveLanguage(){
        Language lang = TestMethods.insertLanguage(em, TestConstants.LANGUAGE);

        languageDao.removeLanguage(lang.getId());

        assertTrue(lang.isDeleted());
    }

    @Test
    public void testRemoveLanguageEmpty(){
        Language lang = TestMethods.insertLanguage(em, TestConstants.LANGUAGE);

        languageDao.removeLanguage(TestConstants.INVALID_LANGUAGE_ID);

        assertFalse(lang.isDeleted());
    }

    @Test
    public void testGetAllLanguagesByNameCount(){
        Language lang1 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE);
        Language lang2 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        Language lang3 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE3);
        int langCount = languageDao.getAllLanguagesCountByName(TestConstants.VALID_LANGUAGE_NAME_SEARCH, true);
        assertEquals(3, langCount);
    }

    @Test
    public void testGetAllLanguagesByNonExistentNameCount(){
        Language lang1 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE);
        Language lang2 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        Language lang3 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE3);
        int langCount = languageDao.getAllLanguagesCountByName(TestConstants.INVALID_LANGUAGE_NAME_SEARCH, true);
        assertEquals(0, langCount);
    }
}

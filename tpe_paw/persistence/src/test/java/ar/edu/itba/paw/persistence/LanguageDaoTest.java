package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.LanguageDao;
import ar.edu.itba.paw.models.Language;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


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
    public void addLanguageTest(){
        Language language = TestMethods.insertLanguage(this.em, TestConstants.LANGUAGE);
        Assert.assertTrue(language.getId() > 0);
    }

    @Test
    public void addLanguagesTest(){
        List<String> languages = Arrays.asList(TestConstants.LANGUAGE, TestConstants.LANGUAGE2, TestConstants.LANGUAGE3);
        this.languageDao.addLanguages(languages);

        // TODO: VER COMO HACER
    }

    @Test
    public void addLanguagesEmptyTest(){
        List<String> stringList = Collections.emptyList();
        this.languageDao.addLanguages(stringList);

        // TODO: VER COMO HACER
    }

    @Test
    public void getAllTest(){
        Language lang1 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE);
        Language lang2 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        Language lang3 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE3);
        Collection<Language> langs = this.languageDao.getAllLanguages();

        Assert.assertFalse(langs.isEmpty());
        Assert.assertEquals(3, langs.size());
        List<Long> langIds = langs.stream().mapToLong(Language::getId).boxed().collect(Collectors.toList());
        Assert.assertTrue(langIds.contains(lang1.getId()));
        Assert.assertTrue(langIds.contains(lang2.getId()));
        Assert.assertTrue(langIds.contains(lang3.getId()));
    }

    @Test
    public void getAllEmptyTest(){
        Collection<Language> maybeLanguages = this.languageDao.getAllLanguages();
        Assert.assertTrue(maybeLanguages.isEmpty());
    }

    @Test
    public void findByIdTest(){
        Language language = TestMethods.insertLanguage(this.em, TestConstants.LANGUAGE);

        Optional<Language> maybeLanguage = languageDao.findById(language.getId());

        Assert.assertTrue(maybeLanguage.isPresent());
        Assert.assertEquals(language.getId(), maybeLanguage.get().getId());
        Assert.assertEquals(TestConstants.LANGUAGE, maybeLanguage.get().getName());
    }

    @Test
    public void findByNonExistentIdTest(){
        TestMethods.insertLanguage(this.em, TestConstants.LANGUAGE);
        Optional<Language> maybeLanguage = languageDao.findById(TestConstants.INVALID_LANGUAGE_ID);
        Assert.assertFalse(maybeLanguage.isPresent());
    }

    @Test
    public void findByNameTest(){
        Language language = TestMethods.insertLanguage(this.em, TestConstants.LANGUAGE);
        Optional<Language> maybeLanguage = languageDao.findByName(TestConstants.LANGUAGE);

        Assert.assertTrue(maybeLanguage.isPresent());
        Assert.assertEquals(language.getId(), maybeLanguage.get().getId());
        Assert.assertEquals(TestConstants.LANGUAGE, maybeLanguage.get().getName());
    }

    @Test
    public void findByNonExistentNameTest(){
        TestMethods.insertLanguage(this.em, TestConstants.LANGUAGE);
        Optional<Language> maybeLanguage = languageDao.findByName(TestConstants.INVALID_LANGUAGE_NAME);
        Assert.assertFalse(maybeLanguage.isPresent());
    }

    @Test
    public void findAllLanguagesByNameTest(){
        Language lang1 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE);
        Language lang2 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        Language lang3 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE3);

        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        Snippet activeSnippet = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), lang1, Collections.emptyList(), TestConstants.SNIPPET_FLAGGED, false);
        Snippet deletedSnippet = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), lang2, Collections.emptyList(), false, TestConstants.SNIPPET_DELETED);

        TestMethods.setSnippetsToLanguage(em, lang1, Collections.singletonList(activeSnippet));
        TestMethods.setSnippetsToLanguage(em, lang2, Collections.singletonList(deletedSnippet));

        Collection<Language> collection = languageDao.findAllLanguagesByName(TestConstants.VALID_LANGUAGE_NAME_SEARCH, true,1, 20);
        Assert.assertNotNull(collection);
        Assert.assertTrue(collection.contains(lang1));
        Assert.assertTrue(collection.contains(lang2));
        Assert.assertTrue(collection.contains(lang3));
    }

    @Test
    public void getAllLanguagesByNameHideEmptyTest(){
        Language lang1 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE);
        Language lang2 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        TestMethods.insertLanguage(em, TestConstants.LANGUAGE3);

        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        Snippet activeSnippet = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), lang1, Collections.emptyList(), TestConstants.SNIPPET_FLAGGED, false);
        Snippet deletedSnippet = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), lang2, Collections.emptyList(), false, TestConstants.SNIPPET_DELETED);

        TestMethods.setSnippetsToLanguage(em, lang1, Collections.singletonList(activeSnippet));
        TestMethods.setSnippetsToLanguage(em, lang2, Collections.singletonList(deletedSnippet));
        Collection<Language> collection = languageDao.findAllLanguagesByName(TestConstants.VALID_LANGUAGE_NAME_SEARCH, false,1, TestConstants.LANGUAGE_PAGE_SIZE);
        Assert.assertNotNull(collection);
        Assert.assertEquals(1, collection.size());
        Assert.assertTrue(collection.contains(lang1));
    }


    @Test
    public void findAllLanguagesByNonExistentNameTest(){
        TestMethods.insertLanguage(em, TestConstants.LANGUAGE);
        TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        TestMethods.insertLanguage(em, TestConstants.LANGUAGE3);
        Collection<Language> collection = languageDao.findAllLanguagesByName(TestConstants.INVALID_LANGUAGE_NAME_SEARCH, true,1, 20);
        Assert.assertNotNull(collection);
        Assert.assertTrue(collection.isEmpty());
    }

    @Test
    public void RemoveLanguageTest(){
        Language lang = TestMethods.insertLanguage(em, TestConstants.LANGUAGE);
        languageDao.removeLanguage(lang.getId());
        Assert.assertTrue(lang.isDeleted());
    }

    @Test
    public void removeLanguageEmptyTest(){
        Language lang = TestMethods.insertLanguage(em, TestConstants.LANGUAGE);
        languageDao.removeLanguage(TestConstants.INVALID_LANGUAGE_ID);
        Assert.assertFalse(lang.isDeleted());
    }

    @Test
    public void getAllLanguagesByNameCountTest(){
        Language lang1 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE);
        Language lang2 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        TestMethods.insertLanguage(em, TestConstants.LANGUAGE3);

        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        Snippet activeSnippet = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), lang1, Collections.emptyList(), TestConstants.SNIPPET_FLAGGED, false);
        Snippet deletedSnippet = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), lang2, Collections.emptyList(), false, TestConstants.SNIPPET_DELETED);

        TestMethods.setSnippetsToLanguage(em, lang1, Collections.singletonList(activeSnippet));
        TestMethods.setSnippetsToLanguage(em, lang2, Collections.singletonList(deletedSnippet));

        int langCount = languageDao.getAllLanguagesCountByName(TestConstants.VALID_LANGUAGE_NAME_SEARCH, true);
        Assert.assertEquals(3, langCount);
    }

    @Test
    public void getAllLanguagesByNameCountHideEmptyTest(){
        Language lang1 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE);
        Language lang2 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        TestMethods.insertLanguage(em, TestConstants.LANGUAGE3);

        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        Snippet activeSnippet = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), lang1, Collections.emptyList(), TestConstants.SNIPPET_FLAGGED, false);
        Snippet deletedSnippet = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), lang2, Collections.emptyList(), false, TestConstants.SNIPPET_DELETED);

        TestMethods.setSnippetsToLanguage(em, lang1, Collections.singletonList(activeSnippet));
        TestMethods.setSnippetsToLanguage(em, lang2, Collections.singletonList(deletedSnippet));
        int langCount = languageDao.getAllLanguagesCountByName(TestConstants.VALID_LANGUAGE_NAME_SEARCH, false);
        Assert.assertEquals(1, langCount);
    }

    @Test
    public void getAllLanguagesByNameCountSpecificLangTest(){
        TestMethods.insertLanguage(em, TestConstants.LANGUAGE);
        TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        TestMethods.insertLanguage(em, TestConstants.LANGUAGE3);
        int langCount = languageDao.getAllLanguagesCountByName(TestConstants.LANGUAGE2, true);
        Assert.assertEquals(1, langCount);
    }

    @Test
    public void getAllLanguagesByNameCountSpecificLangHideEmptyTest(){
        TestMethods.insertLanguage(em, TestConstants.LANGUAGE);
        int langCount = languageDao.getAllLanguagesCountByName(TestConstants.LANGUAGE, false);
        Assert.assertEquals(0, langCount);
    }

    @Test
    public void getAllLanguagesByNonExistentNameCountTest(){
        TestMethods.insertLanguage(em, TestConstants.LANGUAGE);
        TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        TestMethods.insertLanguage(em, TestConstants.LANGUAGE3);
        int langCount = languageDao.getAllLanguagesCountByName(TestConstants.INVALID_LANGUAGE_NAME_SEARCH, true);
        Assert.assertEquals(0, langCount);
    }
}

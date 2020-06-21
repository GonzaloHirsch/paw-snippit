package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.FavoriteDao;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class FavoriteDaoTest {

    @Autowired
    private FavoriteDao favoriteDao;

    @PersistenceContext
    private EntityManager em;

    private User verifiedUser;
    private Language defaultLang;

    @Before
    public void setup() {
        this.verifiedUser = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL, TestConstants.USER_DATE, TestConstants.LOCALE_EN, TestConstants.USER_VERIFIED);
        this.defaultLang = TestMethods.insertLanguage(em, TestConstants.LANGUAGE);
    }

    @Test
    public void addToFavoritesRegularTest () {
        Snippet regularSnip = TestMethods.insertSnippet(em, verifiedUser, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, TestConstants.DATE_1, defaultLang, Collections.emptyList(), false, false);

        favoriteDao.addToFavorites(verifiedUser.getId(), regularSnip.getId());

        Assert.assertEquals(1, regularSnip.getUserFavorites().size());
        Assert.assertEquals(1, verifiedUser.getFavorites().size());
    }

    @Test
    public void removeFromFavoritesRegularTest () {
        Snippet regularSnip = TestMethods.insertSnippet(em, verifiedUser, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, TestConstants.DATE_1, defaultLang, Collections.emptyList(), false, false);
        // Needed to be able to perform removal
        List<Snippet> newList = new ArrayList<>();
        newList.add(regularSnip);
        TestMethods.setUserFavoriteSnippets(em, verifiedUser, newList);
        // Not part of the Unit testing flow but needed to make sure snippet
        // has been inserted and test is not passing just because list had
        // been empty all the time
        Assert.assertEquals(1, verifiedUser.getFavorites().size());

        favoriteDao.removeFromFavorites(verifiedUser.getId(), regularSnip.getId());

        Assert.assertEquals(0, regularSnip.getUserFavorites().size());
        Assert.assertEquals(0, verifiedUser.getFavorites().size());
    }

    @Test
    public void removeDeletedSnippetFromFavoritesRegularTest () {
        Snippet deletedSnippet = TestMethods.insertSnippet(em, verifiedUser, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, TestConstants.DATE_1, defaultLang, Collections.emptyList(), false, true);
        // Needed to be able to perform removal
        List<Snippet> newList = new ArrayList<>();
        newList.add(deletedSnippet);
        TestMethods.setUserFavoriteSnippets(em, verifiedUser, newList);
        // Not part of the Unit testing flow but needed to make sure snippet
        // has been inserted and test is not passing just because list had
        // been empty all the time
        Assert.assertEquals(1, verifiedUser.getFavorites().size());

        favoriteDao.removeFromFavorites(verifiedUser.getId(), deletedSnippet.getId());

        Assert.assertEquals(0, deletedSnippet.getUserFavorites().size());
        Assert.assertEquals(0, verifiedUser.getFavorites().size());
    }

    @Test
    public void removeFlaggedSnippetFromFavoritesRegularTest () {
        Snippet flaggedSnippet = TestMethods.insertSnippet(em, verifiedUser, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, TestConstants.DATE_1, defaultLang, Collections.emptyList(), true, false);
        // Needed to be able to perform removal
        List<Snippet> newList = new ArrayList<>();
        newList.add(flaggedSnippet);
        TestMethods.setUserFavoriteSnippets(em, verifiedUser, newList);
        // Not part of the Unit testing flow but needed to make sure snippet
        // has been inserted and test is not passing just because list had
        // been empty all the time
        Assert.assertEquals(1, verifiedUser.getFavorites().size());

        favoriteDao.removeFromFavorites(verifiedUser.getId(), flaggedSnippet.getId());

        Assert.assertEquals(0, flaggedSnippet.getUserFavorites().size());
        Assert.assertEquals(0, verifiedUser.getFavorites().size());
    }
}
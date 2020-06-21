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

//package ar.edu.itba.paw.persistence;
//
//import ar.edu.itba.paw.interfaces.dao.FavoriteDao;
//import ar.edu.itba.paw.models.Favorite;
//import ar.edu.itba.paw.models.User;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.jdbc.JdbcTestUtils;
//
//import javax.sql.DataSource;
//
//import java.util.Collection;
//import java.util.Optional;
//
//import static ar.edu.itba.paw.persistence.TestConstants.*;
//
//import static junit.framework.TestCase.*;
//
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
//public class FavoriteDaoTest {
//
//    @Autowired
//    private DataSource ds;
//
//    @Autowired
//    private FavoriteDao favoriteDao;
//
//    private JdbcTemplate jdbcTemplate;
//    private SimpleJdbcInsert jdbcInsertFavorite;
//
//    private long defaultSnippetId;
//    private User defaultUser;
//
//    @Before
//    public void setUp(){
//        jdbcTemplate = new JdbcTemplate(ds);
//
//        jdbcInsertFavorite = new SimpleJdbcInsert(ds).withTableName(FAVORITES_TABLE);
//        SimpleJdbcInsert jdbcInsertSnippet= new SimpleJdbcInsert(ds).withTableName(SNIPPETS_TABLE).usingGeneratedKeyColumns("id");
//        SimpleJdbcInsert jdbcInsertUser = new SimpleJdbcInsert(ds).withTableName(USERS_TABLE).usingGeneratedKeyColumns("id");
//        SimpleJdbcInsert jdbcInsertLanguage = new SimpleJdbcInsert(ds).withTableName(LANGUAGES_TABLE).usingGeneratedKeyColumns("id");
//
//
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,VOTES_FOR_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,USERS_TABLE);
//        defaultUser = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,DESCR,LOCALE_EN);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,LANGUAGES_TABLE);
//        long languageId = insertLanguageIntoDb(jdbcInsertLanguage,LANGUAGE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
//        defaultSnippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,languageId,0);
//    }
//
//  /*  @Test
//    public void testAddToFavorites(){
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,VOTES_FOR_TABLE);
//        FavoriteDao favoriteSpyDao = Mockito.spy(favoriteDao);
//       // Mockito.doReturn(Optional.empty()).when(favoriteSpyDao).getFavorite(defaultUser.getId(),defaultSnippetId);
//        // TODO getFavorites doesnt exists anymore
//        favoriteSpyDao.addToFavorites(defaultUser.getId(),defaultSnippetId);
//
//        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,FAVORITES_TABLE));
//    }*/
//
//    @Test
//    public void testAddToFavorites2(){
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,VOTES_FOR_TABLE);
//        FavoriteDao favoriteSpyDao = Mockito.spy(favoriteDao);
////        Mockito.doReturn(Optional.of(new Favorite(defaultUser.getId(),defaultSnippetId))).when(favoriteSpyDao).getFavorite(defaultUser.getId(),defaultSnippetId);
//
//        favoriteSpyDao.addToFavorites(defaultUser.getId(),defaultSnippetId);
//
//        assertEquals(0,JdbcTestUtils.countRowsInTable(jdbcTemplate,FAVORITES_TABLE));
//    }
//
//    @Test
//    public void testGetFavorite(){
////        JdbcTestUtils.deleteFromTables(jdbcTemplate,VOTES_FOR_TABLE);
////        insertFavoriteIntoDb(jdbcInsertFavorite,defaultSnippetId,defaultUser.getId());
////
////        Optional<Favorite> maybeFav = favoriteDao.getFavorite(defaultUser.getId(),defaultSnippetId);
////
////        assertTrue(maybeFav.isPresent());
////        assertEquals(defaultSnippetId,maybeFav.get().getSnippet());
////        assertEquals(defaultUser.getId(),maybeFav.get().getUser());
//    }
//
//    @Test
//    public void testGetFavoriteEmpty(){
////        Optional<Favorite> maybeFav = favoriteDao.getFavorite(defaultUser.getId(),defaultSnippetId);
////
////        assertFalse(maybeFav.isPresent());
//    }
//
//    @Test
//    public void testGetUserFavorites(){
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,VOTES_FOR_TABLE);
//        insertFavoriteIntoDb(jdbcInsertFavorite,defaultSnippetId,defaultUser.getId());
//
////        Collection<Favorite> maybeCollection = favoriteDao.getUserFavorites(defaultUser.getId());
////
////        assertNotNull(maybeCollection);
////        assertEquals(defaultSnippetId,maybeCollection.stream().findFirst().get().getSnippet());
////        assertEquals(defaultUser.getId(),maybeCollection.stream().findFirst().get().getUser());
//    }
////
////    @Test
////    public void testGetUserFavoritesEmpty(){
////        JdbcTestUtils.deleteFromTables(jdbcTemplate,VOTES_FOR_TABLE);
////        insertFavoriteIntoDb(jdbcInsertFavorite,defaultSnippetId,defaultUser.getId());
////
////        Collection<Favorite> maybeCollection = favoriteDao.getUserFavorites(defaultUser.getId()+10);
////
////        assertNotNull(maybeCollection);
////        assertEquals(0,maybeCollection.size());
////    }
//
//    @Test
//    public void removeFromFavorites(){
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,VOTES_FOR_TABLE);
//        insertFavoriteIntoDb(jdbcInsertFavorite,defaultSnippetId,defaultUser.getId());
//
//        favoriteDao.removeFromFavorites(defaultUser.getId(),defaultSnippetId);
//
//        assertEquals(0,JdbcTestUtils.countRowsInTable(jdbcTemplate,FAVORITES_TABLE));
//    }
//
//    @Test
//    public void removeFromFavoritesEmpty(){
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,VOTES_FOR_TABLE);
//        insertFavoriteIntoDb(jdbcInsertFavorite,defaultSnippetId,defaultUser.getId());
//
//        favoriteDao.removeFromFavorites(defaultUser.getId()+10,defaultSnippetId+10);
//
//        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,FAVORITES_TABLE));
//    }
//}
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
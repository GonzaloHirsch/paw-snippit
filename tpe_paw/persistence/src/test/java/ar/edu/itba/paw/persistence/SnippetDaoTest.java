package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.models.*;
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

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class SnippetDaoTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private SnippetDao snippetDao;

    private User owner;
    private Language language;
    private Tag tag;

    @Before
    public void setUp() {
        owner = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL, TestConstants.USER_DATE, TestConstants.LOCALE_EN, TestConstants.USER_VERIFIED);
        language = TestMethods.insertLanguage(em, TestConstants.LANGUAGE);
        tag = TestMethods.insertTag(em, TestConstants.TAG);
    }
    
    @Test
    public void testCreate() {
        final long snippetId = snippetDao.createSnippet(
                owner.getId(),
                TestConstants.SNIPPET_TITLE,
                TestConstants.SNIPPET_DESCR,
                TestConstants.SNIPPET_CODE,
                Timestamp.from(Instant.now()),
                language.getId(),
                Collections.emptyList()
        );
        Assert.assertTrue(snippetId >= 0);
    }

    @Test
    public void testFindSnippetById() {
        Snippet snippet = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), TestConstants.SNIPPET_FLAGGED, false);

        Optional<Snippet> maybeSnippet = snippetDao.findSnippetById(snippet.getId());
        assertTrue(maybeSnippet.isPresent());
        assertEquals(owner.getUsername(), maybeSnippet.get().getOwner().getUsername());
        assertEquals(TestConstants.SNIPPET_TITLE, maybeSnippet.get().getTitle());
        assertEquals(TestConstants.SNIPPET_DESCR, maybeSnippet.get().getDescription());
        assertTrue(maybeSnippet.get().getTags().contains(tag));
        assertTrue(maybeSnippet.get().isFlagged());
    }

    @Test
    public void deleteExistingSnippetByIdTest() {
        Snippet snippet = TestMethods.insertSnippet(
                em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag),
                false,
                false
        );
        Assert.assertFalse(snippet.isDeleted());
        Assert.assertTrue(snippetDao.deleteSnippetById(snippet.getId()));
        Assert.assertTrue(snippet.isDeleted());
    }

    @Test
    public void deleteDeletedSnippetByIdTest() {
        Snippet snippet = TestMethods.insertSnippet(
                em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag),
                false,
                true
        );
        Assert.assertTrue(snippet.isDeleted());
        Assert.assertTrue(snippetDao.deleteSnippetById(snippet.getId()));
        Assert.assertTrue(snippet.isDeleted());
    }

    @Test
    public void deleteNonExistingSnippetByIdTest() {
        Assert.assertFalse(snippetDao.deleteSnippetById(TestConstants.SNIPPET_INVALID_ID));
    }

    @Test
    public void restoreDeletedSnippetByIdTest() {
        Snippet snippet = TestMethods.insertSnippet(em,
                owner,
                TestConstants.SNIPPET_TITLE,
                TestConstants.SNIPPET_DESCR,
                TestConstants.SNIPPET_CODE,
                Timestamp.from(Instant.now()),
                language,
                Collections.singletonList(tag),
                false,
                true
        );
        Assert.assertTrue(snippet.isDeleted());
        Assert.assertTrue(snippetDao.restoreSnippetById(snippet.getId()));
        Assert.assertFalse(snippet.isDeleted());
    }

    @Test
    public void restoreNonExistingSnippetByIdTest() {
        Assert.assertFalse(snippetDao.restoreSnippetById(TestConstants.SNIPPET_INVALID_ID));
    }

    @Test
    public void flagDeletedSnippetTest() {
        Snippet snippet = TestMethods.insertSnippet(em,
                owner,
                TestConstants.SNIPPET_TITLE,
                TestConstants.SNIPPET_DESCR,
                TestConstants.SNIPPET_CODE,
                Timestamp.from(Instant.now()),
                language,
                Collections.emptyList(),
                false,
                true
        );
        Assert.assertFalse(snippet.isFlagged());
        Assert.assertTrue(snippetDao.flagSnippet(snippet.getId()));
        Assert.assertTrue(snippet.isFlagged());
        Assert.assertTrue(snippet.isDeleted());
    }

    @Test
    public void flagSnippetWhenFlaggedTest() {
        Snippet snippet = TestMethods.insertSnippet(
                em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(),
                true,
                false
        );
        Assert.assertTrue(snippet.isFlagged());
        Assert.assertTrue(snippetDao.flagSnippet(snippet.getId()));
        Assert.assertTrue(snippet.isFlagged());
    }


    @Test
    public void flagNonExistingSnippetTest() {
        Assert.assertFalse(snippetDao.flagSnippet(TestConstants.SNIPPET_INVALID_ID));
    }

    @Test
    public void unflagExistingSnippetTest() {
        Snippet snippet = TestMethods.insertSnippet(
                em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(),
                true,
                false
        );
        Assert.assertTrue(snippet.isFlagged());
        Assert.assertTrue(snippetDao.unflagSnippet(snippet.getId()));
        Assert.assertFalse(snippet.isFlagged());
    }

    @Test
    public void unflagNonExistingSnippetTest() {
        Assert.assertFalse(snippetDao.unflagSnippet(TestConstants.SNIPPET_INVALID_ID));
    }

    @Test
    public void getAllSnippetsCountTest() {
        assertEquals(0, snippetDao.getAllSnippetsCount());
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), true, false);
        assertEquals(1, snippetDao.getAllSnippetsCount());
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), true, false);
        assertEquals(2, snippetDao.getAllSnippetsCount());
    }

    @Test
    public void getAllFlaggedSnippetsCountTest() {
        User flaggedSnippetsOwner = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), false, false);
        assertEquals(0, snippetDao.getAllFlaggedSnippetsCount());
        TestMethods.insertSnippet(em, flaggedSnippetsOwner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), true, false);
        assertEquals(1, snippetDao.getAllFlaggedSnippetsCount());
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), false, false);
        assertEquals(1, snippetDao.getAllFlaggedSnippetsCount());
        TestMethods.insertSnippet(em, flaggedSnippetsOwner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), true, false);
        assertEquals(2, snippetDao.getAllFlaggedSnippetsCount());
    }

    @Test
    public void getAllDeletedSnippetsByOwnerCountTest() {
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), false, false);
        assertEquals(0, snippetDao.getAllDeletedSnippetsByOwnerCount(owner.getId()));
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), true, TestConstants.SNIPPET_DELETED);
        assertEquals(1, snippetDao.getAllDeletedSnippetsByOwnerCount(owner.getId()));
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), false, false);
        assertEquals(1, snippetDao.getAllDeletedSnippetsByOwnerCount(owner.getId()));
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), true, TestConstants.SNIPPET_DELETED);
        assertEquals(2, snippetDao.getAllDeletedSnippetsByOwnerCount(owner.getId()));
    }

    @Test
    public void getAllSnippetsByLanguageCountTest() {
        Language language2 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        Language language3 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE3);

        //language -> active
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), false, false);
        //language -> deleted
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), false, TestConstants.SNIPPET_DELETED);
        assertEquals(1, snippetDao.getAllSnippetsByLanguageCount(language.getId()));
        assertEquals(0, snippetDao.getAllSnippetsByLanguageCount(language2.getId()));
        assertEquals(0, snippetDao.getAllSnippetsByLanguageCount(language3.getId()));

        //language2 -> deleted
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language2, Collections.emptyList(), false, TestConstants.SNIPPET_DELETED);
        //language3 -> active
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language3, Collections.emptyList(), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language3, Collections.emptyList(), false, false);
        assertEquals(1, snippetDao.getAllSnippetsByLanguageCount(language.getId()));
        assertEquals(0, snippetDao.getAllSnippetsByLanguageCount(language2.getId()));
        assertEquals(2, snippetDao.getAllSnippetsByLanguageCount(language3.getId()));
    }

    @Test
    public void getAllFollowingSnippetsCountTest() {
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);
        TestMethods.setUserFollowingTags(em, user, Collections.singletonList(tag));

        //Snippet contain only tag
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
        assertEquals(2, snippetDao.getAllFollowingSnippetsCount(user.getId()));

        TestMethods.setUserFollowingTags(em, user, Arrays.asList(tag, tag2));
        TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag2), false, TestConstants.SNIPPET_DELETED);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Arrays.asList(tag, tag2), false, false);
        TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag2), false, false);
        assertEquals(4, snippetDao.getAllFollowingSnippetsCount(user.getId()));

        /* Unfollowing tag2 */
        TestMethods.setUserFollowingTags(em, user, Collections.singletonList(tag));
        assertEquals(3, snippetDao.getAllFollowingSnippetsCount(user.getId()));
    }

    @Test
    public void getAllFavoriteSnippetsCountTest() {
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        Snippet snip = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
        Snippet deletedSnip = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, TestConstants.SNIPPET_DELETED);
        TestMethods.setUserFavoriteSnippets(em, user, Arrays.asList(snip, deletedSnip));

        assertEquals(2, snippetDao.getAllFavoriteSnippetsCount(user.getId()));

        Snippet snip2 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag2), false, TestConstants.SNIPPET_DELETED);
        Snippet deletedSnip2 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Arrays.asList(tag, tag2), false, false);
        TestMethods.setUserFavoriteSnippets(em, user, Arrays.asList(snip2, deletedSnip, deletedSnip2));

        assertEquals(3, snippetDao.getAllFavoriteSnippetsCount(user.getId()));
    }

    @Test
    public void getAllSnippetsByOwnerCountTest() {
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
        //Deleted
        TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, TestConstants.SNIPPET_DELETED);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, TestConstants.SNIPPET_DELETED);

        assertEquals(0, snippetDao.getAllSnippetsByOwnerCount(user.getId()));
        assertEquals(2, snippetDao.getAllSnippetsByOwnerCount(owner.getId()));
    }

    @Test
    public void getAllSnippetsByTagCountTest() {
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);
        Snippet badSnippet = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
        Snippet goodSnippet = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag2), false, false);
        Snippet toBeDeletedSnippet = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Arrays.asList(tag, tag2), false, false);

        Vote userDownVote = TestMethods.insertVote(em, user, badSnippet, false);
        Vote userUpVote = TestMethods.insertVote(em, user, goodSnippet, true);
        Vote userUpVote2 = TestMethods.insertVote(em, user, toBeDeletedSnippet, true);

        assertEquals(2, snippetDao.getAllUpvotedSnippetsCount(user.getId()));
        toBeDeletedSnippet.setDeleted(true);
        assertEquals(1, snippetDao.getAllUpvotedSnippetsCount(user.getId()));
    }



    // TODO CHECK! FALLA!
//    @Test
//    public void getNewSnippetsForTagsCountTest() {
//        Instant firstSince = Instant.now();
//        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
//
//        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
//        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
//        TestMethods.setUserFollowingTags(em, owner, Collections.singletonList(tag));
//        assertEquals(2, snippetDao.getNewSnippetsForTagsCount(firstSince, Collections.singletonList(tag), owner.getId()));
//    }

//    @Test
//    @Transactional
//    public void testFindSnippetByCriteria(){
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
//        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE, defaultLanguageId,0);
//
//        Optional<Snippet> maybeSnippet = snippetDao.findSnippetByCriteria(
//                SnippetDao.Types.TITLE,
//                TITLE,
//                SnippetDao.Locations.HOME,
//                SnippetDao.Orders.ASC,
//                null,
//                null,
//                1,
//                6).stream().findFirst();
//
//        assertTrue(maybeSnippet.isPresent());
//        assertEquals(snippetId, (long)maybeSnippet.get().getId());
//        assertEquals(defaultUser.getUsername(), maybeSnippet.get().getOwner().getUsername());
//    }
//
//    @Test
//    public void testFindSnippetByCriteriaEmpty(){
//        Optional<Snippet> maybeSnippet = snippetDao.findSnippetByCriteria(
//                SnippetDao.Types.TITLE,
//                TITLE,
//                SnippetDao.Locations.HOME,
//                SnippetDao.Orders.ASC,
//                null,
//                null,
//                1,
//                6).stream().findFirst();
//
//        assertFalse(maybeSnippet.isPresent());
//    }
//
//    @Test
//    public void testFindSnippetByDeepCriteria(){
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
//        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
//
//        Optional<Snippet> maybeSnippet = snippetDao.findSnippetByDeepCriteria(null,
//                null,
//                0,
//                10,
//                0,
//                10,
//                defaultLanguageId,
//                null,
//                TITLE,
//                defaultUser.getUsername(),
//                SnippetDao.Orders.ASC,
//                SnippetDao.Types.ALL,
//                true,
//                1, 6).stream().findFirst();
//
//        assertTrue(maybeSnippet.isPresent());
//        assertEquals(TITLE,maybeSnippet.get().getTitle());
//
//    }
//
//    @Test
//    public void testFindSnippetByDeepCriteriaTest(){
//        Optional<Snippet> maybeSnippet = snippetDao.findSnippetByDeepCriteria(null,
//                null,
//                0,
//                10,
//                0,
//                10,
//                defaultLanguageId,
//                null,
//                TITLE,
//                defaultUser.getUsername(),
//                SnippetDao.Orders.ASC,
//                SnippetDao.Types.ALL,
//                true,
//                1, 6).stream().findFirst();
//
//        assertFalse(maybeSnippet.isPresent());
//
//    }
//
//    @Test
//    public void testGetAllSnippets(){
//        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
//        long snippetId2 = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
//
//        Collection<Snippet> maybeColl = snippetDao.getAllSnippets(1,PAGE_SIZE);
//
//        assertNotNull(maybeColl);
//        assertEquals(2,maybeColl.size());
//        List<Long> idList = maybeColl.stream().mapToLong(Snippet::getId).boxed().collect(Collectors.toList());
//        assertTrue(idList.contains(snippetId));
//        assertTrue(idList.contains(snippetId2));
//
//    }
//
//    @Test
//    public void testGetAllSnippetsEmpty(){
//        Collection<Snippet> maybeColl = snippetDao.getAllSnippets(1,PAGE_SIZE);
//
//        assertNotNull(maybeColl);
//        assertTrue(maybeColl.isEmpty());
//
//    }
//
//    @Test
//    public void testGetAllFavoriteSnippets(){
//        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
//        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
//        insertFavoriteIntoDb(jdbcInsertFavorite,snippetId,defaultUser.getId());
//
//        Collection<Snippet> maybeCollection = snippetDao.getAllFavoriteSnippets(defaultUser.getId(),1,PAGE_SIZE);
//
//        assertNotNull(maybeCollection);
//        assertEquals(1,maybeCollection.size());
//        Snippet s = (Snippet) maybeCollection.toArray()[0];
//        assertEquals(snippetId,(long)s.getId());
//    }
//
//   @Test
//    public void testGetAllFavoriteSnippetsEmpty(){
//        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
//
//        Collection<Snippet> maybeCollection = snippetDao.getAllFavoriteSnippets(defaultUser.getId(),1,PAGE_SIZE);
//
//        assertNotNull(maybeCollection);
//        assertTrue(maybeCollection.isEmpty());
//    }
//
//    @Test
//    public void testGetAllFollowingSnippets(){
//        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
//        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
//        insertFollowingIntoDb(jdbcInsertFollowing,defaultTag.getId(),defaultUser.getId());
//        insertSnippetTagIntoDb(jdbcInsertSnippetTags,snippetId,defaultTag.getId());
//
//        Collection<Snippet> maybeCollection = snippetDao.getAllFollowingSnippets(defaultUser.getId(),1,PAGE_SIZE);
//
//        assertNotNull(maybeCollection);
//        assertEquals(1,maybeCollection.size());
//        Snippet s = (Snippet) maybeCollection.toArray()[0];
//        assertEquals(snippetId,(long)s.getId());
//    }
//
//    @Test
//    public void testGetAllFollowingSnippetsEmpty(){
//        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
//
//        Collection<Snippet> maybeCollection = snippetDao.getAllFollowingSnippets(defaultUser.getId(),1,PAGE_SIZE);
//
//        assertNotNull(maybeCollection);
//        assertEquals(0,maybeCollection.size());
//    }
//
//    @Test
//    public void testGetAllUpvotedSnippets(){
//        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
//        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
//        insertVotesForIntoDb(jdbcInsertVote,snippetId,defaultUser.getId(),1);
//
//
//        Collection<Snippet> maybeCollection = snippetDao.getAllUpVotedSnippets(defaultUser.getId(),1,PAGE_SIZE);
//
//        assertNotNull(maybeCollection);
//        assertEquals(1,maybeCollection.size());
//        Snippet s = (Snippet) maybeCollection.toArray()[0];
//        assertEquals((long) snippetId,(long) s.getId());
//    }
//
//    @Test
//    public void testGetAllUpvotedSnippetsEmpty(){
//        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
//
//        Collection<Snippet> maybeCollection = snippetDao.getAllUpVotedSnippets(defaultUser.getId(),1,PAGE_SIZE);
//
//        assertNotNull(maybeCollection);
//        assertEquals(0,maybeCollection.size());
//    }
//
//    @Test
//    public void testGetAllFlaggedSnippets(){
//        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,1);
//
//        Collection<Snippet> maybeCollection = snippetDao.getAllFlaggedSnippets(1,PAGE_SIZE);
//
//        assertNotNull(maybeCollection);
//        assertEquals(1,maybeCollection.size());
//        Snippet s = (Snippet) maybeCollection.toArray()[0];
//        assertEquals(snippetId,(long)s.getId());
//    }
//
//    @Test
//    public void testGetAllFlaggedSnippetsEmpty(){
//        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
//
//        Collection<Snippet> maybeCollection = snippetDao.getAllFlaggedSnippets(1,PAGE_SIZE);
//
//        assertNotNull(maybeCollection);
//        assertEquals(0,maybeCollection.size());
//
//    }
//
//    @Test
//    public void testFindAllSnippetsByOwner(){
//        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
//        insertSnippetIntoDb(jdbcInsertSnippet,altUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
//
//        Collection<Snippet> maybeCollection = snippetDao.getAllSnippetsByOwner(defaultUser.getId(),1,PAGE_SIZE);
//
//        assertNotNull(maybeCollection);
//        assertEquals(1,maybeCollection.size());
//        Snippet s = (Snippet) maybeCollection.toArray()[0];
//        assertEquals(snippetId,(long)s.getId());
//    }
//
//    @Test
//    public void testFindAllSnippetsByOwnerEmpty(){
//      insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
//
//        Collection<Snippet> maybeCollection = snippetDao.getAllSnippetsByOwner(altUser.getId()+3,1,PAGE_SIZE);
//
//        assertNotNull(maybeCollection);
//        assertEquals(0,maybeCollection.size());
//
//    }
//
//    @Test
//    public void testFindSnippetsWithLanguageEmpty(){
//        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
//
//        Collection<Snippet> maybeCollection = snippetDao.getSnippetsWithLanguage(defaultLanguageId+33,1,PAGE_SIZE);
//
//        assertNotNull(maybeCollection);
//        assertEquals(0,maybeCollection.size());
//    }
//
//
//    @Test
//    public void testDeleteSnippetByIdEmpty(){
//        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
//
//        snippetDao.deleteSnippetById(snippetId+10);
//
//        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate,SNIPPETS_TABLE));
//
//    }
//
//    @Test
//    public void testFindSnippetForTag() {
//        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(), TITLE, DESCR, CODE, defaultLanguageId,0);
//        insertSnippetTagIntoDb(jdbcInsertSnippetTags,snippetId,defaultTag.getId());
//
//        Optional<Snippet> snippet = snippetDao.findSnippetsForTag(defaultTag.getId(), 1, 10).stream().findFirst();
//
//        assertTrue(snippet.isPresent());
//        assertEquals(snippetId,(long)snippet.get().getId());
//    }
//
//    @Test
//    public void testFindSnippetForTagEmpty() {
//        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(), TITLE, DESCR, CODE, defaultLanguageId,0);
//        insertSnippetTagIntoDb(jdbcInsertSnippetTags,snippetId,defaultTag.getId());
//
//        Optional<Snippet> snippet = snippetDao.findSnippetsForTag(defaultTag.getId()+10, 1, 10).stream().findFirst();
//
//        assertFalse(snippet.isPresent());
//    }
//
//    @Test
//    public void testGetNewSnippetForTagsCount(){
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
//        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
//        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE2,DESCR,CODE,defaultLanguageId,0);
//
//        Instant d = Instant.now().minus(7, ChronoUnit.DAYS);
//        int snippetCount = snippetDao.getNewSnippetsForTagsCount(d, new ArrayList<Tag>(), 1);
//
//        assertEquals(0,snippetCount);
//    }
}

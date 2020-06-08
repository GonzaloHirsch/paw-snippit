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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

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
        Assert.assertTrue(maybeSnippet.isPresent());
        Assert.assertEquals(owner.getUsername(), maybeSnippet.get().getOwner().getUsername());
        Assert.assertEquals(TestConstants.SNIPPET_TITLE, maybeSnippet.get().getTitle());
        Assert.assertEquals(TestConstants.SNIPPET_DESCR, maybeSnippet.get().getDescription());
        Assert.assertTrue(maybeSnippet.get().getTags().contains(tag));
        Assert.assertTrue(maybeSnippet.get().isFlagged());
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
        Assert.assertEquals(0, snippetDao.getAllSnippetsCount());
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), true, false);
        Assert.assertEquals(1, snippetDao.getAllSnippetsCount());
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), true, false);
        Assert.assertEquals(2, snippetDao.getAllSnippetsCount());
    }

    @Test
    public void getAllFlaggedSnippetsCountTest() {
        User flaggedSnippetsOwner = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), false, false);
        Assert.assertEquals(0, snippetDao.getAllFlaggedSnippetsCount());
        TestMethods.insertSnippet(em, flaggedSnippetsOwner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), true, false);
        Assert.assertEquals(1, snippetDao.getAllFlaggedSnippetsCount());
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), false, false);
        Assert.assertEquals(1, snippetDao.getAllFlaggedSnippetsCount());
        TestMethods.insertSnippet(em, flaggedSnippetsOwner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), true, false);
        Assert.assertEquals(2, snippetDao.getAllFlaggedSnippetsCount());
    }

    @Test
    public void getAllDeletedSnippetsByOwnerCountTest() {
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), false, false);
        Assert.assertEquals(0, snippetDao.getAllDeletedSnippetsByOwnerCount(owner.getId()));
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), true, TestConstants.SNIPPET_DELETED);
        Assert.assertEquals(1, snippetDao.getAllDeletedSnippetsByOwnerCount(owner.getId()));
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), false, false);
        Assert.assertEquals(1, snippetDao.getAllDeletedSnippetsByOwnerCount(owner.getId()));
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), true, TestConstants.SNIPPET_DELETED);
        Assert.assertEquals(2, snippetDao.getAllDeletedSnippetsByOwnerCount(owner.getId()));
    }

    @Test
    public void getAllSnippetsByLanguageCountTest() {
        Language language2 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        Language language3 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE3);

        //language -> active
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), false, false);
        //language -> deleted
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), false, TestConstants.SNIPPET_DELETED);
        Assert.assertEquals(1, snippetDao.getAllSnippetsByLanguageCount(language.getId()));
        Assert.assertEquals(0, snippetDao.getAllSnippetsByLanguageCount(language2.getId()));
        Assert.assertEquals(0, snippetDao.getAllSnippetsByLanguageCount(language3.getId()));

        //language2 -> deleted
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language2, Collections.emptyList(), false, TestConstants.SNIPPET_DELETED);
        //language3 -> active
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language3, Collections.emptyList(), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language3, Collections.emptyList(), false, false);
        Assert.assertEquals(1, snippetDao.getAllSnippetsByLanguageCount(language.getId()));
        Assert.assertEquals(0, snippetDao.getAllSnippetsByLanguageCount(language2.getId()));
        Assert.assertEquals(2, snippetDao.getAllSnippetsByLanguageCount(language3.getId()));
    }

    @Test
    public void getAllFollowingSnippetsCountTest() {
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);
        TestMethods.setUserFollowingTags(em, user, Collections.singletonList(tag));

        //Snippet contain only tag
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
        Assert.assertEquals(2, snippetDao.getAllFollowingSnippetsCount(user.getId()));

        TestMethods.setUserFollowingTags(em, user, Arrays.asList(tag, tag2));
        TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag2), false, TestConstants.SNIPPET_DELETED);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Arrays.asList(tag, tag2), false, false);
        TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag2), false, false);
        Assert.assertEquals(4, snippetDao.getAllFollowingSnippetsCount(user.getId()));

        /* Unfollowing tag2 */
        TestMethods.setUserFollowingTags(em, user, Collections.singletonList(tag));
        Assert.assertEquals(3, snippetDao.getAllFollowingSnippetsCount(user.getId()));
    }

    @Test
    public void getAllFavoriteSnippetsCountTest() {
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        Snippet snip = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
        Snippet deletedSnip = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, TestConstants.SNIPPET_DELETED);
        TestMethods.setUserFavoriteSnippets(em, user, Arrays.asList(snip, deletedSnip));

        Assert.assertEquals(2, snippetDao.getAllFavoriteSnippetsCount(user.getId()));

        Snippet snip2 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag2), false, TestConstants.SNIPPET_DELETED);
        Snippet deletedSnip2 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Arrays.asList(tag, tag2), false, false);
        TestMethods.setUserFavoriteSnippets(em, user, Arrays.asList(snip2, deletedSnip, deletedSnip2));

        Assert.assertEquals(3, snippetDao.getAllFavoriteSnippetsCount(user.getId()));
    }

    @Test
    public void getAllSnippetsByOwnerCountTest() {
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
        //Deleted
        TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, TestConstants.SNIPPET_DELETED);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, TestConstants.SNIPPET_DELETED);

        Assert.assertEquals(0, snippetDao.getAllSnippetsByOwnerCount(user.getId()));
        Assert.assertEquals(2, snippetDao.getAllSnippetsByOwnerCount(owner.getId()));
    }

    @Test
    public void getAllSnippetsByTagCountTest() {
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);
        Snippet badSnippet = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
        Snippet goodSnippet = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag2), false, false);
        Snippet toBeDeletedSnippet = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Arrays.asList(tag, tag2), false, false);

        TestMethods.insertVote(em, user, badSnippet, false);
        TestMethods.insertVote(em, user, goodSnippet, true);
        TestMethods.insertVote(em, user, toBeDeletedSnippet, true);

        Assert.assertEquals(2, snippetDao.getAllUpvotedSnippetsCount(user.getId()));
        toBeDeletedSnippet.setDeleted(true);
        Assert.assertEquals(1, snippetDao.getAllUpvotedSnippetsCount(user.getId()));
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

    @Test
    public void getAllSnippetsTest() {
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        Snippet deletedSnippet = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, TestConstants.SNIPPET_DELETED);
        Snippet activeSnippet1 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
        Snippet activeSnippet2 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
        Snippet activeSnippet3 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
        Snippet activeSnippet4 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), false, false);
        Snippet activeSnippet5 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), false, false);

        Collection<Snippet> allSnippets = snippetDao.getAllSnippets(1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(allSnippets);
        Assert.assertEquals(5, allSnippets.size());
        Assert.assertTrue(allSnippets.contains(activeSnippet1));
        Assert.assertTrue(allSnippets.contains(activeSnippet5));
        Assert.assertFalse(allSnippets.contains(deletedSnippet));

        TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), false, false);
        TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.emptyList(), false, false);
        Collection<Snippet> secondPageSnippets = snippetDao.getAllSnippets(2, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(secondPageSnippets);
        Assert.assertEquals(1, secondPageSnippets.size());
    }

    @Test
    public void getAllSnippetsEmptyTest(){
        Collection<Snippet> collection = snippetDao.getAllSnippets(1,TestConstants.SNIPPET_PAGE_SIZE);

        Assert.assertNotNull(collection);
        Assert.assertTrue(collection.isEmpty());
    }

    @Test
    public void getAllFavoriteSnippetsTest() {
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        Snippet snip = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
        Snippet deletedSnip = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, TestConstants.SNIPPET_DELETED);
        TestMethods.setUserFavoriteSnippets(em, user, Arrays.asList(snip, deletedSnip));

        Collection<Snippet> favSnippets = snippetDao.getAllFavoriteSnippets(user.getId(), 1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(favSnippets);
        Assert.assertEquals(2, favSnippets.size());
        Assert.assertTrue(favSnippets.contains(snip));
        Assert.assertTrue(favSnippets.contains(deletedSnip));

        Snippet snip2 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag2), false, TestConstants.SNIPPET_DELETED);
        TestMethods.setUserFavoriteSnippets(em, user, Arrays.asList(snip2, deletedSnip));
        favSnippets = snippetDao.getAllFavoriteSnippets(user.getId(), 1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(favSnippets);
        Assert.assertEquals(2, favSnippets.size());
        Assert.assertTrue(favSnippets.contains(snip2));
        Assert.assertTrue(favSnippets.contains(deletedSnip));
    }

    @Test
    public void getAllFavoriteSnippetsEmptyTest() {
        Collection<Snippet> favSnippets = snippetDao.getAllFavoriteSnippets(owner.getId(), 1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(favSnippets);
        Assert.assertEquals(0, favSnippets.size());
    }

    @Test
    public void getSnippetsWithLanguageTest() {
        Language language2 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);

        /* Language 1 */
        Snippet snipWithLanguage1 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
        Snippet deletedSnipWithLanguage1 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, TestConstants.SNIPPET_DELETED);

        Collection<Snippet> snipsWithLanguage1 = snippetDao.getSnippetsWithLanguage(language.getId(), 1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(snipsWithLanguage1);
        Assert.assertEquals(1, snipsWithLanguage1.size());
        Assert.assertTrue(snipsWithLanguage1.contains(snipWithLanguage1));
        Assert.assertFalse(snipsWithLanguage1.contains(deletedSnipWithLanguage1));

        /* Language 2 */
        Snippet snipWithLanguage2 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language2, Collections.singletonList(tag), false, false);
        Snippet anotherSnipWithLanguage2 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language2, Collections.singletonList(tag), false, false);
        Snippet deletedSnipWithLanguage2 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language2, Collections.singletonList(tag), false, TestConstants.SNIPPET_DELETED);

        Collection<Snippet> snipsWithLanguage2 = snippetDao.getSnippetsWithLanguage(language2.getId(), 1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(snipsWithLanguage2);
        Assert.assertEquals(2, snipsWithLanguage2.size());
        Assert.assertTrue(snipsWithLanguage2.contains(snipWithLanguage2));
        Assert.assertTrue(snipsWithLanguage2.contains(anotherSnipWithLanguage2));
        Assert.assertFalse(snipsWithLanguage2.contains(deletedSnipWithLanguage2));
    }

    public void getSnippetsWithLanguageEmptyTest() {
        Collection<Snippet> snipsWithLanguage1 = snippetDao.getSnippetsWithLanguage(language.getId(), 1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(snipsWithLanguage1);
        Assert.assertEquals(0, snipsWithLanguage1.size());
    }

    @Test
    public void getSnippetsWithLanguagePagingTest() {
        /* 2 out of 9 are deleted snippets */
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, TestConstants.SNIPPET_DELETED);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, TestConstants.SNIPPET_DELETED);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Timestamp.from(Instant.now()), language, Collections.singletonList(tag), false, false);

        Collection<Snippet> snippetsFirstPage = snippetDao.getSnippetsWithLanguage(language.getId(), 1, TestConstants.SNIPPET_PAGE_SIZE);
        Collection<Snippet> snippetsSecondPage = snippetDao.getSnippetsWithLanguage(language.getId(), 2, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(snippetsFirstPage);
        Assert.assertNotNull(snippetsSecondPage);
        Assert.assertEquals(TestConstants.SNIPPET_PAGE_SIZE, snippetsFirstPage.size());
        Assert.assertEquals(7 - TestConstants.SNIPPET_PAGE_SIZE, snippetsSecondPage.size());
    }

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

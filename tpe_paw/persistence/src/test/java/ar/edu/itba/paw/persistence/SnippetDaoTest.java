package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.models.Language;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
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
import java.time.Instant;
import java.util.*;

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
                Instant.now(),
                language.getId(),
                Collections.emptyList()
        );
        Assert.assertTrue(snippetId >= 0);
    }

    @Test
    public void testFindSnippetById() {
        Snippet snippet = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), TestConstants.SNIPPET_FLAGGED, false);

        Optional<Snippet> maybeSnippet = snippetDao.findSnippetById(snippet.getId());
        Assert.assertTrue(maybeSnippet.isPresent());
        Assert.assertEquals(owner.getUsername(), maybeSnippet.get().getOwner().getUsername());
        Assert.assertEquals(TestConstants.SNIPPET_TITLE, maybeSnippet.get().getTitle());
        Assert.assertEquals(TestConstants.SNIPPET_DESCR, maybeSnippet.get().getDescription());
        Assert.assertTrue(maybeSnippet.get().getTags().contains(tag));
        Assert.assertTrue(maybeSnippet.get().isFlagged());
    }

    @Test
    public void testFindInvalidSnippetById() {
        Optional<Snippet> maybeSnippet = snippetDao.findSnippetById(TestConstants.SNIPPET_INVALID_ID);
        Assert.assertFalse(maybeSnippet.isPresent());
    }

    @Test
    public void deleteExistingSnippetByIdTest() {
        Snippet snippet = TestMethods.insertSnippet(
                em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag),
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
                em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag),
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
                Instant.now(),
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
                Instant.now(),
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
                em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(),
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
                em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(),
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
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), true, false);
        Assert.assertEquals(1, snippetDao.getAllSnippetsCount());
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), true, false);
        Assert.assertEquals(2, snippetDao.getAllSnippetsCount());
    }

    @Test
    public void getAllFlaggedSnippetsCountTest() {
        User flaggedSnippetsOwner = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), false, false);
        Assert.assertEquals(0, snippetDao.getAllFlaggedSnippetsCount());
        TestMethods.insertSnippet(em, flaggedSnippetsOwner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), true, false);
        Assert.assertEquals(1, snippetDao.getAllFlaggedSnippetsCount());
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), false, false);
        Assert.assertEquals(1, snippetDao.getAllFlaggedSnippetsCount());
        TestMethods.insertSnippet(em, flaggedSnippetsOwner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), true, false);
        Assert.assertEquals(2, snippetDao.getAllFlaggedSnippetsCount());
    }

    @Test
    public void getAllDeletedSnippetsByOwnerCountTest() {
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), false, false);
        Assert.assertEquals(0, snippetDao.getAllDeletedSnippetsByOwnerCount(owner.getId()));
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), true, TestConstants.SNIPPET_DELETED);
        Assert.assertEquals(1, snippetDao.getAllDeletedSnippetsByOwnerCount(owner.getId()));
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), false, false);
        Assert.assertEquals(1, snippetDao.getAllDeletedSnippetsByOwnerCount(owner.getId()));
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), true, TestConstants.SNIPPET_DELETED);
        Assert.assertEquals(2, snippetDao.getAllDeletedSnippetsByOwnerCount(owner.getId()));
    }

    @Test
    public void getAllSnippetsByLanguageCountTest() {
        Language language2 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE2);
        Language language3 = TestMethods.insertLanguage(em, TestConstants.LANGUAGE3);

        //language -> active
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), false, false);
        //language -> deleted
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), false, TestConstants.SNIPPET_DELETED);
        Assert.assertEquals(1, snippetDao.getAllSnippetsByLanguageCount(language.getId()));
        Assert.assertEquals(0, snippetDao.getAllSnippetsByLanguageCount(language2.getId()));
        Assert.assertEquals(0, snippetDao.getAllSnippetsByLanguageCount(language3.getId()));

        //language2 -> deleted
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language2, Collections.emptyList(), false, TestConstants.SNIPPET_DELETED);
        //language3 -> active
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language3, Collections.emptyList(), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language3, Collections.emptyList(), false, false);
        Assert.assertEquals(1, snippetDao.getAllSnippetsByLanguageCount(language.getId()));
        Assert.assertEquals(0, snippetDao.getAllSnippetsByLanguageCount(language2.getId()));
        Assert.assertEquals(2, snippetDao.getAllSnippetsByLanguageCount(language3.getId()));
    }

    @Test
    public void getAllFollowingSnippetsCountTest() {
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        TestMethods.setUserFollowingTags(em, user, Arrays.asList(tag, tag2));
        TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag2), false, TestConstants.SNIPPET_DELETED);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Arrays.asList(tag, tag2), false, false);
        TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag2), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        Assert.assertEquals(4, snippetDao.getAllFollowingSnippetsCount(user.getId()));

        /* Unfollowing tag2 */
        TestMethods.setUserFollowingTags(em, user, Collections.singletonList(tag));
        Assert.assertEquals(3, snippetDao.getAllFollowingSnippetsCount(user.getId()));
    }

    @Test
    public void getAllFavoriteSnippetsCountTest() {
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        Snippet snip = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        Snippet deletedSnip = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, TestConstants.SNIPPET_DELETED);
        TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag2), false, TestConstants.SNIPPET_DELETED);
        TestMethods.setUserFavoriteSnippets(em, user, Arrays.asList(snip, deletedSnip));

        Assert.assertEquals(2, snippetDao.getAllFavoriteSnippetsCount(user.getId()));
    }

    @Test
    public void getAllSnippetsByOwnerCountTest() {
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        //Deleted
        TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, TestConstants.SNIPPET_DELETED);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, TestConstants.SNIPPET_DELETED);

        Assert.assertEquals(0, snippetDao.getAllSnippetsByOwnerCount(user.getId()));
        Assert.assertEquals(2, snippetDao.getAllSnippetsByOwnerCount(owner.getId()));
    }

    @Test
    public void getAllSnippetsByTagCountTest() {
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag2), false, TestConstants.SNIPPET_DELETED);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Arrays.asList(tag, tag2), false, false);
        TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag2), false, false);

        Assert.assertEquals(3, snippetDao.getAllSnippetsByTagCount(tag.getId()));
        Assert.assertEquals(2, snippetDao.getAllSnippetsByTagCount(tag2.getId()));
    }

    @Test
    public void getAllUpvotedSnippetsCountTest() {
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);
        Snippet badSnippet = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        Snippet goodSnippet = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag2), false, false);
        Snippet goodSnippet2 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Arrays.asList(tag, tag2), false, false);

        TestMethods.insertVote(em, user, badSnippet, false);
        TestMethods.insertVote(em, user, goodSnippet, true);
        TestMethods.insertVote(em, user, goodSnippet2, true);

        Assert.assertEquals(2, snippetDao.getAllUpvotedSnippetsCount(user.getId()));
    }

    @Test
    public void getAllUpvotedSnippetsCountDeletingTest() {
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);
        Snippet badSnippet = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        Snippet goodSnippet = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag2), false, false);
        Snippet toBeDeletedSnippet = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Arrays.asList(tag, tag2), false, false);

        TestMethods.insertVote(em, user, badSnippet, false);
        TestMethods.insertVote(em, user, goodSnippet, true);
        TestMethods.insertVote(em, user, toBeDeletedSnippet, true);

        toBeDeletedSnippet.setDeleted(true);
        Assert.assertEquals(1, snippetDao.getAllUpvotedSnippetsCount(user.getId()));
    }

    // TODO CHECK! FALLA!
//    @Test
//    public void getNewSnippetsForTagsCountTest() {
//        Instant firstSince = Instant.now();
//        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
//
//        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
//        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
//        TestMethods.setUserFollowingTags(em, owner, Collections.singletonList(tag));
//        assertEquals(2, snippetDao.getNewSnippetsForTagsCount(firstSince, Collections.singletonList(tag), owner.getId()));
//    }

    @Test
    public void getAllSnippetsTest() {
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        Snippet deletedSnippet = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, TestConstants.SNIPPET_DELETED);
        Snippet activeSnippet1 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        Snippet activeSnippet2 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        Snippet activeSnippet3 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        Snippet activeSnippet4 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), false, false);
        Snippet activeSnippet5 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), false, false);

        Collection<Snippet> allSnippets = snippetDao.getAllSnippets(1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(allSnippets);
        Assert.assertEquals(5, allSnippets.size());
        Assert.assertTrue(allSnippets.contains(activeSnippet1));
        Assert.assertTrue(allSnippets.contains(activeSnippet5));
        Assert.assertFalse(allSnippets.contains(deletedSnippet));

        TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), false, false);
        TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), false, false);
        Collection<Snippet> secondPageSnippets = snippetDao.getAllSnippets(2, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(secondPageSnippets);
        Assert.assertEquals(1, secondPageSnippets.size());
    }

    @Test
    public void getAllSnippetsEmptyTest() {
        Collection<Snippet> collection = snippetDao.getAllSnippets(1, TestConstants.SNIPPET_PAGE_SIZE);

        Assert.assertNotNull(collection);
        Assert.assertTrue(collection.isEmpty());
    }

    @Test
    public void getAllFavoriteSnippetsTest() {
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        Snippet snip = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        Snippet snip2 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag2), false, TestConstants.SNIPPET_DELETED);
        Snippet deletedSnip = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, TestConstants.SNIPPET_DELETED);
        TestMethods.setUserFavoriteSnippets(em, user, Arrays.asList(snip, deletedSnip));

        Collection<Snippet> favSnippets = snippetDao.getAllFavoriteSnippets(user.getId(), 1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(favSnippets);
        Assert.assertEquals(2, favSnippets.size());
        Assert.assertTrue(favSnippets.contains(snip));
        Assert.assertTrue(favSnippets.contains(deletedSnip));
        Assert.assertFalse(favSnippets.contains(snip2));
    }

    @Test
    public void getAllFavoriteSnippetsEmptyTest() {
        Collection<Snippet> favSnippets = snippetDao.getAllFavoriteSnippets(owner.getId(), 1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(favSnippets);
        Assert.assertTrue(favSnippets.isEmpty());
    }

    @Test
    public void getSnippetsWithLanguageTest() {

        /* Language 1 */
        Snippet snipWithLanguage1 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        Snippet snipWithLanguage2 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        Snippet deletedSnipWithLanguage1 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, TestConstants.SNIPPET_DELETED);

        Collection<Snippet> snipsWithLanguage1 = snippetDao.getSnippetsWithLanguage(language.getId(), 1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(snipsWithLanguage1);
        Assert.assertEquals(2, snipsWithLanguage1.size());
        Assert.assertTrue(snipsWithLanguage1.contains(snipWithLanguage1));
        Assert.assertFalse(snipsWithLanguage1.contains(deletedSnipWithLanguage1));
    }

    @Test
    public void getSnippetsWithLanguageEmptyTest() {
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), false, TestConstants.SNIPPET_DELETED);

        Collection<Snippet> snipsWithLanguage1 = snippetDao.getSnippetsWithLanguage(language.getId(), 1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(snipsWithLanguage1);
        Assert.assertTrue(snipsWithLanguage1.isEmpty());
    }

    @Test
    public void getSnippetsWithLanguagePagingTest() {
        /* 2 out of 9 are deleted snippets */
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, TestConstants.SNIPPET_DELETED);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, TestConstants.SNIPPET_DELETED);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);

        Collection<Snippet> snippetsFirstPage = snippetDao.getSnippetsWithLanguage(language.getId(), 1, TestConstants.SNIPPET_PAGE_SIZE);
        Collection<Snippet> snippetsSecondPage = snippetDao.getSnippetsWithLanguage(language.getId(), 2, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(snippetsFirstPage);
        Assert.assertNotNull(snippetsSecondPage);
        Assert.assertEquals(TestConstants.SNIPPET_PAGE_SIZE, snippetsFirstPage.size());
        Assert.assertEquals(7 - TestConstants.SNIPPET_PAGE_SIZE, snippetsSecondPage.size());
    }

    @Test
    public void getAllDeletedSnippetsByOwnerTest() {
        Snippet snip1 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), false, false);
        Snippet snip2 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), true, TestConstants.SNIPPET_DELETED);
        Snippet snip3 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), false, TestConstants.SNIPPET_DELETED);
        Snippet snip4 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), true, TestConstants.SNIPPET_DELETED);

        Collection<Snippet> deletedSnippets = snippetDao.getAllDeletedSnippetsByOwner(owner.getId(), 1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(deletedSnippets);
        Assert.assertEquals(3, deletedSnippets.size());
        Assert.assertTrue(deletedSnippets.contains(snip4));
        Assert.assertTrue(deletedSnippets.contains(snip3));
        Assert.assertTrue(deletedSnippets.contains(snip2));
        Assert.assertFalse(deletedSnippets.contains(snip1));
    }

    @Test
    public void getAllDeletedSnippetsByOwnerEmptyTest() {
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), false, false);

        Collection<Snippet> deletedSnippets = snippetDao.getAllDeletedSnippetsByOwner(owner.getId(), 1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(deletedSnippets);
        Assert.assertEquals(0, deletedSnippets.size());
    }

    @Test
    public void getAllSnippetsByOwnerTest() {
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        Snippet active1 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        Snippet active2 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        Snippet active3 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        //Deleted
        Snippet deleted1 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, TestConstants.SNIPPET_DELETED);

        Collection<Snippet> userSnips = snippetDao.getAllSnippetsByOwner(user.getId(), 1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(userSnips);
        Assert.assertEquals(2, userSnips.size());
        Assert.assertTrue(userSnips.contains(active3));
        Assert.assertFalse(userSnips.contains(deleted1));
    }

    @Test
    public void getAllSnippetsByOwnerEmptyTest() {
        Snippet deleted1 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, TestConstants.SNIPPET_DELETED);

        Collection<Snippet> ownerSnips = snippetDao.getAllSnippetsByOwner(owner.getId(), 1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(ownerSnips);
        Assert.assertEquals(0, ownerSnips.size());
        Assert.assertFalse(ownerSnips.contains(deleted1));
    }

    @Test
    public void getAllFlaggedSnippetsTest() {
        User flaggedSnippetsOwner = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        Snippet flagged1 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), TestConstants.SNIPPET_FLAGGED, false);
        Snippet notFlagged = TestMethods.insertSnippet(em, flaggedSnippetsOwner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), false, false);
        Snippet flaggedAndDeleted = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), TestConstants.SNIPPET_FLAGGED, TestConstants.SNIPPET_DELETED);
        Snippet flagged2 = TestMethods.insertSnippet(em, flaggedSnippetsOwner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), TestConstants.SNIPPET_FLAGGED, false);

        Collection<Snippet> flaggedSnippets = snippetDao.getAllFlaggedSnippets(1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(flaggedSnippets);
        Assert.assertEquals(3, flaggedSnippets.size());
        Assert.assertTrue(flaggedSnippets.contains(flagged1));
        Assert.assertTrue(flaggedSnippets.contains(flagged2));
        Assert.assertTrue(flaggedSnippets.contains(flaggedAndDeleted));
        Assert.assertFalse(flaggedSnippets.contains(notFlagged));
    }

    @Test
    public void getAllFlaggedSnippetsPagingTest() {
        User flaggedSnippetsOwner = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), TestConstants.SNIPPET_FLAGGED, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), TestConstants.SNIPPET_FLAGGED, TestConstants.SNIPPET_DELETED);
        TestMethods.insertSnippet(em, flaggedSnippetsOwner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), TestConstants.SNIPPET_FLAGGED, false);
        TestMethods.insertSnippet(em, flaggedSnippetsOwner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), TestConstants.SNIPPET_FLAGGED, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), TestConstants.SNIPPET_FLAGGED, TestConstants.SNIPPET_DELETED);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), TestConstants.SNIPPET_FLAGGED, TestConstants.SNIPPET_DELETED);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), TestConstants.SNIPPET_FLAGGED, false);
        TestMethods.insertSnippet(em, flaggedSnippetsOwner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), TestConstants.SNIPPET_FLAGGED, false);

        Collection<Snippet> flaggedFirstSnippets = snippetDao.getAllFlaggedSnippets(1, TestConstants.SNIPPET_PAGE_SIZE);
        Collection<Snippet> flaggedSecondSnippets = snippetDao.getAllFlaggedSnippets(2, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(flaggedFirstSnippets);
        Assert.assertNotNull(flaggedSecondSnippets);
        Assert.assertEquals(TestConstants.SNIPPET_PAGE_SIZE, flaggedFirstSnippets.size());
        Assert.assertEquals(8 - TestConstants.SNIPPET_PAGE_SIZE, flaggedSecondSnippets.size());
    }

    @Test
    public void getAllFlaggedSnippetsEmptyTest() {
        Collection<Snippet> flaggedSnippets = snippetDao.getAllFlaggedSnippets(1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(flaggedSnippets);
        Assert.assertTrue(flaggedSnippets.isEmpty());
    }

    @Test
    public void getAllUpvotedSnippetsTest() {
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);
        Snippet badSnippet = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        Snippet goodSnippet = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        Snippet toBeDeletedSnippet = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), false, false);

        TestMethods.insertVote(em, user, badSnippet, false);
        TestMethods.insertVote(em, user, goodSnippet, true);
        TestMethods.insertVote(em, user, toBeDeletedSnippet, true);

        Collection<Snippet> upvotedSnippets = snippetDao.getAllUpVotedSnippets(user.getId(), 1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(upvotedSnippets);
        Assert.assertEquals(2, upvotedSnippets.size());
        Assert.assertFalse(upvotedSnippets.contains(badSnippet));
    }

    @Test
    public void getAllUpvotedSnippetsDeletingTest() {
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);
        Snippet badSnippet = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        Snippet goodSnippet = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        Snippet toBeDeletedSnippet = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), false, false);

        TestMethods.insertVote(em, user, badSnippet, false);
        TestMethods.insertVote(em, user, goodSnippet, true);
        TestMethods.insertVote(em, user, toBeDeletedSnippet, true);

        toBeDeletedSnippet.setDeleted(true);

        Collection<Snippet> upvotedSnippets = snippetDao.getAllUpVotedSnippets(user.getId(), 1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(upvotedSnippets);
        Assert.assertEquals(1, upvotedSnippets.size());
        Assert.assertFalse(upvotedSnippets.contains(toBeDeletedSnippet));
    }

    @Test
    public void getAllUpvotedSnippetsEmptyTest() {
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);
        Snippet toBeDeletedSnippet = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.emptyList(), false, false);

        TestMethods.insertVote(em, user, toBeDeletedSnippet, true);
        toBeDeletedSnippet.setDeleted(true);

        Collection<Snippet> upvotedSnippets = snippetDao.getAllUpVotedSnippets(user.getId(), 1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(upvotedSnippets);
        Assert.assertTrue(upvotedSnippets.isEmpty());
    }

    @Test
    public void getAllFollowingSnippetsTest() {
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
        Tag tag3 = TestMethods.insertTag(em, TestConstants.TAG3);
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        TestMethods.setUserFollowingTags(em, user, Arrays.asList(tag, tag2));
        Snippet deleted = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag2), false, TestConstants.SNIPPET_DELETED);
        Snippet snip1 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Arrays.asList(tag, tag2, tag3), false, false);
        Snippet snip2 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag2), false, false);
        Snippet snip3 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Arrays.asList(tag3, tag2), false, false);
        Snippet snip4 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag3), false, false);

        Collection<Snippet> followingSnippets = snippetDao.getAllFollowingSnippets(user.getId(), 1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(followingSnippets);
        Assert.assertEquals(4, followingSnippets.size());
        Assert.assertFalse(followingSnippets.contains(deleted));
        Assert.assertTrue(followingSnippets.contains(snip1));
        Assert.assertTrue(followingSnippets.contains(snip2));
        Assert.assertTrue(followingSnippets.contains(snip3));
        Assert.assertTrue(followingSnippets.contains(snip4));
    }

    @Test
    public void getAllFollowingSnippetsEmptyTest() {
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
        TestMethods.setUserFollowingTags(em, owner, Arrays.asList(tag, tag2));
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Arrays.asList(tag, tag2), false, TestConstants.SNIPPET_DELETED);

        Collection<Snippet> followingSnippets = snippetDao.getAllFollowingSnippets(owner.getId(), 1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(followingSnippets);
        Assert.assertTrue(followingSnippets.isEmpty());
    }

    @Test
    public void findSnippetsForTagTest() {
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
        Tag tag3 = TestMethods.insertTag(em, TestConstants.TAG3);
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        Snippet deleted = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag2), false, TestConstants.SNIPPET_DELETED);
        Snippet snip1 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Arrays.asList(tag, tag2, tag3), false, false);
        Snippet snip2 = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag2), false, false);
        Snippet snip3 = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Arrays.asList(tag3, tag2), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);

        Collection<Snippet> tagSnippets = snippetDao.findSnippetsForTag(tag2.getId(), 1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(tagSnippets);
        Assert.assertEquals(3, tagSnippets.size());
        Assert.assertFalse(tagSnippets.contains(deleted));
        Assert.assertTrue(tagSnippets.contains(snip1));
        Assert.assertTrue(tagSnippets.contains(snip2));
        Assert.assertTrue(tagSnippets.contains(snip3));
    }

    @Test
    public void findSnippetsForTagPagingTest() {
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
        Tag tag3 = TestMethods.insertTag(em, TestConstants.TAG3);
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_ES, false);

        // 1 of 8 is deleted
        TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag2), false, TestConstants.SNIPPET_DELETED);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Arrays.asList(tag, tag2, tag3), false, false);
        TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag2), false, false);
        TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag2), false, false);
        TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag2), false, false);
        TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag2), false, false);
        TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag2), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE2, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Arrays.asList(tag3, tag2), false, false);

        Collection<Snippet> tagSnippets = snippetDao.findSnippetsForTag(tag2.getId(), 2, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(tagSnippets);
        Assert.assertEquals(7 - TestConstants.SNIPPET_PAGE_SIZE, tagSnippets.size());
    }

    @Test
    public void findSnippetsForTagEmptyFirstPageTest() {
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, TestConstants.SNIPPET_DELETED);

        Collection<Snippet> tagSnippets = snippetDao.findSnippetsForTag(tag.getId(), 1, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(tagSnippets);
        Assert.assertTrue(tagSnippets.isEmpty());
    }

    @Test
    public void findSnippetsForTagEmptySecondPageTest() {
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);
        TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), false, false);

        Collection<Snippet> tagSnippets = snippetDao.findSnippetsForTag(tag.getId(), 2, TestConstants.SNIPPET_PAGE_SIZE);
        Assert.assertNotNull(tagSnippets);
        Assert.assertTrue(tagSnippets.isEmpty());
    }

    /* Testing HOME location */
    @Test
    public void findSnippetByCriteriaHomeTitleTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.TITLE,
                TestConstants.SNIPPET_TITLE_TERM,
                SnippetDao.Locations.HOME,
                SnippetDao.Orders.ASC,
                null,
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertFalse(result.contains(snippets.get(TestConstants.SNIPPET_TITLE)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE2)));
        Assert.assertFalse(result.contains(snippets.get(TestConstants.SNIPPET_TITLE3)));
    }

    @Test
    public void findSnippetByCriteriaHomeCapsTitleTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.TITLE,
                TestConstants.SNIPPET_TITLE_TERM_CAPS,
                SnippetDao.Locations.HOME,
                SnippetDao.Orders.ASC,
                null,
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertEquals(3, result.size());
        Assert.assertFalse(result.contains(snippets.get(TestConstants.SNIPPET_TITLE)));
        Assert.assertFalse(result.contains(snippets.get(TestConstants.SNIPPET_TITLE3)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE2)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE5)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE8)));
    }

    @Test
    public void getSnippetByCriteriaCountHomeCapsTitleTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        TestMethods.snippetCreation(em, tags, languages, users);
        
        int count = snippetDao.getSnippetByCriteriaCount(
                SnippetDao.Types.TITLE,
                TestConstants.SNIPPET_TITLE_TERM_CAPS,
                SnippetDao.Locations.HOME,
                null,
                null
        );
        Assert.assertEquals(3, count);
    }

    @Test
    public void findSnippetByCriteriaHomeInvalidTitleTest() {
        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.TITLE,
                TestConstants.SNIPPET_TITLE_TERM_INVALID,
                SnippetDao.Locations.HOME,
                SnippetDao.Orders.ASC,
                null,
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void findSnippetByCriteriaHomeAllTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.ALL,
                TestConstants.GENERAL_TERM,
                SnippetDao.Locations.HOME,
                SnippetDao.Orders.ASC,
                null,
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertEquals(6, result.size());
        Assert.assertFalse(result.contains(snippets.get(TestConstants.SNIPPET_TITLE)));
        Assert.assertFalse(result.contains(snippets.get(TestConstants.SNIPPET_TITLE3)));
        Assert.assertFalse(result.contains(snippets.get(TestConstants.SNIPPET_TITLE9)));
    }

    @Test
    public void findSnippetByCriteriaHomeTagTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.TAG,
                TestConstants.TAG_TERM,
                SnippetDao.Locations.HOME,
                SnippetDao.Orders.ASC,
                null,
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertEquals(TestConstants.SNIPPET_PAGE_SIZE, result.size());
        Assert.assertFalse(result.contains(snippets.get(TestConstants.SNIPPET_TITLE)));
        Assert.assertFalse(result.contains(snippets.get(TestConstants.SNIPPET_TITLE3)));
        Assert.assertFalse(result.contains(snippets.get(TestConstants.SNIPPET_TITLE9)));
    }

    @Test
    public void getSnippetByCriteriaCountHomeTagTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        TestMethods.snippetCreation(em, tags, languages, users);
        int count = snippetDao.getSnippetByCriteriaCount(
                SnippetDao.Types.TAG,
                TestConstants.TAG_TERM,
                SnippetDao.Locations.HOME,
                null,
                null
        );
        Assert.assertEquals(6, count);
    }

    @Test
    public void findSnippetByCriteriaHomeLanguageTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.LANGUAGE,
                TestConstants.LANGUAGE_TERM,
                SnippetDao.Locations.HOME,
                SnippetDao.Orders.ASC,
                null,
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertEquals(4, result.size());
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE4)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE5)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE8)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE9)));
    }

    /* Testing USER location */
    @Test
    public void findSnippetByCriteriaOwnerTitleTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.TITLE,
                TestConstants.SNIPPET_TITLE_TERM_MANY,
                SnippetDao.Locations.USER,
                SnippetDao.Orders.ASC,
                owner.getId(),
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE9)));
    }

    @Test
    public void findSnippetByCriteriaUserTitleTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.TITLE,
                TestConstants.SNIPPET_TITLE_TERM_MANY,
                SnippetDao.Locations.USER,
                SnippetDao.Orders.ASC,
                (users.get(TestConstants.USER_USERNAME2)).getId(),
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE4)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE7)));
    }

    @Test
    public void findSnippetByCriteriaUserUsernameInvalidTest() {
        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.USER,
                TestConstants.USER_NAME_TERM_INVALID,
                SnippetDao.Locations.USER,
                SnippetDao.Orders.ASC,
                owner.getId(),
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void findSnippetByCriteriaUserCodeTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.CONTENT,
                TestConstants.CODE_TERM,
                SnippetDao.Locations.USER,
                SnippetDao.Orders.ASC,
                owner.getId(),
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE9)));
    }

    /* Testing FLAGGED location */
    @Test
    public void findSnippetByCriteriaFlaggedTitleTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.TITLE,
                TestConstants.SNIPPET_TITLE_TERM_CAPS,
                SnippetDao.Locations.FLAGGED,
                SnippetDao.Orders.ASC,
                null,
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE2)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE3)));
    }

    @Test
    public void findSnippetByCriteriaFlaggedTitleInvalidTest() {
        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.TITLE,
                TestConstants.SNIPPET_TITLE_TERM_UNIQUE,
                SnippetDao.Locations.FLAGGED,
                SnippetDao.Orders.ASC,
                null,
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void findSnippetByCriteriaFlaggedCodeTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.CONTENT,
                TestConstants.CODE_TERM,
                SnippetDao.Locations.FLAGGED,
                SnippetDao.Orders.ASC,
                null,
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE2)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE9)));
    }

    @Test
    public void findSnippetByCriteriaFlaggedUserInvalidTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        TestMethods.snippetCreation(em, tags, languages, users);
        
        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.USER,
                TestConstants.USER_NAME_TERM_INVALID,
                SnippetDao.Locations.FLAGGED,
                SnippetDao.Orders.ASC,
                null,
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void getSnippetByCriteriaCountFlaggedUserInvalidTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        TestMethods.snippetCreation(em, tags, languages, users);
        
        int count = snippetDao.getSnippetByCriteriaCount(
                SnippetDao.Types.USER,
                TestConstants.USER_NAME_TERM_INVALID,
                SnippetDao.Locations.FLAGGED,
                null,
                null
        );
        Assert.assertEquals(0, count);
    }

    @Test
    public void findSnippetByCriteriaFlaggedAllTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.ALL,
                TestConstants.GENERAL_TERM,
                SnippetDao.Locations.FLAGGED,
                SnippetDao.Orders.ASC,
                null,
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE2)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE3)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE6)));
    }

    /* Testing FAVORITE location */
    @Test
    public void findSnippetByCriteriaFavoritesTitleTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        TestMethods.setUserFavoriteSnippets(em, owner, Arrays.asList(
                snippets.get(TestConstants.SNIPPET_TITLE),
                snippets.get(TestConstants.SNIPPET_TITLE3),
                snippets.get(TestConstants.SNIPPET_TITLE5),
                snippets.get(TestConstants.SNIPPET_TITLE6),
                snippets.get(TestConstants.SNIPPET_TITLE9)
        ));
        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.TITLE,
                TestConstants.SNIPPET_TITLE_TERM_MANY,
                SnippetDao.Locations.FAVORITES,
                SnippetDao.Orders.ASC,
                owner.getId(),
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertEquals(4, result.size());
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE3)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE5)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE9)));
    }

    @Test
    public void getSnippetByCriteriaCountFavoritesTitleTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        TestMethods.setUserFavoriteSnippets(em, owner, Arrays.asList(
                snippets.get(TestConstants.SNIPPET_TITLE),
                snippets.get(TestConstants.SNIPPET_TITLE3),
                snippets.get(TestConstants.SNIPPET_TITLE5),
                snippets.get(TestConstants.SNIPPET_TITLE6),
                snippets.get(TestConstants.SNIPPET_TITLE9)
        ));
        int count = snippetDao.getSnippetByCriteriaCount(
                SnippetDao.Types.TITLE,
                TestConstants.SNIPPET_TITLE_TERM_MANY,
                SnippetDao.Locations.FAVORITES,
                owner.getId(),
                null
        );
        Assert.assertEquals(4, count);
    }

    @Test
    public void findSnippetByCriteriaFavoritesUserTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        TestMethods.setUserFavoriteSnippets(em, owner, Arrays.asList(
                snippets.get(TestConstants.SNIPPET_TITLE),
                snippets.get(TestConstants.SNIPPET_TITLE3),
                snippets.get(TestConstants.SNIPPET_TITLE5),
                snippets.get(TestConstants.SNIPPET_TITLE6),
                snippets.get(TestConstants.SNIPPET_TITLE9)
        ));

        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.USER,
                TestConstants.USER_NAME_TERM,
                SnippetDao.Locations.FAVORITES,
                SnippetDao.Orders.ASC,
                owner.getId(),
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE5)));
    }

    @Test
    public void findSnippetByCriteriaFavoritesCodeTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        TestMethods.setUserFavoriteSnippets(em, owner, Arrays.asList(
                snippets.get(TestConstants.SNIPPET_TITLE),
                snippets.get(TestConstants.SNIPPET_TITLE3),
                snippets.get(TestConstants.SNIPPET_TITLE5),
                snippets.get(TestConstants.SNIPPET_TITLE6),
                snippets.get(TestConstants.SNIPPET_TITLE9)
        ));

        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.CONTENT,
                TestConstants.CODE_TERM,
                SnippetDao.Locations.FAVORITES,
                SnippetDao.Orders.ASC,
                owner.getId(),
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE5)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE9)));
    }

    @Test
    public void findSnippetByCriteriaFavoritesTagInvalidTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        TestMethods.setUserFavoriteSnippets(em, owner, Arrays.asList(
                snippets.get(TestConstants.SNIPPET_TITLE),
                snippets.get(TestConstants.SNIPPET_TITLE3),
                snippets.get(TestConstants.SNIPPET_TITLE5),
                snippets.get(TestConstants.SNIPPET_TITLE6),
                snippets.get(TestConstants.SNIPPET_TITLE9)
        ));
        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.TAG,
                TestConstants.TAG_TERM_INVALID,
                SnippetDao.Locations.FAVORITES,
                SnippetDao.Orders.ASC,
                owner.getId(),
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void findSnippetByCriteriaFavoritesLanguageTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        TestMethods.setUserFavoriteSnippets(em, owner, Arrays.asList(
                snippets.get(TestConstants.SNIPPET_TITLE),
                snippets.get(TestConstants.SNIPPET_TITLE3),
                snippets.get(TestConstants.SNIPPET_TITLE5),
                snippets.get(TestConstants.SNIPPET_TITLE6),
                snippets.get(TestConstants.SNIPPET_TITLE9)
        ));
        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.LANGUAGE,
                TestConstants.LANGUAGE_TERM,
                SnippetDao.Locations.FAVORITES,
                SnippetDao.Orders.ASC,
                owner.getId(),
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE5)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE9)));
    }

    /* Testing FOLLOWING location */
    @Test
    public void findSnippetByCriteriaFollowingTitleTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        TestMethods.setUserFollowingTags(em, owner, Arrays.asList(
                tags.get(TestConstants.TAG),
                tags.get(TestConstants.TAG2)
        ));
        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.TITLE,
                TestConstants.SNIPPET_TITLE_TERM_CAPS,
                SnippetDao.Locations.FOLLOWING,
                SnippetDao.Orders.ASC,
                owner.getId(),
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE2)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE5)));
    }

    @Test
    public void findSnippetByCriteriaFollowingUsernameTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        TestMethods.setUserFollowingTags(em, owner, Arrays.asList(
                tags.get(TestConstants.TAG),
                tags.get(TestConstants.TAG2)
        ));
        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.USER,
                TestConstants.USER_NAME_TERM,
                SnippetDao.Locations.FOLLOWING,
                SnippetDao.Orders.ASC,
                owner.getId(),
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE2)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE5)));
    }

    @Test
    public void findSnippetByCriteriaFollowingCodeTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        TestMethods.setUserFollowingTags(em, owner, Arrays.asList(
                tags.get(TestConstants.TAG),
                tags.get(TestConstants.TAG2)
        ));
        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.CONTENT,
                TestConstants.CODE_TERM,
                SnippetDao.Locations.FOLLOWING,
                SnippetDao.Orders.ASC,
                owner.getId(),
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertEquals(4, result.size());
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE2)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE4)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE5)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE7)));
    }

    @Test
    public void findSnippetByCriteriaFollowingCodeInvalidTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        TestMethods.setUserFollowingTags(em, owner, Arrays.asList(
                tags.get(TestConstants.TAG),
                tags.get(TestConstants.TAG2)
        ));
        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.CONTENT,
                TestConstants.CODE_TERM_INVALID,
                SnippetDao.Locations.FOLLOWING,
                SnippetDao.Orders.ASC,
                owner.getId(),
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void findSnippetByCriteriaFollowingTagTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        TestMethods.setUserFollowingTags(em, owner, Arrays.asList(
                tags.get(TestConstants.TAG),
                tags.get(TestConstants.TAG2)
        ));
        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.TAG,
                TestConstants.TAG_TERM,
                SnippetDao.Locations.FOLLOWING,
                SnippetDao.Orders.ASC,
                owner.getId(),
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertEquals(5, result.size());
        Assert.assertFalse(result.contains(snippets.get(TestConstants.SNIPPET_TITLE)));
        Assert.assertFalse(result.contains(snippets.get(TestConstants.SNIPPET_TITLE3)));
        Assert.assertFalse(result.contains(snippets.get(TestConstants.SNIPPET_TITLE9)));
        Assert.assertFalse(result.contains(snippets.get(TestConstants.SNIPPET_TITLE8)));
    }

    @Test
    public void findSnippetByCriteriaFollowingLanguageTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em, tag);
        Map<String, Language> languages = TestMethods.languageCreation(em, language);
        Map<String, User> users = TestMethods.userCreation(em, owner);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        TestMethods.setUserFollowingTags(em, owner, Arrays.asList(
                tags.get(TestConstants.TAG),
                tags.get(TestConstants.TAG2)
        ));
        Collection<Snippet> result = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.LANGUAGE,
                TestConstants.LANGUAGE_TERM,
                SnippetDao.Locations.FOLLOWING,
                SnippetDao.Orders.ASC,
                owner.getId(),
                null,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE4)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE5)));
    }

    

    /* Testing finding snippet by deep criteria */
    @Test
    public void findSnippetByDeepCriteriaSpanningManyYearsDateTest() {
        Map<String, Snippet> snippets = TestMethods.dataForSearchByDeepCriteria(em, TestMethods.userCreationDeepSearch(em, owner), tag, language);

        Collection<Snippet> result = snippetDao.findSnippetByDeepCriteria(
                TestConstants.DATE_9,
                TestConstants.DATE_1,
                null,
                null,
                null,
                null,
                null,
                null,
                "",
                "",
                SnippetDao.Orders.ASC,
                SnippetDao.Types.TITLE,
                true,
                1,
                TestConstants.SNIPPET_PAGE_SIZE);

        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE2)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE3)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE4)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE5)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE6)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE7)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE8)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE9)));
        
    }

    @Test
    public void findSnippetByDeepCriteriaMinutesDateTest() {
        Map<String, Snippet> snippets = TestMethods.dataForSearchByDeepCriteria(em, TestMethods.userCreationDeepSearch(em, owner), tag, language);

        Collection<Snippet> result = snippetDao.findSnippetByDeepCriteria(
                TestConstants.DATE_MIN_MINUTES,
                TestConstants.DATE_MAX_MINUTES,
                null,
                null,
                null,
                null,
                null,
                null,
                "",
                "",
                SnippetDao.Orders.ASC,
                SnippetDao.Types.TITLE,
                true,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );

        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE4)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE6)));
    }

    @Test
    public void findSnippetByDeepCriteriaYearDateTest() {
        Map<String, Snippet> snippets = TestMethods.dataForSearchByDeepCriteria(em, TestMethods.userCreationDeepSearch(em, owner), tag, language);

        Collection<Snippet> result = snippetDao.findSnippetByDeepCriteria(
                TestConstants.DATE_MIN_YEAR,
                TestConstants.DATE_MAX_YEAR,
                null,
                null,
                null,
                null,
                null,
                null,
                "",
                "",
                SnippetDao.Orders.ASC,
                SnippetDao.Types.ALL,
                true,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );

        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE3)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE4)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE5)));
        Assert.assertTrue(result.contains(snippets.get(TestConstants.SNIPPET_TITLE6)));
    }


    @Test
    public void findSnippetByDeepCriteriaVotesTest() {
        Map<String, Snippet> snippets = TestMethods.dataForSearchByDeepCriteria(em, TestMethods.userCreationDeepSearch(em, owner), tag, language);

        Collection<Snippet> result1 = snippetDao.findSnippetByDeepCriteria(
                null,
                null,
                null,
                null,
                TestConstants.VOTE_VALUE_1,
                TestConstants.VOTE_VALUE_1,
                null,
                null,
                "",
                "",
                SnippetDao.Orders.ASC,
                SnippetDao.Types.TITLE,
                true,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );

        Collection<Snippet> result2 = snippetDao.findSnippetByDeepCriteria(
                null,
                null,
                null,
                null,
                TestConstants.VOTE_VALUE_2,
                TestConstants.VOTE_VALUE_3,
                null,
                null,
                "",
                "",
                SnippetDao.Orders.ASC,
                SnippetDao.Types.TITLE,
                true,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );

        Collection<Snippet> result3 = snippetDao.findSnippetByDeepCriteria(
                null,
                null,
                null,
                null,
                TestConstants.VOTE_VALUE_4,
                TestConstants.VOTE_VALUE_3,
                null,
                null,
                "",
                "",
                SnippetDao.Orders.ASC,
                SnippetDao.Types.TITLE,
                true,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );

        Assert.assertTrue(result1.contains(snippets.get(TestConstants.SNIPPET_TITLE6)));
        Assert.assertTrue(result1.contains(snippets.get(TestConstants.SNIPPET_TITLE7)));
        Assert.assertTrue(result1.contains(snippets.get(TestConstants.SNIPPET_TITLE8)));
        Assert.assertTrue(result1.contains(snippets.get(TestConstants.SNIPPET_TITLE9)));

        Assert.assertTrue(result2.contains(snippets.get(TestConstants.SNIPPET_TITLE)));
        Assert.assertTrue(result2.contains(snippets.get(TestConstants.SNIPPET_TITLE2)));
        Assert.assertTrue(result2.contains(snippets.get(TestConstants.SNIPPET_TITLE4)));
        Assert.assertTrue(result2.contains(snippets.get(TestConstants.SNIPPET_TITLE5)));

        Assert.assertTrue(result3.isEmpty());

    }

    @Test
    public void findSnippetByDeepCriteriaReputationNegativeTest() {
        Map<String, Snippet> snippets = TestMethods.dataForSearchByDeepCriteria(em, TestMethods.userCreationDeepSearch(em, owner), tag, language);
        Collection<Snippet> result1 = snippetDao.findSnippetByDeepCriteria(
                null,
                null, // Instant.now()
                TestConstants.REP_VALUE_1,
                TestConstants.REP_VALUE_2,
                null,
                null,
                language.getId(),
                tag.getId(),
                "",
                "",
                SnippetDao.Orders.ASC,
                SnippetDao.Types.TITLE,
                true,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertTrue(result1.contains(snippets.get(TestConstants.SNIPPET_TITLE2)));
    }

    @Test
    public void findSnippetByDeepCriteriaReputationNonnegativeTest() {
        Map<String, Snippet> snippets = TestMethods.dataForSearchByDeepCriteria(em, TestMethods.userCreationDeepSearch(em, owner), tag, language);
        Collection<Snippet> result2 = snippetDao.findSnippetByDeepCriteria(
                null,
                null, // Instant.now()
                TestConstants.REP_VALUE_3,
                TestConstants.REP_VALUE_5,
                null,
                null,
                language.getId(),
                tag.getId(),
                "",
                "",
                SnippetDao.Orders.ASC,
                SnippetDao.Types.TITLE,
                true,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );
        Assert.assertTrue(result2.contains(snippets.get(TestConstants.SNIPPET_TITLE)));
        Assert.assertTrue(result2.contains(snippets.get(TestConstants.SNIPPET_TITLE3)));
        Assert.assertTrue(result2.contains(snippets.get(TestConstants.SNIPPET_TITLE4)));
        Assert.assertTrue(result2.contains(snippets.get(TestConstants.SNIPPET_TITLE5)));
    }

    @Test
    public void findSnippetByDeepCriteriaReputationNoneTest() {
        Map<String, Snippet> snippets = TestMethods.dataForSearchByDeepCriteria(em, TestMethods.userCreationDeepSearch(em, owner), tag, language);
        Collection<Snippet> result3 = snippetDao.findSnippetByDeepCriteria(
                null,
                null, // Instant.now()
                TestConstants.REP_VALUE_6,
                TestConstants.REP_VALUE_6,
                null,
                null,
                language.getId(),
                tag.getId(),
                "",
                "",
                SnippetDao.Orders.ASC,
                SnippetDao.Types.TITLE,
                true,
                1,
                TestConstants.SNIPPET_PAGE_SIZE
        );

        Assert.assertTrue(result3.isEmpty());
    }

    /* Testing getting snippet by deep criteria count */

    @Test
    public void findSnippetByDeepCriteriaCountSpanningManyYearsDateTest() {
        Map<String, Snippet> snippets = TestMethods.dataForSearchByDeepCriteria(em, TestMethods.userCreationDeepSearch(em, owner), tag, language);

        int result = snippetDao.getSnippetByDeepCriteriaCount(
                TestConstants.DATE_9,
                TestConstants.DATE_1,
                null,
                null,
                null,
                null,
                null,
                null,
                "",
                "",
                true);

        Assert.assertEquals(9, result);
    }

    @Test
    public void findSnippetByDeepCriteriaCountVotesZeroTest() {
        Map<String, Snippet> snippets = TestMethods.dataForSearchByDeepCriteria(em, TestMethods.userCreationDeepSearch(em, owner), tag, language);

        int result1 = snippetDao.getSnippetByDeepCriteriaCount(
                null,
                null,
                null,
                null,
                TestConstants.VOTE_VALUE_1,
                TestConstants.VOTE_VALUE_1,
                null,
                null,
                "",
                "",
                true
        );

        Assert.assertEquals(4, result1);
    }

    @Test
    public void findSnippetByDeepCriteriaCountVotesPositiveTest() {
        Map<String, Snippet> snippets = TestMethods.dataForSearchByDeepCriteria(em, TestMethods.userCreationDeepSearch(em, owner), tag, language);
        int result2 = snippetDao.getSnippetByDeepCriteriaCount(
                null,
                null,
                null,
                null,
                TestConstants.VOTE_VALUE_2,
                TestConstants.VOTE_VALUE_3,
                null,
                null,
                "",
                "",
                true
        );
        Assert.assertEquals(4, result2);
    }

    @Test
    public void findSnippetByDeepCriteriaCountVotesNoneTest() {
        Map<String, Snippet> snippets = TestMethods.dataForSearchByDeepCriteria(em, TestMethods.userCreationDeepSearch(em, owner), tag, language);

        int result3 = snippetDao.getSnippetByDeepCriteriaCount(
                null,
                null,
                null,
                null,
                TestConstants.VOTE_VALUE_4,
                TestConstants.VOTE_VALUE_3,
                null,
                null,
                "",
                "",
                true
        );

        Assert.assertEquals(0, result3);
    }

    @Test
    public void findSnippetByDeepCriteriaCountReputationNegativeTest() {
        Map<String, Snippet> snippets = TestMethods.dataForSearchByDeepCriteria(em, TestMethods.userCreationDeepSearch(em, owner), tag, language);
        int result1 = snippetDao.getSnippetByDeepCriteriaCount(
                null,
                null, // Instant.now()
                TestConstants.REP_VALUE_1,
                TestConstants.REP_VALUE_2,
                null,
                null,
                language.getId(),
                tag.getId(),
                "",
                "",
                true
        );
        Assert.assertEquals(1, result1);
    }

    @Test
    public void findSnippetByDeepCriteriaCountReputationNonnegativeTest() {
        Map<String, Snippet> snippets = TestMethods.dataForSearchByDeepCriteria(em, TestMethods.userCreationDeepSearch(em, owner), tag, language);
        int result2 = snippetDao.getSnippetByDeepCriteriaCount(
                null,
                null, // Instant.now()
                TestConstants.REP_VALUE_3,
                TestConstants.REP_VALUE_5,
                null,
                null,
                language.getId(),
                tag.getId(),
                "",
                "",
                true
        );
        Assert.assertEquals(4, result2);
    }

    @Test
    public void findSnippetByDeepCriteriaCountReputationNoneTest() {
        Map<String, Snippet> snippets = TestMethods.dataForSearchByDeepCriteria(em, TestMethods.userCreationDeepSearch(em, owner), tag, language);
        int result3 = snippetDao.getSnippetByDeepCriteriaCount(
                null,
                null, // Instant.now()
                TestConstants.REP_VALUE_6,
                TestConstants.REP_VALUE_6,
                null,
                null,
                language.getId(),
                tag.getId(),
                "",
                "",
                true
        );
        Assert.assertEquals(0, result3);
    }
}
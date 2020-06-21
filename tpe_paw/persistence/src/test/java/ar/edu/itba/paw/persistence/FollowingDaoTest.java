package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.FollowingDao;
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
import java.time.Instant;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class FollowingDaoTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private FollowingDao followingDao;

    private User defaultUser;

    @Before
    public void setup(){
        defaultUser = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL, TestConstants.USER_DATE, TestConstants.LOCALE_EN, TestConstants.USER_VERIFIED);
    }

    @Test
    public void getFollowedTagsForUserTest() {
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);

        TestMethods.setUserFollowingTags(em, defaultUser, Arrays.asList(tag, tag2));

        Collection<Tag> followingTags = followingDao.getFollowedTagsForUser(defaultUser.getId());
        Assert.assertNotNull(followingTags);
        Assert.assertEquals(2, followingTags.size());
        Assert.assertTrue(followingTags.contains(tag));
        Assert.assertTrue(followingTags.contains(tag2));
    }

    @Test
    public void getMostPopularFollowedTagsForUserEmptyTagTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em);
        Map<String, Language> languages = TestMethods.languageCreation(em);
        Map<String, User> users = TestMethods.userCreation(em);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);

        Tag unpopularTag = TestMethods.insertTag(em, TestConstants.TAG4);

        TestMethods.setUserFollowingTags(
                em,
                defaultUser,
                Arrays.asList(
                        tags.get(TestConstants.TAG),
                        tags.get(TestConstants.TAG2),
                        tags.get(TestConstants.TAG3),
                        unpopularTag
                )
        );

        Collection<Tag> followingTags = followingDao.getMostPopularFollowedTagsForUser(defaultUser.getId(), 10);
        Assert.assertNotNull(followingTags);
        Assert.assertEquals(3, followingTags.size());
        Assert.assertFalse(followingTags.contains(unpopularTag));
    }

    @Test
    public void getMostPopularFollowedTagsForUserTagWithDeletedSnippetTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em);
        Map<String, Language> languages = TestMethods.languageCreation(em);
        Map<String, User> users = TestMethods.userCreation(em);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);
        
        /* Only contains a deleted snippet */
        Tag unpopularTag = TestMethods.insertTag(em, TestConstants.TAG4);
        TestMethods.insertSnippet(em, defaultUser, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), languages.get(TestConstants.LANGUAGE), Collections.singletonList(unpopularTag), false, TestConstants.SNIPPET_DELETED);

        TestMethods.setUserFollowingTags(
                em,
                defaultUser,
                Arrays.asList(
                        tags.get(TestConstants.TAG),
                        tags.get(TestConstants.TAG2),
                        tags.get(TestConstants.TAG3),
                        unpopularTag
                )
        );

        Collection<Tag> followingTags = followingDao.getMostPopularFollowedTagsForUser(defaultUser.getId(), 10);
        Assert.assertNotNull(followingTags);
        Assert.assertEquals(3, followingTags.size());
        Assert.assertFalse(followingTags.contains(unpopularTag));
    }

    @Test
    public void getMostPopularFollowedTagsForUserTooManyTagTest() {
        Map<String, Tag> tags = TestMethods.tagsCreation(em);
        Map<String, Language> languages = TestMethods.languageCreation(em);
        Map<String, User> users = TestMethods.userCreation(em);
        Map<String, Snippet> snippets = TestMethods.snippetCreation(em, tags, languages, users);
        
        /* Only contains a deleted snippet */
        Tag unpopularTag = TestMethods.insertTag(em, TestConstants.TAG4);
        TestMethods.insertSnippet(em, defaultUser, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), languages.get(TestConstants.LANGUAGE), Collections.singletonList(unpopularTag), false, TestConstants.SNIPPET_DELETED);

        TestMethods.setUserFollowingTags(
                em,
                defaultUser,
                Arrays.asList(
                        tags.get(TestConstants.TAG),
                        tags.get(TestConstants.TAG2),
                        tags.get(TestConstants.TAG3),
                        unpopularTag
                )
        );

        /* Only want the two most popular --> TAG is only in 3 snippets */
        Collection<Tag> followingTags = followingDao.getMostPopularFollowedTagsForUser(defaultUser.getId(), 2);
        Assert.assertNotNull(followingTags);
        Assert.assertEquals(2, followingTags.size());
        Assert.assertFalse(followingTags.contains(unpopularTag));
        Assert.assertFalse(followingTags.contains(snippets.get(TestConstants.TAG)));
    }

    @Test
    public void getMostPopularFollowedTagsForUserNoTagsTest() {
        /* Only contains a deleted snippet */
        Tag unpopularTag = TestMethods.insertTag(em, TestConstants.TAG4);
        TestMethods.insertSnippet(em, defaultUser, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), TestMethods.insertLanguage(em, TestConstants.LANGUAGE2), Collections.singletonList(unpopularTag), false, TestConstants.SNIPPET_DELETED);

        TestMethods.setUserFollowingTags(
                em,
                defaultUser,
                Collections.singletonList(unpopularTag)
        );

        /* Only want the two most popular --> TAG is only in 3 snippets */
        Collection<Tag> followingTags = followingDao.getMostPopularFollowedTagsForUser(defaultUser.getId(), 2);
        Assert.assertNotNull(followingTags);
        Assert.assertTrue(followingTags.isEmpty());
    }


    @Test
    public void followTagTest(){
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);
        int before = TestMethods.countRows(em, TestConstants.FOLLOWING_TABLE);

        followingDao.followTag(defaultUser.getId(), tag.getId());

        Assert.assertEquals(0, before);
        Assert.assertEquals(1, TestMethods.countRows(em, TestConstants.FOLLOWING_TABLE));
        Assert.assertTrue(tag.getFollowingUsers().contains(defaultUser));
        Assert.assertTrue(defaultUser.getFollowedTags().contains(tag));
    }

    @Test
    public void followTagEmptyTest(){
        followingDao.followTag(defaultUser.getId(),  -1);
        Assert.assertTrue(defaultUser.getFollowedTags().isEmpty());
        Assert.assertEquals(0, TestMethods.countRows(em, TestConstants.FOLLOWING_TABLE));
    }

    @Test
    public void unfollowTagTest(){
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);
        TestMethods.setUserFollowingTags(
                em,
                defaultUser,
                // Wrapper to support modifications( singletonList is inmutable)
                new LinkedList<>(Collections.singletonList(tag))
        );
        tag.setFollowingUsers(new LinkedList<>(Collections.singletonList(defaultUser)));

        int beforeCount = TestMethods.countRows(em, TestConstants.FOLLOWING_TABLE);

        followingDao.unfollowTag(defaultUser.getId(), tag.getId());

        Assert.assertEquals(1, beforeCount);
        Assert.assertEquals(0, TestMethods.countRows(em, TestConstants.FOLLOWING_TABLE));
        Assert.assertFalse(tag.getFollowingUsers().contains(defaultUser));
        Assert.assertFalse(defaultUser.getFollowedTags().contains(tag));
    }

    @Test
    public void unfollowTagEmptyTest(){
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);
        TestMethods.setUserFollowingTags(
                em,
                defaultUser,
                new LinkedList<>(Collections.singletonList(tag))
        );
        tag.setFollowingUsers(new LinkedList<>(Collections.singletonList(defaultUser)));

        int beforeCount = TestMethods.countRows(em, TestConstants.FOLLOWING_TABLE);

        followingDao.unfollowTag(defaultUser.getId(), -1);

        Assert.assertEquals(1, beforeCount);
        Assert.assertEquals(1, TestMethods.countRows(em, TestConstants.FOLLOWING_TABLE));
        Assert.assertTrue(tag.getFollowingUsers().contains(defaultUser));
        Assert.assertTrue(defaultUser.getFollowedTags().contains(tag));
    }

}

package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.TagDao;
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
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class TagDaoTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private TagDao tagDao;

    @Before
    public void setUp() {
    }

    @Test
    public void testAddTag(){
        int beforeAddCount = TestMethods.countRows(em, TestConstants.TAG_TABLE);
        Tag tag = tagDao.addTag(TestConstants.TAG);
        int afterAddCount = TestMethods.countRows(em, TestConstants.TAG_TABLE);

        Assert.assertNotNull(tag);
        Assert.assertEquals(TestConstants.TAG, tag.getName());
        Assert.assertEquals(0, beforeAddCount);
        Assert.assertEquals( 1, afterAddCount);
    }

    @Test
    public void testAddRepeatedTag(){
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);
        int beforeAddCount = TestMethods.countRows(em, TestConstants.TAG_TABLE);
        Tag tagRepeated = tagDao.addTag(TestConstants.TAG);
        int afterAddCount = TestMethods.countRows(em, TestConstants.TAG_TABLE);

        Assert.assertNotNull(tag);
        Assert.assertEquals(tag, tagRepeated);
        Assert.assertEquals(1, beforeAddCount);
        Assert.assertEquals( 1, afterAddCount);
    }

    @Test
    public void testFindById(){
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);

        Optional<Tag> maybeTag = tagDao.findById(tag.getId());

        Assert.assertTrue(maybeTag.isPresent());
        Assert.assertEquals(tag.getId(), maybeTag.get().getId());
        Assert.assertEquals(TestConstants.TAG,maybeTag.get().getName());
    }

    @Test
    public void testFindByIdEmpty(){
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);

        Optional<Tag> maybeTag = tagDao.findById(TestConstants.INVALID_TAG_ID);

        Assert.assertFalse(maybeTag.isPresent());
    }

    @Test
    public void testFindByName(){
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);

        Optional<Tag> maybeTag = tagDao.findByName(TestConstants.TAG);

        Assert.assertTrue(maybeTag.isPresent());
        Assert.assertEquals(tag.getId(), maybeTag.get().getId());
        Assert.assertEquals(TestConstants.TAG,maybeTag.get().getName());
    }

    @Test
    public void testFindByNameEmpty(){
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);

        Optional<Tag> maybeTag = tagDao.findByName(TestConstants.TAG2);

        Assert.assertFalse(maybeTag.isPresent());
    }

    @Test
    public void testAddTags() {
        int beforeAddCount = TestMethods.countRows(em, TestConstants.TAG_TABLE);
        this.tagDao.addTags(Arrays.asList(TestConstants.TAG, TestConstants.TAG2, TestConstants.TAG3));
        int afterAddCount = TestMethods.countRows(em, TestConstants.TAG_TABLE);
        Assert.assertEquals(0, beforeAddCount);
        Assert.assertEquals(3, afterAddCount);
    }

    @Test
    public void testAddRepeatedTags() {
        int beforeAddCount = TestMethods.countRows(em, TestConstants.TAG_TABLE);
        this.tagDao.addTags(Arrays.asList(TestConstants.TAG, TestConstants.TAG, TestConstants.TAG3));
        int afterAddCount = TestMethods.countRows(em, TestConstants.TAG_TABLE);
        Assert.assertEquals(0, beforeAddCount);
        Assert.assertEquals(2, afterAddCount);
    }

    @Test
    public void testRemoveExistingTag() {
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);
        int beforeRemovalCount = TestMethods.countRows(em, TestConstants.TAG_TABLE);
        this.tagDao.removeTag(tag.getId());
        int afterRemovalCount = TestMethods.countRows(em, TestConstants.TAG_TABLE);
        Assert.assertEquals(beforeRemovalCount, 1);
        Assert.assertEquals(afterRemovalCount, 0);
    }

    @Test
    public void testRemoveNonExistingTag() {
        int beforeRemovalCount = TestMethods.countRows(em, TestConstants.TAG_TABLE);
        this.tagDao.removeTag(TestConstants.INVALID_TAG_ID);
        int afterRemovalCount = TestMethods.countRows(em, TestConstants.TAG_TABLE);
        Assert.assertEquals(beforeRemovalCount, 0);
        Assert.assertEquals(afterRemovalCount, 0);
    }

    @Test
    public void testGetAllTags(){
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);

        Collection<Tag> tags = tagDao.getAllTags();

        Assert.assertNotNull(tags);
        Assert.assertEquals(2,tags.size());
        List<Long> maybeList = tags.stream().mapToLong(Tag::getId).boxed().collect(Collectors.toList());
        Assert.assertTrue(maybeList.contains(tag.getId()));
        Assert.assertTrue(maybeList.contains(tag2.getId()));
    }

    @Test
    public void testGetAllTagsEmpty() {
        Collection<Tag> tags = tagDao.getAllTags();

        Assert.assertNotNull(tags);
        Assert.assertEquals(0, tags.size());
    }


    @Test
    public void testGetAllTagsCountByName(){
        TestMethods.insertTag(em, TestConstants.TAG);
        TestMethods.insertTag(em, TestConstants.TAG2);

        int res1 = tagDao.getAllTagsCountByName(TestConstants.TAG, true, false, null);
        int res2 = tagDao.getAllTagsCountByName(TestConstants.TAG2, true, false, null);

        Assert.assertEquals(1,res1);
        Assert.assertEquals(1,res2);
    }

    @Test
    public void testGetAllTagsCountByNameEmpty(){
        TestMethods.insertTag(em, TestConstants.TAG);
        TestMethods.insertTag(em, TestConstants.TAG2);

        Assert.assertEquals(0,tagDao.getAllTagsCountByName(TestConstants.TAG4, true, false, null));
    }

    @Test
    public void testGetAllTagsCount(){
        TestMethods.insertTag(em, TestConstants.TAG);
        TestMethods.insertTag(em, TestConstants.TAG2);

        int res = tagDao.getAllTagsCount(true, false, null);

        Assert.assertEquals(2,res);
    }

    @Test
    public void testGetAllTagsCountEmpty(){
        int res = tagDao.getAllTagsCount(true, false, null);

        Assert.assertEquals(0,res);
    }

    @Test
    public void testFindTagsByName(){
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);

        Collection<Tag> collection = tagDao.findTagsByName(TestConstants.TAG_TERM, true, false, null, 1,TestConstants.TAG_PAGE_SIZE);

        Assert.assertNotNull(collection);
        Assert.assertEquals(2,collection.size());
        Assert.assertTrue(collection.contains(tag));
        Assert.assertTrue(collection.contains(tag2));
    }

    @Test
    public void testFindTagsByNameNotShowEmpty(){
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);

        Collection<Tag> collection = tagDao.findTagsByName(TestConstants.TAG_TERM, false, false, null,1,TestConstants.TAG_PAGE_SIZE);

        Assert.assertNotNull(collection);
        Assert.assertEquals(0,collection.size());
    }

    @Test
    public void testFindTagsByNameEmpty(){
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);

        Collection<Tag> collection = tagDao.findTagsByName(TestConstants.TAG4, true, false, null, 1,TestConstants.TAG_PAGE_SIZE);

        Assert.assertNotNull(collection);
        Assert.assertEquals(0,collection.size());

    }

    @Test
    public void testFindTagsByNameWithUserShowAll(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL, Instant.now(),TestConstants.LOCALE_EN,false);

        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
        Tag tag3 = TestMethods.insertTag(em, TestConstants.TAG3);

        TestMethods.setUserFollowingTags(em, user, Arrays.asList(tag, tag2));

        Collection<Tag> collection = tagDao.findTagsByName(TestConstants.TAG_TERM, true, false, user.getId(), 1,TestConstants.TAG_PAGE_SIZE);

        Assert.assertNotNull(collection);
        Assert.assertEquals(3,collection.size());
    }

    @Test
    public void testFindTagsByNameShowFollowing(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL, Instant.now(),TestConstants.LOCALE_EN,false);

        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
        Tag tag3 = TestMethods.insertTag(em, TestConstants.TAG3);

        TestMethods.setUserFollowingTags(em, user, Arrays.asList(tag, tag2));

        Collection<Tag> collection = tagDao.findTagsByName(TestConstants.TAG_TERM, true, true, user.getId(), 1,TestConstants.TAG_PAGE_SIZE);

        Assert.assertNotNull(collection);
        Assert.assertEquals(2,collection.size());
    }

    @Test
    public void testFindTagsByNameShowFollowingHideEmpty(){
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
        Tag tag3 = TestMethods.insertTag(em, TestConstants.TAG3);

        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL, Instant.now(),TestConstants.LOCALE_EN,false);
        Language language = TestMethods.insertLanguage(em, TestConstants.LANGUAGE);
        Snippet snippet = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), TestConstants.SNIPPET_FLAGGED, false);

        TestMethods.setUserFollowingTags(em, user, Arrays.asList(tag, tag2));

        Collection<Tag> collection = tagDao.findTagsByName(TestConstants.TAG_TERM, false, true, user.getId(), 1,TestConstants.TAG_PAGE_SIZE);

        Assert.assertNotNull(collection);
        Assert.assertEquals(1,collection.size());
    }

    @Test
    public void testFindTagsByNameCountShowFollowingHideEmpty(){
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
        Tag tag3 = TestMethods.insertTag(em, TestConstants.TAG3);

        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL, Instant.now(),TestConstants.LOCALE_EN,false);
        Language language = TestMethods.insertLanguage(em, TestConstants.LANGUAGE);
        Snippet snippet = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag), TestConstants.SNIPPET_FLAGGED, false);

        TestMethods.setUserFollowingTags(em, user, Arrays.asList(tag, tag2));

        int tagCount = tagDao.getAllTagsCountByName(TestConstants.TAG_TERM, false, true, user.getId());

        Assert.assertEquals(1,tagCount);
    }

    @Test
    public void testFindTagsByNameShowFollowingHideEmptyNoSnippets(){
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);
        Tag tag3 = TestMethods.insertTag(em, TestConstants.TAG3);

        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL, Instant.now(),TestConstants.LOCALE_EN,false);
        Language language = TestMethods.insertLanguage(em, TestConstants.LANGUAGE);
        Snippet snippet = TestMethods.insertSnippet(em, user, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), language, Collections.singletonList(tag3), TestConstants.SNIPPET_FLAGGED, false);

        TestMethods.setUserFollowingTags(em, user, Arrays.asList(tag, tag2));

        Collection<Tag> collection = tagDao.findTagsByName(TestConstants.TAG_TERM, false, true, user.getId(), 1,TestConstants.TAG_PAGE_SIZE);

        Assert.assertNotNull(collection);
        Assert.assertTrue(collection.isEmpty());
    }

    @Test
    public void testFindSpecificTagsByName() {
        TestMethods.insertTag(em, TestConstants.TAG);
        TestMethods.insertTag(em, TestConstants.TAG2);
        TestMethods.insertTag(em, TestConstants.TAG3);

        Collection<Tag> collection = tagDao.findSpecificTagsByName(Arrays.asList(TestConstants.TAG, TestConstants.TAG2, TestConstants.TAG3));

        Assert.assertNotNull(collection);
        Assert.assertEquals(3, collection.size());
    }

    @Test
    public void testFindSpecificTagsByNameEmpty() {
        TestMethods.insertTag(em, TestConstants.TAG);
        TestMethods.insertTag(em, TestConstants.TAG2);
        TestMethods.insertTag(em, TestConstants.TAG3);

        Collection<Tag> collection = tagDao.findSpecificTagsByName(Collections.singletonList(TestConstants.TAG_TERM));

        Assert.assertNotNull(collection);
        Assert.assertTrue(collection.isEmpty());
    }

    @Test
    public void testFindSpecificTagsByNameNoTags() {
        Collection<Tag> collection = tagDao.findSpecificTagsByName(Collections.singletonList(TestConstants.TAG_TERM));

        Assert.assertNotNull(collection);
        Assert.assertTrue(collection.isEmpty());
    }


}
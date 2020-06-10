package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.TagDao;
import ar.edu.itba.paw.models.Language;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class TagDaoTest {

    @Autowired private DataSource ds;
    @Autowired private TagDao tagDao;

    @PersistenceContext
    private EntityManager em;

    private long defaultSnippetId;

    private User user;
    private Language language;

    @Before
    public void setUp() {
        user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL, TestConstants.USER_DATE, TestConstants.LOCALE_EN, TestConstants.USER_VERIFIED);
        language = TestMethods.insertLanguage(em, TestConstants.LANGUAGE);
    }


    @Test
    @Transactional
    public void testAddTag(){
        Tag maybeTag = tagDao.addTag(TestConstants.TAG);

        Assert.assertNotNull(maybeTag);
        Assert.assertEquals(TestConstants.TAG, maybeTag.getName());
    }

   @Test
   @Transactional
    public void testAddTagS(){
        List<String> stringList = Arrays.asList(TestConstants.TAG,TestConstants.TAG2,null);

        tagDao.addTags(stringList);
    }

    @Test
    public void testAddTagSEmpty(){
        List<String> stringList = new ArrayList<>();

        tagDao.addTags(stringList);
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

        Optional<Tag> maybeTag = tagDao.findById(-1);

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
    public void testGetAllTags(){
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);

        Collection<Tag> maybeTags = tagDao.getAllTags();

        Assert.assertNotNull(maybeTags);
        Assert.assertEquals(2,maybeTags.size());
        List<Long> maybeList = maybeTags.stream().mapToLong(Tag::getId).boxed().collect(Collectors.toList());
        Assert.assertTrue(maybeList.contains(tag.getId()));
        Assert.assertTrue(maybeList.contains(tag2.getId()));
    }

    @Test
    public void testGetAllTagsEmpty(){
        Collection<Tag> maybeTags = tagDao.getAllTags();

        Assert.assertNotNull(maybeTags);
        Assert.assertEquals(0,maybeTags.size());
    }

//    @Test
//    public void testFindTagsForSnippet(){
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,TAGS_TABLE);
//        long tagId1 = insertTagIntoDb(jdbcInsertTag,TAG);
//        long tagId2 = insertTagIntoDb(jdbcInsertTag,TAG2);
//        insertSnippetTagIntoDb(jdbcInsertTagSnippet,defaultSnippetId,tagId1);
//        insertSnippetTagIntoDb(jdbcInsertTagSnippet,defaultSnippetId,tagId2);
//
//        Collection<Tag> maybeTags = tagDao.findTagsForSnippet(defaultSnippetId);
//
//        Assert.assertEquals(2,maybeTags.size());
//        List<Long> maybeList = maybeTags.stream().mapToLong(Tag::getId).boxed().collect(Collectors.toList());
//        Assert.assertTrue(maybeList.contains(tagId1));
//        Assert.assertTrue(maybeList.contains(tagId2));
//    }

    // TODO REMOVE
//    @Test
//    public void testFindTagsForSnippetEmpty(){
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,TAGS_TABLE);
//        insertTagIntoDb(jdbcInsertTag,TAG);
//        insertTagIntoDb(jdbcInsertTag,TAG2);
//
//        Collection<Tag> maybeTags = tagDao.findTagsForSnippet(defaultSnippetId);
//
//        Assert.assertEquals(0,maybeTags.size());
//    }


    @Test
    @Transactional
    public void testRemoveTag(){
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);

        tagDao.removeTag(tag.getId());
    }

    @Test
    public void testRemoveTagEmpty(){
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);

        tagDao.removeTag(tag.getId());
    }

    @Test
    public void testGetAllTagsCountByName(){
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);

        int res1 = tagDao.getAllTagsCountByName(TestConstants.TAG, true);
        int res2 = tagDao.getAllTagsCountByName(TestConstants.TAG2, true);

        Assert.assertEquals(1,res1);
        Assert.assertEquals(1,res2);
    }

    @Test
    public void testGetAllTagsCountByNameEmpty(){
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);

        int res = tagDao.getAllTagsCountByName("Not a tag name-xxxxx", true);

        Assert.assertEquals(0,res);
    }

    @Test
    public void testGetAllTagsCount(){
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);

        int res = tagDao.getAllTagsCount(true);

        Assert.assertEquals(2,res);
    }

    @Test
    public void testGetAllTagsCountEmpty(){
        int res = tagDao.getAllTagsCount(true);

        Assert.assertEquals(0,res);
    }

    @Test
    public void testFindTagsByName(){
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);

        Collection<Tag> maybeCollection = tagDao.findTagsByName("tag", true,1,TestConstants.TAG_PAGE_SIZE);

        Assert.assertNotNull(maybeCollection);
        Assert.assertEquals(2,maybeCollection.size());
        List<Long> idCol = maybeCollection.stream().mapToLong(Tag::getId).boxed().collect(Collectors.toList());
        Assert.assertTrue(idCol.contains(tag.getId()));
        Assert.assertTrue(idCol.contains(tag2.getId()));
    }

    @Test
    public void testFindTagsByNameEmpty(){
        Tag tag = TestMethods.insertTag(em, TestConstants.TAG);
        Tag tag2 = TestMethods.insertTag(em, TestConstants.TAG2);

        Collection<Tag> maybeCollection = tagDao.findTagsByName("notatagname-xxxx", true,1,TestConstants.TAG_PAGE_SIZE);

        Assert.assertNotNull(maybeCollection);
        Assert.assertEquals(0,maybeCollection.size());

    }



}
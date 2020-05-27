package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.TagDao;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
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

import static ar.edu.itba.paw.persistence.TestHelper.*;
import static org.junit.Assert.*;

/*
 Tested Methods:
    void addTags(List<String> tags);
    Tag addTag(String name);
     Optional<Tag> findById(long id);
    Optional<Tag> findByName(String name);
     Collection<Tag> getAllTags();
 Not tested Methods:
    void addSnippetTag(long snippetOd, long tagId);

*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class TagDaoTest {

    @Autowired private DataSource ds;
    @Autowired private TagDao tagDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertTag;
    private SimpleJdbcInsert jdbcInsertTagSnippet;

    @PersistenceContext
    private EntityManager em;

    private long defaultSnippetId;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertTag = new SimpleJdbcInsert(ds).withTableName(TAGS_TABLE).usingGeneratedKeyColumns("id");
        SimpleJdbcInsert jdbcInsertSnippet = new SimpleJdbcInsert(ds).withTableName(SNIPPETS_TABLE).usingGeneratedKeyColumns("id");
        SimpleJdbcInsert jdbcInsertLanguage = new SimpleJdbcInsert(ds).withTableName(LANGUAGES_TABLE).usingGeneratedKeyColumns("id");
        SimpleJdbcInsert jdbcInsertUser = new SimpleJdbcInsert(ds).withTableName(USERS_TABLE).usingGeneratedKeyColumns("id");
        jdbcInsertTagSnippet = new SimpleJdbcInsert(ds).withTableName(SNIPPET_TAGS_TABLE);

        JdbcTestUtils.deleteFromTables(jdbcTemplate,TAGS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,VOTES_FOR_TABLE);

        JdbcTestUtils.deleteFromTables(jdbcTemplate,USERS_TABLE);
        User user = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,DESCR,LOCALE_EN);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, LANGUAGES_TABLE);
        long languageId = insertLanguageIntoDb(jdbcInsertLanguage,LANGUAGE);

        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        defaultSnippetId = insertSnippetIntoDb(jdbcInsertSnippet,user.getId(),TITLE,DESCR,CODE, languageId,0);
    }


    @Test
    @Transactional
    public void testAddTag(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,TAGS_TABLE);

        Tag maybeTag = tagDao.addTag(TAG);
        em.flush();
        assertNotNull(maybeTag);
        assertEquals(TAG.toLowerCase(),maybeTag.getName());
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,TAGS_TABLE));
    }

   @Test
   @Transactional
    public void testAddTagS(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,TAGS_TABLE);
        List<String> stringList = Arrays.asList(TAG,TAG2,null);

        tagDao.addTags(stringList);
        em.flush();

        assertEquals(2,JdbcTestUtils.countRowsInTable(jdbcTemplate,TAGS_TABLE));
    }

    @Test
    public void testAddTagSEmpty(){
        List<String> stringList = new ArrayList<>();

        tagDao.addTags(stringList);

        assertEquals(0,JdbcTestUtils.countRowsInTable(jdbcTemplate,TAGS_TABLE));
    }

    @Test
    public void testFindById(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,TAGS_TABLE);
        long tagId = insertTagIntoDb(jdbcInsertTag,TAG);

        Optional<Tag> maybeTag = tagDao.findById(tagId);

        assertTrue(maybeTag.isPresent());
        assertEquals(tagId, (long) maybeTag.get().getId());
        assertEquals(TAG,maybeTag.get().getName());
    }

    @Test
    public void testFindByIdEmpty(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,TAGS_TABLE);
        long tagId = insertTagIntoDb(jdbcInsertTag,TAG);

        Optional<Tag> maybeTag = tagDao.findById(tagId+10);

        assertFalse(maybeTag.isPresent());
    }

    @Test
    public void testFindByName(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,TAGS_TABLE);
        long tagId = insertTagIntoDb(jdbcInsertTag,TAG);

        Optional<Tag> maybeTag = tagDao.findByName(TAG);

        assertTrue(maybeTag.isPresent());
        assertEquals(tagId, (long) maybeTag.get().getId());
        assertEquals(TAG,maybeTag.get().getName());
    }

    @Test
    public void testFindByNameEmpty(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,TAGS_TABLE);
        insertTagIntoDb(jdbcInsertTag,TAG);

        Optional<Tag> maybeTag = tagDao.findByName(TAG2);

        assertFalse(maybeTag.isPresent());
    }

    @Test
    public void testGetAllTags(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,TAGS_TABLE);
        long tagId1 = insertTagIntoDb(jdbcInsertTag,TAG);
        long tagId2 = insertTagIntoDb(jdbcInsertTag,TAG2);

        Collection<Tag> maybeTags = tagDao.getAllTags();

        assertNotNull(maybeTags);
        assertEquals(2,maybeTags.size());
        List<Long> maybeList = maybeTags.stream().mapToLong(Tag::getId).boxed().collect(Collectors.toList());
        assertTrue(maybeList.contains(tagId1));
        assertTrue(maybeList.contains(tagId2));
    }

    @Test
    public void testGetAllTagsEmpty(){
        Collection<Tag> maybeTags = tagDao.getAllTags();

        assertNotNull(maybeTags);
        assertEquals(0,maybeTags.size());
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
//        assertEquals(2,maybeTags.size());
//        List<Long> maybeList = maybeTags.stream().mapToLong(Tag::getId).boxed().collect(Collectors.toList());
//        assertTrue(maybeList.contains(tagId1));
//        assertTrue(maybeList.contains(tagId2));
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
//        assertEquals(0,maybeTags.size());
//    }

    @Test
    @Transactional
    public void testRemoveTag(){
        long tagId = insertTagIntoDb(jdbcInsertTag,TAG);
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,TAGS_TABLE));
        tagDao.removeTag(tagId);
        em.flush();

        assertEquals(0,JdbcTestUtils.countRowsInTable(jdbcTemplate,TAGS_TABLE));
    }

    @Test
    public void testRemoveTagEmpty(){
        long tagId = insertTagIntoDb(jdbcInsertTag,TAG);

        tagDao.removeTag(tagId+10);

        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,TAGS_TABLE));
    }

    @Test
    public void testGetAllTagsCountByName(){
        long tagId1 = insertTagIntoDb(jdbcInsertTag,TAG);
        long tagId2 = insertTagIntoDb(jdbcInsertTag,TAG2);

        int res1 = tagDao.getAllTagsCountByName(TAG);
        int res2 = tagDao.getAllTagsCountByName(TAG2);

        assertEquals(1,res1);
        assertEquals(1,res2);
    }

    @Test
    public void testGetAllTagsCountByNameEmpty(){
        long tagId1 = insertTagIntoDb(jdbcInsertTag,TAG);
        long tagId2 = insertTagIntoDb(jdbcInsertTag,TAG2);

        int res = tagDao.getAllTagsCountByName("zzzz");

        assertEquals(0,res);
    }

    @Test
    public void testGetAllTagsCount(){
        insertTagIntoDb(jdbcInsertTag,TAG);

        int res = tagDao.getAllTagsCount();

        assertEquals(1,res);
    }

    @Test
    public void testGetAllTagsCountEmpty(){
        int res = tagDao.getAllTagsCount();

        assertEquals(0,res);
    }

    @Test
    public void testFindTagsByName(){
        long tagId1 = insertTagIntoDb(jdbcInsertTag,TAG);
        long tagId2 = insertTagIntoDb(jdbcInsertTag,TAG2);

        Collection<Tag> maybeCollection = tagDao.findTagsByName("tag",1,PAGE_SIZE);

        assertNotNull(maybeCollection);
        assertEquals(2,maybeCollection.size());
        List<Long> idCol = maybeCollection.stream().mapToLong(Tag::getId).boxed().collect(Collectors.toList());
        assertTrue(idCol.contains(tagId1));
        assertTrue(idCol.contains(tagId2));
    }

    @Test
    public void testFindTagsByNameEmpty(){
        long tagId1 = insertTagIntoDb(jdbcInsertTag,TAG);
        long tagId2 = insertTagIntoDb(jdbcInsertTag,TAG2);

        Collection<Tag> maybeCollection = tagDao.findTagsByName("zzzz",1,PAGE_SIZE);

        assertNotNull(maybeCollection);
        assertEquals(0,maybeCollection.size());

    }



}
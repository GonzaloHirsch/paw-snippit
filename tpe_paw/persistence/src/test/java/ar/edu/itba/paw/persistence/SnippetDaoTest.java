package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.dao.TagDao;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static ar.edu.itba.paw.persistence.TestHelper.*;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SnippetDaoTest {

    @Autowired
    private DataSource ds;

    @Autowired
    @InjectMocks
    private SnippetDao snippetDao;

    @Mock
    private TagDao mockTagDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertSnippet;
    private SimpleJdbcInsert jdbcInsertSnippetTags;
    private SimpleJdbcInsert jdbcInsertFavorite;
    private SimpleJdbcInsert jdbcInsertFollowing;
    private SimpleJdbcInsert jdbcInsertVote;

    private User defaultUser;
    private User altUser;
    private long defaultLanguageId;
    private Tag defaultTag;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        MockitoAnnotations.initMocks(this); // Replaces @RunWith(MockitJUnitRunner.class)

        jdbcInsertSnippet = new SimpleJdbcInsert(ds).withTableName(SNIPPETS_TABLE).usingGeneratedKeyColumns("id");
        SimpleJdbcInsert jdbcInsertUser = new SimpleJdbcInsert(ds).withTableName(USERS_TABLE).usingGeneratedKeyColumns("id");
        SimpleJdbcInsert jdbcInsertLanguage = new SimpleJdbcInsert(ds).withTableName(LANGUAGES_TABLE).usingGeneratedKeyColumns("id");
        SimpleJdbcInsert jdbcInsertTag = new SimpleJdbcInsert(ds).withTableName(TAGS_TABLE).usingGeneratedKeyColumns("id");
        jdbcInsertSnippetTags = new SimpleJdbcInsert(ds).withTableName(SNIPPET_TAGS_TABLE);
        jdbcInsertFavorite = new SimpleJdbcInsert(ds).withTableName(FAVORITES_TABLE);
        jdbcInsertFollowing = new SimpleJdbcInsert(ds).withTableName(FOLLOWS_TABLE);
        jdbcInsertVote = new SimpleJdbcInsert(ds).withTableName(VOTES_FOR_TABLE);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, SNIPPETS_TABLE);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, USERS_TABLE);
        defaultUser = insertUserIntoDb(jdbcInsertUser, USERNAME, PASSWORD, EMAIL, DESCR);
        altUser = insertUserIntoDb(jdbcInsertUser,USERNAME2,PASSWORD2,EMAIL2,DESCR);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, LANGUAGES_TABLE);
        defaultLanguageId = insertLanguageIntoDb(jdbcInsertLanguage,LANGUAGE);

        JdbcTestUtils.deleteFromTables(jdbcTemplate,TAGS_TABLE);
        defaultTag = new Tag(insertTagIntoDb(jdbcInsertTag,TAG),TAG);
    }


    @Test
    public void testCreate() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);

        final long snippetId = snippetDao.createSnippet(defaultUser.getId(), TITLE, DESCR, CODE, DATE.format(Calendar.getInstance().getTime()), defaultLanguageId);

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, SNIPPETS_TABLE));
    }


    @Test
    public void testFindSnippetByCriteria(){
        // Precondiciones
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE, defaultLanguageId,0);

        // Ejercitacion
        Optional<Snippet> maybeSnippet = snippetDao.findSnippetByCriteria(
                SnippetDao.QueryTypes.SEARCH,
                SnippetDao.Types.TITLE,
                TITLE,
                SnippetDao.Locations.HOME,
                SnippetDao.Orders.ASC,
                null,
                null,
                1,
                6).stream().findFirst();

        //Evaluacion
        assertTrue(maybeSnippet.isPresent());
        assertEquals(defaultUser.getUsername(), maybeSnippet.get().getOwner().getUsername());
        assertEquals(TITLE, maybeSnippet.get().getTitle());
        assertEquals(DESCR, maybeSnippet.get().getDescription());
    }

    @Test
    public void findSnippetByDeepCriteriaTest(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);

        Optional<Snippet> maybeSnippet = snippetDao.findSnippetByDeepCriteria(null,
                null,
                0,
                10,
                0,
                10,
                defaultLanguageId,
                null,
                TITLE,
                defaultUser.getUsername(),
                "title",
                "asc",
                true,
                1, 6).stream().findFirst();

        assertTrue(maybeSnippet.isPresent());
        assertEquals(TITLE,maybeSnippet.get().getTitle());

    }

    @Test
    public void testGetAllSnippets(){
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
        long snippetId2 = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);

        Collection<Snippet> maybeColl = snippetDao.getAllSnippets(1,PAGE_SIZE);

        assertEquals(2,JdbcTestUtils.countRowsInTable(jdbcTemplate,SNIPPETS_TABLE));
    }

    @Test
    public void testGetAllFavoriteSnippets(){
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);;
        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
        insertFavoriteIntoDb(jdbcInsertFavorite,snippetId,defaultUser.getId());

        Collection<Snippet> maybeCollection = snippetDao.getAllFavoriteSnippets(defaultUser.getId(),1,PAGE_SIZE);

        assertNotNull(maybeCollection);
        assertEquals(1,maybeCollection.size());
        Snippet s = (Snippet) maybeCollection.toArray()[0];
        assertEquals(snippetId,s.getId());
    }

    @Test
    public void testGetAllFollowingSnippets(){
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);;
        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
        insertFollowingIntoDb(jdbcInsertFollowing,defaultTag.getId(),defaultUser.getId());
        insertSnippetTagIntoDb(jdbcInsertSnippetTags,snippetId,defaultTag.getId());

        Collection<Snippet> maybeCollection = snippetDao.getAllFollowingSnippets(defaultUser.getId(),1,PAGE_SIZE);

        assertNotNull(maybeCollection);
        assertEquals(1,maybeCollection.size());
        Snippet s = (Snippet) maybeCollection.toArray()[0];
        assertEquals(snippetId,s.getId());
    }

    @Test
    public void testGetAllUpvotedSnippets(){
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);;
        long decoyId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
        insertVotesForIntoDb(jdbcInsertVote,snippetId,defaultUser.getId(),1);


        Collection<Snippet> maybeCollection = snippetDao.getAllUpVotedSnippets(defaultUser.getId(),1,PAGE_SIZE);

        assertNotNull(maybeCollection);
        assertEquals(1,maybeCollection.size());
        Snippet s = (Snippet) maybeCollection.toArray()[0];
        assertEquals(snippetId,s.getId());
    }

    @Test
    public void testGetAllFlaggedSnippets(){
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,1);;
        long decoyId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);

        Collection<Snippet> maybeCollection = snippetDao.getAllFlaggedSnippets(1,PAGE_SIZE);

        assertNotNull(maybeCollection);
        assertEquals(1,maybeCollection.size());
        Snippet s = (Snippet) maybeCollection.toArray()[0];
        assertEquals(snippetId,s.getId());
    }

    @Test
    public void testFindAllSnippetsByOwner(){
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);;
        long decoyId = insertSnippetIntoDb(jdbcInsertSnippet,altUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);

        Collection<Snippet> maybeCollection = snippetDao.findAllSnippetsByOwner(defaultUser.getId(),1,PAGE_SIZE);

        assertNotNull(maybeCollection);
        assertEquals(1,maybeCollection.size());
        Snippet s = (Snippet) maybeCollection.toArray()[0];
        assertEquals(snippetId,s.getId());
    }

    @Test
    public void testFindSnippetsWithLanguage(){
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);;

        Collection<Snippet> maybeCollection = snippetDao.findSnippetsWithLanguage(defaultLanguageId,1,PAGE_SIZE);

        assertNotNull(maybeCollection);
        assertEquals(1,maybeCollection.size());
        Snippet s = (Snippet) maybeCollection.toArray()[0];
        assertEquals(snippetId,s.getId());
    }

    @Test
    public void testFindById() {
        // Precondiciones
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE, defaultLanguageId,0);
        Collection<Tag> tagList = Collections.singletonList(defaultTag);
        Mockito.when(mockTagDao.findTagsForSnippet(snippetId)).thenReturn(tagList);

        // Ejercitacion
        Optional<Snippet> maybeSnippet = snippetDao.findSnippetById(snippetId);

        //Evaluacion
        assertTrue(maybeSnippet.isPresent());
        assertEquals(defaultUser.getUsername(), maybeSnippet.get().getOwner().getUsername());
        assertEquals(TITLE, maybeSnippet.get().getTitle());
        assertEquals(DESCR, maybeSnippet.get().getDescription());
        assertTrue(maybeSnippet.get().getTags().contains(defaultTag));
    }

    @Test
    public void testDeleteSnippetById(){
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
        long decoyId = insertSnippetIntoDb(jdbcInsertSnippet,altUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);

        boolean result = snippetDao.deleteSnippetById(snippetId);

        assertTrue(result);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate,SNIPPETS_TABLE));

    }

    //FALLA TODO: Check why
    @Test
    public void testGetNewSnippetsForTagsCount(){
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
        insertSnippetTagIntoDb(jdbcInsertSnippetTags,snippetId,defaultTag.getId());
        Calendar weekBefore = Calendar.getInstance();
        weekBefore.add(Calendar.WEEK_OF_YEAR, -1);
        Timestamp weekBeforeTs = new Timestamp(weekBefore.getTime().getTime());

        int result = snippetDao.getNewSnippetsForTagsCount(DATE.format(weekBeforeTs),Arrays.asList(defaultTag),defaultUser.getId());

        assertEquals(1,result);
    }

    @Test
    public void testFindSnippetForTag() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, SNIPPETS_TABLE);
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(), TITLE, DESCR, CODE, defaultLanguageId,0);
        insertSnippetTagIntoDb(jdbcInsertSnippetTags,snippetId,defaultTag.getId());

        Optional<Snippet> snippet = snippetDao.findSnippetsForTag(defaultTag.getId()).stream().findFirst();

        assertTrue(snippet.isPresent());
        assertEquals(snippetId,snippet.get().getId());
    }

    @Test
    public void testFlagSnippet(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, SNIPPETS_TABLE);
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(), TITLE, DESCR, CODE, defaultLanguageId,0);

        snippetDao.flagSnippet(snippetId);

        int flagged = jdbcTemplate.queryForObject("SELECT flagged FROM complete_snippets WHERE id = ?",new Object[]{snippetId},Integer.class);
        assertEquals(1,flagged);
    }

    @Test
    public void testUnflagSnippet(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, SNIPPETS_TABLE);
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(), TITLE, DESCR, CODE, defaultLanguageId,1);

        snippetDao.unflagSnippet(snippetId);

        int flagged = jdbcTemplate.queryForObject("SELECT flagged FROM complete_snippets WHERE id = ?",new Object[]{snippetId},Integer.class);
        assertEquals(0,flagged);
    }



    @Test
    public void testGetAllSnippetsCount(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE2,DESCR,CODE,defaultLanguageId,0);

        int snippetCount = snippetDao.getAllSnippetsCount();

        assertEquals(2,snippetCount);
    }



    @Test
    public void testGetAllFollowingSnippetsCount(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(), TITLE, DESCR, CODE, defaultLanguageId,0);

        int count = snippetDao.getAllFavoriteSnippetsCount(defaultUser.getId());

        assertEquals(0,count);
    }





















}

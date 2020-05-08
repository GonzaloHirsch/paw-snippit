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
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
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

    private User defaultUser;
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

        JdbcTestUtils.deleteFromTables(jdbcTemplate, SNIPPETS_TABLE);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, USERS_TABLE);
        defaultUser = insertUserIntoDb(jdbcInsertUser, USERNAME, PASSWORD, EMAIL, DESCR,LOCALE_EN);

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
    public void testFindById() {
        // Precondiciones
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE, defaultLanguageId);
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
    public void testFindAllSnippetsForUser(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId);

        Optional<Snippet> maybeSnippet = snippetDao.findAllSnippetsByOwner(defaultUser.getId(),1, 6).stream().findFirst();

        assertTrue(maybeSnippet.isPresent());
        assertEquals(snippetId, maybeSnippet.get().getId());
    }

    @Test
    public void testGetAllSnippetsCount(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId);
        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE2,DESCR,CODE,defaultLanguageId);

        int snippetCount = snippetDao.getAllSnippetsCount();

        assertEquals(2,snippetCount);
    }

    @Test
    public void testFindSnippetForTag() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, SNIPPETS_TABLE);
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(), TITLE, DESCR, CODE, defaultLanguageId);
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("snippet_id", snippetId); put("tag_id", defaultTag.getId());
        }};
        jdbcInsertSnippetTags.execute(map);

        Optional<Snippet> snippet = snippetDao.findSnippetsForTag(defaultTag.getId()).stream().findFirst();

        assertTrue(snippet.isPresent());
        assertEquals(snippetId,snippet.get().getId());
    }

    @Test
    public void testGetAllFollowingSnippetsCount(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(), TITLE, DESCR, CODE, defaultLanguageId);

        int count = snippetDao.getAllFavoriteSnippetsCount(defaultUser.getId());

        assertEquals(0,count);
    }


    @Test
    public void testFindSnippetByCriteria(){
        // Precondiciones
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE, defaultLanguageId);

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
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId);

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
    public void findSnippetByCriteriaMultiTest(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId);







    }



}

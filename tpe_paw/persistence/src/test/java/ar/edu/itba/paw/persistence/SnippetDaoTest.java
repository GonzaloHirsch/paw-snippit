package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.models.Language;
import ar.edu.itba.paw.models.Snippet;
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

import javax.sql.DataSource;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ar.edu.itba.paw.persistence.TestHelper.*;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SnippetDaoTest {

    @Autowired
    private DataSource ds;
    @Autowired
    private SnippetDao snippetDao;
    private JdbcTemplate jdbcTemplate;


    private SimpleJdbcInsert jdbcInsertUser;
    private SimpleJdbcInsert jdbcInsertSnippet;
    private SimpleJdbcInsert jdbcInsertLanguage;
    private SimpleJdbcInsert jdbcInsertTag;
    private SimpleJdbcInsert jdbcInsertSnippetTags;

    private User defaultUser;
    private Language defaultLanguage;
    private Tag defaultTag;



    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);

        jdbcInsertSnippet = new SimpleJdbcInsert(ds).withTableName(SNIPPETS_TABLE).usingGeneratedKeyColumns("id");
        jdbcInsertUser = new SimpleJdbcInsert(ds).withTableName(USERS_TABLE).usingGeneratedKeyColumns("id");
        jdbcInsertLanguage = new SimpleJdbcInsert(ds).withTableName(LANGUAGES_TABLE).usingGeneratedKeyColumns("id");
        jdbcInsertTag = new SimpleJdbcInsert(ds).withTableName(TAGS_TABLE).usingGeneratedKeyColumns("id");
        jdbcInsertSnippetTags = new SimpleJdbcInsert(ds).withTableName(SNIPPET_TAGS_TABLE);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, SNIPPETS_TABLE);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, USERS_TABLE);
        defaultUser = insertUserIntoDb(jdbcInsertUser, USERNAME, PASSWORD, EMAIL, "");

        JdbcTestUtils.deleteFromTables(jdbcTemplate, LANGUAGES_TABLE);
        defaultLanguage = new Language(insertLanguageIntoDb(jdbcInsertLanguage,LANGUAGE), "language1");

        JdbcTestUtils.deleteFromTables(jdbcTemplate,TAGS_TABLE);
        defaultTag = new Tag(insertTagIntoDb(jdbcInsertTag,TAG),TAG);
    }


    @Test
    public void testCreate() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);

        final long snippetId = snippetDao.createSnippet(defaultUser, TITLE, DESCR, CODE, DATE.format(Calendar.getInstance().getTime().getTime()), defaultLanguage.getId());

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "snippets"));
    }

    @Test
    public void testFindById() {
        // Precondiciones
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser,TITLE,DESCR,CODE, defaultLanguage);

        // Ejercitacion
        Optional<Snippet> maybeSnippet = snippetDao.findSnippetById(snippetId);

        //Evaluacion
        assertTrue(maybeSnippet.isPresent());
        assertEquals(defaultUser.getUsername(), maybeSnippet.get().getOwner().getUsername());
        assertEquals(TITLE, maybeSnippet.get().getTitle());
        assertEquals(DESCR, maybeSnippet.get().getDescription());
    }

    @Test
    public void testFindAllSnippetsForUser(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser,TITLE,DESCR,CODE,defaultLanguage);

        Optional<Snippet> maybeSnippet = snippetDao.findAllSnippetsByOwner(defaultUser.getId(),1).stream().findFirst();

        assertTrue(maybeSnippet.isPresent());
        assertEquals(snippetId, maybeSnippet.get().getId());
    }

    @Test
    public void testGetAllSnippetsCount(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser,TITLE,DESCR,CODE,defaultLanguage);
        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser,TITLE2,DESCR,CODE,defaultLanguage);

        int snippetCount = snippetDao.getAllSnippetsCount();

        assertEquals(2,snippetCount);
    }

    @Test
    public void testFindSnippetForTag() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, SNIPPETS_TABLE);
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser, TITLE, DESCR, CODE, defaultLanguage);
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
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser, TITLE, DESCR, CODE, defaultLanguage);

        int count = snippetDao.getAllFavoriteSnippetsCount(defaultUser.getId());

        assertEquals(0,count);
    }

    /*
    @Test
    public void testFindSnippetByCriteria(){
        // Precondiciones
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        long snippetId = insertSnippetInDatabase(defaultUser,TITLE,DESCR,CODE, defaultLanguage);

        // Ejercitacion
        Optional<Snippet> maybeSnippet = snippetDao.findSnippetByCriteria(SnippetDao.QueryTypes.SEARCH,SnippetDao.Types.TITLE, TITLE,SnippetDao.Locations.HOME,SnippetDao.Orders.ASC, user.getId(),1).stream().findFirst();

        //Evaluacion
        assertNotNull(maybeSnippet);
        assertTrue(maybeSnippet.isPresent());
        assertEquals(user.getUsername(), maybeSnippet.get().getOwner().getUsername());
        assertEquals(TITLE, maybeSnippet.get().getTitle());
        assertEquals(DESCR, maybeSnippet.get().getDescription());
    }

     */

}

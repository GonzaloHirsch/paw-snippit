package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.LanguageDao;
import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static junit.framework.TestCase.*;

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
    private final String SNIPPETS_TABLE = "snippets";
    private final String USERS_TABLE = "users";
    private final String LANGUAGES_TABLE = "languages";
    private final String TAGS_TABLE = "tags";
    private final String SNIPPET_TAGS_TABLE = "snippet_tags";

    private static final String PASSWORD = "Password";
    private static final String USERNAME = "Username";
    private static final String EMAIL = "email@email.com";
    private static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static final String TITLE = "Snippet Title";
    private static final String TITLE2 = "Snippet Title 2";
    private static final String DESCR = "Description";
    private static final String CODE = "Snippet Code";

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
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("username", USERNAME);
            put("password", PASSWORD);
            put("email", EMAIL);
            put("reputation", 0);
            put("date_joined", DATE.format(Calendar.getInstance().getTime().getTime()));
        }};
        long userId = jdbcInsertUser.executeAndReturnKey(map).longValue();
        defaultUser = new User(userId, USERNAME, PASSWORD, EMAIL, "", 0, DATE.format(Calendar.getInstance().getTime().getTime()), null);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, LANGUAGES_TABLE);
        Map<String, Object> languageDataMap = new HashMap<String, Object>() {{
            put("name", "language1");
        }};
        long languageId = jdbcInsertLanguage.executeAndReturnKey(languageDataMap).longValue();
        defaultLanguage = new Language(languageId, "language1");

        JdbcTestUtils.deleteFromTables(jdbcTemplate,TAGS_TABLE);
        Map<String, Object> tagDataMap = new HashMap<String, Object>() {{
            put("name", "tag1");
        }};
        long tagId = jdbcInsertTag.executeAndReturnKey(tagDataMap).longValue();
        defaultTag = new Tag(tagId,"tag1");
    }

    private long insertSnippetInDatabase(User user, String title, String description, String code, Language language){
        final Map<String, Object> snippetDataMap = new HashMap<String,Object>(){{
            put("user_id", user.getId());
            put("title", title);
            put("description",description);
            put("code",code);
            put("date_created", DATE.format(Calendar.getInstance().getTime().getTime()));
            put("language_id", language.getId());
        }};
        final long snippetId = jdbcInsertSnippet.executeAndReturnKey(snippetDataMap).longValue();
        return snippetId;
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
        long snippetId = insertSnippetInDatabase(defaultUser,TITLE,DESCR,CODE, defaultLanguage);

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
        long snippetId = insertSnippetInDatabase(defaultUser,TITLE,DESCR,CODE,defaultLanguage);

        Optional<Snippet> maybeSnippet = snippetDao.findAllSnippetsByOwner(defaultUser.getId(),1).stream().findFirst();

        assertTrue(maybeSnippet.isPresent());
        assertEquals(snippetId, maybeSnippet.get().getId());
    }

    @Test
    public void testGetAllSnippetsCount(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        insertSnippetInDatabase(defaultUser,TITLE,DESCR,CODE,defaultLanguage);
        insertSnippetInDatabase(defaultUser,TITLE2,DESCR,CODE,defaultLanguage);

        int snippetCount = snippetDao.getAllSnippetsCount();

        assertEquals(2,snippetCount);
    }

    @Test
    public void testFindSnippetForTag() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, SNIPPETS_TABLE);
        long snippetId = insertSnippetInDatabase(defaultUser, TITLE, DESCR, CODE, defaultLanguage);
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
        long snippetId = insertSnippetInDatabase(defaultUser, TITLE, DESCR, CODE, defaultLanguage);

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

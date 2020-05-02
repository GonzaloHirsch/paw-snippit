package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.LanguageDao;
import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.models.Language;
import ar.edu.itba.paw.models.Snippet;
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

@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SnippetDaoTest {

    @Autowired
    private DataSource ds;
    @Autowired
    private SnippetDao snippetDao;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LanguageDao languageDao;
    private Language lang;

    @Autowired
    private UserDao userDao;
    private User user;

    private SimpleJdbcInsert jdbcInsertUser;
    private SimpleJdbcInsert jdbcInsertSnippet;
    private SimpleJdbcInsert jdbcInsertLanguage;

    private final String SNIPPETS_TABLE = "snippets";
    private final String USERS_TABLE = "users";
    private final String LANGUAGES_TABLE = "languages";

    private static final String PASSWORD = "Password";
    private static final String USERNAME = "Username";
    private static final String EMAIL = "email@email.com";
    private static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private static final String TITLE = "Snippet Title";
    private static final String DESCR = "Description";
    private static final String CODE = "Snippet Code";

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);

        jdbcInsertSnippet = new SimpleJdbcInsert(ds).withTableName(SNIPPETS_TABLE).usingGeneratedKeyColumns("id");
        jdbcInsertUser = new SimpleJdbcInsert(ds).withTableName(USERS_TABLE).usingGeneratedKeyColumns("id");
        jdbcInsertLanguage = new SimpleJdbcInsert(ds).withTableName(LANGUAGES_TABLE).usingGeneratedKeyColumns("id");

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
        user = new User(userId, USERNAME, PASSWORD, EMAIL, "", 0, DATE.format(Calendar.getInstance().getTime().getTime()), null);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, LANGUAGES_TABLE);
        Map<String, Object> languageDataMap = new HashMap<String, Object>() {{
            put("name", "language1");
        }};
        long languageId = jdbcInsertLanguage.executeAndReturnKey(languageDataMap).longValue();
        lang = new Language(languageId, "language1");
    }

    @Test
    public void testCreate() {
        // Ejercitacion
        final long snippetId = snippetDao.createSnippet(user, TITLE, DESCR, CODE, DATE.format(Calendar.getInstance().getTime().getTime()), lang.getId());

        // Evaluacion
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "snippets"));
    }

    @Test
    public void testFindById() {
        // Precondiciones
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        final Map<String, Object> snippetDataMap = new HashMap<String,Object>(){{
            put("user_id", user.getId());
            put("title", TITLE);
            put("description",DESCR);
            put("code",CODE);
            put("date_created", DATE.format(Calendar.getInstance().getTime().getTime()));
            put("language_id", lang.getId());
        }};
        final long snippetId = jdbcInsertSnippet.executeAndReturnKey(snippetDataMap).longValue();

        // Ejercitacion
        Optional<Snippet> maybeSnippet = snippetDao.findSnippetById(snippetId);

        //Evaluacion
        assertNotNull(maybeSnippet);
        assertTrue(maybeSnippet.isPresent());
        assertEquals(user.getUsername(), maybeSnippet.get().getOwner().getUsername());
        assertEquals(TITLE, maybeSnippet.get().getTitle());
        assertEquals(DESCR, maybeSnippet.get().getDescription());
    }
    

}

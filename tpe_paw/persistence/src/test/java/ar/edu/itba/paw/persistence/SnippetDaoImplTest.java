package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.dao.TagDao;
import ar.edu.itba.paw.models.Snippet;
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

import javax.sql.DataSource;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  TestConfig.class)
public class SnippetDaoImplTest {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private final String SNIPPETS_TABLE = "snippets";
    private final String USER_TABLE = "users";
    private final String LANGUAGES_TABLE = "languages";

    private Number userId;
    private Number languageId;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertUser;
    private SimpleJdbcInsert jdbcInsertSnippet;
    private SimpleJdbcInsert jdbcInsertLanguage;
    private SnippetDaoImpl snippetDao;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp(){
        snippetDao = new SnippetDaoImpl(ds);
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertUser = new SimpleJdbcInsert(ds).withTableName(USER_TABLE).usingGeneratedKeyColumns("id");
        jdbcInsertSnippet = new SimpleJdbcInsert(ds).withTableName(SNIPPETS_TABLE).usingGeneratedKeyColumns("id");
        jdbcInsertLanguage = new SimpleJdbcInsert(ds).withTableName(LANGUAGES_TABLE).usingGeneratedKeyColumns("id");

        //Agregamos a la tabla el user(lo ponemos aca porque lo vamos a usar seguido y no es el objeto del test)
        JdbcTestUtils.deleteFromTables(jdbcTemplate,USER_TABLE);
        Map<String, Object> userDataMap = new HashMap<String, Object>() {{ put("username", "JaneRoe");put("password", "asd");put("email", "janeroe@gmail.com"); }};
        userId = jdbcInsertUser.executeAndReturnKey(userDataMap);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, LANGUAGES_TABLE);
        Map<String, Object> languageDataMap = new HashMap<String, Object>() {{ put("name", "language1");}};
        languageId = jdbcInsertLanguage.executeAndReturnKey(languageDataMap);

    }


    @Test
    public void testFindSnippetByTitle() {
        //precondiciones
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        final Map<String, Object> snippetDataMap = new HashMap<String,Object>(){{
            put("user_id", userId);
            put("title", "snippet1");
            put("description","description");
            put("code","codeasdfadsf");
            put("language_id", languageId);
            put("date_created", sdf.format(new Timestamp(System.currentTimeMillis())));
        }};
        final Number snippetId = jdbcInsertSnippet.executeAndReturnKey(snippetDataMap);

        //ejercitacion
        Optional<Snippet> maybeSnippet = snippetDao.findSnippetById(snippetId.longValue());
        //Optional<Snippet> maybeSnippet = snippetDao.findSnippetByCriteria(SnippetDao.QueryTypes.SEARCH,SnippetDao.Types.TITLE, "s1",SnippetDao.Locations.HOME,SnippetDao.Orders.ASC, userId.longValue(),1).stream().findFirst();

        //asserts
        assertTrue(maybeSnippet.isPresent());
        assertEquals(snippetId.longValue(), maybeSnippet.get().getId());
    }


}
package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  TestConfig.class)
public class SnippetDaoImplTest {

    private final String SNIPPETS_TABLE = "snippets";
    private final String USER_TABLE = "users";

    private Number userId;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertUser;
    private SimpleJdbcInsert jdbcInsertSnippet;
    private SnippetDaoImpl snippetDao;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp(){
        snippetDao = new SnippetDaoImpl(ds);
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertUser = new SimpleJdbcInsert(ds).withTableName("users").usingGeneratedKeyColumns("id");
        jdbcInsertSnippet = new SimpleJdbcInsert(ds).withTableName("snippets").usingGeneratedKeyColumns("id");

        //Agregamos a la tabla el user(lo ponemos aca porque lo vamos a usar seguido y no es el objeto del test)
        JdbcTestUtils.deleteFromTables(jdbcTemplate,USER_TABLE);
        Map<String, String> userDataMap = new HashMap<String, String>() {{ put("username", "JaneRoe");put("password", "asd");put("email", "janeroe@gmail.com"); }};
        userId = jdbcInsertUser.executeAndReturnKey(userDataMap);
    }

    /*
    @Test
    public void testFindSnippetByTitle() {
        //precondiciones
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        Map<String, String> snippetDataMap = new HashMap<String, String>() {{ put("user_id", userId.toString()); put("title", "s1"); put("code", "x");put("description","d");}};
        Number snippetId = jdbcInsertSnippet.executeAndReturnKey(snippetDataMap);

        //ejercitacion
        Optional<Snippet> maybeSnippet = snippetDao.findSnippetByCriteria(SnippetDao.Types.TITLE, "s1",SnippetDao.Locations.HOME,SnippetDao.Orders.ASC, null).stream().findFirst();

        //asserts
        assertTrue(maybeSnippet.isPresent());
        assertEquals(snippetId, maybeSnippet.get().getId());
    }
    */
     */

}

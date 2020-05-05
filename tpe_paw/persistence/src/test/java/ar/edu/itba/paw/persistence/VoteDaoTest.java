package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.dao.TagDao;
import ar.edu.itba.paw.interfaces.dao.VoteDao;
import ar.edu.itba.paw.models.Language;
import ar.edu.itba.paw.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

import static ar.edu.itba.paw.persistence.TestHelper.*;

import static junit.framework.TestCase.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class VoteDaoTest {

    @Autowired
    private DataSource ds;

    @Autowired
    @InjectMocks
    private VoteDao voteDao;

    @Mock
    private SnippetDao mockSnippetDao;
    @Mock
    private TagDao mockTagDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertVotesFor;


    private long defaultUserId;
    private long defaultSnippetId;


    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        MockitoAnnotations.initMocks(this); // Replaces @RunWith(MockitJUnitRunner.class)

        jdbcInsertVotesFor = new SimpleJdbcInsert(ds).withTableName(VOTES_FOR_TABLE);
        SimpleJdbcInsert jdbcInsertSnippet= new SimpleJdbcInsert(ds).withTableName(SNIPPETS_TABLE).usingGeneratedKeyColumns("id");
        SimpleJdbcInsert jdbcInsertUser = new SimpleJdbcInsert(ds).withTableName(USERS_TABLE).usingGeneratedKeyColumns("id");
        SimpleJdbcInsert jdbcInsertLanguage = new SimpleJdbcInsert(ds).withTableName(LANGUAGES_TABLE).usingGeneratedKeyColumns("id");

        defaultUserId = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,DESCR).getId();
        long languageId = insertLanguageIntoDb(jdbcInsertLanguage,LANGUAGE);
        defaultSnippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUserId,TITLE,DESCR,CODE,languageId);

    }


    @Test
    public void testAddVote() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,VOTES_FOR_TABLE);

        voteDao.addVote(defaultSnippetId,defaultUserId,1);

        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,VOTES_FOR_TABLE));

    }


}
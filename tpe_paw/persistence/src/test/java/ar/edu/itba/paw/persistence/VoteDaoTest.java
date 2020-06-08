//package ar.edu.itba.paw.persistence;
//
//import ar.edu.itba.paw.interfaces.dao.SnippetDao;
//import ar.edu.itba.paw.interfaces.dao.TagDao;
//import ar.edu.itba.paw.interfaces.dao.UserDao;
//import ar.edu.itba.paw.interfaces.dao.VoteDao;
//import ar.edu.itba.paw.models.Language;
//import ar.edu.itba.paw.models.Snippet;
//import ar.edu.itba.paw.models.User;
//import ar.edu.itba.paw.models.Vote;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.jdbc.JdbcTestUtils;
//
//import javax.sql.DataSource;
//
//import java.util.Collection;
//import java.util.Optional;
//
//import static ar.edu.itba.paw.persistence.TestConstants.*;
//
//import static junit.framework.TestCase.*;
//
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
//public class VoteDaoTest {
//
//    @Autowired
//    private DataSource ds;
//
//    @InjectMocks
//    private VoteDao voteDao;
//
//    @Mock
//    private SnippetDao mockSnippetDao;
//    @Mock
//    private UserDao mockUserDao;
//
//    private JdbcTemplate jdbcTemplate;
//    private SimpleJdbcInsert jdbcInsertVotesFor;
//
//
//    private User defaultUser;
//    private long defaultSnippetId;
//
//
//    @Before
//    public void setUp() {
//        jdbcTemplate = new JdbcTemplate(ds);
////        voteDao = new VoteDaoImpl(ds);
//        MockitoAnnotations.initMocks(this); // Replaces @RunWith(MockitJUnitRunner.class)
//
//        jdbcInsertVotesFor = new SimpleJdbcInsert(ds).withTableName(VOTES_FOR_TABLE);
//        SimpleJdbcInsert jdbcInsertSnippet= new SimpleJdbcInsert(ds).withTableName(SNIPPETS_TABLE).usingGeneratedKeyColumns("id");
//        SimpleJdbcInsert jdbcInsertUser = new SimpleJdbcInsert(ds).withTableName(USERS_TABLE).usingGeneratedKeyColumns("id");
//        SimpleJdbcInsert jdbcInsertLanguage = new SimpleJdbcInsert(ds).withTableName(LANGUAGES_TABLE).usingGeneratedKeyColumns("id");
//
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,VOTES_FOR_TABLE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,USERS_TABLE);
//        defaultUser = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,DESCR,LOCALE_EN);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,LANGUAGES_TABLE);
//        long languageId = insertLanguageIntoDb(jdbcInsertLanguage,LANGUAGE);
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
//        defaultSnippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,languageId,0);
//
//    }
//
//
//    @Test
//    public void testAddVote() {
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,VOTES_FOR_TABLE);
//        //To mock a method of the same class.
//        VoteDao voteSpyDao = Mockito.spy(voteDao);
//        Mockito.doReturn(Optional.empty()).when(voteSpyDao).getVote(defaultUser.getId(),defaultSnippetId);
//
//        voteSpyDao.addVote(defaultUser.getId(),defaultSnippetId,true);
//
//        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,VOTES_FOR_TABLE));
//
//    }
//
//    //To test when the vote is already in the DB.
//    @Test
//    public void testAddVote2() {
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,VOTES_FOR_TABLE);
//        insertVotesForIntoDb(jdbcInsertVotesFor,defaultSnippetId,defaultUser.getId(),-1);
//        //To mock a method of the same class.
//        VoteDao voteSpyDao = Mockito.spy(voteDao);
//        Mockito.doReturn(Optional.of(new Vote(defaultUser,null,false))).when(voteSpyDao).getVote(defaultUser.getId(),defaultSnippetId);
//
//        voteSpyDao.addVote(defaultUser.getId(),defaultSnippetId,true);
//
//        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,VOTES_FOR_TABLE));
//
//    }
//
//    @Test
//    public void testGetVote(){
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,VOTES_FOR_TABLE);
//        insertVotesForIntoDb(jdbcInsertVotesFor,defaultSnippetId,defaultUser.getId(),1);
//        Mockito.when(mockSnippetDao.findSnippetById(defaultSnippetId)).thenReturn(Optional.of(new Snippet(defaultSnippetId,defaultUser,CODE,TITLE, DESCR,null,null,null,0,false)));
//        Mockito.when(mockUserDao.findUserById(defaultUser.getId())).thenReturn(Optional.of(defaultUser));
//
//        Optional<Vote> maybeVote = voteDao.getVote(defaultUser.getId(),defaultSnippetId);
//
//        assertTrue(maybeVote.isPresent());
////        assertEquals(defaultSnippetId, maybeVote.get().getSnippet().getId());
//        assertEquals(defaultUser.getId(),maybeVote.get().getUser().getId());
//        assertTrue(maybeVote.get().isPositive());
//    }
//
//    @Test
//    public void testGetVoteNotExists(){
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,VOTES_FOR_TABLE);
//        Mockito.when(mockSnippetDao.findSnippetById(defaultSnippetId)).thenReturn(Optional.of(new Snippet(defaultSnippetId,defaultUser,CODE,TITLE, DESCR,null,null,null,0,false)));
//        Mockito.when(mockUserDao.findUserById(defaultUser.getId())).thenReturn(Optional.of(defaultUser));
//
//        Optional<Vote> maybeVote = voteDao.getVote(defaultUser.getId(),defaultSnippetId);
//
//        assertFalse(maybeVote.isPresent());
//    }
//
//    @Test
//    public void testGetUserVotes(){
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,VOTES_FOR_TABLE);
//        insertVotesForIntoDb(jdbcInsertVotesFor,defaultSnippetId,defaultUser.getId(),1);
//        Mockito.when(mockSnippetDao.findSnippetById(defaultSnippetId)).thenReturn(Optional.of(new Snippet(defaultSnippetId,defaultUser,CODE,TITLE, DESCR,null,null,null,0,false)));
//        Mockito.when(mockUserDao.findUserById(defaultUser.getId())).thenReturn(Optional.of(defaultUser));
//
//        Collection<Vote> maybeVotes = voteDao.getUserVotes(defaultUser.getId());
//
//        assertEquals(1, maybeVotes.size());
//    }
//
//    @Test
//    public void testGetUserVotesEmpty(){
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,VOTES_FOR_TABLE);
//        insertVotesForIntoDb(jdbcInsertVotesFor,defaultSnippetId,defaultUser.getId(),1);
//        Mockito.when(mockSnippetDao.findSnippetById(defaultSnippetId)).thenReturn(Optional.of(new Snippet(defaultSnippetId,defaultUser,CODE,TITLE, DESCR,null,null,null,0,false)));
//        Mockito.when(mockUserDao.findUserById(defaultUser.getId())).thenReturn(Optional.of(defaultUser));
//
//        Collection<Vote> maybeVotes = voteDao.getUserVotes(defaultUser.getId()+10);
//
//        assertEquals(0, maybeVotes.size());
//    }
//
//    @Test
//    public void testWithdrawVote(){
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,VOTES_FOR_TABLE);
//        insertVotesForIntoDb(jdbcInsertVotesFor,defaultSnippetId,defaultUser.getId(),1);
//
//        voteDao.withdrawVote(defaultUser.getId(),defaultSnippetId);
//
//        assertEquals(0,JdbcTestUtils.countRowsInTable(jdbcTemplate,VOTES_FOR_TABLE));
//    }
//
//    @Test
//    public void testWithdrawVoteEmpty(){
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,VOTES_FOR_TABLE);
//        insertVotesForIntoDb(jdbcInsertVotesFor,defaultSnippetId,defaultUser.getId(),1);
//
//        voteDao.withdrawVote(defaultUser.getId(),defaultSnippetId+10);
//
//        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,VOTES_FOR_TABLE));
//    }
//
//    @Test
//    public void testGetVoteBalance(){
//        JdbcTestUtils.deleteFromTables(jdbcTemplate,VOTES_FOR_TABLE);
//        insertVotesForIntoDb(jdbcInsertVotesFor,defaultSnippetId,defaultUser.getId(),1);
//
//        Optional<Integer> maybeVoteBalance = voteDao.getVoteBalance(defaultSnippetId);
//
//        assertTrue(maybeVoteBalance.isPresent());
//        assertEquals(Integer.valueOf(1), maybeVoteBalance.get());
//    }
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//

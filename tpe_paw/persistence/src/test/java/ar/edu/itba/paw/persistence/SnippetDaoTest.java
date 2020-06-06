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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.persistence.TestHelper.*;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SnippetDaoTest {

    @Autowired
    private DataSource ds;

    @PersistenceContext
    private EntityManager em;

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
        JdbcTestUtils.deleteFromTables(jdbcTemplate, ROLES_USER_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, USERS_TABLE);
        altUser = insertUserIntoDb(jdbcInsertUser,USERNAME2,PASSWORD2,EMAIL2,DESCR,LOCALE_EN);
        defaultUser = insertUserIntoDb(jdbcInsertUser, USERNAME, PASSWORD, EMAIL, DESCR,LOCALE_EN);

        JdbcTestUtils.deleteFromTables(jdbcTemplate, LANGUAGES_TABLE);
        defaultLanguageId = insertLanguageIntoDb(jdbcInsertLanguage,LANGUAGE);

        JdbcTestUtils.deleteFromTables(jdbcTemplate,TAGS_TABLE);
        defaultTag = new Tag(insertTagIntoDb(jdbcInsertTag,TAG),TAG);
    }


    @Test
    // TODO check transactional
    @Transactional
    public void testCreate() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);

        final long snippetId = snippetDao.createSnippet(defaultUser.getId(), TITLE, DESCR, CODE, Timestamp.from(Instant.now()), defaultLanguageId, Collections.emptySet());
        em.flush();
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, SNIPPETS_TABLE));
    }


    @Test
    @Transactional
    public void testFindSnippetByCriteria(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE, defaultLanguageId,0);

        Optional<Snippet> maybeSnippet = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.TITLE,
                TITLE,
                SnippetDao.Locations.HOME,
                SnippetDao.Orders.ASC,
                null,
                null,
                1,
                6).stream().findFirst();

        assertTrue(maybeSnippet.isPresent());
        assertEquals(snippetId, (long)maybeSnippet.get().getId());
        assertEquals(defaultUser.getUsername(), maybeSnippet.get().getOwner().getUsername());
    }

    @Test
    public void testFindSnippetByCriteriaEmpty(){
        Optional<Snippet> maybeSnippet = snippetDao.findSnippetByCriteria(
                SnippetDao.Types.TITLE,
                TITLE,
                SnippetDao.Locations.HOME,
                SnippetDao.Orders.ASC,
                null,
                null,
                1,
                6).stream().findFirst();

        assertFalse(maybeSnippet.isPresent());
    }

    @Test
    public void testFindSnippetByDeepCriteria(){
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
                SnippetDao.Orders.ASC,
                SnippetDao.Types.ALL,
                true,
                1, 6).stream().findFirst();

        assertTrue(maybeSnippet.isPresent());
        assertEquals(TITLE,maybeSnippet.get().getTitle());

    }

    @Test
    public void testFindSnippetByDeepCriteriaTest(){
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
                SnippetDao.Orders.ASC,
                SnippetDao.Types.ALL,
                true,
                1, 6).stream().findFirst();

        assertFalse(maybeSnippet.isPresent());

    }

    @Test
    public void testGetAllSnippets(){
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
        long snippetId2 = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);

        Collection<Snippet> maybeColl = snippetDao.getAllSnippets(1,PAGE_SIZE);

        assertNotNull(maybeColl);
        assertEquals(2,maybeColl.size());
        List<Long> idList = maybeColl.stream().mapToLong(Snippet::getId).boxed().collect(Collectors.toList());
        assertTrue(idList.contains(snippetId));
        assertTrue(idList.contains(snippetId2));

    }

    @Test
    public void testGetAllSnippetsEmpty(){
        Collection<Snippet> maybeColl = snippetDao.getAllSnippets(1,PAGE_SIZE);

        assertNotNull(maybeColl);
        assertTrue(maybeColl.isEmpty());

    }

    @Test
    public void testGetAllFavoriteSnippets(){
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
        insertFavoriteIntoDb(jdbcInsertFavorite,snippetId,defaultUser.getId());

        Collection<Snippet> maybeCollection = snippetDao.getAllFavoriteSnippets(defaultUser.getId(),1,PAGE_SIZE);

        assertNotNull(maybeCollection);
        assertEquals(1,maybeCollection.size());
        Snippet s = (Snippet) maybeCollection.toArray()[0];
        assertEquals(snippetId,(long)s.getId());
    }

   @Test
    public void testGetAllFavoriteSnippetsEmpty(){
        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);

        Collection<Snippet> maybeCollection = snippetDao.getAllFavoriteSnippets(defaultUser.getId(),1,PAGE_SIZE);

        assertNotNull(maybeCollection);
        assertTrue(maybeCollection.isEmpty());
    }

    @Test
    public void testGetAllFollowingSnippets(){
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
        insertFollowingIntoDb(jdbcInsertFollowing,defaultTag.getId(),defaultUser.getId());
        insertSnippetTagIntoDb(jdbcInsertSnippetTags,snippetId,defaultTag.getId());

        Collection<Snippet> maybeCollection = snippetDao.getAllFollowingSnippets(defaultUser.getId(),1,PAGE_SIZE);

        assertNotNull(maybeCollection);
        assertEquals(1,maybeCollection.size());
        Snippet s = (Snippet) maybeCollection.toArray()[0];
        assertEquals(snippetId,(long)s.getId());
    }

    @Test
    public void testGetAllFollowingSnippetsEmpty(){
        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);

        Collection<Snippet> maybeCollection = snippetDao.getAllFollowingSnippets(defaultUser.getId(),1,PAGE_SIZE);

        assertNotNull(maybeCollection);
        assertEquals(0,maybeCollection.size());
    }

    @Test
    public void testGetAllUpvotedSnippets(){
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
        insertVotesForIntoDb(jdbcInsertVote,snippetId,defaultUser.getId(),1);


        Collection<Snippet> maybeCollection = snippetDao.getAllUpVotedSnippets(defaultUser.getId(),1,PAGE_SIZE);

        assertNotNull(maybeCollection);
        assertEquals(1,maybeCollection.size());
        Snippet s = (Snippet) maybeCollection.toArray()[0];
        assertEquals((long) snippetId,(long) s.getId());
    }

    @Test
    public void testGetAllUpvotedSnippetsEmpty(){
        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);

        Collection<Snippet> maybeCollection = snippetDao.getAllUpVotedSnippets(defaultUser.getId(),1,PAGE_SIZE);

        assertNotNull(maybeCollection);
        assertEquals(0,maybeCollection.size());
    }

    @Test
    public void testGetAllFlaggedSnippets(){
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,1);

        Collection<Snippet> maybeCollection = snippetDao.getAllFlaggedSnippets(1,PAGE_SIZE);

        assertNotNull(maybeCollection);
        assertEquals(1,maybeCollection.size());
        Snippet s = (Snippet) maybeCollection.toArray()[0];
        assertEquals(snippetId,(long)s.getId());
    }

    @Test
    public void testGetAllFlaggedSnippetsEmpty(){
        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);

        Collection<Snippet> maybeCollection = snippetDao.getAllFlaggedSnippets(1,PAGE_SIZE);

        assertNotNull(maybeCollection);
        assertEquals(0,maybeCollection.size());

    }

    @Test
    public void testFindAllSnippetsByOwner(){
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
        insertSnippetIntoDb(jdbcInsertSnippet,altUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);

        Collection<Snippet> maybeCollection = snippetDao.getAllSnippetsByOwner(defaultUser.getId(),1,PAGE_SIZE);

        assertNotNull(maybeCollection);
        assertEquals(1,maybeCollection.size());
        Snippet s = (Snippet) maybeCollection.toArray()[0];
        assertEquals(snippetId,(long)s.getId());
    }

    @Test
    public void testFindAllSnippetsByOwnerEmpty(){
      insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);

        Collection<Snippet> maybeCollection = snippetDao.getAllSnippetsByOwner(altUser.getId()+3,1,PAGE_SIZE);

        assertNotNull(maybeCollection);
        assertEquals(0,maybeCollection.size());

    }

    @Test
    public void testFindSnippetsWithLanguage(){
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);

        Collection<Snippet> maybeCollection = snippetDao.getSnippetsWithLanguage(defaultLanguageId,1,PAGE_SIZE);

        assertNotNull(maybeCollection);
        assertEquals(1,maybeCollection.size());
        Snippet s = (Snippet) maybeCollection.toArray()[0];
        assertEquals(snippetId,(long)s.getId());
    }

    @Test
    public void testFindSnippetsWithLanguageEmpty(){
        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);

        Collection<Snippet> maybeCollection = snippetDao.getSnippetsWithLanguage(defaultLanguageId+33,1,PAGE_SIZE);

        assertNotNull(maybeCollection);
        assertEquals(0,maybeCollection.size());
    }

    @Test
    @Transactional
    public void testFindById() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        Collection<Tag> tagList = Collections.singletonList(defaultTag);
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE, defaultLanguageId,0);
        Mockito.when(mockTagDao.findTagsForSnippet(snippetId)).thenReturn(tagList);

        Optional<Snippet> maybeSnippet = snippetDao.findSnippetById(snippetId);

        assertTrue(maybeSnippet.isPresent());
        assertEquals(defaultUser.getUsername(), maybeSnippet.get().getOwner().getUsername());
        assertEquals(TITLE, maybeSnippet.get().getTitle());
        assertEquals(DESCR, maybeSnippet.get().getDescription());
        // TODO fix tag insertion
//        assertTrue(maybeSnippet.get().getTags().contains(defaultTag));
    }

    @Test
    public void testFindByIdEmpty() {
        Mockito.when(mockTagDao.findTagsForSnippet(10)).thenReturn(null);

        Optional<Snippet> maybeSnippet = snippetDao.findSnippetById(10);

        assertFalse(maybeSnippet.isPresent());
    }

    @Test
    @Transactional
    public void testDeleteSnippetById(){
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
        insertSnippetIntoDb(jdbcInsertSnippet,altUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);

        boolean result = snippetDao.deleteSnippetById(snippetId);
        em.flush();
        assertTrue(result);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate,SNIPPETS_TABLE));
    }

    @Test
    public void testDeleteSnippetByIdEmpty(){
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);

        snippetDao.deleteSnippetById(snippetId+10);

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate,SNIPPETS_TABLE));

    }

    @Test
    public void testFindSnippetForTag() {
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(), TITLE, DESCR, CODE, defaultLanguageId,0);
        insertSnippetTagIntoDb(jdbcInsertSnippetTags,snippetId,defaultTag.getId());

        Optional<Snippet> snippet = snippetDao.findSnippetsForTag(defaultTag.getId(), 1, 10).stream().findFirst();

        assertTrue(snippet.isPresent());
        assertEquals(snippetId,(long)snippet.get().getId());
    }

    @Test
    public void testFindSnippetForTagEmpty() {
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(), TITLE, DESCR, CODE, defaultLanguageId,0);
        insertSnippetTagIntoDb(jdbcInsertSnippetTags,snippetId,defaultTag.getId());

        Optional<Snippet> snippet = snippetDao.findSnippetsForTag(defaultTag.getId()+10, 1, 10).stream().findFirst();

        assertFalse(snippet.isPresent());
    }

    @Test
    @Transactional
    public void testFlagSnippet(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, SNIPPETS_TABLE);
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(), TITLE, DESCR, CODE, defaultLanguageId,0);

        snippetDao.flagSnippet(snippetId);
        em.flush();
        int flagged = jdbcTemplate.queryForObject("SELECT flagged FROM complete_snippets WHERE id = ?",new Object[]{snippetId},Integer.class);
        assertEquals(1,flagged);
    }

    @Test
    public void testFlagSnippetEmpty(){
        snippetDao.flagSnippet(15);
    }

    @Test
    @Transactional
    public void testUnflagSnippet(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, SNIPPETS_TABLE);
        long snippetId = insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(), TITLE, DESCR, CODE, defaultLanguageId,1);

        snippetDao.unflagSnippet(snippetId);
        em.flush();
        int flagged = jdbcTemplate.queryForObject("SELECT flagged FROM complete_snippets WHERE id = ?",new Object[]{snippetId},Integer.class);
        assertEquals(0,flagged);
    }

    @Test
    public void testUnflagSnippetEmpty(){
        snippetDao.unflagSnippet(15);
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
    public void testGetNewSnippetForTagsCount(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE,DESCR,CODE,defaultLanguageId,0);
        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(),TITLE2,DESCR,CODE,defaultLanguageId,0);

        Instant d = Instant.now().minus(7, ChronoUnit.DAYS);
        int snippetCount = snippetDao.getNewSnippetsForTagsCount(d, new ArrayList<Tag>(), 1);

        assertEquals(0,snippetCount);
    }

    @Test
    public void testGetAllFollowingSnippetsCount(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,SNIPPETS_TABLE);
        insertSnippetIntoDb(jdbcInsertSnippet,defaultUser.getId(), TITLE, DESCR, CODE, defaultLanguageId,0);

        int count = snippetDao.getAllFavoriteSnippetsCount(defaultUser.getId());

        assertEquals(0,count);
    }

}

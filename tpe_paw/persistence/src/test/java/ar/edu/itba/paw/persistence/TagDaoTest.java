package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.TagDao;
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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

/*
 Tested Methods:
    void addTags(List<String> tags);
    Tag addTag(String name);
     Optional<Tag> findById(long id);
    Optional<Tag> findByName(String name);
     Collection<Tag> getAllTags();
 Not tested Methods:
    Collection<Tag> findTagsForSnippet(long snippetId);


    void addSnippetTag(long snippetOd, long tagId);

*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class TagDaoTest {

    @Autowired private DataSource ds;
    @Autowired private TagDao tagDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertTag;
    private SimpleJdbcInsert jdbcInsertSnippet;
    private SimpleJdbcInsert jdbcInsertUser;
    private SimpleJdbcInsert jdbcInsertLanguage;

    private final String TAGS_TABLE = "tags";
    private final String SNIPPETS_TABLE = "snippets";
    private final String USERS_TABLE = "users";
    private final String LANGUAGES_TABLE = "languages";

    private final String TAG = "tag1";
    private final String TAG2 = "tag2";
    private static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private Snippet defaultSnippet;

    private final static RowMapper<Tag> ROW_MAPPER = (rs, rowNum) -> new Tag(rs.getInt("id"), rs.getString("name"));

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertTag = new SimpleJdbcInsert(ds).withTableName(TAGS_TABLE).usingGeneratedKeyColumns("id");
        jdbcInsertSnippet = new SimpleJdbcInsert(ds).withTableName(SNIPPETS_TABLE).usingGeneratedKeyColumns("id");
        jdbcInsertLanguage = new SimpleJdbcInsert(ds).withTableName(LANGUAGES_TABLE).usingGeneratedKeyColumns("id");
        jdbcInsertUser = new SimpleJdbcInsert(ds).withTableName(USERS_TABLE).usingGeneratedKeyColumns("id");

        JdbcTestUtils.deleteFromTables(jdbcTemplate, USERS_TABLE);
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("username", "username");
            put("password", "password");
            put("email", "email@email.com");
            put("reputation", 0);
            put("date_joined", DATE.format(Calendar.getInstance().getTime().getTime()));
        }};
        long userId = jdbcInsertUser.executeAndReturnKey(map).longValue();

        JdbcTestUtils.deleteFromTables(jdbcTemplate, LANGUAGES_TABLE);
        Map<String, Object> languageDataMap = new HashMap<String, Object>() {{
            put("name", "language1");
        }};
        long languageId = jdbcInsertLanguage.executeAndReturnKey(languageDataMap).longValue();

        final Map<String, Object> snippetDataMap = new HashMap<String,Object>(){{
            put("user_id", userId);
            put("title", "title");
            put("description","description");
            put("code","code code code");
            put("date_created", DATE.format(Calendar.getInstance().getTime().getTime()));
            put("language_id", languageId);
        }};
        final long snippetId = jdbcInsertSnippet.executeAndReturnKey(snippetDataMap).longValue();
    }

    private long insertTagIntoDatabase(String name){
        final Map<String, Object> map = new HashMap<String,Object>(){{
            put("name", name);
        }};
        return jdbcInsertTag.executeAndReturnKey(map).longValue();
    }
    @Test
    public void testAddTag(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,TAGS_TABLE);

        Tag maybeTag = tagDao.addTag(TAG);

        assertNotNull(maybeTag);
        assertEquals(TAG.toLowerCase(),maybeTag.getName());
        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,TAGS_TABLE));
    }

    @Test
    public void testAddTagS(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,TAGS_TABLE);
        List<String> stringList = Arrays.asList(TAG,TAG2,null);

        tagDao.addTags(stringList);

        assertEquals(2,JdbcTestUtils.countRowsInTable(jdbcTemplate,TAGS_TABLE));
    }

    @Test
    public void testFindById(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,TAGS_TABLE);
        long tagId = insertTagIntoDatabase(TAG);

        Optional<Tag> maybeTag = tagDao.findById(tagId);

        assertTrue(maybeTag.isPresent());
        assertEquals(tagId,maybeTag.get().getId());
        assertEquals(TAG,maybeTag.get().getName());
    }

    @Test
    public void testFindByName(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,TAGS_TABLE);
        long tagId = insertTagIntoDatabase(TAG);

        Optional<Tag> maybeTag = tagDao.findByName(TAG);

        assertTrue(maybeTag.isPresent());
        assertEquals(tagId,maybeTag.get().getId());
        assertEquals(TAG,maybeTag.get().getName());
    }

    @Test
    public void testGetAllTags(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,TAGS_TABLE);
        long tagId1 = insertTagIntoDatabase(TAG);
        long tagId2 = insertTagIntoDatabase(TAG2);

        Collection<Tag> maybeTags = tagDao.getAllTags();

        assertEquals(2,maybeTags.size());
        List<String> maybeList = new ArrayList<>();
        for(Tag t:maybeTags){
            maybeList.add(t.getName());
        }
        assertTrue(maybeList.contains(TAG));
        assertTrue(maybeList.contains(TAG));
    }

    @Test
    public void testAddSnippetTag(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,TAGS_TABLE);
        long tagId = insertTagIntoDatabase(TAG);
    }


}
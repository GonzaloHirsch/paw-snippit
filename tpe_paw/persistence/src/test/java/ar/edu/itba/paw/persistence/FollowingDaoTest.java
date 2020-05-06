package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.FavoriteDao;
import ar.edu.itba.paw.interfaces.dao.FollowingDao;
import ar.edu.itba.paw.models.Favorite;
import ar.edu.itba.paw.models.Tag;
import ar.edu.itba.paw.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

import java.util.Collection;
import java.util.Optional;

import static ar.edu.itba.paw.persistence.TestHelper.*;

import static junit.framework.TestCase.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class FollowingDaoTest {

    @Autowired
    private DataSource ds;

    private FollowingDao followingDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertFollowing;

    private long defaultTagId;
    private User defaultUser;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(ds);
        followingDao = new FollowingDaoImpl(ds);

        jdbcInsertFollowing = new SimpleJdbcInsert(ds).withTableName(FOLLOWS_TABLE);

        SimpleJdbcInsert jdbcInsertUser = new SimpleJdbcInsert(ds).withTableName(USERS_TABLE).usingGeneratedKeyColumns("id");
        SimpleJdbcInsert jdbcInsertTag = new SimpleJdbcInsert(ds).withTableName(TAGS_TABLE).usingGeneratedKeyColumns("id");

        JdbcTestUtils.deleteFromTables(jdbcTemplate,FOLLOWS_TABLE);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,TAGS_TABLE);
        defaultTagId = insertTagIntoDb(jdbcInsertTag,TAG);
        JdbcTestUtils.deleteFromTables(jdbcTemplate,USERS_TABLE);
        defaultUser = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,DESCR);

    }

    @Test
    public void followTag(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,FOLLOWS_TABLE);

        followingDao.followTag(defaultUser.getId(),defaultTagId);

        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,FOLLOWS_TABLE));
    }

    @Test
    public void testGetFollowedTagsForUser(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,FOLLOWS_TABLE);
        insertFollowingIntoDb(jdbcInsertFollowing,defaultTagId,defaultUser.getId());

        Collection<Tag> maybeTags = followingDao.getFollowedTagsForUser(defaultUser.getId());

        assertNotNull(maybeTags);
        assertEquals(1,maybeTags.size());

    }

    @Test
    public void testGetFollowedTagsForUser2(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,FOLLOWS_TABLE);

        Collection<Tag> maybeTags = followingDao.getFollowedTagsForUser(6);

        assertNotNull(maybeTags);
        assertEquals(0,maybeTags.size());

    }

    @Test
    public void testUnfollowTag(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,FOLLOWS_TABLE);
        insertFollowingIntoDb(jdbcInsertFollowing,defaultTagId,defaultUser.getId());

        followingDao.unfollowTag(defaultUser.getId(),defaultTagId);

        assertEquals(0,JdbcTestUtils.countRowsInTable(jdbcTemplate,FOLLOWS_TABLE));

    }




}

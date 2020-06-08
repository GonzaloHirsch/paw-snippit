package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.RoleDao;
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
import java.util.Collection;

import static ar.edu.itba.paw.persistence.TestConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class RoleDaoTest {
    @Autowired
    private DataSource ds;

    @Autowired
    private RoleDao roleDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertUserRole;

    private long adminRoleId;
    private long userRoleId;
    private User defaultUser;
/*
    @Before
    public void setup(){
        jdbcTemplate = new JdbcTemplate(ds);

        SimpleJdbcInsert jdbcInsertRole = new SimpleJdbcInsert(ds).withTableName(ROLES_TABLE).usingGeneratedKeyColumns("id");
        jdbcInsertUserRole = new SimpleJdbcInsert(ds).withTableName(ROLES_USER_TABLE);

        JdbcTestUtils.deleteFromTables(jdbcTemplate,USERS_TABLE);
        SimpleJdbcInsert jdbcInsertUser = new SimpleJdbcInsert(ds).withTableName(USERS_TABLE).usingGeneratedKeyColumns("id");
        defaultUser = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,DESCR,LOCALE_EN);

        JdbcTestUtils.deleteFromTables(jdbcTemplate,ROLES_TABLE);
        adminRoleId = insertRoleIntoDb(jdbcInsertRole,ADMIN_ROLE);
        userRoleId = insertRoleIntoDb(jdbcInsertRole,USER_ROLE);
    }*/

    /*@Transactional
    @Test
    public void testAssignUserRole(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,ROLES_USER_TABLE);

        roleDao.assignUserRole(defaultUser.getId());

        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,ROLES_USER_TABLE));
        long maybeRoleId = jdbcTemplate.queryForObject("SELECT role_id FROM user_roles",Long.class);
        assertEquals(adminRole.getId(),maybeRoleId);
    }*/

    /*@Transactional
    @Test
    public void testAssignAdminRole(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,ROLES_USER_TABLE);

        roleDao.assignAdminRole(defaultUser.getId());

        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,ROLES_USER_TABLE));
        long maybeRoleId = jdbcTemplate.queryForObject("SELECT role_id FROM user_roles",Long.class);
        assertEquals(userRole,maybeRoleId);
    }*/

    /*@Transactional
    @Test
    public void testGetUserRoles(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,ROLES_USER_TABLE);
        insertUserRoleIntoDb(jdbcInsertUserRole,adminRoleId,defaultUser.getId());
        insertUserRoleIntoDb(jdbcInsertUserRole,userRoleId,defaultUser.getId());

        Collection<String> maybeRoles = roleDao.getUserRoles(defaultUser.getId());

        assertEquals(2,maybeRoles.size());
        assertTrue(maybeRoles.contains(ADMIN_ROLE));
        assertTrue(maybeRoles.contains(USER_ROLE));
    }*/
    /*
    @Test
    public void testGetUserRolesEmpty(){
        Collection<String> maybeRoles = roleDao.getUserRoles(defaultUser.getId());

        assertEquals(0,maybeRoles.size());
    }*/
}

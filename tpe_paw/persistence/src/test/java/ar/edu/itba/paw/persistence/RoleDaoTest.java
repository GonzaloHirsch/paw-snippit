package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.RoleDao;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class RoleDaoTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private RoleDao roleDao;

    private Role adminRole;
    private Role userRole;
    private User defaultUser;

    @Before
    public void setup(){
        adminRole = TestMethods.insertRole(em, TestConstants.ADMIN_ROLE);
        userRole = TestMethods.insertRole(em, TestConstants.USER_ROLE);
        defaultUser = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL, TestConstants.USER_DATE, TestConstants.LOCALE_EN, TestConstants.USER_VERIFIED);
    }

    //TODO no anda pq el get siempre devuelve null
    @Test
    public void assignUserRoleTest(){
        Assert.assertTrue(roleDao.assignUserRole(defaultUser.getId()));
    }

    @Test
    public void assignUserRoleToInvalidUserTest(){
        Assert.assertFalse(roleDao.assignUserRole(TestConstants.USER_INVALID_ID));
    }

    //TODO no anda pq el get siempre devuelve null
    @Test
    public void assignAdminRoleTest(){
        Assert.assertTrue(roleDao.assignAdminRole(defaultUser.getId()));
    }

    @Test
    public void assignAdminRoleToInvalidUserTest(){
        Assert.assertFalse(roleDao.assignAdminRole(TestConstants.USER_INVALID_ID));
    }

    @Test
    public void getAllRolesTest() {
        Collection<Role> roles = roleDao.getAllRoles();
        Assert.assertNotNull(roles);
        Assert.assertEquals(2, roles.size());
        Assert.assertTrue(roles.contains(userRole));
        Assert.assertTrue(roles.contains(adminRole));
    }

    @Test
    public void getUserRolesEmptyTest() {
        Collection<String > roles = roleDao.getUserRoles(defaultUser.getId());
        Assert.assertNotNull(roles);
        Assert.assertTrue(roles.isEmpty());
    }

    @Test
    public void getUserRolesAllRolesTest() {
        TestMethods.setUserRoles(em, defaultUser, Arrays.asList(userRole, adminRole));

        Collection<String > roles = roleDao.getUserRoles(defaultUser.getId());
        Assert.assertNotNull(roles);
        Assert.assertEquals(2, roles.size());
        Assert.assertTrue(roles.contains(TestConstants.ADMIN_ROLE));
        Assert.assertTrue(roles.contains(TestConstants.USER_ROLE));
    }

    @Test
    public void getUserRolesOnlyUserTest() {
        TestMethods.setUserRoles(em, defaultUser, Collections.singletonList(userRole));

        Collection<String > roles = roleDao.getUserRoles(defaultUser.getId());
        Assert.assertNotNull(roles);
        Assert.assertEquals(1, roles.size());
        Assert.assertTrue(roles.contains(TestConstants.USER_ROLE));
    }

    @Test
    public void getUserRolesOnlyAdminTest() {
        TestMethods.setUserRoles(em, defaultUser, Arrays.asList(adminRole));

        Collection<String > roles = roleDao.getUserRoles(defaultUser.getId());
        Assert.assertNotNull(roles);
        Assert.assertEquals(1, roles.size());
        Assert.assertTrue(roles.contains(TestConstants.ADMIN_ROLE));
    }
}

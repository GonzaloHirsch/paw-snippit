package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.UserDao;
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
import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @PersistenceContext
    private EntityManager em;

    @Before
    public void setUp() {

    }

    @Test
    public void testCreateUser() {
        int beforeAddCount = TestMethods.countRows(em, TestConstants.USER_TABLE);
        final User user = userDao.createUser(TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL, Instant.now(), TestConstants.LOCALE_EN);

        Assert.assertTrue(user.getId() > 0);
        Assert.assertEquals(TestConstants.USER_USERNAME, user.getUsername());
        Assert.assertEquals(TestConstants.USER_PASSWORD, user.getPassword());
        Assert.assertEquals(TestConstants.USER_EMAIL, user.getEmail());
        Assert.assertEquals(TestConstants.LOCALE_EN, user.getLocale());
        Assert.assertEquals(0, beforeAddCount);
        Assert.assertEquals(1, TestMethods.countRows(em, TestConstants.USER_TABLE));
    }

    @Test
    public void testFindUserById(){
        User expectedUser = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,false);

        Optional<User> maybeUser = userDao.findUserById(expectedUser.getId());

        Assert.assertTrue(maybeUser.isPresent());
        Assert.assertEquals(expectedUser.getId(), maybeUser.get().getId());
        Assert.assertEquals(expectedUser.getUsername(), maybeUser.get().getUsername());
        Assert.assertEquals(expectedUser.getPassword(), maybeUser.get().getPassword());
        Assert.assertEquals(expectedUser.getEmail(), maybeUser.get().getEmail());
    }

    @Test
    public void testFindUserByIdEmpty(){
        User expectedUser = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,false);

        Optional<User> maybeUser = userDao.findUserById(expectedUser.getId()+10);

        Assert.assertFalse(maybeUser.isPresent());
    }

    @Test
    public void testFindUserByUsername(){
        User expectedUser = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,false);

        Optional<User> maybeUser = userDao.findUserByUsername(TestConstants.USER_USERNAME);

        Assert.assertTrue(maybeUser.isPresent());
        Assert.assertEquals(expectedUser.getId(), maybeUser.get().getId());
        Assert.assertEquals(expectedUser.getUsername(), maybeUser.get().getUsername());
        Assert.assertEquals(expectedUser.getPassword(), maybeUser.get().getPassword());
        Assert.assertEquals(expectedUser.getEmail(), maybeUser.get().getEmail());
    }
    
    @Test
    public void testFindUserByUsernameEmpty(){
        TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,false);

        Optional<User> maybeUser = userDao.findUserByUsername("NOT AN USERNAME");

        Assert.assertFalse(maybeUser.isPresent());
    }

    @Test
    public void testFindUserByEmail(){
        User expectedUser = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,false);

        Optional<User> maybeUser = userDao.findUserByEmail(TestConstants.USER_EMAIL);

        Assert.assertTrue(maybeUser.isPresent());
        Assert.assertEquals(expectedUser.getId(), maybeUser.get().getId());
        Assert.assertEquals(expectedUser.getUsername(), maybeUser.get().getUsername());
        Assert.assertEquals(expectedUser.getPassword(), maybeUser.get().getPassword());
        Assert.assertEquals(expectedUser.getEmail(), maybeUser.get().getEmail());
    }
    
    @Test
    public void testFindUserByEmailEmpty(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,false);

        Optional<User> maybeUser = userDao.findUserByEmail("NOT AN EMAIL");

        Assert.assertFalse(maybeUser.isPresent());
    }

    @Test
    public void testGetAllVerifiedUsers() {
        Map<String, User> users = TestMethods.userCreation(em);
        Collection<User> verifiedUsers = this.userDao.getAllVerifiedUsers();

        Assert.assertNotNull(verifiedUsers);
        Assert.assertEquals(2, verifiedUsers.size());
        Assert.assertFalse(verifiedUsers.contains(users.get(TestConstants.USER_USERNAME2)));
    }

    @Test
    public void testGetAllVerifiedUsersEmpty() {
        Collection<User> verifiedUsers = this.userDao.getAllVerifiedUsers();

        Assert.assertNotNull(verifiedUsers);
        Assert.assertTrue(verifiedUsers.isEmpty());
    }
    
    @Test
    public void testUpdateDescription(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,false);
        String newDescription = "New Description";

        userDao.changeDescription(user.getId(),newDescription);

        Assert.assertEquals(newDescription, user.getDescription());
        Assert.assertEquals(user.getId(),user.getId());
    }

    @Test
    public void testChangePassword(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,false);

        String newPassword = "newpassword";

        userDao.changePassword(user.getEmail(),newPassword);

        Assert.assertEquals(newPassword, user.getPassword());
        Assert.assertEquals(user.getId(),user.getId());
    }

    @Test
    public void testChangeDescription(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,false);
        String newDescription = "new description";

        userDao.changeDescription(user.getId(),newDescription);

        Assert.assertEquals(newDescription, user.getDescription());
        Assert.assertEquals(user.getId(), user.getId());
    }

    @Test
    public void testChangeReputation(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,false);

        userDao.changeReputation(user.getId(),10);

        Assert.assertEquals(10, user.getReputation());
    }

    
    @Test
    public void testChangeReputationEmpty(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,false);

        userDao.changeReputation(user.getId()+10,10);

        Assert.assertEquals(0,user.getReputation());
    }

    
    @Test
    public void getAllUsersTests(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,false);

        Collection<User> collection = userDao.getAllUsers();

        Assert.assertNotNull(collection);
        Assert.assertEquals(1,collection.size());
        Assert.assertEquals(user.getId(),collection.stream().findFirst().get().getId());
    }

    
    @Test
    public void getAllUsersTestsEmpty(){
        Collection<User> maybeCollection = userDao.getAllUsers();

        Assert.assertNotNull(maybeCollection);
        Assert.assertEquals(0,maybeCollection.size());
    }

    
    @Test
    public void testUpdateLocaleRegion(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,false);

        userDao.updateLocale(user.getId(), TestConstants.LOCALE_ES);

        Assert.assertEquals(TestConstants.LOCALE_ES.getCountry(),user.getRegion());
    }

    @Test
    
    public void testUpdateLocaleLanguage(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,false);

        userDao.updateLocale(user.getId(),TestConstants.LOCALE_ES);

        Assert.assertEquals(TestConstants.LOCALE_ES.getCountry(),user.getRegion());
    }


    
    @Test
    public void testUpdateLocaleRegionEmpty(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,false);

        userDao.updateLocale(user.getId()+10, TestConstants.LOCALE_ES);

        Assert.assertEquals(TestConstants.LOCALE_EN.getCountry(),user.getRegion());
    }

    
    @Test
    public void testUpdateLocaleLanguageEmpty(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,false);

        userDao.updateLocale(user.getId()+10, TestConstants.LOCALE_ES);

        Assert.assertEquals(TestConstants.LOCALE_EN.getCountry().toUpperCase(),user.getLang().toUpperCase());
    }

    
    @Test
    public void testGetLocaleLanguage(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,false);

        String loc = userDao.getLocaleLanguage(user.getId());

        Assert.assertEquals(TestConstants.LOCALE_EN.getLanguage().toUpperCase(),loc.toUpperCase());
    }

    
    @Test
    public void testGetLocaleLanguageEmpty(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,false);

        String loc = userDao.getLocaleLanguage(user.getId()+10);
        Assert.assertEquals(loc, "");
    }

    
    @Test
    public void testGetLocaleRegion(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,false);

        String loc = userDao.getLocaleRegion(user.getId());

        Assert.assertEquals(TestConstants.LOCALE_EN.getCountry(),loc);
    }

    
    @Test
    public void testUserEmailIsVerified(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,false);
        Assert.assertFalse(userDao.userEmailIsVerified(user.getId()));
    }

    @Test
    public void testUserEmailIsVerifiedTrue(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,TestConstants.USER_VERIFIED);
        Assert.assertTrue(userDao.userEmailIsVerified(user.getId()));
    }

    
    @Test
    public void testUserEmailIsVerifiedEmpty(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,false);
        Assert.assertFalse(userDao.userEmailIsVerified(TestConstants.USER_INVALID_ID));
    }

    
    @Test
    public void testVerifyUserEmail(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,false);

        userDao.verifyUserEmail(user.getId());

        Assert.assertTrue(user.isVerified());
    }

    
    @Test
    public void testVerifyUserEmailEmpty(){
        User user = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL,Instant.now(), TestConstants.LOCALE_EN,false);

        userDao.verifyUserEmail(user.getId()+10);

        Assert.assertFalse(user.isVerified());
    }

}

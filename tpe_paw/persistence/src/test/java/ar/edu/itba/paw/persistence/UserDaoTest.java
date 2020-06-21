package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.time.Instant;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

import static ar.edu.itba.paw.persistence.TestConstants.*;
import static junit.framework.TestCase.*;

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
        final User user = userDao.createUser(USER_USERNAME, USER_PASSWORD, USER_EMAIL, Instant.now(),new Locale("en"));

        assertTrue(user.getId()>0);
    }

    @Test
    public void testFindUserById(){
        User expectedUser = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);

        Optional<User> maybeUser = userDao.findUserById(expectedUser.getId());

        assertTrue(maybeUser.isPresent());
        assertEquals(expectedUser.getId(), maybeUser.get().getId());
        assertEquals(expectedUser.getUsername(), maybeUser.get().getUsername());
        assertEquals(expectedUser.getPassword(), maybeUser.get().getPassword());
        assertEquals(expectedUser.getEmail(), maybeUser.get().getEmail());
    }

    @Test
    public void testFindUserByIdEmpty(){
        User expectedUser = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);

        Optional<User> maybeUser = userDao.findUserById(expectedUser.getId()+10);

        assertFalse(maybeUser.isPresent());
    }

    
    @Test
    public void testFindUserByUsername(){
        User expectedUser = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);

        Optional<User> maybeUser = userDao.findUserByUsername(USER_USERNAME);

        assertTrue(maybeUser.isPresent());
        assertEquals(expectedUser.getId(), maybeUser.get().getId());
        assertEquals(expectedUser.getUsername(), maybeUser.get().getUsername());
        assertEquals(expectedUser.getPassword(), maybeUser.get().getPassword());
        assertEquals(expectedUser.getEmail(), maybeUser.get().getEmail());
    }

    
    @Test
    public void testFindUserByUsernameEmpty(){
        User expectedUser = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);

        Optional<User> maybeUser = userDao.findUserByUsername("NOT AN USERNAME");

        assertFalse(maybeUser.isPresent());
    }

    
    @Test
    public void testFindUserByEmail(){
        User expectedUser = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);

        Optional<User> maybeUser = userDao.findUserByEmail(USER_EMAIL);

        assertTrue(maybeUser.isPresent());
        assertEquals(expectedUser.getId(), maybeUser.get().getId());
        assertEquals(expectedUser.getUsername(), maybeUser.get().getUsername());
        assertEquals(expectedUser.getPassword(), maybeUser.get().getPassword());
        assertEquals(expectedUser.getEmail(), maybeUser.get().getEmail());
    }

    
    @Test
    public void testFindUserByEmailEmpty(){
        User user = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);

        Optional<User> maybeUser = userDao.findUserByEmail("NOT AN EMAIL");

        assertFalse(maybeUser.isPresent());
    }

    
    @Test
    public void testFindUpdateDescription(){
        User user = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);
        String newDescription = "New Description";

        userDao.changeDescription(user.getId(),newDescription);

        assertEquals(newDescription, user.getDescription());
        assertEquals(user.getId(),user.getId());
    }

    
    @Test
    public void testFindUpdateDescriptionEmpty(){
        String newDescription = "New Description";

        userDao.changeDescription(334548,newDescription);
    }

    
    @Test
    public void testChangePassword(){
        User user = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);

        String newPassword = "newpassword";

        userDao.changePassword(user.getEmail(),newPassword);

        assertEquals(newPassword, user.getPassword());
        assertEquals(user.getId(),user.getId());
    }

    
    @Test
    public void testChangePasswordEmpty(){
        String newPassword = "newpassword";

        userDao.changePassword("NOT AN EMAIL",newPassword);
    }

    
    @Test
    public void testChangeDescription(){
        User user = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);
        String newDescription = "new description";

        userDao.changeDescription(user.getId(),newDescription);

        assertEquals(newDescription, user.getDescription());
        assertEquals(user.getId(), user.getId());
    }

    
    @Test
    public void testChangeDescriptionEmpty(){
        User user = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);
        String newDescription = "new description";

        userDao.changeDescription(user.getId()+10,newDescription);
    }

    
    @Test
    public void testChangeReputation(){
        User user = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);

        userDao.changeReputation(user.getId(),10);
        ;

        int rep = user.getReputation();
        assertEquals(10,rep);
    }

    
    @Test
    public void testChangeReputationEmpty(){
        User user = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);

        userDao.changeReputation(user.getId()+10,10);

        int rep = user.getReputation();
        assertEquals(0,rep);
    }

    
    @Test
    public void getAllUsersTests(){
        User user = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);

        Collection<User> maybeCollection = userDao.getAllUsers();

        assertNotNull(maybeCollection);
        assertEquals(1,maybeCollection.size());
        assertEquals(user.getId(),maybeCollection.stream().findFirst().get().getId());
    }

    
    @Test
    public void getAllUsersTestsEmpty(){
        Collection<User> maybeCollection = userDao.getAllUsers();

        assertNotNull(maybeCollection);
        assertEquals(0,maybeCollection.size());
    }

    
    @Test
    public void testUpdateLocaleRegion(){
        User user = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);

        userDao.updateLocale(user.getId(),LOCALE_ES);

        assertEquals(LOCALE_ES.getCountry(),user.getRegion());
    }

    @Test
    
    public void testUpdateLocaleLanguage(){
        User user = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);

        userDao.updateLocale(user.getId(),LOCALE_ES);

        assertEquals(LOCALE_ES.getCountry(),user.getRegion());
    }


    
    @Test
    public void testUpdateLocaleRegionEmpty(){
        User user = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);

        userDao.updateLocale(user.getId()+10,LOCALE_ES);

        assertEquals(LOCALE_EN.getCountry(),user.getRegion());
    }

    
    @Test
    public void testUpdateLocaleLanguageEmpty(){
        User user = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);

        userDao.updateLocale(user.getId()+10,LOCALE_ES);

        assertEquals(LOCALE_EN.getCountry().toUpperCase(),user.getLang().toUpperCase());
    }

    
    @Test
    public void testGetLocaleLanguage(){
        User user = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);

        String loc = userDao.getLocaleLanguage(user.getId());

        assertEquals(LOCALE_EN.getLanguage().toUpperCase(),loc.toUpperCase());
    }

    
    @Test
    public void testGetLocaleLanguageEmpty(){
        User user = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);

        String loc = userDao.getLocaleLanguage(user.getId()+10);
        assertEquals(loc, "");
    }

    
    @Test
    public void testGetLocaleRegion(){
        User user = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);

        String loc = userDao.getLocaleRegion(user.getId());

        assertEquals(LOCALE_EN.getCountry(),loc);
    }

    
    @Test
    public void testGetLocaleRegionEmpty(){
        User user = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);

        String loc = userDao.getLocaleRegion(user.getId()+10);
        assertEquals(loc, "");
    }

    
    @Test
    public void testUserEmailIsVerified(){
        User user = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);

        boolean result = userDao.userEmailIsVerified(user.getId());

        assertFalse(result);
    }

    
    @Test
    public void testUserEmailIsVerifiedEmpty(){
        User user = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);

        boolean result = userDao.userEmailIsVerified(user.getId()+10);
        assertFalse(result);
    }

    
    @Test
    public void testVerifyUserEmail(){
        User user = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);

        userDao.verifyUserEmail(user.getId());

        assertTrue(user.isVerified());
    }

    
    @Test
    public void testVerifyUserEmailEmpty(){
        User user = TestMethods.insertUser(em, USER_USERNAME, USER_PASSWORD, USER_EMAIL,Instant.now(),LOCALE_EN,false);

        userDao.verifyUserEmail(user.getId()+10);

        assertFalse(user.isVerified());
    }

}

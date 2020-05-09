package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

import static ar.edu.itba.paw.persistence.TestHelper.*;
import static junit.framework.TestCase.*;

/*
Tested Methods:
    long createUser(String username, String password, String email, String description, int reputation, String dateJoined);
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserById(final long id);
    Optional<User> findUserByEmail(String email);
    void updateDescription(String username, String newDescription);
    void changePassword(String email, String password);
    void changeDescription(final long userId, final String description);

Not tested Methods:
    void changeProfilePhoto(final long userId, final byte[] photo);

*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class UserDaoTest {

    @Autowired private DataSource ds;
    @Autowired @InjectMocks private UserDao userDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertUser;



    private final static RowMapper<User> ROW_MAPPER = (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("email"),
            rs.getString("description"),
            rs.getInt("reputation"),
            rs.getString("date_joined"),
            rs.getBytes("icon"),
            new Locale(rs.getString("lang"), rs.getString("region")),
            rs.getInt("verified") == 1
    );


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Replaces @RunWith(MockitJUnitRunner.class)
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertUser = new SimpleJdbcInsert(ds).withTableName(USERS_TABLE).usingGeneratedKeyColumns("id");

        JdbcTestUtils.deleteFromTables(jdbcTemplate, USERS_TABLE);
    }


    @Test
    public void testCreateUser() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,USERS_TABLE);

        final long userId = userDao.createUser(USERNAME, PASSWORD, EMAIL, "", 0, DATE.format(Calendar.getInstance().getTime().getTime()),new Locale("en"));

        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
    }

    @Test
    public void testFindUserById(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,USERS_TABLE);
        User expectedUser = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,"",LOCALE_EN);

        Optional<User> maybeUser = userDao.findUserById(expectedUser.getId());

        assertTrue(maybeUser.isPresent());
        assertEquals(expectedUser.getId(), maybeUser.get().getId());
        assertEquals(expectedUser.getUsername(), maybeUser.get().getUsername());
        assertEquals(expectedUser.getPassword(), maybeUser.get().getPassword());
        assertEquals(expectedUser.getEmail(), maybeUser.get().getEmail());
    }

    @Test
    public void testFindUserByIdEmpty(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,USERS_TABLE);
        User expectedUser = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,"",LOCALE_EN);

        Optional<User> maybeUser = userDao.findUserById(expectedUser.getId()+10);

        assertFalse(maybeUser.isPresent());
    }

    @Test
    public void testFindUserByUsername(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,USERS_TABLE);
        User expectedUser = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,"",LOCALE_EN);
        insertUserIntoDb(jdbcInsertUser,USERNAME2,PASSWORD2,EMAIL2,"",LOCALE_EN);

        Optional<User> maybeUser = userDao.findUserByUsername(USERNAME);

        assertTrue(maybeUser.isPresent());
        assertEquals(expectedUser.getId(), maybeUser.get().getId());
        assertEquals(expectedUser.getUsername(), maybeUser.get().getUsername());
        assertEquals(expectedUser.getPassword(), maybeUser.get().getPassword());
        assertEquals(expectedUser.getEmail(), maybeUser.get().getEmail());
    }

    @Test
    public void testFindUserByUsernameEmpty(){
        insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,"",LOCALE_EN);

        Optional<User> maybeUser = userDao.findUserByUsername("NOT AN USERNAME");

        assertFalse(maybeUser.isPresent());
    }

    @Test
    public void testFindUserByEmail(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,USERS_TABLE);
        User expectedUser = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,"",LOCALE_EN);
        insertUserIntoDb(jdbcInsertUser,USERNAME2,PASSWORD2,EMAIL2,"",LOCALE_EN);

        Optional<User> maybeUser = userDao.findUserByEmail(EMAIL);

        assertTrue(maybeUser.isPresent());
        assertEquals(expectedUser.getId(), maybeUser.get().getId());
        assertEquals(expectedUser.getUsername(), maybeUser.get().getUsername());
        assertEquals(expectedUser.getPassword(), maybeUser.get().getPassword());
        assertEquals(expectedUser.getEmail(), maybeUser.get().getEmail());
    }

    @Test
    public void testFindUserByEmailEmpty(){
        insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,"",LOCALE_EN);

        Optional<User> maybeUser = userDao.findUserByEmail("NOT AN EMAIL");

        assertFalse(maybeUser.isPresent());
    }

    @Test
    public void testFindUpdateDescription(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,USERS_TABLE);
        User user = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,"",LOCALE_EN);
        String newDescription = "New Description";

        userDao.updateDescription(user.getUsername(),newDescription);

        Optional<User> maybeUser = jdbcTemplate.query("SELECT * FROM users WHERE id = ?", ROW_MAPPER, user.getId()).stream().findFirst();
        assertTrue(maybeUser.isPresent());
        assertEquals(newDescription, maybeUser.get().getDescription());
        assertEquals(user.getId(),maybeUser.get().getId());
    }

    @Test
    public void testFindUpdateDescriptionEmpty(){
        String newDescription = "New Description";

        userDao.updateDescription("NOT AN USERNAME",newDescription);
    }

    @Test
    public void testChangePassword(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,USERS_TABLE);
        User user = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,"",LOCALE_EN);
        String newPassword = "newpassword";

        userDao.changePassword(user.getEmail(),newPassword);

        Optional<User> maybeUser = jdbcTemplate.query("SELECT * FROM users WHERE id = ?", ROW_MAPPER, user.getId()).stream().findFirst();
        assertTrue(maybeUser.isPresent());
        assertEquals(newPassword, maybeUser.get().getPassword());
        assertEquals(user.getId(),maybeUser.get().getId());
    }

    @Test
    public void testChangePasswordEmpty(){
        String newPassword = "newpassword";

        userDao.changePassword("NOT AN EMAIL",newPassword);
    }

    @Test
    public void testChangeDescription(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,USERS_TABLE);
        User user = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,"",LOCALE_EN);
        String newDescription = "new description";

        userDao.changeDescription(user.getId(),newDescription);

        // Manually obtaining the snippet. The idea is not to use other methods of the dao, because its unit testing
        Optional<User> maybeUser = jdbcTemplate.query("SELECT * FROM users WHERE id = ?", ROW_MAPPER, user.getId()).stream().findFirst();
        assertTrue(maybeUser.isPresent());
        assertEquals(newDescription, maybeUser.get().getDescription());
        assertEquals(user.getId(),maybeUser.get().getId());
    }

    @Test
    public void testChangeDescriptionEmpty(){
        User user = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,"",LOCALE_EN);
        String newDescription = "new description";

        userDao.changeDescription(user.getId()+10,newDescription);
    }

    @Test
    public void testChangeReputation(){
        User user = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,"",LOCALE_EN);

        userDao.changeReputation(user.getId(),10);

        int rep = jdbcTemplate.queryForObject("SELECT reputation FROM users WHERE id = ?",new Object[]{user.getId()},Integer.class);
        assertEquals(10,rep);
    }

    @Test
    public void testChangeReputationEmpty(){
        User user = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,"",LOCALE_EN);

        userDao.changeReputation(user.getId()+10,10);

        int rep = jdbcTemplate.queryForObject("SELECT reputation FROM users WHERE id = ?",new Object[]{user.getId()},Integer.class);
        assertEquals(0,rep);
    }

    @Test
    public void getAllUsersTests(){
        User user = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,"",LOCALE_EN);

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
    public void testUpdateLocale(){
      User user = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,"",LOCALE_EN);

      userDao.updateLocale(user.getId(),LOCALE_ES);

      String locale = jdbcTemplate.queryForObject("SELECT region FROM users WHERE id = ?",new Object[]{user.getId()},String.class);
      assertEquals(LOCALE_ES.toString(),locale);
    }

    @Test
    public void testUpdateLocaleEmpty(){
        User user = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,"",LOCALE_EN);

        userDao.updateLocale(user.getId()+10,LOCALE_ES);

        String locale = jdbcTemplate.queryForObject("SELECT region FROM users WHERE id = ?",new Object[]{user.getId()},String.class);
        assertEquals(LOCALE_EN.toString(),locale);
    }

    @Test
    public void testGetLocaleLanguage(){
        User user = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,"",LOCALE_EN);

        String loc = userDao.getLocaleLanguage(user.getId());

        assertEquals(LOCALE_EN.getLanguage(),loc);
    }

    @Test
    public void testGetLocaleLanguageEmpty(){
        User user = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,"",LOCALE_EN);

        String loc = userDao.getLocaleLanguage(user.getId()+10);
    }

    @Test
    public void testGetLocaleRegion(){
        User user = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,"",LOCALE_EN);

        String loc = userDao.getLocaleRegion(user.getId());

        assertEquals(LOCALE_EN.getCountry(),loc);
    }

    @Test
    public void testGetLocaleRegionEmpty(){
        User user = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,"",LOCALE_EN);

        String loc = userDao.getLocaleRegion(user.getId()+10);
    }

    @Test
    public void testUserEmailIsVerified(){
        User user = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,"",LOCALE_EN);

        boolean result = userDao.userEmailIsVerified(user.getId());

        assertFalse(result);
    }

    @Test
    public void testUserEmailIsVerifiedEmpty(){
        User user = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,"",LOCALE_EN);

        boolean result = userDao.userEmailIsVerified(user.getId()+10);
    }

    @Test
    public void testVerifyUserEmail(){
        User user = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,"",LOCALE_EN);

        userDao.verifyUserEmail(user.getId());

        int verified = jdbcTemplate.queryForObject("SELECT verified FROM users WHERE id=?",new Object[]{user.getId()},Integer.class);
        assertEquals(1,verified);
    }

    @Test
    public void testVerifyUserEmailEmpty(){
        User user = insertUserIntoDb(jdbcInsertUser,USERNAME,PASSWORD,EMAIL,"",LOCALE_EN);

        userDao.verifyUserEmail(user.getId()+10);

        int verified = jdbcTemplate.queryForObject("SELECT verified FROM users WHERE id=?",new Object[]{user.getId()},Integer.class);
        assertEquals(0,verified);
    }




}

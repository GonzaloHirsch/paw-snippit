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

        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
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
    public void testChangePassowrd(){
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

}

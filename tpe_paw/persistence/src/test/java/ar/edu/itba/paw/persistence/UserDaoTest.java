package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired private UserDao userDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertUser;

    private final String USERS_TABLE = "users";

    private static final String PASSWORD = "Password";
    private static final String USERNAME = "Username";
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD2 = "Password2";
    private static final String USERNAME2 = "Username2";
    private static final String EMAIL2 = "email2@email.com";
    private static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private final static RowMapper<User> ROW_MAPPER = (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("email"),
            rs.getString("description"),
            rs.getInt("reputation"),
            rs.getString("date_joined"),
            rs.getBytes("icon")
    );


    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertUser = new SimpleJdbcInsert(ds).withTableName(USERS_TABLE).usingGeneratedKeyColumns("id");

        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
    }

    private User insertUserIntoDatabase(String username, String password, String email,String description){
        Map<String, Object> map = new HashMap<String, Object>() {{
            put("username", username);
            put("password", password);
            put("email", email);
            put("reputation", 0);
            put("date_joined", DATE.format(Calendar.getInstance().getTime().getTime()));
        }};
        long userId = jdbcInsertUser.executeAndReturnKey(map).longValue();
        return new User(userId, username, password, email, description, 0, DATE.format(Calendar.getInstance().getTime().getTime()), null);

    }

    @Test
    public void testCreateUser() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate,USERS_TABLE);
        final long userId = userDao.createUser(USERNAME, PASSWORD, EMAIL, "", 0, DATE.format(Calendar.getInstance().getTime().getTime()));

        assertNotNull(userId);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "users"));
    }

    @Test
    public void testFindUserById(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,USERS_TABLE);
        User expectedUser = insertUserIntoDatabase(USERNAME,PASSWORD,EMAIL,"");

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
        User expectedUser = insertUserIntoDatabase(USERNAME,PASSWORD,EMAIL,"");
        insertUserIntoDatabase(USERNAME2,PASSWORD2,EMAIL2,"");

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
        User expectedUser = insertUserIntoDatabase(USERNAME,PASSWORD,EMAIL,"");
        insertUserIntoDatabase(USERNAME2,PASSWORD2,EMAIL2,"");

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
        User user = insertUserIntoDatabase(USERNAME,PASSWORD,EMAIL,"");
        String newDescription = "New Description";

        userDao.updateDescription(user.getUsername(),newDescription);

        // Manually obtaining the snippet. The idea is not to use other methods of the dao, because its unit testing
        Optional<User> maybeUser = jdbcTemplate.query("SELECT * FROM users WHERE id = ?", ROW_MAPPER, user.getId()).stream().findFirst();
        assertTrue(maybeUser.isPresent());
        assertEquals(newDescription, maybeUser.get().getDescription());
        assertEquals(user.getId(),maybeUser.get().getId());

    }

    @Test
    public void testChangePassowrd(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,USERS_TABLE);
        User user = insertUserIntoDatabase(USERNAME,PASSWORD,EMAIL,"");
        String newPassword = "newpassword";

        userDao.changePassword(user.getEmail(),newPassword);

        // Manually obtaining the snippet. The idea is not to use other methods of the dao, because its unit testing
        Optional<User> maybeUser = jdbcTemplate.query("SELECT * FROM users WHERE id = ?", ROW_MAPPER, user.getId()).stream().findFirst();
        assertTrue(maybeUser.isPresent());
        assertEquals(newPassword, maybeUser.get().getPassword());
        assertEquals(user.getId(),maybeUser.get().getId());
    }

    @Test
    public void testChangeDescription(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,USERS_TABLE);
        User user = insertUserIntoDatabase(USERNAME,PASSWORD,EMAIL,"");
        String newDescription = "new description";

        userDao.changeDescription(user.getId(),newDescription);

        // Manually obtaining the snippet. The idea is not to use other methods of the dao, because its unit testing
        Optional<User> maybeUser = jdbcTemplate.query("SELECT * FROM users WHERE id = ?", ROW_MAPPER, user.getId()).stream().findFirst();
        assertTrue(maybeUser.isPresent());
        assertEquals(newDescription, maybeUser.get().getDescription());
        assertEquals(user.getId(),maybeUser.get().getId());
    }

}

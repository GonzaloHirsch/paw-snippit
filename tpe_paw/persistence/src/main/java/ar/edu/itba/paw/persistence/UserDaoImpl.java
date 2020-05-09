package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class UserDaoImpl implements UserDao {
    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<User> ROW_MAPPER = new RowMapper<User>() {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
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
        }
    };

    @Autowired
    public UserDaoImpl(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName("users").usingGeneratedKeyColumns("id");
    }

    @Override
    @Transactional
    public long createUser(String username,String password, String email, String description, int reputation, String dateJoined, Locale locale) {
        final Map<String, Object> args = new HashMap<>()    ;
        args.put("username",username);
        args.put("password",password);
        args.put("email",email);
        args.put("description", description);
        args.put("reputation",reputation);
        args.put("date_joined",dateJoined);
        args.put("lang", locale.getLanguage());
        args.put("region", locale.getCountry());
        args.put("verified", 0);

        return jdbcInsert.executeAndReturnKey(args).longValue();
    }

    @Override
    public Optional<User> findUserById(long id) {
        return this.jdbcTemplate.query("SELECT * FROM users WHERE id = ?", ROW_MAPPER, id)
                .stream().findFirst();
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return this.jdbcTemplate.query("SELECT * FROM users WHERE username = ?", ROW_MAPPER, username)
                .stream().findFirst();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return this.jdbcTemplate.query("SELECT * FROM users WHERE email = ?", ROW_MAPPER, email)
                .stream().findFirst();
    }

    @Override
    @Transactional
    public void updateDescription(String username, String newDescription){
        this.jdbcTemplate.update("UPDATE users SET description = ? WHERE username = ?", newDescription, username);
    }

    @Override
    @Transactional
    public void changePassword(String email, String password){
        this.jdbcTemplate.update("UPDATE users SET password = ? WHERE email = ?", password, email);
    }

    @Override
    @Transactional
    public void changeProfilePhoto(long userId, byte[] photo) {
        this.jdbcTemplate.update("UPDATE users SET icon = ? WHERE id = ?", photo, userId);
    }

    @Override
    @Transactional
    public void changeDescription(final long userId, final String description) {
        this.jdbcTemplate.update("UPDATE users SET description = ? WHERE id = ?", description, userId);
    }

    @Override
    @Transactional
    public void changeReputation(long userId, int value) {
        this.jdbcTemplate.update("UPDATE users SET reputation = reputation + ? WHERE id = ?", value, userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        return this.jdbcTemplate.query("SELECT * FROM users", ROW_MAPPER);
    }

    @Override
    public Collection<User> getAllVerifiedUsers() {
        return this.jdbcTemplate.query("SELECT * FROM users WHERE verified = 1", ROW_MAPPER);
    }

    @Override
    @Transactional
    public void updateLocale(long userId, Locale locale) {
        this.jdbcTemplate.update("UPDATE users SET lang = ?, region = ? WHERE id = ?", locale.getLanguage(), locale.getCountry(), userId);
    }

    @Override
    public String getLocaleLanguage(long userId) {
        try {
            return this.jdbcTemplate.queryForObject("SELECT lang FROM users WHERE id = ?", new Object[]{userId}, String.class);
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String getLocaleRegion(long userId) {
        try {
            return this.jdbcTemplate.queryForObject("SELECT region FROM users WHERE id = ?", new Object[]{userId}, String.class);
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public boolean userEmailIsVerified(long userId) {
        return this.jdbcTemplate.queryForObject("SELECT verified FROM users WHERE id = ?", new Object[]{userId}, Integer.class) == 1;
    }

    @Override
    @Transactional
    public void verifyUserEmail(long userId) {
        this.jdbcTemplate.update("UPDATE users SET verified = 1 WHERE id = ?", userId);
    }

}

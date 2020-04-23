package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

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
                    rs.getDate("date_joined")); //ni idea como hacer esto
        }
    };

    @Autowired
    public UserDaoImpl(final DataSource ds){

        jdbcTemplate = new JdbcTemplate(ds);

        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("users").usingGeneratedKeyColumns("id");
    }

    @Override
    public User createUser(String username,String password, String email, String description, int reputation, Date dateJoined) {
        final Map<String, Object> args = new HashMap<>();
        args.put("username",username);
        args.put("password",password);
        args.put("email",email);
        args.put("description", description);
        args.put("reputation",reputation);
        args.put("date_joined",dateJoined);

        long result = jdbcInsert.executeAndReturnKey(args).longValue();
        return new User(result, username, password, email, description, reputation, dateJoined);
    }

    @Override
    public Optional<User> findUserById(long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE id = ?", ROW_MAPPER, id)
                .stream().findFirst();
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = ?", ROW_MAPPER, username)
                .stream().findFirst();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM users WHERE email = ?", ROW_MAPPER, email)
                .stream().findFirst();
    }

    @Override
    public void updateDescription(String username, String newDescription){
        jdbcTemplate.update("UPDATE users SET description = ? WHERE username = ?", newDescription, username);
    }

    @Override
    public void changePassword(String email, String password){
        jdbcTemplate.update("UPDATE users SET password = ? WHERE email = ?", password, email);
    }

    @Override
    public Optional<User> getCurrentUser() {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = ?", ROW_MAPPER, "JaneRoe")
                .stream().findFirst();
    }

}

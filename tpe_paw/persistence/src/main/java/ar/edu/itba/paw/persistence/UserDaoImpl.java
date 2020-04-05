package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserDaoImpl implements UserDao {
    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<User> ROW_MAPPER = new RowMapper<User>() {

        @Override public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("description"),
                    rs.getDate("dateJoined")); //ni idea como hacer esto
        }
    };

    @Autowired
    public UserDaoImpl(final DataSource ds){

        jdbcTemplate = new JdbcTemplate(ds);

        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("users");
    }

    @Override
    public User createUser(String username,String password, String email, String description, Date dateJoined) {
        final Map<String, Object> args = new HashMap<>();
        args.put("username",username);
        args.put("password",password);
        args.put("email",email);
        args.put("description",description);
        args.put("dateJoined",dateJoined);

        int result = jdbcInsert.execute(args);
        return new User(username, password, email, description, dateJoined);
    }

    @Override
    public User findUserByUsername(String username) {
        final List<User> list = jdbcTemplate.query("SELECT * FROM users WHERE username = ?", ROW_MAPPER, username);
        if(list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public void updateDescription(String username, String newDescription){
        jdbcTemplate.update("UPDATE users SET description = ? WHERE username = ?", newDescription, username);
    }

    @Override
    public void changePassword(String email, String password){
        jdbcTemplate.update("UPDATE users SET password = ? WHERE email = ?", password, email);
    }
}

package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.RoleDao;
import ar.edu.itba.paw.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class RoleDaoImpl implements RoleDao {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    private final static String USER_ROLE = "USER";
    private final static String ADMIN_ROLE = "ADMIN";


    private final static RowMapper<Role> ROW_MAPPER = (rs, rowNum) -> new Role(rs.getInt("id"), rs.getString("role"));

    @Autowired
    RoleDaoImpl(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds).withTableName("user_roles");
    }

    private void addToUserRoles(final long userId, final long roleId) {
        Map<String, Object> args = new HashMap<>();
        args.put("user_id", userId);
        args.put("role_id", roleId);
        jdbcInsert.execute(args);
    }

    @Override
    public Collection<Role> getAllRoles() {
        return jdbcTemplate.query("SELECT * FROM roles", ROW_MAPPER);
    }

    @Override
    public Collection<String> getUserRoles(long userId) {
        return jdbcTemplate.queryForList("SELECT r.role FROM user_roles AS ur LEFT OUTER JOIN roles AS r ON ur.role_id = r.id WHERE ur.user_id = ?", new Object[]{userId}, String.class);
    }

    @Override
    public void assignUserRole(long userId) {
       long roleId = jdbcTemplate.queryForObject("SELECT id FROM roles WHERE role = ?", new Object[]{USER_ROLE}, Long.class);

       this.addToUserRoles(userId, roleId);

    }

    @Override
    public void assignAdminRole(long userId) {
        long roleId = jdbcTemplate.queryForObject("SELECT id FROM roles WHERE role = ?", new Object[]{ADMIN_ROLE}, Long.class);

        this.addToUserRoles(userId, roleId);
    }

    @Override
    public String getAdminRoleName() {
        return ADMIN_ROLE;
    }

    @Override
    public String getUserRoleName() {
        return USER_ROLE;
    }

}

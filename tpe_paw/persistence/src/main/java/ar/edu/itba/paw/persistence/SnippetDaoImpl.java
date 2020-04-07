package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class SnippetDaoImpl implements SnippetDao {

    private JdbcTemplate jdbcTemplate;

    private final static RowMapper<Snippet> ROW_MAPPER = new RowMapper<Snippet>() {

        @Autowired
        private UserDao userDao;

        @Override
        public Snippet mapRow(ResultSet rs, int rowNum) throws SQLException {
            User userOwner = (userDao.findUserById(rs.getLong("owner"))).orElse(null);
            return new Snippet(
                    rs.getLong("id"),
                    userOwner,
                    rs.getString("code"),
                    rs.getString("title"),
                    rs.getString("description")
            );
        }
    };

    public SnippetDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);

    }

    @Override
    public Collection<Snippet> getSnippetByTag(String tag) {
        /*
        final Collection<Snippet> snippets = jdbcTemplate.query("SELECT * FROM snippets WHERE id = ?", ROW_MAPPER, id);
        final List<User> list =
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
        */
        return null;
    }
}

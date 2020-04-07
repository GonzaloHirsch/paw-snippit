package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
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

    @Autowired
    public SnippetDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS snippets("
                + "id SERIAL PRIMARY KEY,"
                + "ownerId INTEGER,"
                + "code VARCHAR(1000) NOT NULL,"
                + "title VARCHAR(100) NOT NULL,"
                + "description VARCHAR(250)"
                + ")");
    }

    @Override
    public Collection<Snippet> getSnippetByTag(String tag) {
        final String tagToUse = "%" + tag + "%";
        return jdbcTemplate.query("SELECT id, ownerId, code, title, description FROM snippets AS s INNER JOIN taggedSnippets AS ts ON s.id = ts.snippetId INNER JOIN tags AS t WHERE t.id = ts.tagId AND t.name LIKE ?", ROW_MAPPER, tagToUse);
    }
}

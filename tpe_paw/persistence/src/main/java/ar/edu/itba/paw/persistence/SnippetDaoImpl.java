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
import java.util.Optional;

@Repository
public class SnippetDaoImpl implements SnippetDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserDao userDao;

    private final RowMapper<Snippet> ROW_MAPPER = new RowMapper<Snippet>() {


        @Override
        public Snippet mapRow(ResultSet rs, int rowNum) throws SQLException {
            User userOwner = (userDao.findUserById(rs.getLong("user_id"))).orElse(null);
            return new Snippet(
                    rs.getLong("id"),
                    userOwner,
                    rs.getString("code"),
                    rs.getString("language_id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getDate("date_created")
            );
        }
    };

    @Autowired
    public SnippetDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public Collection<Snippet> getAllSnippets(){
        return jdbcTemplate.query("SELECT * FROM snippets",ROW_MAPPER);
    }

    @Override
    public Collection<Snippet> findSnippetsByTag(String tag, String source, String userId) {
        SnippetQuery snippetQuery = new SnippetQuery.Builder()
                .setSource(source, userId)
                .setType("tag", tag).build();
        return jdbcTemplate.query(snippetQuery.getQuery(), snippetQuery.getParams(), ROW_MAPPER);
    }

    @Override
    public Collection<Snippet> findSnippetsByTitle(String title, String source, String userId) {
        SnippetQuery snippetQuery = new SnippetQuery.Builder()
                .setSource(source, userId)
                .setType("title", title).build();
        return jdbcTemplate.query(snippetQuery.getQuery(), snippetQuery.getParams(), ROW_MAPPER);
    }

    @Override
    public Collection<Snippet> findSnippetsByContent(String content, String source, String userId) {
        SnippetQuery snippetQuery = new SnippetQuery.Builder()
                .setSource(source, userId)
                .setType("content", content).build();
        return jdbcTemplate.query(snippetQuery.getQuery(), snippetQuery.getParams(), ROW_MAPPER);
    }

    @Override
    public Optional<Snippet> findSnippetById(long id){
        return jdbcTemplate.query("SELECT * FROM snippets WHERE id = ?", ROW_MAPPER, id).stream().findFirst();
    }
}

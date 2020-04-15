package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.LanguageDao;
import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.dao.TagDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.models.Language;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Optional;

@Repository
public class SnippetDaoImpl implements SnippetDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserDao userDao;
    @Autowired
    private LanguageDao languageDao;
    @Autowired
    private TagDao tagDao;

    private final RowMapper<Snippet> ROW_MAPPER = new RowMapper<Snippet>() {
        @Override
        public Snippet mapRow(ResultSet rs, int rowNum) throws SQLException {
            long snippetId = rs.getLong("id");

            User userOwner = userDao.findUserById(rs.getLong("user_id")).orElse(null);

            Language language = languageDao.findById(rs.getLong("language_id")).orElse(null);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(rs.getTimestamp("date_created").getTime());

            return new Snippet(
                    snippetId,
                    userOwner,
                    rs.getString("code"),
                    rs.getString("title"),
                    rs.getString("description"),
                    new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(calendar.getTime()),
                    language != null ? language.getName() : "",
                    tagDao.findTagsForSnippet(snippetId)
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
    public Collection<Snippet> getAllFavoriteSnippets(Long userId) {
        return jdbcTemplate.query("SELECT s.id, s.user_id, s.code, s.title, s.description, s.language_id, s.date_created FROM snippets AS s JOIN favorites AS fav ON fav.snippet_id = s.id WHERE fav.user_id = ?",ROW_MAPPER, userId);
    }

    @Override
    public Collection<Snippet> getAllFollowingSnippets(Long userId) {
        return jdbcTemplate.query("SELECT sn.id, sn.user_id, sn.code, sn.title, sn.description, sn.language_id, sn.date_created FROM snippets AS sn JOIN snippet_tags AS st ON st.snippet_id = sn.id JOIN follows AS fol ON st.tag_id = fol.tag_id WHERE fol.user_id = ?",ROW_MAPPER, userId);
    }

    @Override
    public Collection<Snippet> findSnippetByCriteria(Types type, String term, Locations location, Orders order, Long userId) {
        SnippetSearchQuery searchQuery = new SnippetSearchQuery.Builder(location, userId, type, term)
                .setOrder(order, type)
                .build();
        return jdbcTemplate.query(searchQuery.getQuery(), searchQuery.getParams(), ROW_MAPPER);
    }

    @Override
    public Optional<Snippet> findSnippetById(long id){
        return Optional.of((Snippet) jdbcTemplate.queryForObject("SELECT * FROM snippets WHERE id = ?", ROW_MAPPER, id));
    }
}

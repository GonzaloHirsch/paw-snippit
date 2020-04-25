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
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
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

    @Autowired
    private TagDao tagDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LanguageDao languageDao;

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final RowMapper<Snippet> ROW_MAPPER = new RowMapper<Snippet>() {
        @Override
        public Snippet mapRow(ResultSet rs, int rowNum) throws SQLException {
            User userOwner = new User(
                    rs.getLong("user_id"),
                    rs.getString("username"),
                    null,
                    null,
                    null,
                    rs.getInt("reputation"),
                    null
            );

            String language = rs.getString("language");

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(rs.getTimestamp("date_created").getTime());

            return new Snippet(
                    rs.getLong("id"),
                    userOwner,
                    rs.getString("code"),
                    rs.getString("title"),
                    rs.getString("description"),
                    new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(calendar.getTime()),
                    language != null ? language : "",
                    null
            );
        }
    };

    @Autowired
    public SnippetDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName("snippets").usingGeneratedKeyColumns("id");
    }

    @Override
    public Collection<Snippet> getAllSnippets(int page, int pageSize) {
        return jdbcTemplate.query("SELECT * FROM complete_snippets LIMIT ? OFFSET ?", ROW_MAPPER, pageSize, pageSize * page);
    }


    @Override
    public Collection<Snippet> getAllFavoriteSnippets(Long userId) {
        return jdbcTemplate.query("SELECT s.id, s.user_id, s.username, s.reputation, s.code, s.title, s.description, s.language, s.date_created FROM complete_snippets AS s JOIN favorites AS fav ON fav.snippet_id = s.id WHERE fav.user_id = ?", ROW_MAPPER, userId);
    }

    @Override
    public Collection<Snippet> getAllFollowingSnippets(Long userId) {
        return jdbcTemplate.query("SELECT sn.id, sn.user_id, sn.username, sn.reputation, sn.code, sn.title, sn.description, sn.language, sn.date_created FROM complete_snippets AS sn JOIN snippet_tags AS st ON st.snippet_id = sn.id JOIN follows AS fol ON st.tag_id = fol.tag_id WHERE fol.user_id = ?", ROW_MAPPER, userId);
    }

    @Override
    public Collection<Snippet> findAllSnippetsByOwner(long userId) {
        return jdbcTemplate.query("SELECT * FROM complete_snippets AS s WHERE s.user_id = ?", ROW_MAPPER, userId);
    }

    @Override
    public Collection<Snippet> findSnippetByCriteria(QueryTypes queryType, Types type, String term, Locations location, Orders order, Long userId, int page, int pageSize) {
        SnippetSearchQuery searchQuery = new SnippetSearchQuery.Builder(queryType, location, userId, type, term)
                .setOrder(order, type)
                .setPaging(page, pageSize)
                .build();
        return jdbcTemplate.query(searchQuery.getQuery(), searchQuery.getParams(), ROW_MAPPER);
    }

    @Override
    public Optional<Snippet> findSnippetById(long id) {
        Optional<Snippet> snippet = Optional.of((Snippet) jdbcTemplate.queryForObject("SELECT * FROM complete_snippets AS s WHERE s.id = ?", ROW_MAPPER, id));
        if (snippet.isPresent() && snippet.get().getTags() == null) {
            snippet.get().setTags(tagDao.findTagsForSnippet(snippet.get().getId()));
        }
        return snippet;
    }

    @Override
    public Collection<Snippet> findSnippetsForTag(long tagId) {
        return jdbcTemplate.query("SELECT DISTINCT * FROM complete_snippets AS cs JOIN snippet_tags AS st ON cs.id = st.snippet_id WHERE st.tag_id = ?", ROW_MAPPER, tagId);
    }

    @Override
    public int getAllSnippetsCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM complete_snippets", Integer.class);
    }

    @Override
    public int getSnippetByCriteriaCount(QueryTypes queryType, Types type, String term, Locations location, Long userId) {
        SnippetSearchQuery searchQuery = new SnippetSearchQuery.Builder(queryType, location, userId, type, term)
                .build();
        return jdbcTemplate.queryForObject(searchQuery.getQuery(), searchQuery.getParams(), Integer.class);
    }
}

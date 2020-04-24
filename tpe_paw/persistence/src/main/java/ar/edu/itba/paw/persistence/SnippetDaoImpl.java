package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.LanguageDao;
import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.dao.TagDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.models.Language;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.Tag;
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
import java.util.*;

@Repository
public class SnippetDaoImpl implements SnippetDao {

    @Autowired
    private TagDao tagDao;
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
    public Collection<Snippet> getAllSnippets(){
        return jdbcTemplate.query("SELECT * FROM complete_snippets", ROW_MAPPER);
    }


    @Override
    public Collection<Snippet> getAllFavoriteSnippets(Long userId) {
        return jdbcTemplate.query("SELECT s.id, s.user_id, s.username, s.reputation, s.code, s.title, s.description, s.language, s.date_created FROM complete_snippets AS s JOIN favorites AS fav ON fav.snippet_id = s.id WHERE fav.user_id = ?",ROW_MAPPER, userId);
    }

    @Override
    public Collection<Snippet> getAllFollowingSnippets(Long userId) {
        return jdbcTemplate.query("SELECT sn.id, sn.user_id, sn.username, sn.reputation, sn.code, sn.title, sn.description, sn.language, sn.date_created FROM complete_snippets AS sn JOIN snippet_tags AS st ON st.snippet_id = sn.id JOIN follows AS fol ON st.tag_id = fol.tag_id WHERE fol.user_id = ?",ROW_MAPPER, userId);
    }

    @Override
    public Collection<Snippet> findAllSnippetsByOwner(long userId) {
        return jdbcTemplate.query("SELECT * FROM snippets AS s WHERE s.user_id = ?",ROW_MAPPER, userId);
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
        Optional<Snippet> snippet = Optional.of((Snippet) jdbcTemplate.queryForObject("SELECT * FROM complete_snippets AS s WHERE s.id = ?", ROW_MAPPER, id));
        if (snippet.isPresent() && snippet.get().getTags() == null) {
            snippet.get().setTags(tagDao.findTagsForSnippet(snippet.get().getId()));
        }
        return snippet;
    }

    @Override
    public Optional<Snippet> createSnippet(User owner, String title, String description,String code, String dateCreated, Long language){
        final Map<String, Object> snippetDataMap = new HashMap<String,Object>(){{
            put("user_id", owner.getUserId());
            put("title",title);
            put("description",description);
            put("code",code);
            put("date_created",dateCreated);
            put("language_id",language);
        }};
        final Number snippetId = jdbcInsert.executeAndReturnKey(snippetDataMap);

        Optional<Language> maybeLanguage = languageDao.findById(language);
        String languageName = "";
        if(maybeLanguage.isPresent()) languageName = maybeLanguage.get().getName();

        return Optional.of(new Snippet(snippetId.longValue(),owner,code,title,description,dateCreated,languageName,new ArrayList<>()));
    }

    @Override
    public Collection<Snippet> findSnippetsForTag(long tagId) {
        return jdbcTemplate.query("SELECT DISTINCT * FROM snippets AS s1 WHERE EXISTS" +
                "(SELECT * FROM snippet_tags AS st WHERE s1.id = st.snippet_id AND st.tag_id = ?)", ROW_MAPPER, tagId);
    }
}

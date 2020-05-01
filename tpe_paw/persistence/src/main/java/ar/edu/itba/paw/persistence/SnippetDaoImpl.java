package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.LanguageDao;
import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.dao.TagDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
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
import java.util.*;

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
                    null,
                    rs.getBytes("icon")
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
                    null,
                    rs.getInt("votes")
            );
        }
    };

    @Autowired
    public SnippetDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName("snippets").usingGeneratedKeyColumns("id");
    }

    @Override
    public Collection<Snippet> getAllSnippets(int page) {
        return jdbcTemplate.query("SELECT * FROM complete_snippets LIMIT ? OFFSET ?", ROW_MAPPER, PAGE_SIZE, PAGE_SIZE * (page - 1));
    }

    @Override
    public Collection<Snippet> getAllFavoriteSnippets(final long userId, int page) {
        return jdbcTemplate.query("SELECT s.id, s.user_id, s.username, s.reputation, s.code, s.title, s.description, s.language, s.date_created, s.icon, s.votes FROM complete_snippets AS s JOIN favorites AS fav ON fav.snippet_id = s.id WHERE fav.user_id = ? LIMIT ? OFFSET ?", ROW_MAPPER, userId, PAGE_SIZE, PAGE_SIZE * (page - 1));
    }

    @Override
    public Collection<Snippet> getAllFollowingSnippets(final long userId, int page) {
        return jdbcTemplate.query("SELECT sn.id, sn.user_id, sn.username, sn.reputation, sn.code, sn.title, sn.description, sn.language, sn.date_created, sn.icon, s.votes FROM complete_snippets AS sn JOIN snippet_tags AS st ON st.snippet_id = sn.id JOIN follows AS fol ON st.tag_id = fol.tag_id WHERE fol.user_id = ? LIMIT ? OFFSET ?", ROW_MAPPER, userId, PAGE_SIZE, PAGE_SIZE * (page - 1));
    }

    @Override
    public Collection<Snippet> getAllUpVotedSnippets(final long userId, int page) {
        return jdbcTemplate.query("SELECT sn.id, sn.user_id, sn.username, sn.reputation, sn.code, sn.title, sn.description, sn.language, sn.date_created, sn.icon, s.votes FROM complete_snippets AS sn JOIN votes_for AS v ON sn.id = v.snippet_id WHERE v.user_id = ? AND v.type = 1 LIMIT ? OFFSET ?", ROW_MAPPER, userId, PAGE_SIZE, PAGE_SIZE * (page - 1));
    }

    @Override
    public Collection<Snippet> findAllSnippetsByOwner(long userId, int page) {
        return jdbcTemplate.query("SELECT * FROM complete_snippets AS s WHERE s.user_id = ? LIMIT ? OFFSET ?", ROW_MAPPER, userId, PAGE_SIZE, PAGE_SIZE * (page - 1));
    }

    @Override
    public Collection<Snippet> findSnippetByCriteria(QueryTypes queryType, Types type, String term, Locations location, Orders order, Long userId, int page) {
        SnippetSearchQuery searchQuery = new SnippetSearchQuery.Builder(queryType, location, userId, type, term)
                .setOrder(order, type)
                .setPaging(page, PAGE_SIZE)
                .build();
        return jdbcTemplate.query(searchQuery.getQuery(), searchQuery.getParams(), ROW_MAPPER);
    }

    @Override
    public Collection<Snippet> findSnippetByDeepCriteria(Calendar dateMin, Calendar dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, String language, int page) {
        SnippetDeepSearchQuery.Builder queryBuilder = new SnippetDeepSearchQuery.Builder();
        SnippetDeepSearchQuery searchQuery;
        if (dateMin == null && dateMax == null && repMin == null && repMax == null && voteMin == null && voteMax == null){
            searchQuery = queryBuilder.setPaging(page, PAGE_SIZE).build();
        } else {
            boolean isFirst = true;
            queryBuilder = queryBuilder.where();
            if (dateMin != null || dateMax != null){
                queryBuilder = queryBuilder.addDateRange(dateMin, dateMax);
                isFirst = false;
            }
            if (repMin != null || repMax != null){
                if (!isFirst){
                    queryBuilder = queryBuilder.and();
                } else {
                    isFirst = false;
                }
                queryBuilder = queryBuilder.addReputationRange(repMin, repMax);
            }
            if (voteMin != null || voteMax != null){
                if (!isFirst){
                    queryBuilder = queryBuilder.and();
                } else {
                    isFirst = false;
                }
                queryBuilder = queryBuilder.addVotesRange(voteMin, voteMax);
            }
            if (language != null){
                if (!isFirst){
                    queryBuilder = queryBuilder.and();
                }
                queryBuilder = queryBuilder.addLanguage(language);
            }
            searchQuery = queryBuilder.setOrder().setPaging(page, PAGE_SIZE).build();
        }
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
    public Long createSnippet(User owner, String title, String description,String code, String dateCreated, Long language){
        final Map<String, Object> snippetDataMap = new HashMap<String,Object>(){{
            put("user_id", owner.getId());
            put("title",title);
            put("description",description);
            put("code",code);
            put("date_created",dateCreated);
            put("language_id",language);
        }};
        final Number snippetId = jdbcInsert.executeAndReturnKey(snippetDataMap);

        return snippetId.longValue();
    }

    @Override
    public Collection<Snippet> findSnippetsForTag(long tagId) {
        return jdbcTemplate.query("SELECT DISTINCT * FROM complete_snippets AS cs JOIN snippet_tags AS st ON cs.id = st.snippet_id WHERE st.tag_id = ?", ROW_MAPPER, tagId);
    }

    @Override
    public int getPageSize() {
        return PAGE_SIZE;
    }

    @Override
    public int getAllSnippetsCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT id) FROM complete_snippets", Integer.class);
    }

    @Override
    public int getAllFavoriteSnippetsCount(long userId) {
        return jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT s.id) FROM complete_snippets AS s JOIN favorites AS fav ON fav.snippet_id = s.id WHERE fav.user_id = ?", new Object[]{userId}, Integer.class);
    }

    @Override
    public int getAllFollowingSnippetsCount(long userId) {
        return jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT sn.id) FROM complete_snippets AS sn JOIN snippet_tags AS st ON st.snippet_id = sn.id JOIN follows AS fol ON st.tag_id = fol.tag_id WHERE fol.user_id = ?", new Object[]{userId}, Integer.class);
    }

    @Override
    public int getAllSnippetsByOwnerCount(long userId) {
        return jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT s.id) FROM complete_snippets AS s WHERE s.user_id = ?", new Object[]{userId}, Integer.class);
    }

    @Override
    public int getAllSnippetsByTagCount(long tagId) {
        return jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT cs.id) FROM complete_snippets AS cs JOIN snippet_tags AS st ON cs.id = st.snippet_id WHERE st.tag_id = ?", new Object[]{tagId}, Integer.class);
    }

    @Override
    public int getSnippetByCriteriaCount(QueryTypes queryType, Types type, String term, Locations location, Long userId) {
        SnippetSearchQuery searchQuery = new SnippetSearchQuery.Builder(queryType, location, userId, type, term)
                .build();
        return jdbcTemplate.queryForObject(searchQuery.getQuery(), searchQuery.getParams(), Integer.class);
    }
}

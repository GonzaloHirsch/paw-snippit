package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.dao.TagDao;
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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Collectors;

//@Repository
@Deprecated
public class SnippetDaoImpl implements SnippetDao {

    @Autowired
    private TagDao tagDao;

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public static final DateTimeFormatter DATE = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.UK)
            .withZone(ZoneId.systemDefault());

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
                    rs.getBytes("icon"),
                    new Locale(rs.getString("lang"), rs.getString("region")),
                    rs.getInt("verified") == 1
            );

            String language = rs.getString("language");

            return new Snippet(
                    rs.getLong("id"),
                    userOwner,
                    rs.getString("code"),
                    rs.getString("title"),
                    rs.getString("description"),
                    DATE.format(rs.getTimestamp("date_created").toInstant()),
                    language != null ? language : "",
                    null,
                    rs.getInt("votes"),
                    rs.getInt("flagged") != 0
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
        return this.jdbcTemplate.query("SELECT * FROM complete_snippets ORDER BY id LIMIT ? OFFSET ?", ROW_MAPPER, pageSize, pageSize * (page - 1));
    }

    @Override
    public Collection<Snippet> getAllFavoriteSnippets(final long userId, int page, int pageSize) {
        return this.jdbcTemplate.query("SELECT DISTINCT s.id, s.user_id, s.username, s.reputation, s.lang, s.region, s.verified, s.code, s.title, s.description, s.language, s.date_created, s.icon, s.flagged, s.votes FROM complete_snippets AS s JOIN favorites AS fav ON fav.snippet_id = s.id WHERE fav.user_id = ? ORDER BY s.id LIMIT ? OFFSET ?", ROW_MAPPER, userId, pageSize, pageSize * (page - 1));
    }

    @Override
    public Collection<Snippet> getAllFollowingSnippets(final long userId, int page, int pageSize) {
        return this.jdbcTemplate.query("SELECT DISTINCT sn.id, sn.user_id, sn.username, sn.reputation, sn.lang, sn.region, sn.verified, sn.code, sn.title, sn.description, sn.language, sn.date_created, sn.icon, sn.flagged, sn.votes FROM complete_snippets AS sn INNER JOIN snippet_tags AS st ON st.snippet_id = sn.id INNER JOIN follows AS fol ON st.tag_id = fol.tag_id WHERE fol.user_id = ? ORDER BY sn.id LIMIT ? OFFSET ?", ROW_MAPPER, userId, pageSize, pageSize * (page - 1));
    }

    @Override
    public Collection<Snippet> getAllUpVotedSnippets(final long userId, int page, int pageSize) {
        return this.jdbcTemplate.query("SELECT DISTINCT sn.id, sn.user_id, sn.username, sn.reputation, sn.lang, sn.region, sn.verified, sn.code, sn.title, sn.description, sn.language, sn.date_created, sn.icon, sn.flagged, sn.votes FROM complete_snippets AS sn JOIN votes_for AS v ON sn.id = v.snippet_id WHERE v.user_id = ? AND v.type = 1 ORDER BY sn.id LIMIT ? OFFSET ?", ROW_MAPPER, userId, pageSize, pageSize * (page - 1));
    }

    @Override
    public Collection<Snippet> getAllFlaggedSnippets(int page, int pageSize) {
        return this.jdbcTemplate.query("SELECT * FROM complete_snippets WHERE flagged = 1 ORDER BY id LIMIT ? OFFSET ?", ROW_MAPPER, pageSize, pageSize * (page - 1));
    }

    @Override
    public Collection<Snippet> findAllSnippetsByOwner(long userId, int page, int pageSize) {
        return this.jdbcTemplate.query("SELECT * FROM complete_snippets AS s WHERE s.user_id = ? ORDER BY s.id LIMIT ? OFFSET ?", ROW_MAPPER, userId, pageSize, pageSize * (page - 1));
    }

    @Override
    public Collection<Snippet> findSnippetsWithLanguage(long langId, int page, int pageSize) {
        return this.jdbcTemplate.query("SELECT * FROM complete_snippets AS s WHERE s.language_id = ? ORDER BY s.id LIMIT ? OFFSET ?", ROW_MAPPER, langId, pageSize, pageSize * (page - 1));
    }

    @Override
    public Collection<Snippet> findSnippetByCriteria(Types type, String term, Locations location, Orders order, Long userId, Long resourceId, int page, int pageSize) {
        return null;
    }

    @Override
    public Collection<Snippet> findSnippetByDeepCriteria(String dateMin, String dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, Orders order, Types type, Boolean includeFlagged, int page, int pageSize) {
        return null;
    }

    @Override
    public Optional<Snippet> findSnippetById(long id) {
        Optional<Snippet> snippet = this.jdbcTemplate.query("SELECT * FROM complete_snippets AS s WHERE s.id = ?", ROW_MAPPER, id).stream().findFirst();
        if (snippet.isPresent() && snippet.get().getTags() == null) {
            snippet.get().setTags(this.tagDao.findTagsForSnippet(snippet.get().getId()));
        }
        return snippet;
    }

    @Override
    public boolean deleteSnippetById(long id) {
        boolean success = true;
        try {
            jdbcTemplate.update("DELETE FROM snippets WHERE id = ?", id);
        } catch (Exception e) {
            success = false;
        }
        return success;
    }

    @Override
    public int getNewSnippetsForTagsCount(String dateMin, Collection<Tag> tags, long userId) {
        List<Long> tagIds = tags.stream().mapToLong(Tag::getId).boxed().collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (Long id : tagIds) {
            sb.append("st.tag_id = ?");
            if (count < tagIds.size() - 1) {
                sb.append(" OR ");
            }
            count++;
        }
        Object[] params = new Object[tagIds.size() + 2];
        params[0] = userId;
        params[1] = dateMin;
        int i = 2;
        for (Long id : tagIds) {
            params[i++] = id;
        }
        return this.jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT s.id) FROM complete_snippets AS s LEFT OUTER JOIN snippet_tags AS st ON s.id = st.snippet_id WHERE s.user_id != ? AND s.date_created::date >= ?::date AND ( " + sb.toString() + " )", params, Integer.class);
    }

    @Override
    public Long createSnippet(long ownerId, String title, String description, String code, String dateCreated, Long languageId) {
        final Map<String, Object> snippetDataMap = new HashMap<String, Object>() {{
            put("user_id", ownerId);
            put("title", title);
            put("description", description);
            put("code", code);
            put("date_created", dateCreated);
            put("language_id", languageId);
            put("flagged", 0);
        }};

        return this.jdbcInsert.executeAndReturnKey(snippetDataMap).longValue();
    }

    @Override
    public Collection<Snippet> findSnippetsForTag(long tagId, int page, int pageSize) {
        return this.jdbcTemplate.query("SELECT DISTINCT cs.id, cs.user_id, cs.username, cs.reputation, cs.lang, cs.region, cs.verified, cs.code, cs.title, cs.description, cs.language, cs.date_created, cs.icon, cs.flagged, cs.votes  FROM complete_snippets AS cs JOIN snippet_tags AS st ON cs.id = st.snippet_id WHERE st.tag_id = ? ORDER BY cs.id LIMIT ? OFFSET ?", ROW_MAPPER, tagId, pageSize, pageSize * (page - 1));
    }

    @Override
    public void flagSnippet(long snippetId) {
        this.jdbcTemplate.update("UPDATE snippets SET flagged = 1 WHERE id = ?", snippetId);
    }

    @Override
    public void unflagSnippet(long snippetId) {
        this.jdbcTemplate.update("UPDATE snippets SET flagged = 0 WHERE id = ?", snippetId);
    }

    @Override
    public int getAllSnippetsCount() {
        return this.jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT id) FROM complete_snippets", Integer.class);
    }

    @Override
    public int getAllFavoriteSnippetsCount(long userId) {
        return this.jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT s.id) FROM complete_snippets AS s JOIN favorites AS fav ON fav.snippet_id = s.id WHERE fav.user_id = ?", new Object[]{userId}, Integer.class);
    }

    @Override
    public int getAllFollowingSnippetsCount(long userId) {
        return this.jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT sn.id) FROM complete_snippets AS sn JOIN snippet_tags AS st ON st.snippet_id = sn.id JOIN follows AS fol ON st.tag_id = fol.tag_id WHERE fol.user_id = ?", new Object[]{userId}, Integer.class);
    }

    @Override
    public int getAllUpvotedSnippetsCount(long userId) {
        return this.jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT sn.id) FROM complete_snippets AS sn JOIN votes_for AS vf ON vf.snippet_id = sn.id WHERE vf.user_id = ?", new Object[]{userId}, Integer.class);
    }

    @Override
    public int getAllFlaggedSnippetsCount() {
        return this.jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT id) FROM complete_snippets WHERE flagged = 1", Integer.class);
    }

    @Override
    public int getAllSnippetsByOwnerCount(long userId) {
        return this.jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT s.id) FROM complete_snippets AS s WHERE s.user_id = ?", new Object[]{userId}, Integer.class);
    }

    @Override
    public int getAllSnippetsByTagCount(long tagId) {
        return this.jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT cs.id) FROM complete_snippets AS cs JOIN snippet_tags AS st ON cs.id = st.snippet_id WHERE st.tag_id = ?", new Object[]{tagId}, Integer.class);
    }

    @Override
    public int getAllSnippetsByLanguageCount(long langId) {
        return this.jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT s.id) FROM complete_snippets AS s WHERE s.language_id = ?", new Object[]{langId}, Integer.class);
    }

    @Override
    public int getSnippetByCriteriaCount(Types type, String term, Locations location, Long userId, Long resourceId) {
        return 0;
    }

    @Override
    public int getSnippetByDeepCriteriaCount(String dateMin, String dateMax, Integer repMin, Integer repMax, Integer voteMin, Integer voteMax, Long languageId, Long tagId, String title, String username, Boolean includeFlagged) {
        return 0;
    }
}

package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.TagDao;
import ar.edu.itba.paw.models.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Deprecated
public class TagDaoImpl implements TagDao {

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SimpleJdbcInsert jdbcInsertSnippetTag;
    private final static RowMapper<Tag> ROW_MAPPER = new RowMapper<Tag>() {

        @Override
        public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Tag(rs.getInt("id"), rs.getString("name"));
        }
    };

    @Autowired
    public TagDaoImpl(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate((dataSource));
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("tags").usingGeneratedKeyColumns("id");
        this.jdbcInsertSnippetTag = new SimpleJdbcInsert(jdbcTemplate).withTableName("snippet_tags");
    }

    @Override
    public Optional<Tag> findById(final long id) {
        return jdbcTemplate.query("SELECT * FROM tags WHERE id = ?", ROW_MAPPER, id)
                .stream().findFirst();
    }

    @Override
    public Optional<Tag> findByName(final String name) {
        return jdbcTemplate.query("SELECT * FROM tags WHERE LOWER(name) = LOWER(?)", ROW_MAPPER, name)
                .stream().findFirst();
    }

    @Override
    public Tag addTag(String name) {
        final Map<String, Object> args = new HashMap<>();
        args.put("name", name.toLowerCase());
        final Number tagId = jdbcInsert.executeAndReturnKey(args);
        return new Tag(tagId.longValue(), name.toLowerCase());
    }

    @Override
    public Collection<Tag> findTagsForSnippet(long snippetId) {
        return jdbcTemplate.query("SELECT * FROM tags WHERE id IN (SELECT tag_id FROM snippet_tags WHERE snippet_id = ?)", ROW_MAPPER, snippetId);
    }

    @Override
    public void addSnippetTag(long snippetId, long tagId) {
        final Map<String, Object> snippetTagDataMap = new HashMap<String, Object>() {{
            put("snippet_id", snippetId);
            put("tag_id", tagId);
        }};
        jdbcInsertSnippetTag.execute(snippetTagDataMap);
    }

    @Override
    public void addTags(List<String> tags) {
        List<MapSqlParameterSource> entries = new ArrayList<>();
        if(tags.size() > 0) {
            for (String tag : tags) {
                if (tag != null && tag.compareTo("") != 0) {
                    MapSqlParameterSource entry = new MapSqlParameterSource()
                            .addValue("name", tag.toLowerCase());
                    entries.add(entry);
                }
            }
            MapSqlParameterSource[] array = entries.toArray(new MapSqlParameterSource[entries.size()]);
            jdbcInsert.executeBatch(array);
        }
    }

    @Override
    public void removeTag(long tagId) {
        jdbcTemplate.update("DELETE FROM tags WHERE id = ?", new Object[]{tagId});
    }


    @Override
    public Collection<Tag> getAllTags(int page, int pageSize) {
        return jdbcTemplate.query("SELECT * FROM tags ORDER BY id LIMIT ? OFFSET ?", ROW_MAPPER, pageSize, pageSize * (page - 1));
    }

    @Override
    public Collection<Tag> getAllTags() {
        return jdbcTemplate.query("SELECT * FROM tags ORDER BY id", ROW_MAPPER);
    }

    @Override
    public int getAllTagsCountByName(String name) {
        return this.jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT id) FROM tags WHERE LOWER(name) LIKE LOWER(?)", new Object[]{"%" + name + "%"}, Integer.class);
    }

    @Override
    public int getAllTagsCount() {
        return this.jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT id) FROM tags", Integer.class);
    }

    @Override
    public Collection<Tag> findTagsByName(String name, int page, int pageSize) {
        return this.jdbcTemplate.query("SELECT * FROM tags AS t WHERE LOWER(t.name) LIKE LOWER(?) ORDER BY t.id LIMIT ? OFFSET ?", ROW_MAPPER, "%" + name + "%", pageSize, pageSize * (page - 1));
    }
}

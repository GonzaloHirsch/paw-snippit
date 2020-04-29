package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.TagDao;
import ar.edu.itba.paw.models.Tag;
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
import java.util.Optional;

@Repository
public class TagDaoImpl implements TagDao {

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SimpleJdbcInsert jdbcInsertSnippetTag;
    private final static RowMapper<Tag> ROW_MAPPER = new RowMapper<Tag>(){

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
                .stream().findFirst(); //Tiene incorporado el optional
    }

    @Override
    public Tag addTag(String name) {
        final Map<String, Object> args = new HashMap<>();
        args.put("name", name);
        final Number tagId = jdbcInsert.executeAndReturnKey(args);
        return new Tag(tagId.longValue(), name);
    }

    @Override
    public Collection<Tag> findTagsForSnippet(long snippetId) {
        return jdbcTemplate.query("SELECT * FROM tags WHERE id IN " +
                "(SELECT tag_id FROM snippet_tags WHERE snippet_id = ?)", ROW_MAPPER, snippetId);
    }

    @Override
    public void addSnippetTag(long snippetId, long tagId){
        final Map<String, Object> snippetTagDataMap = new HashMap<String,Object>(){{
            put("snippet_id", snippetId);
            put("tag_id", tagId);
        }};
        jdbcInsertSnippetTag.execute(snippetTagDataMap);
    }

    @Override
    public Collection<Tag> getAllTags() {
        return jdbcTemplate.query("SELECT * FROM tags", ROW_MAPPER);
    }

    @Override
    public Optional<Tag> findTagById(long tagId) {
        return jdbcTemplate.query("SELECT * FROM tags WHERE id = ?", ROW_MAPPER, tagId).stream().findFirst();
    }
}

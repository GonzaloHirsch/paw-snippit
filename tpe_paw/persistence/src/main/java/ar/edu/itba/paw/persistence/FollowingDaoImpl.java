package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.FollowingDao;
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

@Repository
public class FollowingDaoImpl implements FollowingDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Tag> ROW_MAPPER = new RowMapper<Tag>(){

        @Override
        public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Tag(rs.getInt("id"), rs.getString("name"));
        }
    };

    @Autowired
    FollowingDaoImpl(DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(ds).withTableName("follows");
    }

    @Override
    public Collection<Tag> getFollowedTagsForUser(long userId) {
        return jdbcTemplate.query("SELECT * FROM tags AS t1 WHERE EXISTS" +
                "(SELECT * FROM follows AS f WHERE f.user_id = ? AND f.tag_id = t1.id)", ROW_MAPPER, userId);
    }

    @Override
    public void followTag(long userId, long tagId) {
        Map<String, Object> args = new HashMap<>();
        args.put("user_id", userId);
        args.put("tag_id", tagId);
        jdbcInsert.execute(args);
    }

    @Override
    public void unfollowTag(long userId, long tagId) {
        jdbcTemplate.update("DELETE FROM follows WHERE user_id = ? AND tag_id = ?", userId, tagId);
    }

    @Override
    public boolean userFollowsTag(long userId, long tagId) {
        return jdbcTemplate.query("SELECT * FROM tags AS t1 WHERE EXISTS" +
                "(SELECT * FROM follows AS f WHERE f.user_id = ? AND f.tag_id = ?)", ROW_MAPPER, userId, tagId)
                .stream().findFirst().isPresent();
    }
}

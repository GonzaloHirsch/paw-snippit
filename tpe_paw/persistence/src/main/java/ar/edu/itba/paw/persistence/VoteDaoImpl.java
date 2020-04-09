package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.dao.VoteDao;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.Vote;
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
public class VoteDaoImpl implements VoteDao {
    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Vote> ROW_MAPPER = new RowMapper<Vote>() {

        @Autowired
        private UserDao userDao;
        private SnippetDao snippetDao;

        @Override public Vote mapRow(ResultSet rs, int rowNum) throws SQLException {
            Optional<User> user = userDao.findUserById(rs.getLong("user_id"));
            Optional<Snippet> snippet = snippetDao.getSnippetById(rs.getLong("snippet_id"));
            if (!user.isPresent() || !snippet.isPresent())
                throw new SQLException("No User or Snippet Found");
            return new Vote(
                    user.get(),
                    snippet.get(),
                    rs.getInt("type"));
        }
    };

    @Autowired
    public VoteDaoImpl (DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);

        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("votes_for");
    }

    @Override
    public Collection<Vote> getUserVotes(long userId) {
        return jdbcTemplate.query("SELECT * FROM votes_for WHERE user_id = ?", ROW_MAPPER, userId);
    }

    @Override
    public Optional<Vote> getVote(long userId, long voteId) {
        return Optional.of(jdbcTemplate.queryForObject("SELECT * FROM votes_for WHERE user_id = ? AND vote_id = voteId", ROW_MAPPER, userId, voteId));
    }

    @Override
    public void performVote(long userId, long voteId, int voteType) {
        final Map<String, Object> args = new HashMap<>();
        args.put("user_id", userId);
        args.put("voteId", voteId);
        args.put("type", voteType);
        jdbcInsert.execute(args);
    }
}

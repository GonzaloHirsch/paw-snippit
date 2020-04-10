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
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Repository
public class VoteDaoImpl implements VoteDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SimpleJdbcCall jdbcCall;

    @Autowired
    private UserDao userDao;
    @Autowired
    private SnippetDao snippetDao;

    private final RowMapper<Vote> ROW_MAPPER = new RowMapper<Vote>() {


        @Override public Vote mapRow(ResultSet rs, int rowNum) throws SQLException {
            Optional<User> user = userDao.findUserById(rs.getLong("user_id"));
            Optional<Snippet> snippet = snippetDao.findSnippetById(rs.getLong("snippet_id"));
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
        jdbcCall = new SimpleJdbcCall(ds);
    }

    @Override
    public Collection<Vote> getUserVotes(long userId) {
        return jdbcTemplate.query("SELECT * FROM votes_for WHERE user_id = ?", ROW_MAPPER, userId);
    }

    @Override
    public Optional<Vote> getVote(long userId, long snippetId) {
        return jdbcTemplate.query("SELECT * FROM votes_for WHERE user_id = ? AND snippet_id = ?", ROW_MAPPER, userId, snippetId).stream().findFirst();
    }

    @Override
    @Transactional
    public void performVote(long userId, long snippetId, int voteType) {
        // upsert
        String upsert = "INSERT INTO votes_for VALUES(" + userId + ", " + snippetId+ ", " + voteType + ") " +
                "ON CONFLICT ON CONSTRAINT one_snippet_one_vote DO UPDATE " +
                "SET type = " + voteType;
        jdbcTemplate.execute(upsert);
    }
}

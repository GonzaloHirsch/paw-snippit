package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.FavoriteDao;
import ar.edu.itba.paw.interfaces.dao.SnippetDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.models.Favorite;

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
public class FavoriteDaoImpl implements FavoriteDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SimpleJdbcCall jdbcCall;

    @Autowired
    private UserDao userDao;
    @Autowired
    private SnippetDao snippetDao;

    private final RowMapper<Favorite> ROW_MAPPER = new RowMapper<Favorite>() {


        @Override public Favorite mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Favorite(
                    rs.getLong("user_id"),
                    rs.getLong("snippet_id")
            );
        }
    };

    @Autowired
    public FavoriteDaoImpl (DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);

        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("favorites");
        jdbcCall = new SimpleJdbcCall(ds);
    }

    @Override
    public Collection<Favorite> getUserFavorites(long userId) {
        return jdbcTemplate.query("SELECT * FROM favorites WHERE user_id = ?", ROW_MAPPER, userId);
    }

    @Override
    public Optional<Favorite> getFavorite(long userId, long snippetId) {
        return jdbcTemplate.query("SELECT * FROM favorites WHERE user_id = ? AND snippet_id = ?", ROW_MAPPER, userId, snippetId).stream().findFirst();
    }

    @Override
    @Transactional
    public void addToFavorites(long userId, long snippetId) {
        jdbcTemplate.execute("INSERT INTO favorites (user_id, snippet_id) VALUES(" + userId + ", " + snippetId+ ") ");
    }

    @Override
    public void removeFromFavorites(long userId, long snippetId) {
        jdbcTemplate.execute("DELETE FROM favorites WHERE user_id = " + userId + " AND snippet_id = " + snippetId);
    }

}
package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.FavoriteDao;
import ar.edu.itba.paw.models.Favorite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class FavoriteDaoImpl implements FavoriteDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final RowMapper<Favorite> ROW_MAPPER = (rs, rowNum) -> new Favorite(
            rs.getLong("user_id"),
            rs.getLong("snippet_id")
    );

    @Autowired
    public FavoriteDaoImpl (DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName("favorites");
    }

    @Override
    public Collection<Favorite> getUserFavorites(long userId) {
        return this.jdbcTemplate.query("SELECT * FROM favorites WHERE user_id = ?", ROW_MAPPER, userId);
    }

    @Override
    public Optional<Favorite> getFavorite(long userId, long snippetId) {
        return this.jdbcTemplate.query("SELECT * FROM favorites WHERE user_id = ? AND snippet_id = ?", ROW_MAPPER, userId, snippetId).stream().findFirst();
    }

    @Override
    public void addToFavorites(long userId, long snippetId) {
        // Creating the map for the object
        if (!getFavorite(userId, snippetId).isPresent()) {
            final Map<String, Object> args = new HashMap<>();
            args.put("user_id", userId);
            args.put("snippet_id", snippetId);
            // Executing the insert
            this.jdbcInsert.execute(args);
        }
    }

    @Override
    @Transactional
    public void removeFromFavorites(long userId, long snippetId) {
        Object[] args = new Object[] {userId, snippetId};
        this.jdbcTemplate.update("DELETE FROM favorites WHERE user_id = ? AND snippet_id = ?", args);
    }

}
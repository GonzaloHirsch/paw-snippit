package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.LanguageDao;
import ar.edu.itba.paw.models.Language;
import ar.edu.itba.paw.models.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class LanguageDaoImpl implements LanguageDao {

    private JdbcTemplate jdbcTemplate;
    private final static RowMapper<Language> ROW_MAPPER = new RowMapper<Language>(){

        @Override
        public Language mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Language(rs.getInt("id"), rs.getString("name"));
        }
    };

    @Autowired
    public LanguageDaoImpl(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate((dataSource));
    }

    @Override
    public Optional<Language> findById(final long id) {
        return jdbcTemplate.query("SELECT * FROM languages WHERE id = ?", ROW_MAPPER, id)
                .stream().findFirst();
    }
}

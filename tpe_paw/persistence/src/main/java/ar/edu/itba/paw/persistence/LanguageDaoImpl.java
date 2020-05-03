package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.LanguageDao;
import ar.edu.itba.paw.models.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class LanguageDaoImpl implements LanguageDao {

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Language> ROW_MAPPER = new RowMapper<Language>(){

        @Override
        public Language mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Language(rs.getInt("id"), rs.getString("name"));
        }
    };

    @Autowired
    public LanguageDaoImpl(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate((dataSource));
        this.jdbcInsert = new SimpleJdbcInsert(this.jdbcTemplate).withTableName("languages").usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Language> findById(final long id) {
        return jdbcTemplate.query("SELECT * FROM languages WHERE id = ?", ROW_MAPPER, id)
                .stream().findFirst();
    }

    @Override
    public Optional<Language> findByName(final String name) {
        return jdbcTemplate.query("SELECT * FROM languages WHERE LOWER(name) = LOWER(?)", ROW_MAPPER, name)
                .stream().findFirst();
    }

    @Override
    public Collection<Language> getAll() {
        return jdbcTemplate.query("SELECT * FROM languages", ROW_MAPPER);
    }

    @Override
    public Language addLanguage(String lang) {
        final Map<String, Object> args = new HashMap<>();
        args.put("name", lang);
        final Number langId = jdbcInsert.executeAndReturnKey(args);
        return new Language(langId.longValue(), lang);
    }

    @Override
    public void addLanguages(List<String> languages) {
        // TODO -> remove repeated
        List<MapSqlParameterSource> entries = new ArrayList<>();

        for (String l : languages) {
            if (l != null && l.compareTo("") != 0) {
                MapSqlParameterSource entry = new MapSqlParameterSource()
                        .addValue("name", l.toLowerCase());
                entries.add(entry);
            }
        }

        MapSqlParameterSource[] array = entries.toArray(new MapSqlParameterSource[entries.size()]);
        jdbcInsert.executeBatch(array);
    }

    // TODO Transactional
    @Override
    public void removeLanguage(final long langId) {
        if (!this.languageInUse(langId)) {
            jdbcTemplate.update("DELETE FROM languages WHERE id = ?", new Object[]{langId});
        }
    }

    @Override
    public boolean languageInUse(final long langId) {
        return jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT s.id) FROM complete_snippets AS s WHERE s.language_id = ?", new Object[]{langId}, Integer.class) != 0;
    }

}

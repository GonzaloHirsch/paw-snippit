package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.LanguageDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.models.Language;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static junit.framework.TestCase.*;
import static ar.edu.itba.paw.persistence.TestHelper.*;

/*
 Tested Methods:
    Language addLanguage(String lang);
    void addLanguages(List<String> languages);
    Collection<Language> getAll();
    Optional<Language> findById(long id);
    Optional<Language> findByName(String name);
 Not Tested Methods:

 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class LanguageDaoTest {

    @Autowired private DataSource ds;
    @Autowired private LanguageDao languageDao;

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertLanguage;


    private final static RowMapper<Language> ROW_MAPPER = (rs, rowNum) -> new Language(rs.getInt("id"), rs.getString("name"));


    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertLanguage = new SimpleJdbcInsert(ds).withTableName(LANGUAGES_TABLE).usingGeneratedKeyColumns("id");
    }

    

    @Test
    public void testAddLanguage(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,LANGUAGES_TABLE);

        Language maybeLanguage = languageDao.addLanguage(LANGUAGE);

        assertNotNull(maybeLanguage);
        assertEquals(LANGUAGE.toLowerCase(),maybeLanguage.getName());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate,  LANGUAGES_TABLE));
    }

    @Test
    public void testAddLanguageS(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,LANGUAGES_TABLE);
        List<String> stringList = Arrays.asList(LANGUAGE,LANGUAGE2,LANGUAGE3,null);

        languageDao.addLanguages(stringList);

        Collection<Language> maybeLanguages = jdbcTemplate.query("SELECT * FROM languages", ROW_MAPPER);
        assertEquals(3,JdbcTestUtils.countRowsInTable(jdbcTemplate,LANGUAGES_TABLE));
        assertFalse(maybeLanguages.isEmpty());
        List<String> maybeStringList = new ArrayList<String>();
        for(Language l:maybeLanguages){
            maybeStringList.add(l.getName());
        }
        assertTrue(maybeStringList.contains(LANGUAGE.toLowerCase()));
        assertTrue(maybeStringList.contains(LANGUAGE2.toLowerCase()));
        assertTrue(maybeStringList.contains(LANGUAGE3.toLowerCase()));
    }
    
    @Test 
    public void testGetAll(){ 
        JdbcTestUtils.deleteFromTables(jdbcTemplate,LANGUAGES_TABLE);
        long lanId1 = insertLanguageIntoDb(jdbcInsertLanguage, LANGUAGE);
        long lanId2 = insertLanguageIntoDb(jdbcInsertLanguage,LANGUAGE2);
        
        Collection<Language> maybeLanguages = languageDao.getAll();
        
        assertFalse(maybeLanguages.isEmpty());
        assertEquals(2,maybeLanguages.size());
        List<Long> maybeIdList = new ArrayList<Long>();
        for(Language l:maybeLanguages){
            maybeIdList.add(l.getId());
        }
        assertTrue(maybeIdList.contains(lanId1));
        assertTrue(maybeIdList.contains(lanId2));
    }

    @Test
    public void testFindById(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,LANGUAGES_TABLE);
        long lanId1 = insertLanguageIntoDb(jdbcInsertLanguage,LANGUAGE);

        Optional<Language> maybeLanguage = languageDao.findById(lanId1);

        assertTrue(maybeLanguage.isPresent());
        assertEquals(lanId1,maybeLanguage.get().getId());
        assertEquals(LANGUAGE,maybeLanguage.get().getName());
    }

    @Test
    public void testFindByName(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,LANGUAGES_TABLE);
        long lanId1 = insertLanguageIntoDb(jdbcInsertLanguage,LANGUAGE);

        Optional<Language> maybeLanguage = languageDao.findByName(LANGUAGE);

        assertTrue(maybeLanguage.isPresent());
        assertEquals(lanId1,maybeLanguage.get().getId());
        assertEquals(LANGUAGE,maybeLanguage.get().getName());
    }




}

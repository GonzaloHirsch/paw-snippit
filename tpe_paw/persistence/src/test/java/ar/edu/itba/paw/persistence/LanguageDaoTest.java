package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.LanguageDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.models.Language;
import ar.edu.itba.paw.models.Tag;
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
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static junit.framework.TestCase.*;
import static ar.edu.itba.paw.persistence.TestHelper.*;
import static org.junit.Assert.assertTrue;

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

        JdbcTestUtils.deleteFromTables(jdbcTemplate,LANGUAGES_TABLE);
    }

 /*   @Test
    public void testAddLanguage(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,LANGUAGES_TABLE);

        Language maybeLanguage = languageDao.addLanguage(LANGUAGE);

        assertNotNull(maybeLanguage);
        assertEquals(LANGUAGE.toLowerCase(),maybeLanguage.getName());
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate,  LANGUAGES_TABLE));
    }*/

/*    @Test
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
    }*/

    @Test
    public void testAddLanguageSEmpty(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,LANGUAGES_TABLE);
        List<String> stringList = new ArrayList<>();

        languageDao.addLanguages(stringList);

        assertEquals(0,JdbcTestUtils.countRowsInTable(jdbcTemplate,LANGUAGES_TABLE));
    }
    
    @Test 
    public void testGetAll(){ 
        JdbcTestUtils.deleteFromTables(jdbcTemplate,LANGUAGES_TABLE);
        long lanId1 = insertLanguageIntoDb(jdbcInsertLanguage, LANGUAGE);
        long lanId2 = insertLanguageIntoDb(jdbcInsertLanguage,LANGUAGE2);
        
        Collection<Language> maybeLanguages = languageDao.getAllLanguages();
        
        assertFalse(maybeLanguages.isEmpty());
        assertEquals(2,maybeLanguages.size());
        List<Long> maybeIdList = maybeLanguages.stream().mapToLong(Language::getId).boxed().collect(Collectors.toList());
        assertTrue(maybeIdList.contains(lanId1));
        assertTrue(maybeIdList.contains(lanId2));
    }

    @Test
    public void testGetAllEmpty(){
        Collection<Language> maybeLanguages = languageDao.getAllLanguages();

        assertTrue(maybeLanguages.isEmpty());

    }

    @Test
    public void testFindById(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,LANGUAGES_TABLE);
        long lanId1 = insertLanguageIntoDb(jdbcInsertLanguage,LANGUAGE);

        Optional<Language> maybeLanguage = languageDao.findById(lanId1);

        assertTrue(maybeLanguage.isPresent());
//        assertEquals(lanId1,maybeLanguage.get().getId());
        assertEquals(LANGUAGE,maybeLanguage.get().getName());
    }

    @Test
    public void testFindByIdEmpty(){
        Optional<Language> maybeLanguage = languageDao.findById(10);

        assertFalse(maybeLanguage.isPresent());
    }

    @Test
    public void testFindByName(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate,LANGUAGES_TABLE);
        long lanId1 = insertLanguageIntoDb(jdbcInsertLanguage,LANGUAGE);

        Optional<Language> maybeLanguage = languageDao.findByName(LANGUAGE);

        assertTrue(maybeLanguage.isPresent());
//        assertEquals(lanId1,maybeLanguage.get().getId());
        assertEquals(LANGUAGE,maybeLanguage.get().getName());
    }

    @Test
    public void testFindByNameEmpty(){
        Optional<Language> maybeLanguage = languageDao.findByName(LANGUAGE);

        assertFalse(maybeLanguage.isPresent());
    }

    @Test
    public void testFindAllLanguagesByName(){
        long lanId1 = insertLanguageIntoDb(jdbcInsertLanguage, LANGUAGE);
        long lanId2 = insertLanguageIntoDb(jdbcInsertLanguage,LANGUAGE2);

        Collection<Language> maybeCollection = languageDao.findAllLanguagesByName("Language",1,PAGE_SIZE);

        assertNotNull(maybeCollection);
        List<Long> maybeIdList = maybeCollection.stream().mapToLong(Language::getId).boxed().collect(Collectors.toList());
        assertTrue(maybeIdList.contains(lanId1));
        assertTrue(maybeIdList.contains(lanId2));
    }

    @Test
    public void testFindAllLanguagesByNameEmpty(){
        long lanId1 = insertLanguageIntoDb(jdbcInsertLanguage, LANGUAGE);
        long lanId2 = insertLanguageIntoDb(jdbcInsertLanguage,LANGUAGE2);

        Collection<Language> maybeCollection = languageDao.findAllLanguagesByName("zzzz",1,PAGE_SIZE);

        assertNotNull(maybeCollection);
        assertEquals(0,maybeCollection.size());
    }

   /* @Test
    public void testRemoveLanguage(){
        long lanId1 = insertLanguageIntoDb(jdbcInsertLanguage, LANGUAGE);

        languageDao.removeLanguage(lanId1);

        assertEquals(0,JdbcTestUtils.countRowsInTable(jdbcTemplate,LANGUAGES_TABLE));
    }*/

    @Test
    public void testRemoveLanguageEmpty(){
        long lanId1 = insertLanguageIntoDb(jdbcInsertLanguage, LANGUAGE);

        languageDao.removeLanguage(lanId1+10);

        assertEquals(1,JdbcTestUtils.countRowsInTable(jdbcTemplate,LANGUAGES_TABLE));
    }

  /*  @Test
    public void testLanguageInUse(){
        long lanId1 = insertLanguageIntoDb(jdbcInsertLanguage, LANGUAGE);

        boolean result = languageDao.languageInUse(lanId1);

        assertFalse(result);
    }*/

    @Test
    public void testLanguageInUseEmpty(){
        long lanId1 = insertLanguageIntoDb(jdbcInsertLanguage, LANGUAGE);

        boolean result = languageDao.languageInUse(lanId1+10);

        assertFalse(result);
    }






}

package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.LanguageDao;
import ar.edu.itba.paw.models.Language;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class LanguageServiceImplTest {

    private static final Long ID = 1L;
    private static final String NAME = "JAVA";

    @InjectMocks
    private LanguageServiceImpl languageService = new LanguageServiceImpl();

    @Mock
    private LanguageDao mockLanguageDao;

    @Test
    public void testLanguageExistsByIdNotDeleted() {
        // 1. Setup!
        Mockito.when(mockLanguageDao.findById(Mockito.eq(ID))).thenReturn(Optional.of(new Language(ID, NAME, false)));

        // 2. Execute
        boolean exists = languageService.languageExists(ID);

        // 3. Asserts!
        Assert.assertTrue(exists);
    }

    @Test
    public void testLanguageExistsByIdDeleted() {
        // 1. Setup!
        Mockito.when(mockLanguageDao.findById(Mockito.eq(ID))).thenReturn(Optional.of(new Language(ID, NAME, true)));

        // 2. Execute
        boolean exists = languageService.languageExists(ID);

        // 3. Asserts!
        Assert.assertFalse(exists);
    }

    @Test
    public void testLanguageExistsByIdEmpty() {
        // 1. Setup!
        Mockito.when(mockLanguageDao.findById(Mockito.eq(ID))).thenReturn(Optional.empty());

        // 2. Execute
        boolean exists = languageService.languageExists(ID);

        // 3. Asserts!
        Assert.assertFalse(exists);
    }

    @Test
    public void testLanguageExistsByNameNotDeleted() {
        // 1. Setup!
        Mockito.when(mockLanguageDao.findByName(Mockito.eq(NAME))).thenReturn(Optional.of(new Language(ID, NAME, false)));

        // 2. Execute
        boolean exists = languageService.languageExists(NAME);

        // 3. Asserts!
        Assert.assertTrue(exists);
    }

    @Test
    public void testLanguageExistsByNameDeleted() {
        // 1. Setup!
        Mockito.when(mockLanguageDao.findByName(Mockito.eq(NAME))).thenReturn(Optional.of(new Language(ID, NAME, true)));

        // 2. Execute
        boolean exists = languageService.languageExists(NAME);

        // 3. Asserts!
        Assert.assertFalse(exists);
    }

    @Test
    public void testLanguageExistsByNameEmpty() {
        // 1. Setup!
        Mockito.when(mockLanguageDao.findByName(Mockito.eq(NAME))).thenReturn(Optional.empty());

        // 2. Execute
        boolean exists = languageService.languageExists(NAME);

        // 3. Asserts!
        Assert.assertFalse(exists);
    }
}

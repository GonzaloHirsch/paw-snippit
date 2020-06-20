package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.ReportDao;
import ar.edu.itba.paw.models.Language;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Collections;
import java.util.Locale;

@RunWith(MockitoJUnitRunner.class)
public class ReportServiceImplTest {

    private static final Long SNIPPET_ID = 10L;
    private static final String TITLE = "titles";
    private static final String CODE = "codes!";
    private static final String DESCR = "Description";
    private static final String LANGUAGE = "Java";

    private static final Long CURRENT_USER_ID = 1L;
    private static final Long OWNER_ID = 2L;
    private static final String OWNER_USERNAME = "owner";
    private static final String OWNER_EMAIL = "email@email.com";
    private static final String CURRENT_EMAIL = "email2@email.com";
    private static final String CURRENT_USERNAME = "current";
    private static final String PASSWORD = "password";

    @InjectMocks
    private ReportServiceImpl reportService = new ReportServiceImpl();

    @Mock
    private ReportDao mockReportDao;

    @Test
    public void testShowReportedWarningDifferentUser() {
        // 1. Setup!
        User owner = new User(OWNER_ID, OWNER_USERNAME, PASSWORD, OWNER_EMAIL, Instant.now(), Locale.ENGLISH, false);
        User currentUser = new User(CURRENT_USER_ID, CURRENT_USERNAME, PASSWORD, CURRENT_EMAIL, Instant.now(), Locale.ENGLISH, false);
        Snippet snippet = new Snippet(SNIPPET_ID, owner, CODE, TITLE, DESCR, Instant.now(), new Language(LANGUAGE), Collections.emptyList(), false, false);

        Mockito.when(mockReportDao.isReportedAndNotDismissed(Mockito.eq(SNIPPET_ID))).thenReturn(true);

        // 2. Execute
        boolean showWarning = reportService.showReportedWarning(snippet, currentUser);

        // 3. Asserts!
        Assert.assertFalse(showWarning);
    }

    @Test
    public void testShowReportedWarningSnipDeleted() {
        // 1. Setup!
        User owner = new User(OWNER_ID, OWNER_USERNAME, PASSWORD, OWNER_EMAIL, Instant.now(), Locale.ENGLISH, false);
        Snippet snippet = new Snippet(SNIPPET_ID, owner, CODE, TITLE, DESCR, Instant.now(), new Language(LANGUAGE), Collections.emptyList(), false, true);

        Mockito.when(mockReportDao.isReportedAndNotDismissed(Mockito.eq(SNIPPET_ID))).thenReturn(true);

        // 2. Execute
        boolean showWarning = reportService.showReportedWarning(snippet, owner);

        // 3. Asserts!
        Assert.assertFalse(showWarning);
    }

    @Test
    public void testShowReportedWarningSnipFlagged() {
        // 1. Setup!
        User owner = new User(OWNER_ID, OWNER_USERNAME, PASSWORD, OWNER_EMAIL, Instant.now(), Locale.ENGLISH, false);
        Snippet snippet = new Snippet(SNIPPET_ID, owner, CODE, TITLE, DESCR, Instant.now(), new Language(LANGUAGE), Collections.emptyList(), true, false);

        Mockito.when(mockReportDao.isReportedAndNotDismissed(Mockito.eq(SNIPPET_ID))).thenReturn(true);

        // 2. Execute
        boolean showWarning = reportService.showReportedWarning(snippet, owner);

        // 3. Asserts!
        Assert.assertFalse(showWarning);
    }

    @Test
    public void testShowReportedWarningNoCurrentUser() {
        // 1. Setup!
        User owner = new User(OWNER_ID, OWNER_USERNAME, PASSWORD, OWNER_EMAIL, Instant.now(), Locale.ENGLISH, false);
        Snippet snippet = new Snippet(SNIPPET_ID, owner, CODE, TITLE, DESCR, Instant.now(), new Language(LANGUAGE), Collections.emptyList(), false, true);

        Mockito.when(mockReportDao.isReportedAndNotDismissed(Mockito.eq(SNIPPET_ID))).thenReturn(true);

        // 2. Execute
        boolean showWarning = reportService.showReportedWarning(snippet, null);

        // 3. Asserts!
        Assert.assertFalse(showWarning);
    }

    @Test
    public void testShowReportedWarningTrue() {
        // 1. Setup!
        User owner = new User(OWNER_ID, OWNER_USERNAME, PASSWORD, OWNER_EMAIL, Instant.now(), Locale.ENGLISH, false);
        Snippet snippet = new Snippet(SNIPPET_ID, owner, CODE, TITLE, DESCR, Instant.now(), new Language(LANGUAGE), Collections.emptyList(), false, false);

        Mockito.when(mockReportDao.isReportedAndNotDismissed(Mockito.eq(SNIPPET_ID))).thenReturn(true);

        // 2. Execute
        boolean showWarning = reportService.showReportedWarning(snippet, owner);

        // 3. Asserts!
        Assert.assertTrue(showWarning);
    }
}

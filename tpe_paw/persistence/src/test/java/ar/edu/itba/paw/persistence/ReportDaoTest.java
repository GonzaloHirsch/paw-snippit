package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.ReportDao;
import ar.edu.itba.paw.models.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ReportDaoTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ReportDao reportDao;

    private User reporter;
    private User owner;
    private Snippet snippet;


    @Before
    public void setUp() {
        owner = TestMethods.insertUser(em, TestConstants.USER_USERNAME, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL, TestConstants.USER_DATE, TestConstants.LOCALE_EN, TestConstants.USER_VERIFIED);
        reporter = TestMethods.insertUser(em, TestConstants.USER_USERNAME2, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL2, TestConstants.USER_DATE, TestConstants.LOCALE_EN, TestConstants.USER_VERIFIED);
        snippet = TestMethods.insertSnippet(em, owner, TestConstants.SNIPPET_TITLE, TestConstants.SNIPPET_DESCR, TestConstants.SNIPPET_CODE, Instant.now(), TestMethods.insertLanguage(em, TestConstants.LANGUAGE), Collections.emptyList(), false, false);
    }

    @Test
    public void testGetExistingReport() {
        TestMethods.insertReport(em, reporter, snippet, TestConstants.REPORT_DETAIL, false);

        Optional<Report> maybeReport = this.reportDao.getReport(reporter.getId(), snippet.getId());
        Assert.assertTrue(maybeReport.isPresent());
        Assert.assertEquals(reporter, maybeReport.get().getReportedBy());
        Assert.assertEquals(snippet, maybeReport.get().getSnippet());
        Assert.assertEquals(TestConstants.REPORT_DETAIL, maybeReport.get().getDetail());
        Assert.assertFalse(maybeReport.get().isOwnerDismissed());
    }

    @Test
    public void testGetNonExistingReport() {
        TestMethods.insertReport(em, reporter, snippet, TestConstants.REPORT_DETAIL, false);

        Optional<Report> maybeReport = this.reportDao.getReport(owner.getId(), snippet.getId());
        Assert.assertFalse(maybeReport.isPresent());
    }

    @Test
    public void testNewReportSnippet() {
        int beforeReport = TestMethods.countRows(em, TestConstants.REPORTED_TABLE);
        Assert.assertTrue(this.reportDao.reportSnippet(reporter.getId(), snippet.getId(), TestConstants.REPORT_DETAIL));
        int afterReport = TestMethods.countRows(em, TestConstants.REPORTED_TABLE);
        Assert.assertEquals(0, beforeReport);
        Assert.assertEquals(1, afterReport);
    }

    @Test
    public void testExistingReportSnippet() {
        TestMethods.insertReport(em, reporter, snippet, TestConstants.REPORT_DETAIL, false);
        int beforeReport = TestMethods.countRows(em, TestConstants.REPORTED_TABLE);
        Assert.assertFalse(this.reportDao.reportSnippet(reporter.getId(), snippet.getId(), TestConstants.REPORT_DETAIL));
        int afterReport = TestMethods.countRows(em, TestConstants.REPORTED_TABLE);
        Assert.assertEquals(1, beforeReport);
        Assert.assertEquals(1, afterReport);
    }

    @Test
    public void testReportSnippetInvalidReporter() {
        int beforeReport = TestMethods.countRows(em, TestConstants.REPORTED_TABLE);
        Assert.assertFalse(this.reportDao.reportSnippet(TestConstants.USER_INVALID_ID, snippet.getId(), TestConstants.REPORT_DETAIL));
        int afterReport = TestMethods.countRows(em, TestConstants.REPORTED_TABLE);
        Assert.assertEquals(0, beforeReport);
        Assert.assertEquals(0, afterReport);
    }

    @Test
    public void testExistingReportSnippetInvlaidSnippet() {
        int beforeReport = TestMethods.countRows(em, TestConstants.REPORTED_TABLE);
        Assert.assertFalse(this.reportDao.reportSnippet(reporter.getId(), TestConstants.SNIPPET_INVALID_ID, TestConstants.REPORT_DETAIL));
        int afterReport = TestMethods.countRows(em, TestConstants.REPORTED_TABLE);
        Assert.assertEquals(0, beforeReport);
        Assert.assertEquals(0, afterReport);
    }

    @Test
    public void testDismissReportsForSnippet() {
        User user2 = TestMethods.insertUser(em, TestConstants.USER_USERNAME3, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL3, TestConstants.USER_DATE, TestConstants.LOCALE_EN, TestConstants.USER_VERIFIED);
        Report r1 = TestMethods.insertReport(em, reporter, snippet, TestConstants.REPORT_DETAIL, false);
        Report r2 = TestMethods.insertReport(em, user2, snippet, TestConstants.REPORT_DETAIL, false);

        this.reportDao.dismissReportsForSnippet(snippet.getId());
        Assert.assertTrue(r1.isOwnerDismissed());
        Assert.assertTrue(r2.isOwnerDismissed());
    }

    @Test
    public void testDismissReportsForSnippetNewReports() {
        User user2 = TestMethods.insertUser(em, TestConstants.USER_USERNAME3, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL3, TestConstants.USER_DATE, TestConstants.LOCALE_EN, TestConstants.USER_VERIFIED);
        Report r1 = TestMethods.insertReport(em, reporter, snippet, TestConstants.REPORT_DETAIL, false);

        this.reportDao.dismissReportsForSnippet(snippet.getId());
        Report r2 = TestMethods.insertReport(em, user2, snippet, TestConstants.REPORT_DETAIL, false);
        Assert.assertTrue(r1.isOwnerDismissed());
        Assert.assertFalse(r2.isOwnerDismissed());
    }

    @Test
    public void testIsReportedAndNotDismissedAllNotDismissed() {
        TestMethods.insertReport(em, reporter, snippet, TestConstants.REPORT_DETAIL, false);
        Assert.assertTrue(this.reportDao.isReportedAndNotDismissed(snippet.getId()));
    }

    @Test
    public void testIsReportedAndNotDismissedAllDismissed() {
        User user2 = TestMethods.insertUser(em, TestConstants.USER_USERNAME3, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL3, TestConstants.USER_DATE, TestConstants.LOCALE_EN, TestConstants.USER_VERIFIED);
        TestMethods.insertReport(em, user2, snippet, TestConstants.REPORT_DETAIL, true);
        TestMethods.insertReport(em, reporter, snippet, TestConstants.REPORT_DETAIL, true);
        Assert.assertFalse(this.reportDao.isReportedAndNotDismissed(snippet.getId()));
    }

    @Test
    public void testIsReportedAndNotDismissedSomeDismissed() {
        User user2 = TestMethods.insertUser(em, TestConstants.USER_USERNAME3, TestConstants.USER_PASSWORD, TestConstants.USER_EMAIL3, TestConstants.USER_DATE, TestConstants.LOCALE_EN, TestConstants.USER_VERIFIED);
        TestMethods.insertReport(em, user2, snippet, TestConstants.REPORT_DETAIL, false);
        TestMethods.insertReport(em, reporter, snippet, TestConstants.REPORT_DETAIL, true);
        Assert.assertTrue(this.reportDao.isReportedAndNotDismissed(snippet.getId()));
    }

    @Test
    public void testIsReportedAndNotDismissedNoReport() {
        Assert.assertFalse(this.reportDao.isReportedAndNotDismissed(snippet.getId()));
    }

    @Test
    public void testIsReportedAndNotDismissedInvalidSnippet() {
        Assert.assertFalse(this.reportDao.isReportedAndNotDismissed(TestConstants.SNIPPET_INVALID_ID));
    }
}

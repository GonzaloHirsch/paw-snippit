package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.ReportDao;
import ar.edu.itba.paw.interfaces.service.EmailService;
import ar.edu.itba.paw.interfaces.service.ReportService;
import ar.edu.itba.paw.models.Report;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportDao reportDao;
    @Autowired
    private EmailService emailService;

    @Override
    public Optional<Report> getReport(long userId, long snippetId){
        return this.reportDao.getReport(userId,snippetId);
    }

    @Transactional
    @Override
    public boolean reportSnippet(User user, Snippet snippet, String reportDetail, String baseUrl){
        boolean result = this.reportDao.reportSnippet(user.getId(), snippet.getId(), reportDetail);

        if(result) {
            this.emailService.sendReportedEmail(
                    baseUrl + "/snippet/" + snippet.getId(),
                    snippet.getTitle(),
                    snippet.getOwner().getEmail(),
                    snippet.getOwner().getUsername(),
                    reportDetail,
                    user.getUsername(),
                    user.getLocale());
        }

        return result;
    }

    @Override
    public boolean canReport(User user) {
        return user.getReputation() >= MIN_REPUTATION_TO_REPORT;
    }

    /*
     * Want reported warning to show only for the snippet owner and when it is valuable.
     * When the snippet has been deleted or flagged, the warning is no longer relevant.
     */
    @Override
    public boolean showReportedWarning(Snippet snippet, User currentUser) {
        boolean isReported = this.reportDao.isReportedAndNotDismissed(snippet.getId());
        return currentUser != null && currentUser.equals(snippet.getOwner()) && isReported && !snippet.isDeleted() && !snippet.isFlagged();
    }

    @Override
    @Transactional
    public void dismissReportsForSnippet(long snippetId) {
        this.reportDao.dismissReportsForSnippet(snippetId);
    }
}

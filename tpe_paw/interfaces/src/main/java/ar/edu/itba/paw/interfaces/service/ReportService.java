package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Report;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface ReportService {

    int MIN_REPUTATION_TO_REPORT = 5;

    Optional<Report> getReport(long userId, long snippetId);
    boolean reportSnippet(User user, Snippet snippet, String reportDetail, String baseUrl);
    boolean canReport(User user);
    boolean showReportedWarning(Snippet snippet, User currentUser);
    void dismissReportsForSnippet(long snippetId);
}

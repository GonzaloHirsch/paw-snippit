package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Report;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface ReportService {
    Optional<Report> getReport(long userId, long snippetId);
    boolean reportSnippet(User user, Snippet snippet, String reportDetail, String baseUrl);

}

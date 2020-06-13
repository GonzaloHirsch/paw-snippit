package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Report;

import java.util.Optional;

public interface ReportService {
    Optional<Report> getReport(long userId, long snippetId);
    boolean reportSnippet(Long userId, Long snippetId, String reportDetail);

}

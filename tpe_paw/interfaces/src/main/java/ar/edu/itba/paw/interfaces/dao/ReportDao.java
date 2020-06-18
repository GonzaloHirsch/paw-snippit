package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Report;
import ar.edu.itba.paw.models.Vote;

import java.util.Optional;

public interface ReportDao {

    boolean reportSnippet(long userId, long snippetId, String reportDetail);
    Optional<Report> getReport(long userId, long snippetId);
    boolean isReported(long snippetId);
    void dismissReportsForSnippet(long snippetId);
}

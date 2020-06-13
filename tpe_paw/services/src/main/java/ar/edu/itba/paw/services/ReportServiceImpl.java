package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.ReportDao;
import ar.edu.itba.paw.interfaces.service.EmailService;
import ar.edu.itba.paw.interfaces.service.ReportService;
import ar.edu.itba.paw.models.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportDao reportDao;

    @Override
    public Optional<Report> getReport(long userId, long snippetId){
        return this.reportDao.getReport(userId,snippetId);
    }

    @Transactional
    @Override
    public boolean reportSnippet(Long userId, Long snippetId, String reportDetail){
        // Mailing stuff

        return this.reportDao.reportSnippet(userId, snippetId, reportDetail);
    }
}

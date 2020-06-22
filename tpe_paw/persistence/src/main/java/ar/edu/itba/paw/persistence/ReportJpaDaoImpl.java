package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.ReportDao;
import ar.edu.itba.paw.models.Report;
import ar.edu.itba.paw.models.Snippet;
import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
public class ReportJpaDaoImpl implements ReportDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Report> getReport(long userId, long snippetId) {
        final TypedQuery<Report> query = this.em.createQuery("from Report as r where r.reportedBy.id = :user_id and r.snippet.id = :snippet_id", Report.class)
                .setParameter("user_id", userId)
                .setParameter("snippet_id",snippetId);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public boolean isReportedAndNotDismissed(long snippetId) {
        final TypedQuery<Report> query = this.em.createQuery("from Report as r where r.snippet.id = :snippet_id", Report.class)
                .setParameter("snippet_id",snippetId);
        return query.getResultList().stream().anyMatch(report -> !report.isOwnerDismissed());
    }

    @Override
    public boolean reportSnippet(long userId, long snippetId, String detail){
        Optional<Report> maybeReport = this.getReport(userId,snippetId);
        if(!maybeReport.isPresent()){
            User user = this.em.find(User.class, userId);
            Snippet snippet = this.em.find(Snippet.class, snippetId);

            if(user != null && snippet !=null){
                Report report = new Report(user, snippet, detail, false);
                report.addToLists();
                this.em.persist(report);
                return true;
            }
        }
        return false;
    }

    @Override
    public void dismissReportsForSnippet(long snippetId){
        final TypedQuery<Report> query = this.em.createQuery("from Report as r where r.snippet.id = :snippet_id", Report.class)
                .setParameter("snippet_id",snippetId);

        for (Report report : query.getResultList()) {
            report.setOwnerDismissed(true);
            em.persist(report);
        }
    }
}

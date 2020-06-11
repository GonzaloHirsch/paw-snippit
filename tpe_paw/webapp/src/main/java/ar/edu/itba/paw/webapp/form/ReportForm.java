package ar.edu.itba.paw.webapp.form;

public class ReportForm {

    private boolean reported;

    private String reportDetail;


    public boolean isReported() {
        return reported;
    }

    public void setReported(boolean reported) {
        reported = reported;
    }

    public String getReportDetail() {
        return reportDetail;
    }

    public void setReportDetail(String reportDetail) {
        this.reportDetail = reportDetail;
    }
}

package ar.edu.itba.paw.webapp.form;

public class ReportForm {

    private boolean isReported;

    private String reportDetail;


    public boolean isReported() {
        return isReported;
    }

    public void setReported(boolean reported) {
        isReported = reported;
    }

    public String getReportDetail() {
        return reportDetail;
    }

    public void setReportDetail(String reportDetail) {
        this.reportDetail = reportDetail;
    }
}

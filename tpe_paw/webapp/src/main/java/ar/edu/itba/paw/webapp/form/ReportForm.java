package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Size;

public class ReportForm {

    private boolean reported;

    @Size(max=500)
    private String reportDetail;

    public ReportForm (){

    }

    public boolean getReported() {
        return reported;
    }

    public void setReported(boolean reported) {
        this.reported = reported;
    }

    public String getReportDetail() {
        return reportDetail;
    }

    public void setReportDetail(String reportDetail) {
        this.reportDetail = reportDetail;
    }
}

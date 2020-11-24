package ar.edu.itba.paw.webapp.dto;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

public class ReportDto {
    @NotBlank
    @Size(max=300)
    private String reportDetail;

    public String getReportDetail() {
        return reportDetail;
    }

    public void setReportDetail(String reportDetail) {
        this.reportDetail = reportDetail;
    }
}

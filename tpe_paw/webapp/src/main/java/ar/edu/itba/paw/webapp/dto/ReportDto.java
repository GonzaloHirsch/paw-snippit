package ar.edu.itba.paw.webapp.dto;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

public class ReportDto {
    @NotBlank(message = "{NotBlank.reportForm.reportDetail}")
    @Size(max=300, message = "{Size.reportForm.reportDetail}")
    private String reportDetail;

    public String getReportDetail() {
        return reportDetail;
    }

    public void setReportDetail(String reportDetail) {
        this.reportDetail = reportDetail;
    }
}

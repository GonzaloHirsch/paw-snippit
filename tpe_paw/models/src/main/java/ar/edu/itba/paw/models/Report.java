package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "reported")
public class Report {

    /**
     * Method to set the Embedded Id of the Report object
     */
    @PrePersist
    private void prePersist() {
        if (this.id == null) {
            this.id = new ReportId(this.reportedBy.getId(), this.snippet.getId());
        }
    }

    @EmbeddedId
    private ReportId id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @MapsId("reporter_id")
    @JoinColumn(name = "reporter_id")
    private User reportedBy;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @MapsId("snippet_id")
    @JoinColumn(name = "snippet_id")
    private Snippet snippet;

    @Column(length = 310, name = "detail")
    private String detail;

    @Column(name="owner_dismissed")
    private boolean ownerDismissed;

    public Report(){
        // Hibernate constructor
    }

    public Report(User reportedBy, Snippet snippet, String detail, boolean ownerDismissed) {
        this.reportedBy = reportedBy;
        this.snippet = snippet;
        this.detail = detail;
        this.ownerDismissed = ownerDismissed;
    }

    public ReportId getId() {
        return id;
    }

    public void setId(ReportId id) {
        this.id = id;
    }

    public User getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(User reportedBy) {
        this.reportedBy = reportedBy;
    }

    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    /* Owner can dismiss reports if they do not think they are correct */
    public boolean isOwnerDismissed() {
        return this.ownerDismissed;
    }

    public void setOwnerDismissed(boolean dismissed) {
        this.ownerDismissed = dismissed;
    }

    public void addToLists() {
        this.snippet.getReports().add(this);
        this.reportedBy.getReports().add(this);
    }

    /**
     * Returns a brief description of this report. The exact details
     * of the representation are unspecified and subject to change,
     * but the following may be regarded as typical:
     *
     * "[Report: snippet=12, reportedBy=21]"
     */
    @Override public String toString() {
        return String.format("Report: snippet=%d, reportedBy=%d]", this.getSnippet().getId(), this.getReportedBy().getId());
    }
}

package ar.edu.itba.paw.models;

import javax.persistence.*;

@Entity
@Table(name = "reported")
public class Report {

    /**
     * Method to set the Embedded Id of the vote object
     */
    @PrePersist
    private void prePersist() {
        if (this.id == null) {
            this.id = new ReportId(this.user.getId(), this.snippet.getId());
        }
    }

    @EmbeddedId
    ReportId id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @MapsId("snippet_id")
    @JoinColumn(name = "snippet_id")
    private Snippet snippet;

    @Column(name = "detail")
    private String detail;

    public Report(){
        // Hibernate constructor
    }

    public Report(User user, Snippet snippet, String detail) {
        this.user = user;
        this.snippet = snippet;
        this.detail = detail;
    }

    public ReportId getId() {
        return id;
    }

    public void setId(ReportId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}

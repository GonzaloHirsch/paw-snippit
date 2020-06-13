package ar.edu.itba.paw.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ReportId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "snippet_id")
    private Long snippetId;

    public ReportId(){
        // Hibernate constructor
    }

    public ReportId(Long userId, Long snippetId){
        this.userId = userId;
        this.snippetId = snippetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportId reportId = (ReportId) o;
        return Objects.equals(userId, reportId.userId) &&
                Objects.equals(snippetId, reportId.snippetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, snippetId);
    }
}

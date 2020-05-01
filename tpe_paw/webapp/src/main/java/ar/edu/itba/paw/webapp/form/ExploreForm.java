package ar.edu.itba.paw.webapp.form;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

public class ExploreForm {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date minDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date maxDate;
    private Integer minRep;
    private Integer maxRep;
    private Integer minVotes;
    private Integer maxVotes;
    @Min(value=-1)
    private Long language;
    @Min(value=-1)
    private Long tag;
    @Size(max=50)
    private String username;
    @Size(max=50)
    private String title;
    private String sort;
    private String field;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getTag() {
        return tag;
    }

    public void setTag(Long tag) {
        this.tag = tag;
    }

    public Long getLanguage() {
        return language;
    }

    public void setLanguage(Long language) {
        this.language = language;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    public Integer getMinRep() {
        return minRep;
    }

    public void setMinRep(Integer minRep) {
        this.minRep = minRep;
    }

    public Integer getMaxRep() {
        return maxRep;
    }

    public void setMaxRep(Integer maxRep) {
        this.maxRep = maxRep;
    }

    public Integer getMinVotes() {
        return minVotes;
    }

    public void setMinVotes(Integer minVotes) {
        this.minVotes = minVotes;
    }

    public Integer getMaxVotes() {
        return maxVotes;
    }

    public void setMaxVotes(Integer maxVotes) {
        this.maxVotes = maxVotes;
    }
}

package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validations.BeforeToday;
import ar.edu.itba.paw.webapp.validations.DateOrder;
import ar.edu.itba.paw.webapp.validations.IntegerOrder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Date;

@DateOrder(min = "minDate", max = "maxDate", message = "{DateOrder.exploreForm.dates}")
@IntegerOrder(min = "minRep", max = "maxRep", message = "{IntegerOrder.exploreForm.order}")
@IntegerOrder(min = "minVotes", max = "maxVotes", message = "{IntegerOrder.exploreForm.order}")
public class ExploreForm {
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @BeforeToday(message = "{BeforeToday.exploreForm.date}")
    private Date minDate;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @BeforeToday(message = "{BeforeToday.exploreForm.date}")
    private Date maxDate;
    @Max(value = Integer.MAX_VALUE, message = "{Integer.maxValue}")
    @Min(value = Integer.MIN_VALUE, message = "{Integer.minValue}")
    private Integer minRep;
    @Max(value = Integer.MAX_VALUE, message = "{Integer.maxValue}")
    @Min(value = Integer.MIN_VALUE, message = "{Integer.minValue}")
    private Integer maxRep;
    @Max(value = Integer.MAX_VALUE, message = "{Integer.maxValue}")
    @Min(value = Integer.MIN_VALUE, message = "{Integer.minValue}")
    private Integer minVotes;
    @Max(value = Integer.MAX_VALUE, message = "{Integer.maxValue}")
    @Min(value = Integer.MIN_VALUE, message = "{Integer.minValue}")
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
    private boolean includeFlagged;

    public boolean getIncludeFlagged() {
        return includeFlagged;
    }

    public void setIncludeFlagged(boolean includeFlagged) {
        this.includeFlagged = includeFlagged;
    }

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

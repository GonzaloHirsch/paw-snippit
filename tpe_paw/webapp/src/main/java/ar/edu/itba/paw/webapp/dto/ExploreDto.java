package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.validations.DateOrder;
import ar.edu.itba.paw.webapp.validations.IntegerOrder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.ws.rs.QueryParam;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@DateOrder(min = "minDate", max = "maxDate", message = "{DateOrder.exploreForm.dates}")
@IntegerOrder(min = "minRep", max = "maxRep", message = "{IntegerOrder.exploreForm.order}")
@IntegerOrder(min = "minVotes", max = "maxVotes", message = "{IntegerOrder.exploreForm.order}")
public class ExploreDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @QueryParam("minDate")
    private LocalDate minDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @QueryParam("maxDate")
    private LocalDate maxDate;

    @Max(value = Integer.MAX_VALUE, message = "{Integer.maxValue}")
    @Min(value = Integer.MIN_VALUE, message = "{Integer.minValue}")
    @QueryParam("minRep")
    private Integer minRep;

    @Max(value = Integer.MAX_VALUE, message = "{Integer.maxValue}")
    @Min(value = Integer.MIN_VALUE, message = "{Integer.minValue}")
    @QueryParam("maxRep")
    private Integer maxRep;

    @Max(value = Integer.MAX_VALUE, message = "{Integer.maxValue}")
    @Min(value = Integer.MIN_VALUE, message = "{Integer.minValue}")
    @QueryParam("minVotes")
    private Integer minVotes;

    @Max(value = Integer.MAX_VALUE, message = "{Integer.maxValue}")
    @Min(value = Integer.MIN_VALUE, message = "{Integer.minValue}")
    @QueryParam("maxVotes")
    private Integer maxVotes;

    @Min(value=-1, message = "{Min.exploreForm.language}")
    @QueryParam("language")
    private Long language;

    @Min(value=-1, message = "{Min.exploreForm.tag}")
    @QueryParam("tag")
    private Long tag;

    @Size(max=50, message = "{Size.exploreForm.username}")
    @QueryParam("username")
    private String username;

    @Size(max=50, message = "{Size.exploreForm.title}")
    @QueryParam("title")
    private String title;

    @QueryParam("sort")
    private String sort;

    @QueryParam("field")
    private String field;

    @QueryParam("includeFlagged")
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

    public LocalDate getMinDate() {
        return minDate;
    }

    public void setMinDate(LocalDate minDate) {
        this.minDate = minDate;
    }

    public LocalDate getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(LocalDate maxDate) {
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
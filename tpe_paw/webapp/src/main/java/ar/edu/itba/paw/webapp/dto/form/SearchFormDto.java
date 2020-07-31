package ar.edu.itba.paw.webapp.dto.form;

import javax.ws.rs.QueryParam;

public class SearchFormDto {

    @QueryParam("query")
    private String query;

    @QueryParam("type")
    private String type;

    @QueryParam("userId")
    private String userId;

    @QueryParam("sort")
    private String sort;

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSort() {
        return sort;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getQuery() {
        return query;
    }

    public String getType() {
        return type;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setType(String type) {
        this.type = type;
    }
}

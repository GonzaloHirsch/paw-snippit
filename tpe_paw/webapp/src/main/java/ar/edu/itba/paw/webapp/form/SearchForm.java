package ar.edu.itba.paw.webapp.form;

public class SearchForm {
    private String query;
    private String type;
    private String userId;
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

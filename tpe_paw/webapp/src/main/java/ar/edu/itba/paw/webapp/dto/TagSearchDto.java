package ar.edu.itba.paw.webapp.dto;

import javax.ws.rs.QueryParam;

public class TagSearchDto {
    @QueryParam("query")
    private String query;

    @QueryParam("showEmpty")
    private boolean showEmpty;

    @QueryParam("showOnlyFollowing")
    private boolean showOnlyFollowing;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean isShowEmpty() {
        return showEmpty;
    }

    public void setShowEmpty(boolean showEmpty) {
        this.showEmpty = showEmpty;
    }

    public boolean isShowOnlyFollowing() {
        return showOnlyFollowing;
    }

    public void setShowOnlyFollowing(boolean showOnlyFollowing) {
        this.showOnlyFollowing = showOnlyFollowing;
    }
}

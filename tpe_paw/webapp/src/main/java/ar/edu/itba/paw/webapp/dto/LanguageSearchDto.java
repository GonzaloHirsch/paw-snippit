package ar.edu.itba.paw.webapp.dto;

import javax.ws.rs.QueryParam;

public class LanguageSearchDto {
    @QueryParam("name")
    private String name;

    @QueryParam("showEmpty")
    private boolean showEmpty;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShowEmpty() {
        return showEmpty;
    }

    public void setShowEmpty(boolean showEmpty) {
        this.showEmpty = showEmpty;
    }
}

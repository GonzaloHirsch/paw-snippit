package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;

public class PageForm {
    @NotNull
    private int page = 1;

    public void setPage(int page){ this.page = page; }

    public int getPage() {return this.page; }
}

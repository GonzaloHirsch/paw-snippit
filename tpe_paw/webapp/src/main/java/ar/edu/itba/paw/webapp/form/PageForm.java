package ar.edu.itba.paw.webapp.form;

public class PageForm {
    private int page = 1;
    private String sort;
    private int totalPages;

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSort() {
        return sort;
    }

    public void setPage(int page){ this.page = page; }

    public int getPage() {return this.page; }

    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public int getTotalPages() { return this.totalPages; }
}

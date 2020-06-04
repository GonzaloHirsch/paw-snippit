package ar.edu.itba.paw.webapp.form;

public class ItemSearchForm {
    private String name;

    private boolean showEmpty = true;

    public String getName() {
        return name;
    }

    public void setName(String query) {
        this.name = query;
    }

    public boolean isShowEmpty() {
        return showEmpty;
    }

    public void setShowEmpty(boolean showEmpty) {
        this.showEmpty = showEmpty;
    }
}

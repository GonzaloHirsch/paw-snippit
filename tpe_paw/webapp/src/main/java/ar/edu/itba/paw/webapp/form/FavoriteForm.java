package ar.edu.itba.paw.webapp.form;

public class FavoriteForm {
    private boolean isFav;
    private long userId;
    private long snippetId;

    public FavoriteForm() {
    }

    public boolean isFavorite(){
        return isFav;
    }

    public void setIsFav(boolean isFav){
        this.isFav = isFav;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getSnippetId() {
        return snippetId;
    }

    public void setSnippetId(long snippetId) {
        this.snippetId = snippetId;
    }
}

package ar.edu.itba.paw.webapp.form;

public class FavoriteForm {
    private boolean favorite;
    private boolean wasFavorite;
    private long userId;
    private long snippetId;

    public FavoriteForm() {
    }

    public boolean getFavorite(){
        return favorite;
    }

    public void setFavorite(boolean fav){
        this.favorite = fav;
    }

    public boolean getWasFavorite(){
        return wasFavorite;
    }

    public void setWasFavorite(boolean fav){
        this.wasFavorite = fav;
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

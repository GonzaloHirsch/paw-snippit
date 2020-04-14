package ar.edu.itba.paw.webapp.form;

public class FavoriteForm {
    private boolean favorite;
    private long userId;

    public FavoriteForm() {
    }

    public boolean getFavorite(){
        return favorite;
    }

    public void setFavorite(boolean fav){
        this.favorite = fav;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}

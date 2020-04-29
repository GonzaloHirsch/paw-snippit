package ar.edu.itba.paw.webapp.form;

public class FavoriteForm {
    private boolean favorite;
    private long favUserId;

    public FavoriteForm() {
    }

    public boolean getFavorite(){
        return favorite;
    }

    public void setFavorite(boolean fav){
        this.favorite = fav;
    }

}

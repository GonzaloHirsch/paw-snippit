package ar.edu.itba.paw.webapp.form;

@Deprecated
public class FavoriteForm {
    private boolean favorite;

    public FavoriteForm() {
    }

    public boolean getFavorite(){
        return favorite;
    }

    public void setFavorite(boolean fav){
        this.favorite = fav;
    }

}


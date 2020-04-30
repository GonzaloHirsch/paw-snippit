package ar.edu.itba.paw.webapp.form;

public class VoteForm {
    private int type;
    private int oldType;

    public VoteForm() {
    }

    public int getType(){
        return type;
    }
    public void setType(int newType){
        this.type = newType;
    }

    public int getOldType() {
        return oldType;
    }

    public void setOldType(int oldType) {
        this.oldType = oldType;
    }
}

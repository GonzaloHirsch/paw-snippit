package ar.edu.itba.paw.webapp.form;

public class VoteForm {
    private int type;
    private int oldType;
    private long voteUserId;

    public VoteForm() {
    }

    public int getType(){
        return type;
    }
    public void setType(int newType){
        this.type = newType;
    }

    public long getVoteUserId() {
        return this.voteUserId;
    }

    public void setVoteUserId(long voteUserId) {
        this.voteUserId = voteUserId;
    }

    public int getOldType() {
        return oldType;
    }

    public void setOldType(int oldType) {
        this.oldType = oldType;
    }
}

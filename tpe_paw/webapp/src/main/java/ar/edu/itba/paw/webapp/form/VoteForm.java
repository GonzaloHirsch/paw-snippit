package ar.edu.itba.paw.webapp.form;

public class VoteForm {
    private int type;
    private int oldType;
    private long userId;
    private long snippetId;

    public VoteForm() {
    }

    public int getType(){
        return type;
    }
    public void setType(int newType){
        this.type = newType;
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

    public int getOldType() {
        return oldType;
    }

    public void setOldType(int oldType) {
        this.oldType = oldType;
    }
}

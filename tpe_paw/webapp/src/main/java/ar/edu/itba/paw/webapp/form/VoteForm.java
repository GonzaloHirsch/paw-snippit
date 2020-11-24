package ar.edu.itba.paw.webapp.form;

@Deprecated
public class VoteForm {
    private boolean voteSelected;

    public VoteForm() {
    }

    public boolean isVoteSelected() {
        return voteSelected;
    }

    public void setVoteSelected(boolean voteSelected) {
        this.voteSelected = voteSelected;
    }
}

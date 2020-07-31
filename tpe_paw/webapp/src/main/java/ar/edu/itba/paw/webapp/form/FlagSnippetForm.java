package ar.edu.itba.paw.webapp.form;

@Deprecated
public class FlagSnippetForm {
    private boolean flagged;

    public FlagSnippetForm() {}


    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }
}

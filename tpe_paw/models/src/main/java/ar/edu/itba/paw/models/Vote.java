package ar.edu.itba.paw.models;

public class Vote {

    private long userId;
    private long snippetId;
    private int voteType;

    public Vote(long userId, long snippetId, int voteType) {
        this.userId = userId;
        this.snippetId = snippetId;
        this.voteType = voteType;
    }

    public long getUserId() {
        return userId;
    }

    public long getSnippetId() {
        return snippetId;
    }

    public int getVoteType() {
        return voteType;
    }
}

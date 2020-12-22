package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.User;

public class UserStatsDto {
    private int reputation;
    private int followingCount;
    private long activeSnippetCount;


    public static UserStatsDto fromUser(User user) {
        final UserStatsDto dto = new UserStatsDto();

        dto.reputation = user.getReputation();
        dto.activeSnippetCount = user.getCreatedSnippets().stream().filter(s -> !s.isDeleted()).count();
        dto.followingCount = user.getFollowedTags().size();

        return dto;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public long getActiveSnippetCount() {
        return activeSnippetCount;
    }

    public void setActiveSnippetCount(long activeSnippetCount) {
        this.activeSnippetCount = activeSnippetCount;
    }
}

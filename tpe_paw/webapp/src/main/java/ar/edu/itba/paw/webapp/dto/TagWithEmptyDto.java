package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Tag;

import javax.ws.rs.core.UriInfo;

public class TagWithEmptyDto {
    private String name;
    private Long id;
    private boolean isEmpty;
    private boolean isFollowing;

    public static TagWithEmptyDto fromTag(Tag tag, long currentUserId, UriInfo uriInfo){
        final TagWithEmptyDto dto = new TagWithEmptyDto();

        dto.id = tag.getId();
        dto.name = tag.getName();
        dto.isEmpty = tag.getSnippetsUsingIsEmpty();
        dto.isFollowing = tag.getFollowingUsers().stream().anyMatch(u -> u.getId().equals(currentUserId));

        return dto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }
}

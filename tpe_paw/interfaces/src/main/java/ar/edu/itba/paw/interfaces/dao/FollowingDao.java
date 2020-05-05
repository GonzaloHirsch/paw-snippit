package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Tag;

import java.util.Collection;

public interface FollowingDao {
    Collection<Tag> getFollowedTagsForUser(long userId);
    void followTag(long userId, long tagId);
    void unfollowTag(long userId, long tagId);
    boolean userFollowsTag(final long userId, final long tagId);
}

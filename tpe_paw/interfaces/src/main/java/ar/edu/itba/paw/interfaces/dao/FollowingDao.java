package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.Tag;

import java.util.Collection;

public interface FollowingDao {
    Collection<Tag> getFollowedTagsForUser(long userId);
    Collection<Tag> getMostPopularFollowedTagsForUser(long userId, int amount);
    void followTag(long userId, long tagId);
    void unfollowTag(long userId, long tagId);
}

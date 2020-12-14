package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Tag;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TagService {

    Collection<Tag> getAllTags(boolean showEmpty, boolean showOnlyFollowing, Long userId, int page, int pageSize);

    Collection<Tag> getAllTags();

    int getAllTagsCountByName(String name, boolean showEmpty, boolean showOnlyFollowing, Long userId);

    int getAllTagsCount(boolean showEmpty, boolean showOnlyFollowing, Long userId);

    Collection<Tag> findTagsByName(String name, boolean showEmpty, boolean showOnlyFollowing, Long userId, int page, int pageSize);

    Collection<Tag> getFollowedTagsForUser(long userId);

    Collection<Tag> getMostPopularFollowedTagsForUser(long userId, int amount);

    Collection<Tag> getSomeOrderedFollowedTagsForUser(long userId, int amount);

    void followTag(long userId, long tagId);

    void unfollowTag(long userId, long tagId);

    Optional<Tag> findTagById(long tagId);

    void addTags(List<String> tags);

    long addTag(String name);

    boolean tagExists(final String tag);

    boolean tagExists(final long tag);

    void removeTag(final long tagId);

    void updateFollowing(long userId, long tagId, boolean followed);

    boolean userFollowsTag(long userId, long tagId);
}